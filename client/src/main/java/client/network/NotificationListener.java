package client.network;

import client.ClientSession;
import client.controllers.MainController;
import common.model.Worker;
import common.network.DataChunk;
import common.network.Request;
import common.network.Response;
import common.network.Result;
import javafx.application.Platform;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.time.Duration;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationListener {

    private final MainController mainController;
    private Timer debounceTimer;
    private final long time = 500;
    private final int portServer;
    private final String host;
    private final ClientSession clientSession;

    private volatile boolean isRunning = true;



    public NotificationListener(MainController mainController, ClientSession clientSession, int portServer, String host) {
        this.mainController = mainController;
        this.clientSession = clientSession;
        this.portServer = portServer;
        this.host = host;
    }


    public void stopListening(){
        this.isRunning = false;
        if(debounceTimer != null)
            debounceTimer.cancel();
    }


    private void handleServerNotification(Response response){
        if(response.result().isSuccess() && response.result().getValue().equals("UPDATE_SIGNAL")){
            if(debounceTimer != null) {
                debounceTimer.cancel();
            }
            debounceTimer = new Timer();
            debounceTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            fetchLatestData();
                        }
                    },time);
        }
    }



    private void fetchLatestData() {
        Result<List<Worker>> resultWorkers = mainController
                .gerSystemBootstrapper()
                .getProxyWorkerRepository()
                .getAllWorkers();
        Platform.runLater(
                ()-> {
                    mainController.updateTableView(resultWorkers.getValue());
                }
        );
    }



    public void startListening() {

        new Thread(() -> {
            try {
                DatagramChannel channel = DatagramChannel.open();
                channel.configureBlocking(true);
                channel.bind(null);

                InetSocketAddress serverAddress = new InetSocketAddress(host,portServer);

                startHeartbeat(channel, serverAddress);

                ByteBuffer receiveBuffer = ByteBuffer.allocate(4096);
                ByteArrayOutputStream finalBuffer = new ByteArrayOutputStream();
                int totalExpected = -1;
                int chunksReceived = 0;

                while (isRunning) {
                    receiveBuffer.clear();
                    channel.receive(receiveBuffer);
                    receiveBuffer.flip();

                    byte[] chunkBytes = new byte[receiveBuffer.remaining()];
                    receiveBuffer.get(chunkBytes);
                    DataChunk chunk = (DataChunk) deserialize(chunkBytes);

                    if (totalExpected == -1) totalExpected = chunk.size();

                    finalBuffer.write(chunk.payload());
                    chunksReceived++;

                    if (chunksReceived == totalExpected) {
                        Response response = (Response) deserialize(finalBuffer.toByteArray());
                        handleServerNotification(response);
                        finalBuffer.reset();
                        totalExpected = -1;
                        chunksReceived = 0;
                    }
                }
            } catch (Exception e) {
                System.out.println("El oyente de notificaciones se ha detenido.");
            }
        }).start();
    }



    private void startHeartbeat(DatagramChannel channel, InetSocketAddress serverAddress) {
        new Thread(() -> {
            try {
                while (true) {
                    Request signalReq = new Request(clientSession.getUser(), "signal");
                    channel.send(ByteBuffer.wrap(serialize(signalReq)), serverAddress);
                    Thread.sleep(Duration.ofSeconds(120));
                }
            } catch (Exception e) {
                // Hilo terminado
            }
        }).start();
    }



    private byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
        }
        return baos.toByteArray();
    }



    private Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return ois.readObject();
        }
    }
}





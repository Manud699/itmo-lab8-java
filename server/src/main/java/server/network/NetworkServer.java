package server.network;

import server.lifecycle.Shutdownable;
import server.multithread.ExecutorUtils;
import server.multithread.ReadThread;
import server.multithread.ResponseProvider;
import server.repository.CommandRegistry;
import server.repository.CurrentClient;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkServer implements Shutdownable {
    private CurrentClient currentClient;
    private final int port;
    private final CommandRegistry commandRegistry;
    private final ExecutorService readPool;
    private ResponseProvider responseProvider;
    private final static Logger logger = Logger.getLogger(NetworkServer.class.getName());
    private DatagramSocket datagramSocket;


    public NetworkServer(int port, CommandRegistry commandRegistry) {
        this.port = port;
        this.commandRegistry = commandRegistry;
        this.readPool = Executors.newFixedThreadPool(20, getWorkerFactory());
        try {
            newDatagramSocket();
        } catch (SocketException e) {
            logger.severe("No se pudo iniciar DatagramSocket");
        }
        this.responseProvider = new ResponseProvider(datagramSocket);
    }


    public void start() {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(2048);

            DatagramPacket receivePacket = new DatagramPacket(buffer.array(), buffer.capacity());
            logger.info("UDP server started and listening on port " + port);

            for (;;) {
                receivePacket.setLength(buffer.capacity());
                datagramSocket.receive(receivePacket);
                SocketAddress clientAddress = receivePacket.getSocketAddress();
                currentClient.add(clientAddress);
                byte[] dataCopy = Arrays.copyOf(receivePacket.getData(), receivePacket.getLength());
                readPool.submit(new ReadThread(commandRegistry, responseProvider,clientAddress, dataCopy));
                logger.log(Level.INFO, "Dispatched raw packet (" + dataCopy.length + " bytes) from [" + clientAddress + "] to the read pool.");
            }

        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
            ExecutorUtils.shutdownAndAwaitTermination(readPool, "Read-UDP-Pool");
            System.exit(1);
        }
    }

    @Override
    public void shutdownHook(){
        ExecutorUtils.shutdownAndAwaitTermination(readPool, "Read-UDP-Pool");
        responseProvider.shutdownHook();
        logger.info("Server safely stopped.");
        datagramSocket.close();
    }


    private ThreadFactory getWorkerFactory(){
        return runnable -> {
            Thread t = new Thread(runnable);
            t.setName("Task-Dispatcher" + t.getId());
            return t;
        };
    }


    public ResponseProvider getResponseProvider(){
        return  responseProvider;
    }


    public void setCurrentClient(CurrentClient currentClient){
        this.currentClient = currentClient;
    }


    public void newDatagramSocket() throws SocketException {
        this.datagramSocket = new DatagramSocket(port);
    }



}
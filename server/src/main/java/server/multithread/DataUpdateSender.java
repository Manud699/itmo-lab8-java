package server.multithread;

import common.network.Response;
import server.repository.CurrentClient;
import java.net.SocketAddress;
import java.util.Set;

public class DataUpdateSender {

    private final ResponseProvider responseProvider;
    private final CurrentClient currentClient;


    public DataUpdateSender(ResponseProvider responseProvider, CurrentClient currentClient){
        this.responseProvider = responseProvider;
        this.currentClient = currentClient;
    }


    public void send(Response response){
        if(response == null)
            return;

        Set<SocketAddress> sockets = currentClient.getActiveClients();
        for(SocketAddress item : sockets){
            responseProvider.sendResponse(response, item);
        }
    }
}

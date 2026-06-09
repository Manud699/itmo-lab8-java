package server.repository;

import java.net.SocketAddress;
import java.time.Instant;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class CurrentClient {

    private final long limit_time = 300;
    ConcurrentHashMap<SocketAddress, Instant> currentHashMap;


    public CurrentClient(){
        this.currentHashMap = new ConcurrentHashMap<>(64);
    }


    public void add(SocketAddress socketAddress){
            currentHashMap.put(socketAddress, Instant.now());
    }



    private void updateMapClients(){
        Instant instant = Instant.now();
        currentHashMap.entrySet()
                                .removeIf(x -> instant.getEpochSecond() - x.getValue()
                                .getEpochSecond() > limit_time);
    }



    public Set<SocketAddress> getActiveClients() {
        updateMapClients();
        return currentHashMap.keySet();
    }
}

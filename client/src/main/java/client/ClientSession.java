package client;

import common.network.User;

public class ClientSession {

    private User user = null;

    public void setUser(User user){
        this.user = user;
    }

    public User getUser(){
        return user;
    }

    public String getUserName() {
        return (user != null) ? user.name() : null;
    }

    public void removeUser(){
        user = null;
    }

}

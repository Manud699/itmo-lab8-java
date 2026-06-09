package client;

import client.controllers.RunnerLoginController;


public class Runner {

    private final RunnerLoginController runnerLoginController;

    public Runner(RunnerLoginController runnerLoginController){
        this.runnerLoginController = runnerLoginController;

    }

    public void start(){
        runnerLoginController.loadLoginView();
    }
}
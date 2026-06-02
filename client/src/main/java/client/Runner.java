package client;

import client.controllers.RunnerLoginController;
import client.controllers.RunnerMainController;

public class Runner {

    private final RunnerLoginController runnerLoginController;
    private final RunnerMainController runnerMainController;

    public Runner(RunnerLoginController runnerLoginController, RunnerMainController runnerMainController){
        this.runnerLoginController = runnerLoginController;
        this.runnerMainController = runnerMainController;
    }

    public void start(){
        runnerLoginController.loadLoginView();
    }
}
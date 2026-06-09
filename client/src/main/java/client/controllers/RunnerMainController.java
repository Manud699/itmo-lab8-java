package client.controllers;

import client.SystemBootstrapper;
import client.network.NotificationListener;
import common.model.Worker;
import common.network.Result;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.util.List;

public class RunnerMainController {

    private final Stage primaryStage;
    private SystemBootstrapper systemBootstrapper;
    private RunnerLoginController runnerLoginController;
    private NotificationListener notificationListener;


    public RunnerMainController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    public void loadMainView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main_view.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            MainController mainController  = loader.getController();
            mainController.setBootstrapper(systemBootstrapper);
            mainController.setRunnerLoginController(runnerLoginController);


            NotificationListener notificationListener = new NotificationListener(mainController,
                    systemBootstrapper.getClientSession(),
                    systemBootstrapper.getAddressNetwork().port(),
                    systemBootstrapper.getAddressNetwork().host());

            this.notificationListener = notificationListener;
            mainController.setNotificationListener(notificationListener);
            notificationListener.startListening();

            String css = getClass().getResource("/css/dark-theme.css").toExternalForm();
            scene.getStylesheets().add(css);

            Result<List<Worker>> resultList = systemBootstrapper
                    .getProxyWorkerRepository()
                    .getAllWorkers();

            if(!resultList.isSuccess()){
                System.out.println(resultList.getErrorMessage());
                return;
            }

            List<Worker> workerList = resultList.getValue();

            mainController.updateTableView(workerList);

            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.setResizable(true);

        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public void setRunnerLogin(RunnerLoginController runnerLoginController) {
        this.runnerLoginController= runnerLoginController;
    }

    public void setBootstrapper(SystemBootstrapper systemBootstrapper){
        this.systemBootstrapper = systemBootstrapper;
    }

    public void stopNotificationListener(){
        notificationListener.stopListening();
    }
}

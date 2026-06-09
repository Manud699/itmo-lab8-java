package client.controllers;

import client.SystemBootstrapper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RunnerLoginController {

    private Stage primaryStage;
    private SystemBootstrapper systemBootstrapper;
    private RunnerMainController runnerMainController;

    public RunnerLoginController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void loadLoginView() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login_view.fxml"));
            Parent root = loader.load();
            Scene loginScene = new Scene(root);

            String css = getClass().getResource("/css/dark-theme.css").toExternalForm();
            loginScene.getStylesheets().add(css);

            if(!primaryStage.isShowing())
                primaryStage.initStyle(javafx.stage.StageStyle.UNDECORATED);

            primaryStage.setMaximized(false);



            LoginController controller = loader.getController();
            controller.initialize();
            controller.setSystemBootstrapper(systemBootstrapper);
            runnerMainController.setRunnerLogin(this);
            controller.setRunnerMainController(runnerMainController);

            primaryStage.setTitle("ITMO Lab 8");
            primaryStage.setScene(loginScene);
            primaryStage.setResizable(false);
            primaryStage.show();

            primaryStage.centerOnScreen();

        } catch (Exception e) {
            System.err.println("Error, no se pudo cargar la vista de login.");
            e.printStackTrace();
        }
    }


    public void setSystemBootstrapper(SystemBootstrapper systemBootstrapper){
        this.systemBootstrapper = systemBootstrapper;
    }

    public void setRunnerMainController(RunnerMainController runnerMainController) {
        this.runnerMainController = runnerMainController;
    }
}
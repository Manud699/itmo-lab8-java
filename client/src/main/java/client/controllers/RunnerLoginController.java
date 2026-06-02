package client.controllers;

import client.SystemBootstrapper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

            LoginController controller = loader.getController();
            controller.setSystemBootstrapper(systemBootstrapper);

            // ¡EL PUENTE! Le pasamos el controlador de la ventana principal
            controller.setRunnerMainController(runnerMainController);

            primaryStage.setTitle("ITMO Lab 8 - Autenticación");
            primaryStage.setScene(loginScene);
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Error, no se pudo cargar la vista de login.");
            e.printStackTrace();
        }
    }

    public void setPrimaryStage(Stage stage){
        this.primaryStage = stage;
    }

    public void setSystemBootstrapper(SystemBootstrapper systemBootstrapper){
        this.systemBootstrapper = systemBootstrapper;
    }

    public void setRunnerMainController(RunnerMainController runnerMainController) {
        this.runnerMainController = runnerMainController;
    }
}
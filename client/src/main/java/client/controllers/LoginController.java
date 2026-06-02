package client.controllers;

import client.SystemBootstrapper;
import common.network.Result;
import common.network.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.util.function.Function;

public class LoginController {

    @FXML private TextField userName;
    @FXML private PasswordField passwordField;

    private SystemBootstrapper systemBootstrapper;
    private RunnerMainController runnerMainController;

    @FXML
    private void handleLoginAction(){
        executeRedAction(
                user -> systemBootstrapper.getUserRegistry().logging(user),
                "Sesión iniciada correctamente"
        );
    }

    @FXML
    public void handleRegisterAction(){
        executeRedAction(
                userRegister -> systemBootstrapper.getUserRegistry().registrate(userRegister),
                "Registro exitoso."
        );
    }

    public void setRunnerMainController(RunnerMainController runnerMainController) {
        this.runnerMainController = runnerMainController;
    }

    public void setSystemBootstrapper(SystemBootstrapper systemBootstrapper){
        this.systemBootstrapper = systemBootstrapper;
    }

    public void showMessage(String text, Color color){
        Alert.AlertType alertType = (color == Color.RED) ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION;
        Alert alert = new Alert(alertType);
        alert.setTitle(alertType == Alert.AlertType.ERROR ? "Error" : "Éxito");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }



    public void executeRedAction(Function<User, Result<?>> actionRed, String message){
        try {
            var user = new User(userName.getText(), passwordField.getText());
            Result<?> result = actionRed.apply(user);

            if(!result.isSuccess()){
                showMessage(result.getErrorMessage(), Color.RED);
                return;
            }

            showMessage(message, Color.GREEN);
            if (runnerMainController != null) {
                runnerMainController.loadMainView();
                systemBootstrapper.getClientSession().setUser(user);
                return;
            }

        } catch (IllegalArgumentException e){
            showMessage(e.getMessage(), Color.RED);
        } catch (Exception e){
            showMessage("Ocurrió un error inesperado. Inténtalo de nuevo.", Color.RED);
        }
    }
}
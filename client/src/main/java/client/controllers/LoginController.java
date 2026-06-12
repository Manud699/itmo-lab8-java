package client.controllers;

import client.SystemBootstrapper;
import common.network.Result;
import common.network.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.application.Platform;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;

public class LoginController {

    @FXML private TextField userName;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> languageCombox;
    @FXML private ResourceBundle resources;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Label passwordLabel;
    @FXML private Label nameLabel;
    @FXML private TextField passwordTextField;
    @FXML private CheckBox showPasswordCheckBox;

    private SystemBootstrapper systemBootstrapper;
    private RunnerMainController runnerMainController;

    private Map<String, String> mapLanguages = new HashMap<>();
    private Map<String, String> mapLocation = new HashMap<>();


    public void initialize() {
        languageCombox.getItems().clear();
        mapLanguages.put("Ruso", "ru");
        mapLanguages.put("Español (Colombia)", "es");
        mapLocation.put("ru", "RU");
        mapLocation.put("es", "CO");
        passwordTextField.textProperty().bindBidirectional(passwordField.textProperty());
        passwordTextField.setVisible(false);
        passwordTextField.setManaged(false);
        showPasswordCheckBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                passwordTextField.setVisible(true);
                passwordTextField.setManaged(true);
                passwordField.setVisible(false);
                passwordField.setManaged(false);
            } else {
                passwordTextField.setVisible(false);
                passwordTextField.setManaged(false);
                passwordField.setVisible(true);
                passwordField.setManaged(true);
            }
        });
        setBoxComboOptions();


        languageCombox.setOnAction(action -> {
            String idioma = languageCombox.getValue();
            if (idioma != null) {
                changeLanguage(idioma);
            }
        });
    }



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


    private void changeLanguage(String language){
        String languageChoosed = mapLanguages.get(language);
        String locationLanguage = mapLocation.get(languageChoosed);
        var locale = new Locale(languageChoosed, locationLanguage);
        this.resources = ResourceBundle.getBundle("client.i18n.Messages", locale);
        updateTextInterface();
    }



    private void setBoxComboOptions(){

        languageCombox.getItems().addAll("Español (Colombia)", "Ruso");
        languageCombox.setValue("Español (Colombia)");

        languageCombox.setOnAction(action ->
        {
            String idioma = languageCombox.getValue();
            changeLanguage(idioma);
        });
    }


    private void updateTextInterface(){
        if(resources==null) return;
        loginButton.setText(resources.getString("button.login"));
        registerButton.setText(resources.getString("button.register"));
        passwordLabel.setText(resources.getString("label.password"));
        nameLabel.setText(resources.getString("label.name"));

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
                systemBootstrapper.getClientSession().setUser(user);
                runnerMainController.loadMainView();
            }

        } catch (IllegalArgumentException e){
            showMessage(e.getMessage(), Color.RED);
        } catch (Exception e){
            showMessage("Ocurrió un error inesperado. Inténtalo de nuevo.", Color.RED);
        }
    }


    @FXML
    public void handleCloseAction() {
        Platform.exit();
        System.exit(0);
    }


}
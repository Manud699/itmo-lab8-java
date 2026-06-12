package client.controllers;

import client.SystemBootstrapper;
import common.network.Result;
import common.network.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
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

    private final Map<String, String> mapLanguages = new HashMap<>();
    private final Map<String, String> mapLocation = new HashMap<>();

    public void initialize() {
        mapLanguages.put("Ruso", "ru");
        mapLanguages.put("Español (Colombia)", "es");
        mapLocation.put("ru", "RU");
        mapLocation.put("es", "CO");

        passwordTextField.textProperty().bindBidirectional(passwordField.textProperty());
        passwordTextField.setVisible(false);
        passwordTextField.setManaged(false);

        showPasswordCheckBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            passwordTextField.setVisible(isSelected);
            passwordTextField.setManaged(isSelected);
            passwordField.setVisible(!isSelected);
            passwordField.setManaged(!isSelected);
        });

        setBoxComboOptions();
    }

    @FXML
    private void handleLoginAction() {
        executeRedAction(
                user -> systemBootstrapper.getUserRegistry().logging(user),
                "login.success"
        );
    }

    @FXML
    public void handleRegisterAction() {
        executeRedAction(
                userRegister -> systemBootstrapper.getUserRegistry().registrate(userRegister),
                "register.success"
        );
    }

    private void changeLanguage(String language) {
        String languageChoosed = mapLanguages.get(language);
        String locationLanguage = mapLocation.get(languageChoosed);
        Locale locale = new Locale(languageChoosed, locationLanguage);
        this.resources = ResourceBundle.getBundle("client.i18n.Messages", locale);
        updateTextInterface();
    }

    private void setBoxComboOptions() {
        languageCombox.getItems().addAll("Español (Colombia)", "Ruso");
        languageCombox.setValue("Español (Colombia)");
        languageCombox.setOnAction(action -> {
            if (languageCombox.getValue() != null) changeLanguage(languageCombox.getValue());
        });
    }

    private void updateTextInterface() {
        if (resources == null) return;
        loginButton.setText(resources.getString("button.login"));
        registerButton.setText(resources.getString("button.register"));
        passwordLabel.setText(resources.getString("label.password"));
        nameLabel.setText(resources.getString("label.name"));

        if (userName != null) userName.setPromptText(resources.getString("label.username_prompt"));
        if (passwordField != null) passwordField.setPromptText(resources.getString("label.password_prompt"));
        if (showPasswordCheckBox != null) showPasswordCheckBox.setText(resources.getString("checkbox.showPassword"));
    }

    private String getTranslatedText(String key) {
        return (resources != null && resources.containsKey(key)) ? resources.getString(key) : key;
    }

    public void showMessage(String messageKey, Color color) {
        Alert.AlertType alertType = (color == Color.RED) ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION;
        Alert alert = new Alert(alertType);

        alert.setTitle(alertType == Alert.AlertType.ERROR ? getTranslatedText("dialog.error.title") : getTranslatedText("dialog.success.title"));
        alert.setHeaderText(null);
        alert.setContentText(getTranslatedText(messageKey));

        try {
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/dark-theme.css").toExternalForm());
        } catch (Exception ignored) {}

        Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        if (okButton != null) okButton.setText(getTranslatedText("button.ok"));

        alert.showAndWait();
    }

    public void executeRedAction(Function<User, Result<?>> actionRed, String successKey) {
        try {
            var user = new User(userName.getText(), passwordField.getText());
            Result<?> result = actionRed.apply(user);

            if (!result.isSuccess()) {
                showMessage(result.getErrorMessage(), Color.RED);
                return;
            }

            showMessage(successKey, Color.GREEN);
            if (runnerMainController != null) {
                systemBootstrapper.getClientSession().setUser(user);
                runnerMainController.loadMainView();
            }
        } catch (IllegalArgumentException e) {
            showMessage(e.getMessage(), Color.RED);
        } catch (Exception e) {
            showMessage("error.unexpected", Color.RED);
        }
    }

    @FXML
    public void handleCloseAction() {
        Platform.exit();
        System.exit(0);
    }

    public void setRunnerMainController(RunnerMainController runnerMainController) {
        this.runnerMainController = runnerMainController;
    }

    public void setSystemBootstrapper(SystemBootstrapper systemBootstrapper) {
        this.systemBootstrapper = systemBootstrapper;
    }
}
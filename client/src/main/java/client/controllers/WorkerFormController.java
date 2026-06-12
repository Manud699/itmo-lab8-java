package client.controllers;

import common.model.*;
import common.network.Result;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.util.HashMap;

public class WorkerFormController {

    private MainController mainController;
    private Worker workerToUpdate = null;

    private final HashMap<String, Node> dictionaryErrors = new HashMap<>(16);
    private final HashMap<String, Label> dictionaryLabels = new HashMap<>(16);

    @FXML private TextField nameField;
    @FXML private TextField xCoordinateField;
    @FXML private TextField yCoordinateField;
    @FXML private TextField salaryField;
    @FXML private TextField nameOrganizationField;
    @FXML private TextField orgAnnualTurnoverField;
    @FXML private TextField employeesCountField;

    @FXML private ComboBox<Position> comboBoxPosition;
    @FXML private ComboBox<Status> comboBoxStatus;

    @FXML private Label errorName;
    @FXML private Label errorX;
    @FXML private Label errorY;
    @FXML private Label errorSalary;
    @FXML private Label errorPosition;
    @FXML private Label errorStatus;
    @FXML private Label errorOrgName;
    @FXML private Label errorOrgAnnual;
    @FXML private Label errorOrgEmployees;

    @FXML
    public void initialize() {
        comboBoxStatus.getItems().addAll(Status.values());
        comboBoxPosition.getItems().addAll(Position.values());
        initErrorDictionary();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setWorkerForUpdate(Worker worker) {
        this.workerToUpdate = worker;
        nameField.setText(worker.getName());
        xCoordinateField.setText(String.valueOf(worker.getCoordinates().getX()));
        yCoordinateField.setText(String.valueOf(worker.getCoordinates().getY()));
        salaryField.setText(String.valueOf(worker.getSalary()));
        comboBoxPosition.setValue(worker.getPosition());
        comboBoxStatus.setValue(worker.getStatus());
        nameOrganizationField.setText(worker.getOrganization().getFullName());
        orgAnnualTurnoverField.setText(String.valueOf(worker.getOrganization().getAnnualTurnover()));
        employeesCountField.setText(String.valueOf(worker.getOrganization().getEmployeesCount()));
    }

    @FXML
    public void handleSaveAction() {
        clearErrorStyles();

        try {
            String name = nameField.getText();
            float xCoordinate = parseFloatSafe(xCoordinateField, "error.val.xCoord");
            Double yCoordinate = parseDoubleSafe(yCoordinateField, "error.val.yCoord");
            long salary = parseLongSafe(salaryField, "error.val.salary");

            Status status = comboBoxStatus.getValue();
            Position position = comboBoxPosition.getValue();

            String nameOrganization = nameOrganizationField.getText();
            float orgAnnualT = parseFloatSafe(orgAnnualTurnoverField, "error.val.orgAnnual");
            int employeesCount = (int) parseLongSafe(employeesCountField, "error.val.orgEmployeesCount");

            var coordinates = new Coordinates(xCoordinate, yCoordinate);
            var organization = new Organization(nameOrganization, orgAnnualT, employeesCount);

            Worker nuevoWorker = new Worker(0, name, coordinates, null, salary, position, status, organization);

            if (this.workerToUpdate == null) {
                mainController.gerSystemBootstrapper().getProxyWorkerRepository().add(nuevoWorker);
                mainController.printToConsole(mainController.getResources().getString("console.action.success"));
            } else {
                nuevoWorker.setId(workerToUpdate.getId());
                nuevoWorker.setCreationDate(workerToUpdate.getCreationDate());
                Result<Void> result = mainController.gerSystemBootstrapper()
                        .getProxyWorkerRepository()
                        .updateWorkerById(nuevoWorker);
                if (!result.isSuccess()) {
                    mainController.printNetworkError(result.getErrorMessage());
                } else {
                    mainController.printToConsole(mainController.getResources().getString("console.action.success"));
                }
            }
            mainController.closeSidePanel();

        } catch (IllegalArgumentException e) {
            String rawMessage = e.getMessage();
            String[] parts = rawMessage.split("\\|");
            String fieldKey = parts[0];
            String messageKey = (parts.length > 1) ? parts[1] : parts[0];

            Node inputField = dictionaryErrors.get(fieldKey);
            Label errorLabel = dictionaryLabels.get(fieldKey);
            String translatedMessage = mainController.getResources().getString(messageKey);

            if (inputField != null && errorLabel != null) {
                if (!inputField.getStyleClass().contains("input-error")) {
                    inputField.getStyleClass().add("input-error");
                }
                errorLabel.setText(translatedMessage);
            } else {
                mainController.printErrorToConsole(translatedMessage);
            }
        } catch (Exception e) {
            mainController.printErrorToConsole("Error Inesperado: " + e.getMessage());
        }
    }

    @FXML
    public void handleCancelAction() {
        mainController.closeSidePanel();
    }

    protected void clearErrorStyles() {
        dictionaryErrors.values().forEach(node -> node.getStyleClass().remove("input-error"));
        dictionaryLabels.values().forEach(label -> label.setText(""));
    }

    private void initErrorDictionary() {
        dictionaryErrors.put("error.val.name", nameField);
        dictionaryErrors.put("error.val.xCoord", xCoordinateField);
        dictionaryErrors.put("error.val.yCoord", yCoordinateField);
        dictionaryErrors.put("error.val.salary", salaryField);
        dictionaryErrors.put("error.val.status", comboBoxStatus);
        dictionaryErrors.put("error.val.position", comboBoxPosition);
        dictionaryErrors.put("error.val.orgNameLength", nameOrganizationField);
        dictionaryErrors.put("error.val.orgNameEmpty", nameOrganizationField);
        dictionaryErrors.put("error.val.orgAnnual", orgAnnualTurnoverField);
        dictionaryErrors.put("error.val.orgEmployeesCount", employeesCountField);

        dictionaryLabels.put("error.val.name", errorName);
        dictionaryLabels.put("error.val.xCoord", errorX);
        dictionaryLabels.put("error.val.yCoord", errorY);
        dictionaryLabels.put("error.val.salary", errorSalary);
        dictionaryLabels.put("error.val.status", errorStatus);
        dictionaryLabels.put("error.val.position", errorPosition);
        dictionaryLabels.put("error.val.orgNameLength", errorOrgName);
        dictionaryLabels.put("error.val.orgNameEmpty", errorOrgName);
        dictionaryLabels.put("error.val.orgAnnual", errorOrgAnnual);
        dictionaryLabels.put("error.val.orgEmployeesCount", errorOrgEmployees);
    }

    private long parseLongSafe(TextField field, String fieldKey) {
        try {
            return Long.parseLong(field.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldKey + "|error.val.numberFormat");
        }
    }

    private float parseFloatSafe(TextField field, String fieldKey) {
        try {
            return Float.parseFloat(field.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldKey + "|error.val.numberFormat");
        }
    }

    private double parseDoubleSafe(TextField field, String fieldKey) {
        try {
            return Double.parseDouble(field.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldKey + "|error.val.numberFormat");
        }
    }
}
package client.controllers;

import common.model.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.util.HashMap;

public class WorkerFormController {

    public WorkerFormController(){
        this.dictionaryErrors = new HashMap<>(16);
    }

    private MainController mainController;

    @FXML
    private TextField nameField;
    @FXML
    private TextField xCoordinateField;
    @FXML
    private TextField yCoordinateField;
    @FXML
    private TextField salaryField;
    @FXML
    private TextField nameOrganizationField;
    @FXML
    private TextField orgAnnualTurnoverField;
    @FXML
    private TextField employeesCountField;
    @FXML
    private ComboBox<Position> comboBoxPosition;
    @FXML
    private ComboBox<Status> comboBoxStatus;

    @FXML
    private Label addName;
    @FXML
    private Label addX;
    @FXML
    private Label addY;
    @FXML
    private Label addSalary;
    @FXML
    private Label addPosition;
    @FXML
    private Label addStatus;
    @FXML
    private Label addNameOrg;
    @FXML
    private Label addOrgAnnual;
    @FXML
    private Label addEmployees;

    @FXML
    private Button save;
    @FXML
    private Button cancel;

    HashMap <String, Node> dictionaryErrors;

    protected final String ESTILO_ERROR = "-fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 3px;";

    private Worker workerToUpdate = null;



    @FXML
    public void initialize() {
        comboBoxStatus.getItems().addAll(Status.values());
        comboBoxPosition.getItems().addAll(Position.values());
        initErrorDictionary();
    }



    @FXML
    public void handleCancelAction(){
        mainController.closeSidePanel();
    }



    protected void clearErrorStyles() {
        dictionaryErrors.values().forEach(node -> node.setStyle(""));
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



    public void handleSaveAction(){
        clearErrorStyles();
        try {

            String name = nameField.getText();
            float xCoordinate = Float.parseFloat(xCoordinateField.getText());
            Double yCoordinate = Double.parseDouble(yCoordinateField.getText());

            long salary = Long.parseLong(salaryField.getText());
            Status status = comboBoxStatus.getValue();
            Position position = comboBoxPosition.getValue();
            String nameOrganization = nameOrganizationField.getText();
            float orgAnnualT = Float.parseFloat(orgAnnualTurnoverField.getText());
            int employeesCount = Integer.parseInt(employeesCountField.getText());

            var coordinates = new Coordinates(xCoordinate, yCoordinate);
            var organization = new Organization(nameOrganization, orgAnnualT, employeesCount);


            Worker nuevoWorker = new Worker(0, name, coordinates, null, salary, position, status, organization);

            if(this.workerToUpdate == null){
                mainController.gerSystemBootstrapper().getProxyWorkerRepository().add(nuevoWorker);
                mainController.printToConsole("Se ha enviado el comando add al servidor.");
            } else {

                nuevoWorker.setId(workerToUpdate.getId());
                nuevoWorker.setCreationDate(workerToUpdate.getCreationDate());

                mainController.gerSystemBootstrapper().getProxyWorkerRepository().updateWorkerById(nuevoWorker);
                mainController.printToConsole("Se ha enviado el comando update al servidor.");
            }

            mainController.closeSidePanel();

        } catch (NumberFormatException e) {
            String messageTrans = mainController
                    .getResources()
                    .getString("error.val.numberFormat");
            mainController.printToConsole(messageTrans);


        } catch (IllegalArgumentException e) {
            String error = e.getMessage();
            String messageTrans = mainController
                    .getResources()
                    .getString(error);
            mainController.printToConsole(messageTrans);

        } catch (Exception e) {
            mainController.printToConsole(e.getMessage());
        }
    }



    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }



    public void initErrorDictionary() {
        dictionaryErrors.put("error.val.name", nameField);
        dictionaryErrors.put("error.val.xCoord", xCoordinateField);
        dictionaryErrors.put("error.val.yCoord", yCoordinateField);
        dictionaryErrors.put("error.val.salary", salaryField);
        dictionaryErrors.put("error.val.status", comboBoxStatus);
        dictionaryErrors.put("error.val.position",comboBoxPosition);
        dictionaryErrors.put("error.val.orgNameLength", nameOrganizationField );
        dictionaryErrors.put("error.val.orgNameEmpty", nameOrganizationField);
        dictionaryErrors.put("error.val.orgAnnual", orgAnnualTurnoverField);
        dictionaryErrors.put("error.val.orgEmployeesCount", employeesCountField);
    }
}

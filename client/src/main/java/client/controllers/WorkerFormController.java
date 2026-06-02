package client.controllers;

import common.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.time.ZonedDateTime;

public class WorkerFormController {

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
    public void initialize() {
        comboBoxStatus.getItems().addAll(Status.values());
        comboBoxPosition.getItems().addAll(Position.values());

    }



    @FXML
    public void handleCancelAction(){
        mainController.closeSidePanel();
    }


    public void handleSaveAction(){
        try {
            String name = nameField.getText();
            float xCoordinate= Float.parseFloat(xCoordinateField.getText());
            Double yCoordinate = Double.parseDouble(yCoordinateField.getText());

            long salary = Long.parseLong(salaryField.getText());
            Status status =   comboBoxStatus.getValue();
            Position position = comboBoxPosition.getValue();
            String nameOrganization = nameOrganizationField.getText();
            float orgAnnualT = Float.parseFloat(orgAnnualTurnoverField.getText());
            int employeesCount = Integer.parseInt(employeesCountField.getText());

            var coordinates = new Coordinates(xCoordinate, yCoordinate);
            var organization = new Organization(nameOrganization, orgAnnualT, employeesCount);

            long tempId = 0;
            ZonedDateTime tempCreation = null;

            Worker worker = new Worker(tempId, name,  coordinates, tempCreation, salary, position,status, organization);

            mainController.gerSystemBootstrapper()
                          .getProxyWorkerRepository()
                          .add(worker);

        } catch (IllegalArgumentException e){

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
}

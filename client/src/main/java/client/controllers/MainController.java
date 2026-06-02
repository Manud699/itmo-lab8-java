package client.controllers;
import client.SystemBootstrapper;
import common.model.Position;
import common.model.Status;
import common.model.Worker;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class MainController {

    private SystemBootstrapper bootstrapper;


    @FXML
    private TextArea consoleOutput;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private Label labelText;

    @FXML
    private TableView <Worker> workersTable;

    @FXML
    private TableColumn<Worker, Long> idColumn;

    @FXML
    private TableColumn<Worker, String> nameColumn;

    @FXML
    private TableColumn<Worker, Float> xColumn;

    @FXML
    private TableColumn<Worker, Double> yColumn;

    @FXML
    private TableColumn<Worker, String> creationDateColumn;

    @FXML
    private TableColumn<Worker, Long> salaryColumn;

    @FXML
    private TableColumn<Worker, Position> positionColumn;

    @FXML
    private TableColumn<Worker, Status> statusColumn;

    @FXML
    private TableColumn<Worker,String> organizationNameColumn;

    @FXML
    private TableColumn<Worker,Float> orgAnnualTurnoverColumn;

    @FXML
    private TableColumn<Worker,Integer> employeesCountColumn;

    @FXML
    private TableColumn<Worker, String> ownerColumn;

    @FXML
    private ComboBox<String> languageCombox;

    @FXML
    private ResourceBundle resources;

    @FXML
    private Button addButton;

    @FXML
    private Button clearButton;


    private Map<String, String> mapLanguages = new HashMap<>();
    private Map<String, String> mapLocation = new HashMap<>();


    public void initialize() {
        mapLanguages.put("Ruso","ru");
        mapLanguages.put("Español (Colombia)", "es");
        mapLocation.put("ru", "RU");
        mapLocation.put("es", "CO");

        setBoxComboOptions();

        idColumn.setCellValueFactory(cellValue ->
                                        new SimpleLongProperty(cellValue
                                                .getValue()
                                                .getId())
                                                .asObject());

        nameColumn.setCellValueFactory(cellValue ->
                                        new SimpleStringProperty(cellValue
                                                .getValue()
                                                .getName()));

        xColumn.setCellValueFactory(cellValue ->
                                        new SimpleFloatProperty(cellValue
                                                .getValue()
                                                .getCoordinates()
                                                .getX())
                                                .asObject());

        yColumn.setCellValueFactory(cellValue ->
                                        new SimpleDoubleProperty(cellValue
                                                .getValue()
                                                .getCoordinates()
                                                .getY())
                                                .asObject());

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        creationDateColumn.setCellValueFactory(cellValue -> {
            var zoneDateTime = cellValue.getValue().getCreationDate();
            String dateText = zoneDateTime.format(formato);
            return new SimpleStringProperty(dateText);
        });


        salaryColumn.setCellValueFactory(cellValue ->
                                        new SimpleLongProperty(cellValue
                                                .getValue()
                                                .getSalary())
                                                .asObject());


        positionColumn.setCellValueFactory(cellValue ->
                                        new SimpleObjectProperty<>(cellValue
                                                .getValue()
                                                .getPosition()));


        statusColumn.setCellValueFactory(cellValue ->
                                        new SimpleObjectProperty<>(cellValue
                                                .getValue()
                                                .getStatus()));


        organizationNameColumn.setCellValueFactory(cellValue ->
                                        new SimpleStringProperty(cellValue
                                                .getValue()
                                                .getOrganization()
                                                .getFullName()));

        orgAnnualTurnoverColumn.setCellValueFactory(cellValue ->
                                        new SimpleFloatProperty(cellValue
                                                .getValue()
                                                .getOrganization()
                                                .getAnnualTurnover())
                                                .asObject());


        employeesCountColumn.setCellValueFactory(cellValue ->
                                        new SimpleIntegerProperty(cellValue
                                                .getValue()
                                                .getOrganization()
                                                .getEmployeesCount())
                                                .asObject());


        ownerColumn.setCellValueFactory(cellValue ->
                                        new SimpleStringProperty(cellValue
                                                .getValue()
                                                .getCreatorName()));
        changeLanguage("Español (Colombia)");
    }






    public void setBoxComboOptions(){

        languageCombox.getItems().addAll("Español (Colombia)", "Ruso");
        languageCombox.setValue("Español (Colombia)");

        languageCombox.setOnAction(action ->
                        {
                            String idioma = languageCombox.getValue();
                            changeLanguage(idioma);
                        });
    }




        private void changeLanguage(String language){
            String languageChoosed = mapLanguages.get(language);
            String locationLanguage = mapLocation.get(languageChoosed);
            var locale = new Locale(languageChoosed, locationLanguage);
            this.resources = ResourceBundle.getBundle("i18n/messages", locale);
            updateTextInterface();
        }





    @FXML
    public void handleAddAction(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/worker_form_view.fxml"));
            loader.setResources(this.resources);
            VBox vBox = loader.load();
            WorkerFormController formWorker = loader.getController();
            formWorker.setMainController(this);
            mainBorderPane.setRight(vBox);
        } catch (Exception e){
            printToConsole(e.getMessage());
            e.printStackTrace();
        }
    }



    @FXML
    public void handleShowAction(){

    }




    public void printToConsole(String message){
            String timeStamp = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            consoleOutput.appendText("[" + timeStamp + "] " + message + "\n");
    }



    @FXML
    public void handleClearAction(){
        bootstrapper.getProxyWorkerRepository().clear();
        showMessage("Operarion exitosa",Color.GREEN);
    }



    public void showMessage(String message, Color color){
        labelText.setText(message);
        labelText.setTextFill(color);
    }



    public void updateTableView(List<Worker> listFromServer){
        ObservableList<Worker> observableList = FXCollections.observableList(listFromServer);
        workersTable.setItems(observableList);
    }



    public void closeSidePanel(){
        mainBorderPane.setRight(null);
    }



    public void setBootstrapper(SystemBootstrapper systemBootstrapper){
        this.bootstrapper = systemBootstrapper;
    }



    public void updateTextInterface(){

        if(this.resources == null) return;

        addButton.setText(resources.getString("button.add"));
        clearButton.setText(resources.getString("button.clear"));

        idColumn.setText(resources.getString("table.id"));
        nameColumn.setText(resources.getString("table.name"));
        salaryColumn.setText(resources.getString("table.salary"));
        xColumn.setText(resources.getString("table.x"));
        yColumn.setText(resources.getString("table.y"));
        creationDateColumn.setText(resources.getString("table.creation_date"));
        positionColumn.setText(resources.getString("table.position"));
        statusColumn.setText(resources.getString("table.status"));
        organizationNameColumn.setText(resources.getString("table.org_name"));
        orgAnnualTurnoverColumn.setText(resources.getString("table.org_turnover"));
        employeesCountColumn.setText(resources.getString("table.employees"));
        ownerColumn.setText(resources.getString("table.owner"));

    }


    public SystemBootstrapper gerSystemBootstrapper(){
        if(bootstrapper != null) {
            return  bootstrapper;
        }
        throw new IllegalArgumentException("Error, SystemBootstrapper no ha sido inicializado");
    }


    public ResourceBundle getResources(){
        return resources;
    }


}




package client.controllers;
import client.SystemBootstrapper;
import client.network.NotificationListener;
import common.model.Position;
import common.model.Status;
import common.model.Worker;
import common.network.Result;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class MainController {

    private SystemBootstrapper bootstrapper;
    private RunnerLoginController runnerLoginController;
    private NotificationListener notificationListener;

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

    @FXML
    private Button updateButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button signOutButton;

    @FXML
    private Tab workersTab;

    @FXML
    private Button removeHeadButton;

    private Map<String, String> mapLanguages = new HashMap<>();
    private Map<String, String> mapLocation = new HashMap<>();


    public void initialize() {
        mapLanguages.put("Ruso","ru");
        mapLanguages.put("Español (Colombia)", "es");
        mapLocation.put("ru", "RU");
        mapLocation.put("es", "CO");

        setBoxComboOptions();
        setupContextMenu();

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



    private void setBoxComboOptions(){

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
        closeSidePanel();
        workersTable.refresh();
    }



    @FXML
    public void handleAddAction(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/worker_form_view.fxml"));
            loader.setResources(this.resources);
            GridPane gridPane = loader.load();
            WorkerFormController formWorker = loader.getController();
            formWorker.setMainController(this);
            mainBorderPane.setRight(gridPane);
        } catch (Exception e){
            printToConsole(e.getMessage());
            e.printStackTrace();
        }
    }



    @FXML
    public void handleUpdateAction() {
        try {
            var worker = workersTable.getSelectionModel().getSelectedItem();

            if (worker == null) {
                printToConsole("Seleccione un trabajador de la tabla antes de actualizar.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/worker_form_view.fxml"));
            loader.setResources(resources);
            GridPane gridPane = loader.load();
            WorkerFormController formController = loader.getController();
            formController.setMainController(this);

            formController.setWorkerForUpdate(worker);

            mainBorderPane.setRight(gridPane);
        } catch (Exception e) {
            printToConsole(e.getMessage());
        }
    }


        @FXML
        public void handleRemoveHeadAction() {
            try {
                Result<Worker> resultWorker = bootstrapper.getProxyWorkerRepository().removeHead();
                if(!resultWorker.isSuccess()){
                    printToConsole(resultWorker.getErrorMessage());
                    return;
                }
                showRemovedHead(resultWorker.getValue());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Que esta sucediendo? ");
            }
        }



    @FXML
    public void handleClearAction(){
        bootstrapper.getProxyWorkerRepository().clear();
        showMessage("Operarion exitosa",Color.GREEN);
    }


    @FXML
    public void handleExitAction(){
        try {
            Platform.exit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }


    @FXML
    public void handleSignOutAction(){
        try {
            bootstrapper.getClientSession().removeUser();
            notificationListener.stopListening();

            if(runnerLoginController != null)
                runnerLoginController.loadLoginView();

        } catch (Exception e) {
            System.err.println("No fue posible cerrar la cesion");
        }
    }


    private void showRemovedHead(Worker removedWorker) {
        if (removedWorker==null) {
            printToConsole("console.removeHead.empty");
            return;
        }

        String format = resources.getString("console.removeHead.success");
        String logToprint = String.format(
                format,
                removedWorker.getId(),
                removedWorker.getName(),
                removedWorker.getPosition(),
                removedWorker.getSalary(),
                removedWorker.getOrganization().getFullName()
        );
        printToConsole(logToprint);
    }




    private void setupContextMenu(){
        workersTable.setRowFactory(
                tv -> {
                    TableRow<Worker> tableRow = new TableRow<>();

                    ContextMenu contextMenu = new ContextMenu();

                    MenuItem infoItem = new MenuItem(resources.getString("menu.info"));
                    MenuItem updateItem = new MenuItem(resources.getString("menu.update"));

                    infoItem.setOnAction( event -> {
                        var worker = tableRow.getItem();
                        showWorkerDetailsDialog(worker);
                         });

                    updateItem.setOnAction(event -> {
                        workersTable.getSelectionModel().select(tableRow.getItem());
                        handleUpdateAction();
                    });

                    contextMenu.getItems().addAll(infoItem, updateItem);

                    tableRow.contextMenuProperty().bind(
                            javafx.beans.binding.Bindings.when(tableRow.emptyProperty())
                                    .then((ContextMenu) null)
                                    .otherwise(contextMenu)
                    );
                    return tableRow;
                });
    }



    private void showWorkerDetailsDialog(Worker worker) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(resources.getString("dialog.info.title"));
        alert.setHeaderText(resources.getString("dialog.info.header") + worker.getName());

        String formatPlantilla = resources.getString("dialog.info.details");

        String details = String.format(
                formatPlantilla,
                worker.getId(),
                worker.getCreatorName(),
                worker.getCoordinates().getX(),
                worker.getCoordinates().getY(),
                worker.getSalary(),
                worker.getPosition(),
                worker.getStatus(),
                worker.getOrganization().getFullName(),
                worker.getOrganization().getAnnualTurnover(),
                worker.getOrganization().getEmployeesCount(),
                worker.getCreationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        );
        alert.setContentText(details);
        DialogPane dialogPane = alert.getDialogPane();
        try {
            String css = getClass().getResource("/css/dark-theme.css").toExternalForm();
            dialogPane.getStylesheets().add(css);
            dialogPane.getStyleClass().add("form-panel");
        } catch (Exception e) {
            System.out.println("No se pudo cargar el CSS para el diálogo.");
        }
        alert.showAndWait();
    }



    public void printToConsole(String message){
        String timeStamp = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        consoleOutput.appendText("[" + timeStamp + "] " + message + "\n");
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

    public void setNotificationListener(NotificationListener notificationListener){
        this.notificationListener = notificationListener;
    }

    public void setRunnerLoginController(RunnerLoginController runnerLoginController){
        this.runnerLoginController= runnerLoginController;
    }

    public void setBootstrapper(SystemBootstrapper systemBootstrapper){
        this.bootstrapper = systemBootstrapper;
    }



    public void updateTextInterface(){

        if(this.resources == null) return;

        addButton.setText(resources.getString("button.add"));
        clearButton.setText(resources.getString("button.clear"));
        updateButton.setText(resources.getString("button.update"));
        exitButton.setText(resources.getString("button.exit"));
        signOutButton.setText(resources.getString("button.signOut"));
        removeHeadButton.setText(getResources().getString("button.removeHead"));


        workersTab.setText(resources.getString("tab.workers"));

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




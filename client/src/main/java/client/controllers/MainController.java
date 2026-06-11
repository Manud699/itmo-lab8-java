package client.controllers;
import client.SystemBootstrapper;
import client.network.NotificationListener;
import client.repository.CommandHistory;
import client.repository.CommandRegistry;
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

import java.text.NumberFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;


public class MainController {

    private SystemBootstrapper bootstrapper;
    private RunnerLoginController runnerLoginController;
    private NotificationListener notificationListener;

    private final CommandHistory commandHistory = new CommandHistory();

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
    private MenuButton viewMenuButton;

    @FXML
    private MenuItem printSalaryMenuItem;

    @FXML
    private MenuItem headItemMenu;

    @FXML
    private MenuButton deleteMenuButton;

    @FXML
    private MenuItem removeByIdMenuItem;

    @FXML
    private MenuItem removeByPosMenuItem;

    @FXML
    private MenuItem helpItemMenu;

    @FXML
    private MenuItem historyItemMenu;


//    @FXML
//    private Button removeHeadButton;

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



        creationDateColumn.setCellFactory(col -> new TableCell<Worker, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView().getItems().get(getIndex()) == null) {
                    setText(null);
                } else {
                    Worker worker = getTableView().getItems().get(getIndex());
                    DateTimeFormatter formatter = DateTimeFormatter
                            .ofLocalizedDateTime(FormatStyle.SHORT)
                            .withLocale(resources.getLocale());
                    setText(worker.getCreationDate().format(formatter));
                }
            }
        });



        salaryColumn.setCellFactory(col -> new TableCell<Worker, Long>() {
            @Override
            protected void updateItem(Long item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    NumberFormat format = NumberFormat.getCurrencyInstance(resources.getLocale());
                    setText(format.format(item));
                }
            }
        });



        xColumn.setCellFactory(col -> new TableCell<Worker, Float>() {
            @Override
            protected void updateItem(Float item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else setText(java.text.NumberFormat.getNumberInstance(resources.getLocale()).format(item));
            }
        });



        yColumn.setCellFactory(col -> new TableCell<Worker, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else setText(java.text.NumberFormat.getNumberInstance(resources.getLocale()).format(item));
            }
        });



        orgAnnualTurnoverColumn.setCellFactory(col -> new TableCell<Worker, Float>() {
            @Override
            protected void updateItem(Float item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else setText(java.text.NumberFormat.getNumberInstance(resources.getLocale()).format(item));
            }
        });
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
        this.resources = ResourceBundle.getBundle("client.i18n.Messages", locale);
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
            commandHistory.addCommand("cmd.add");
        } catch (Exception e){
            printToConsole(e.getMessage());
            e.printStackTrace();
        }
    }



    @FXML
    public void handleRemoveByIdAction() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(resources.getString("dialog.removeId.title"));
        dialog.setHeaderText(resources.getString("dialog.removeId.header"));

        try {
            dialog.getDialogPane().getStylesheets().add(getClass().getResource("/css/dark-theme.css").toExternalForm());
            dialog.getDialogPane().getStyleClass().add("form-panel");
        } catch (Exception e) { }

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(idStr -> {
            try {
                long id = Long.parseLong(idStr.trim());

                Result<Boolean> response = bootstrapper.getProxyWorkerRepository().removeById(id);

                if (!response.isSuccess()) {
                    printNetworkError(response.getErrorMessage());
                    return;
                }

                printToConsole(resources.getString("console.remove.success"));
                commandHistory.addCommand("cmd.remove_by_id");

            } catch (NumberFormatException e) {
                printToConsole(resources.getString("error.val.numberFormat"));
            } catch (Exception e) {
                printToConsole("Error: " + e.getMessage());
            }
        });
    }


    private void confirmAndRemoveWorker(Worker worker) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(resources.getString("dialog.confirmDelete.title"));

        alert.setHeaderText(String.format(resources.getString("dialog.confirmDelete.header"), worker.getName()));

        try {
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/dark-theme.css").toExternalForm());
            alert.getDialogPane().getStyleClass().add("form-panel");
        } catch (Exception e) {
            System.out.println("No se pudo cargar el CSS para la confirmación.");
        }

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Result<Boolean> response = bootstrapper.getProxyWorkerRepository().removeById(worker.getId());

                if (!response.isSuccess()) {
                    printNetworkError(response.getErrorMessage());
                    return;
                }

                printToConsole(resources.getString("console.remove.success"));
                commandHistory.addCommand("cmd.remove_by_id");

            } catch (Exception e) {
                printToConsole("Error crítico: " + e.getMessage());
            }
        }
    }



    @FXML
    public void handleHistoryAction() {
        List<String> comandosGuardados = commandHistory.getHistoryList();

        if (comandosGuardados.isEmpty()) {
            printToConsole(resources.getString("console.history.empty"));
            return;
        }

        StringBuilder sb = new StringBuilder();

        sb.append(resources.getString("console.history.header")).append("\n\n");

        for (int i = 0; i < comandosGuardados.size(); i++) {
            String claveComando = comandosGuardados.get(i);
            String comandoTraducido = resources.getString(claveComando);
            sb.append("   ").append(i + 1).append(". ").append(comandoTraducido).append("\n");
        }

        printToConsole(sb.toString().trim());

        commandHistory.addCommand("cmd.history");
    }



    @FXML
    public void handleRemoveByPositionAction() {
        List<Position> choices = Arrays.asList(Position.values());

        ChoiceDialog<Position> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle(resources.getString("dialog.removePos.title"));
        dialog.setHeaderText(resources.getString("dialog.removePos.header"));
        commandHistory.addCommand("cmd.remove_by_pos");

        try {
            dialog.getDialogPane().getStylesheets().add(getClass().getResource("/css/dark-theme.css").toExternalForm());
            dialog.getDialogPane().getStyleClass().add("form-panel");
        } catch (Exception e) { }


        Optional<Position> result = dialog.showAndWait();

        result.ifPresent(selectedPosition -> {
            try {
                Result<Integer> response = bootstrapper.getProxyWorkerRepository().removeAllByPosition(selectedPosition);

                if (!response.isSuccess()) {
                    printNetworkError(response.getErrorMessage());
                    return;
                }
                printToConsole(resources.getString("console.remove.success"));

            } catch (Exception e) {
                printToConsole("Error: " + e.getMessage());
            }
        });
    }



    public void handleFieldDescSalary(){
        Result<List<Long>> resultSalary = bootstrapper.getProxyWorkerRepository().getDescendingSalaries();
        if(!resultSalary.isSuccess()){
            printToConsole(resultSalary.getErrorMessage());
            return;
        }
        if(resultSalary.getValue()==null || resultSalary.getValue().isEmpty()){
            printToConsole(resources.getString("console.notResults"));
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(resources.getString("console.salary.header"));
        List<Long> salaryWorkers = resultSalary.getValue();

        NumberFormat moneyFormatter = NumberFormat.getCurrencyInstance(resources.getLocale());

        for(Long item : salaryWorkers){
            stringBuilder.append(moneyFormatter.format(item))
                         .append("\n");
        }
        printToConsole(stringBuilder.toString().trim());
        commandHistory.addCommand("cmd.print_salary");
    }




    @FXML
    public void handleUpdateAction() {
        try {
            var worker = workersTable.getSelectionModel().getSelectedItem();

            if (worker == null) {
                printToConsole("console.choose.worker");
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/worker_form_view.fxml"));
            loader.setResources(resources);
            GridPane gridPane = loader.load();
            WorkerFormController formController = loader.getController();
            formController.setMainController(this);
            formController.setWorkerForUpdate(worker);
            mainBorderPane.setRight(gridPane);
            commandHistory.addCommand("cmd.update");
        } catch (Exception e) {
            printToConsole(e.getMessage());
        }
    }



    public void handleHeadAction() {
        Result<Worker> resultWorker = bootstrapper
                                    .getProxyWorkerRepository()
                                    .getHead();
        if(!resultWorker.isSuccess()){
            printNetworkError(resultWorker.getErrorMessage());
            return;
        }
        if(resultWorker.getValue() == null) {
            printToConsole("console.notResults");
            return;
        }
        showWorkerDetailsDialog(resultWorker.getValue());
        commandHistory.addCommand("cmd.head");
    }



    @FXML
    public void handleClearAction(){
        bootstrapper.getProxyWorkerRepository().clear();
        printToConsole("console.action.success");
        commandHistory.addCommand("cmd.clear");
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


    @FXML
    public void handleHelpAction() {

        String[] commandKeys = {
                "add", "clear", "update", "head", "print_salary",
                "remove_by_id", "remove_by_pos", "history", "help"
        };

        StringBuilder sb = new StringBuilder();

        sb.append(resources.getString("console.help.header")).append("\n\n");


        for (String key : commandKeys) {
            String nombreComando = resources.getString("cmd." + key);
            String descripcion = resources.getString("desc." + key);

            String lineaFormateada = String.format("  > %-25s : %s", nombreComando, descripcion);
            sb.append(lineaFormateada).append("\n");
        }

        printToConsole(sb.toString().trim());

        commandHistory.addCommand("cmd.help");
    }





    private void setupContextMenu(){
        workersTable.setRowFactory(
                tv -> {
                    TableRow<Worker> tableRow = new TableRow<>();

                    ContextMenu contextMenu = new ContextMenu();

                    MenuItem infoItem = new MenuItem(resources.getString("menu.info"));
                    MenuItem updateItem = new MenuItem(resources.getString("menu.update"));
                    MenuItem deleteItem = new MenuItem(resources.getString("menu.delete"));

                    infoItem.setOnAction( event -> {
                        var worker = tableRow.getItem();
                        showWorkerDetailsDialog(worker);
                        commandHistory.addCommand("menu.info");
                         });

                    updateItem.setOnAction(event -> {
                        workersTable.getSelectionModel().select(tableRow.getItem());
                        handleUpdateAction();
                    });

                    deleteItem.setOnAction(event -> {
                        var worker = tableRow.getItem();
                        if (worker != null) {
                            confirmAndRemoveWorker(worker);
                        }
                    });

                    contextMenu.getItems().addAll(infoItem, updateItem, deleteItem);

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
        NumberFormat numberFormat = NumberFormat.getNumberInstance(resources.getLocale());

        DateTimeFormatter formatter = DateTimeFormatter
                                    .ofLocalizedDate(FormatStyle.SHORT)
                                    .localizedBy(resources.getLocale());


        String details = String.format(
                formatPlantilla,
                worker.getId(),
                worker.getCreatorName(),

                numberFormat.format(worker.getCoordinates().getX()),
                numberFormat.format(worker.getCoordinates().getY()),
                numberFormat.format(worker.getSalary()),

                worker.getPosition(),
                worker.getStatus(),
                worker.getOrganization().getFullName(),

                numberFormat.format(worker.getOrganization().getAnnualTurnover()),
                numberFormat.format(worker.getOrganization().getEmployeesCount()),

                worker.getCreationDate().format(formatter)
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



    public void updateTextInterface(){

        if(this.resources == null) return;

        addButton.setText(resources.getString("button.add"));
        clearButton.setText(resources.getString("button.clear"));
        updateButton.setText(resources.getString("button.update"));
        exitButton.setText(resources.getString("button.exit"));
        signOutButton.setText(resources.getString("button.signOut"));
        viewMenuButton.setText(resources.getString("button.view"));
        headItemMenu.setText(resources.getString("menuItem.head"));
        printSalaryMenuItem.setText(resources.getString("menuItem.printSalary"));
        deleteMenuButton.setText(resources.getString("button.delete"));
        removeByIdMenuItem.setText(resources.getString("menuItem.removeById"));
        removeByPosMenuItem.setText(resources.getString("menuItem.removeByPosition"));
        helpItemMenu.setText(resources.getString("menuItem.help"));
        historyItemMenu.setText(resources.getString("menuItem.history"));


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



    public void setNotificationListener(NotificationListener notificationListener){
        this.notificationListener = notificationListener;
    }



    public void setRunnerLoginController(RunnerLoginController runnerLoginController){
        this.runnerLoginController= runnerLoginController;
    }



    public void setBootstrapper(SystemBootstrapper systemBootstrapper){
        this.bootstrapper = systemBootstrapper;
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



    protected void printNetworkError(String serverMessage) {
        try {
            String translatedError = resources.getString(serverMessage);
            printToConsole(translatedError);
        } catch (Exception e) {
            printToConsole(serverMessage);
        }
    }


}




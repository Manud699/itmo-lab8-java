package client.controllers;

import client.SystemBootstrapper;
import client.network.NotificationListener;
import client.repository.CommandHistory;
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
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import javafx.stage.StageStyle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Interpolator;
import javafx.util.Duration;

import java.text.NumberFormat;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class MainController {

    private SystemBootstrapper bootstrapper;
    private RunnerLoginController runnerLoginController;
    private NotificationListener notificationListener;
    private final CommandHistory commandHistory = new CommandHistory();

    private final Map<String, Color> userColors = new HashMap<>();
    private final Map<String, String> mapLanguages = new HashMap<>();
    private final Map<String, String> mapLocation = new HashMap<>();

    private DoubleProperty animationProgress = new SimpleDoubleProperty(0);
    private Timeline animationTimeline;
    private boolean isDarkMode = true;

    private ZoneId currentZone = ZoneId.systemDefault();
    private final Map<String, ZoneId> mapTimeZones = new HashMap<>();

    @FXML private BorderPane mainBorderPane;
    @FXML private Tab workersTab;
    @FXML private ResourceBundle resources;

    @FXML private TableView<Worker> workersTable;
    @FXML private TableColumn<Worker, Long> idColumn;
    @FXML private TableColumn<Worker, String> nameColumn;
    @FXML private TableColumn<Worker, Float> xColumn;
    @FXML private TableColumn<Worker, Double> yColumn;
    @FXML private TableColumn<Worker, String> creationDateColumn;
    @FXML private TableColumn<Worker, Long> salaryColumn;
    @FXML private TableColumn<Worker, Position> positionColumn;
    @FXML private TableColumn<Worker, Status> statusColumn;
    @FXML private TableColumn<Worker, String> organizationNameColumn;
    @FXML private TableColumn<Worker, Float> orgAnnualTurnoverColumn;
    @FXML private TableColumn<Worker, Integer> employeesCountColumn;
    @FXML private TableColumn<Worker, String> ownerColumn;

    @FXML private Pane canvasContainer;
    @FXML private Canvas visualCanvas;

    @FXML private TitledPane consoleTitledPane;
    @FXML private TextArea consoleOutput;
    @FXML private Label welcomeLabel;
    @FXML private ComboBox<String> languageCombox;
    @FXML private ToggleButton themeButton;

    @FXML private Button addButton;
    @FXML private Button clearButton;
    @FXML private Button updateButton;
    @FXML private Button exitButton;
    @FXML private Button signOutButton;
    @FXML private MenuButton deleteMenuButton;
    @FXML private MenuItem removeByIdMenuItem;
    @FXML private MenuItem removeByPosMenuItem;
    @FXML private MenuButton viewMenuButton;
    @FXML private MenuItem printSalaryMenuItem;
    @FXML private MenuItem headItemMenu;
    @FXML private MenuItem helpItemMenu;
    @FXML private MenuItem historyItemMenu;
    @FXML private MenuItem sumSalaryItemMenu;

    @FXML
    public void initialize() {
        mapLanguages.put("Ruso", "ru");
        mapLanguages.put("Español (Colombia)", "es");
        mapLanguages.put("Polaco", "pl");
        mapLanguages.put("Noruego", "no");

        mapLocation.put("ru", "RU");
        mapLocation.put("es", "CO");

        mapTimeZones.put("ru", ZoneId.of("Europe/Moscow"));
        mapTimeZones.put("es", ZoneId.of("America/Bogota"));
        mapTimeZones.put("pl", ZoneId.of("Europe/Warsaw"));
        mapTimeZones.put("no", ZoneId.of("Europe/Oslo"));

        setBoxComboOptions();
        setupTableColumns();
        setupContextMenu();
        setupCanvasInteractions();

        animationProgress.addListener((obs, oldVal, newVal) -> renderCanvasFrame());

        if (canvasContainer != null && visualCanvas != null) {
            visualCanvas.widthProperty().bind(canvasContainer.widthProperty());
            visualCanvas.heightProperty().bind(canvasContainer.heightProperty());
            visualCanvas.widthProperty().addListener(e -> renderCanvasFrame());
            visualCanvas.heightProperty().addListener(e -> renderCanvasFrame());
        }

        changeLanguage("Español (Colombia)");
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(cellValue -> new SimpleLongProperty(cellValue.getValue().getId()).asObject());
        nameColumn.setCellValueFactory(cellValue -> new SimpleStringProperty(cellValue.getValue().getName()));
        xColumn.setCellValueFactory(cellValue -> new SimpleFloatProperty(cellValue.getValue().getCoordinates().getX()).asObject());
        yColumn.setCellValueFactory(cellValue -> new SimpleDoubleProperty(cellValue.getValue().getCoordinates().getY()).asObject());
        salaryColumn.setCellValueFactory(cellValue -> new SimpleLongProperty(cellValue.getValue().getSalary()).asObject());
        positionColumn.setCellValueFactory(cellValue -> new SimpleObjectProperty<>(cellValue.getValue().getPosition()));
        statusColumn.setCellValueFactory(cellValue -> new SimpleObjectProperty<>(cellValue.getValue().getStatus()));
        organizationNameColumn.setCellValueFactory(cellValue -> new SimpleStringProperty(cellValue.getValue().getOrganization().getFullName()));
        orgAnnualTurnoverColumn.setCellValueFactory(cellValue -> new SimpleFloatProperty(cellValue.getValue().getOrganization().getAnnualTurnover()).asObject());
        employeesCountColumn.setCellValueFactory(cellValue -> new SimpleIntegerProperty(cellValue.getValue().getOrganization().getEmployeesCount()).asObject());
        ownerColumn.setCellValueFactory(cellValue -> new SimpleStringProperty(cellValue.getValue().getCreatorName()));

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        creationDateColumn.setCellValueFactory(cellValue -> {
            var zoneDateTime = cellValue.getValue().getCreationDate();
            return new SimpleStringProperty(zoneDateTime.format(formato));
        });

        creationDateColumn.setCellFactory(col -> new TableCell<Worker, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView().getItems().get(getIndex()) == null) {
                    setText(null);
                } else {
                    Worker worker = getTableView().getItems().get(getIndex());
                    DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(resources.getLocale());
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
                    setText(NumberFormat.getCurrencyInstance(resources.getLocale()).format(item));
                }
            }
        });

        xColumn.setCellFactory(createNumberCellFactory());
        yColumn.setCellFactory(createNumberCellFactory());
        orgAnnualTurnoverColumn.setCellFactory(createNumberCellFactory());
    }

    private <T extends Number> javafx.util.Callback<TableColumn<Worker, T>, TableCell<Worker, T>> createNumberCellFactory() {
        return col -> new TableCell<Worker, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(NumberFormat.getNumberInstance(resources.getLocale()).format(item));
                }
            }
        };
    }

    @FXML
    public void handleAddAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/worker_form_view.fxml"));
            loader.setResources(this.resources);
            GridPane gridPane = loader.load();

            WorkerFormController formWorker = loader.getController();
            formWorker.setMainController(this);

            mainBorderPane.setRight(gridPane);
            commandHistory.addCommand("cmd.add");
        } catch (Exception e) {
            printErrorToConsole(e.getMessage());
        }
    }

    @FXML
    public void handleUpdateAction() {
        try {
            Worker worker = workersTable.getSelectionModel().getSelectedItem();
            if (worker == null) {
                printErrorToConsole(resources.getString("console.choose.worker"));
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
            printErrorToConsole(e.getMessage());
        }
    }

    @FXML
    public void handleClearAction() {
        bootstrapper.getProxyWorkerRepository().clear();
        printToConsole(resources.getString("console.action.success"));
        commandHistory.addCommand("cmd.clear");
    }

    @FXML
    public void handleRemoveByIdAction() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(resources.getString("dialog.removeId.title"));
        dialog.setHeaderText(resources.getString("dialog.removeId.header"));
        dialog.setGraphic(null);
        dialog.getDialogPane().setGraphic(null);

        styleAndTranslateDialog(dialog);

        dialog.showAndWait().ifPresent(idStr -> {
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
                printErrorToConsole(resources.getString("error.val.numberFormat"));
            }
        });
    }

    @FXML
    public void handleRemoveByPositionAction() {
        List<Position> choices = Arrays.asList(Position.values());
        ChoiceDialog<Position> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle(resources.getString("dialog.removePos.title"));
        dialog.setHeaderText(resources.getString("dialog.removePos.header"));

        styleAndTranslateDialog(dialog);

        dialog.showAndWait().ifPresent(selectedPosition -> {
            Result<Integer> response = bootstrapper.getProxyWorkerRepository().removeAllByPosition(selectedPosition);

            if (!response.isSuccess()) {
                printNetworkError(response.getErrorMessage());
                return;
            }

            printToConsole(resources.getString("console.remove.success"));
            commandHistory.addCommand("cmd.remove_by_pos");
        });
    }

    public void handleSumOfSalaryAction() {
        Result<Long> result = bootstrapper.getProxyWorkerRepository().sumOfSalary();
        if (!result.isSuccess()) {
            printNetworkError(result.getErrorMessage());
            return;
        }

        long totalSuma = result.getValue() != null ? result.getValue() : 0L;
        NumberFormat moneyFormatter = NumberFormat.getCurrencyInstance(resources.getLocale());

        String output = resources.getString("console.sum.header") + "\n" +
                "   > [" + resources.getString("console.sum.total") + "]: " +
                moneyFormatter.format(totalSuma);

        printToConsole(output);
        commandHistory.addCommand("cmd.sum_of_salary");
    }

    public void handleFieldDescSalary() {
        Result<List<Long>> resultSalary = bootstrapper.getProxyWorkerRepository().getDescendingSalaries();

        if (!resultSalary.isSuccess()) {
            printNetworkError(resultSalary.getErrorMessage());
            return;
        }

        if (resultSalary.getValue() == null || resultSalary.getValue().isEmpty()) {
            printToConsole(resources.getString("console.notResults"));
            return;
        }

        StringBuilder sb = new StringBuilder(resources.getString("console.salary.header")).append("\n");
        NumberFormat moneyFormatter = NumberFormat.getCurrencyInstance(resources.getLocale());

        for (Long item : resultSalary.getValue()) {
            sb.append("   > ").append(moneyFormatter.format(item)).append("\n");
        }

        printToConsole(sb.toString());
        commandHistory.addCommand("cmd.print_salary");
    }

    public void handleHeadAction() {
        Result<Worker> resultWorker = bootstrapper.getProxyWorkerRepository().getHead();

        if (!resultWorker.isSuccess()) {
            printNetworkError(resultWorker.getErrorMessage());
            return;
        }

        if (resultWorker.getValue() == null) {
            printToConsole(resources.getString("console.notResults"));
            return;
        }

        showWorkerDetailsDialog(resultWorker.getValue());
        commandHistory.addCommand("cmd.head");
    }

    @FXML
    public void handleHistoryAction() {
        List<String> comandosGuardados = commandHistory.getHistoryList();

        if (comandosGuardados.isEmpty()) {
            printToConsole(resources.getString("console.history.empty"));
            return;
        }

        StringBuilder sb = new StringBuilder(resources.getString("console.history.header")).append("\n");
        for (int i = 0; i < comandosGuardados.size(); i++) {
            sb.append("   ").append(i + 1).append(". ").append(resources.getString(comandosGuardados.get(i))).append("\n");
        }

        printToConsole(sb.toString());
        commandHistory.addCommand("cmd.history");
    }

    @FXML
    public void handleHelpAction() {
        String[] commandKeys = {"add", "clear", "update", "head", "print_salary", "remove_by_id", "remove_by_pos", "history", "help"};
        StringBuilder sb = new StringBuilder(resources.getString("console.help.header")).append("\n");

        for (String key : commandKeys) {
            sb.append(String.format("  > %-25s : %s\n", resources.getString("cmd." + key), resources.getString("desc." + key)));
        }

        printToConsole(sb.toString());
        commandHistory.addCommand("cmd.help");
    }

    @FXML
    public void handleThemeToggle() {
        isDarkMode = !isDarkMode;
        var stylesheets = mainBorderPane.getScene().getStylesheets();
        stylesheets.clear();

        if (isDarkMode) {
            stylesheets.add(getClass().getResource("/css/dark-theme.css").toExternalForm());
            themeButton.setText(resources.getString("theme.light"));
        } else {
            stylesheets.add(getClass().getResource("/css/light-theme.css").toExternalForm());
            themeButton.setText(resources.getString("theme.dark"));
        }
    }

    @FXML
    public void handleSignOutAction() {
        try {
            if (animationTimeline != null) animationTimeline.stop();

            if (visualCanvas != null) {
                visualCanvas.getGraphicsContext2D().clearRect(0, 0, visualCanvas.getWidth(), visualCanvas.getHeight());
            }

            bootstrapper.getClientSession().removeUser();
            notificationListener.stopListening();

            if (runnerLoginController != null) {
                runnerLoginController.loadLoginView();
            }
        } catch (Exception ignored) {
            // Silencio absoluto para la consola de Helios
        }
    }

    @FXML
    public void handleExitAction() {
        Platform.exit();
        System.exit(0);
    }

    public void updateTableView(List<Worker> listFromServer) {
        ObservableList<Worker> observableList = FXCollections.observableList(listFromServer);
        workersTable.setItems(observableList);
        Platform.runLater(this::drawObjects);
    }

    public void drawObjects() {
        if (visualCanvas == null) return;

        if (animationTimeline != null) {
            animationTimeline.stop();
        }

        animationTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(animationProgress, 0)),
                new KeyFrame(Duration.millis(800), new KeyValue(animationProgress, 1, Interpolator.EASE_OUT))
        );
        animationTimeline.play();
    }

    private void renderCanvasFrame() {
        if (visualCanvas == null) return;

        var session = (bootstrapper != null) ? bootstrapper.getClientSession() : null;
        if (session == null || session.getUserName() == null) {
            return; // Salida limpia. No hay sesión, no hay nada que dibujar.
        }

        if (bootstrapper == null ||
                bootstrapper.getClientSession() == null ||
                bootstrapper.getClientSession().getUserName() == null) {
            return;
        }

        javafx.scene.canvas.GraphicsContext gc = visualCanvas.getGraphicsContext2D();

        double width = Math.max(100, visualCanvas.getWidth());
        double height = Math.max(100, visualCanvas.getHeight());
        double centerX = width / 2;
        double centerY = height / 2;

        gc.clearRect(0, 0, width, height);

        gc.save();
        gc.setStroke(Color.web("#161622"));
        gc.setLineWidth(0.5);

        double gridSize = 40.0;

        for (double x = centerX; x < width; x += gridSize) {
            gc.strokeLine(x, 0, x, height);
        }
        for (double x = centerX; x > 0; x -= gridSize) {
            gc.strokeLine(x, 0, x, height);
        }
        for (double y = centerY; y < height; y += gridSize) {
            gc.strokeLine(0, y, width, y);
        }
        for (double y = centerY; y > 0; y -= gridSize) {
            gc.strokeLine(0, y, width, y);
        }
        gc.restore();

        gc.setStroke(Color.web("#252538"));
        gc.setLineWidth(1.5);
        gc.strokeLine(0, centerY, width, centerY);
        gc.strokeLine(centerX, 0, centerX, height);

        double maxX = 1, maxY = 1;
        for (Worker w : workersTable.getItems()) {
            maxX = Math.max(maxX, Math.abs(w.getCoordinates().getX()));
            maxY = Math.max(maxY, Math.abs(w.getCoordinates().getY()));
        }

        double usableWidth = Math.max(10, centerX - 50);
        double usableHeight = Math.max(10, centerY - 50);
        double globalScale = Math.min(usableWidth / maxX, usableHeight / maxY);
        double progress = animationProgress.get();
        String currentUser = bootstrapper.getClientSession().getUserName();

        List<Worker> sortedWorkers = workersTable.getItems().stream()
                .sorted((w1, w2) -> {
                    int sizeCompare = Long.compare(w2.getSalary(), w1.getSalary());
                    if (sizeCompare != 0) return sizeCompare;
                    return Boolean.compare(w1.getCreatorName().equals(currentUser), w2.getCreatorName().equals(currentUser));
                }).toList();

        sortedWorkers.forEach(worker -> {
            Color userColor = getColorForUser(worker.getCreatorName());
            double x = centerX + (worker.getCoordinates().getX() * globalScale);
            double y = centerY - (worker.getCoordinates().getY() * globalScale);
            double currentSize = (15 + Math.min(worker.getSalary() * 0.0005, 50)) * progress;

            gc.save();
            if (worker.getCreatorName().equals(currentUser)) {
                DropShadow neonGlow = new DropShadow();
                neonGlow.setColor(userColor);
                neonGlow.setRadius(20 * progress);
                neonGlow.setSpread(0.5);
                gc.setEffect(neonGlow);
            }

            gc.setFill(userColor);
            gc.fillOval(x - currentSize / 2, y - currentSize / 2, currentSize, currentSize);
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(1.5);
            gc.strokeOval(x - currentSize / 2, y - currentSize / 2, currentSize, currentSize);
            gc.restore();
        });
    }

    private void setupCanvasInteractions() {
        if (visualCanvas == null) return;

        ContextMenu canvasContextMenu = new ContextMenu();
        MenuItem infoItem = new MenuItem();
        MenuItem updateItem = new MenuItem();
        MenuItem deleteItem = new MenuItem();

        canvasContextMenu.getItems().addAll(infoItem, updateItem, deleteItem);

        visualCanvas.setOnMouseClicked(event -> {
            canvasContextMenu.hide();

            double mouseX = event.getX();
            double mouseY = event.getY();
            double centerX = Math.max(100, visualCanvas.getWidth()) / 2;
            double centerY = Math.max(100, visualCanvas.getHeight()) / 2;

            double maxX = 1, maxY = 1;
            for (Worker w : workersTable.getItems()) {
                maxX = Math.max(maxX, Math.abs(w.getCoordinates().getX()));
                maxY = Math.max(maxY, Math.abs(w.getCoordinates().getY()));
            }

            double globalScale = Math.min(Math.max(10, centerX - 50) / maxX, Math.max(10, centerY - 50) / maxY);
            double progress = animationProgress.get();
            String currentUser = bootstrapper.getClientSession().getUserName();

            List<Worker> items = new ArrayList<>(workersTable.getItems());
            items.sort((w1, w2) -> {
                int sizeCompare = Long.compare(w2.getSalary(), w1.getSalary());
                if (sizeCompare != 0) return sizeCompare;
                return Boolean.compare(w1.getCreatorName().equals(currentUser), w2.getCreatorName().equals(currentUser));
            });
            Collections.reverse(items);

            Optional<Worker> clickedWorker = items.stream().filter(worker -> {
                double x = centerX + (worker.getCoordinates().getX() * globalScale);
                double y = centerY - (worker.getCoordinates().getY() * globalScale);
                double currentSize = (15 + Math.min(worker.getSalary() * 0.0005, 50)) * progress;
                return Math.sqrt(Math.pow(mouseX - x, 2) + Math.pow(mouseY - y, 2)) <= currentSize / 2;
            }).findFirst();

            clickedWorker.ifPresent(worker -> {
                if (event.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
                    if (event.getClickCount() == 1) {
                        showWorkerDetailsDialog(worker);
                    } else if (event.getClickCount() == 2) {
                        workersTable.getSelectionModel().select(worker);
                        handleUpdateAction();
                    }
                } else if (event.getButton() == javafx.scene.input.MouseButton.SECONDARY) {
                    infoItem.setText(resources.getString("menu.info"));
                    updateItem.setText(resources.getString("menu.update"));
                    deleteItem.setText(resources.getString("menu.delete"));

                    infoItem.setOnAction(e -> {
                        showWorkerDetailsDialog(worker);
                        commandHistory.addCommand("menu.info");
                    });

                    updateItem.setOnAction(e -> {
                        workersTable.getSelectionModel().select(worker);
                        handleUpdateAction();
                    });

                    deleteItem.setOnAction(e -> confirmAndRemoveWorker(worker));

                    canvasContextMenu.show(visualCanvas, event.getScreenX(), event.getScreenY());
                }
            });
        });
    }

    private void setBoxComboOptions() {
        languageCombox.getItems().clear();
        languageCombox.getItems().addAll("Español (Colombia)", "Ruso");
        languageCombox.setValue("Español (Colombia)");

        languageCombox.setOnAction(action -> {
            if (languageCombox.getValue() != null) {
                changeLanguage(languageCombox.getValue());
            }
        });
    }

    private void changeLanguage(String language) {
        String languageChoosed = mapLanguages.get(language);
        String locationLanguage = mapLocation.get(languageChoosed);
        Locale locale = new Locale(languageChoosed, locationLanguage);

        this.resources = ResourceBundle.getBundle("client.i18n.Messages", locale);
        currentZone = mapTimeZones.getOrDefault(languageChoosed, ZoneId.systemDefault());

        updateTextInterface();
        closeSidePanel();
        workersTable.refresh();
    }

    private void setupContextMenu() {
        workersTable.setRowFactory(tv -> {
            TableRow<Worker> tableRow = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();

            MenuItem infoItem = new MenuItem(resources.getString("menu.info"));
            MenuItem updateItem = new MenuItem(resources.getString("menu.update"));
            MenuItem deleteItem = new MenuItem(resources.getString("menu.delete"));

            infoItem.setOnAction(event -> {
                showWorkerDetailsDialog(tableRow.getItem());
                commandHistory.addCommand("menu.info");
            });

            updateItem.setOnAction(event -> {
                workersTable.getSelectionModel().select(tableRow.getItem());
                handleUpdateAction();
            });

            deleteItem.setOnAction(event -> confirmAndRemoveWorker(tableRow.getItem()));

            contextMenu.getItems().addAll(infoItem, updateItem, deleteItem);

            tableRow.contextMenuProperty().bind(
                    javafx.beans.binding.Bindings.when(tableRow.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu)
            );
            return tableRow;
        });
    }

    private void confirmAndRemoveWorker(Worker worker) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setTitle(resources.getString("dialog.confirmDelete.title"));
        alert.setHeaderText(String.format(resources.getString("dialog.confirmDelete.header"), worker.getName()));
        alert.setGraphic(null);
        alert.getDialogPane().setGraphic(null);

        styleAndTranslateDialog(alert);

        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                try {
                    Result<Boolean> response = bootstrapper.getProxyWorkerRepository().removeById(worker.getId());

                    if (!response.isSuccess()) {
                        printNetworkError(response.getErrorMessage());
                        return;
                    }

                    printToConsole(resources.getString("console.remove.success"));
                    commandHistory.addCommand("cmd.remove_by_id");
                } catch (Exception e) {
                    printErrorToConsole(e.getMessage());
                }
            }
        });
    }

    private void showWorkerDetailsDialog(Worker worker) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setTitle(resources.getString("dialog.info.title"));
        alert.setHeaderText(resources.getString("dialog.info.header") + worker.getName());
        alert.setGraphic(null);
        alert.getDialogPane().setGraphic(null);

        NumberFormat numberFormat = NumberFormat.getNumberInstance(resources.getLocale());
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(resources.getLocale());

        String details = String.format(
                resources.getString("dialog.info.details"),
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

        styleAndTranslateDialog(alert);
        alert.showAndWait();
    }

    public void printToConsole(String message) {
        if (message == null || message.trim().isEmpty()) return;

        String pattern = resources.containsKey("console.time_format") ? resources.getString("console.time_format") : "HH:mm:ss";
        String timeStamp = LocalTime.now(currentZone).format(DateTimeFormatter.ofPattern(pattern));

        consoleOutput.appendText("[" + timeStamp + "] " + message.trim() + "\n");
    }

    public void printErrorToConsole(String message) {
        if (message == null || message.trim().isEmpty()) return;

        String pattern = resources.containsKey("console.time_format") ? resources.getString("console.time_format") : "HH:mm:ss";
        String timeStamp = LocalTime.now(currentZone).format(DateTimeFormatter.ofPattern(pattern));

        consoleOutput.appendText("[" + timeStamp + "]  ERROR: " + message.trim() + "\n");
    }

    protected void printNetworkError(String serverMessage) {
        try {
            String translatedError = resources.getString(serverMessage);
            printErrorToConsole(translatedError);
        } catch (Exception e) {
            printErrorToConsole(serverMessage);
        }
    }

    private Color getColorForUser(String username) {
        return userColors.computeIfAbsent(username, k -> {
            int hash = Math.abs(k.hashCode());
            return Color.hsb(hash % 360, 0.8, 0.9, 0.55);
        });
    }

    public void updateTextInterface() {
        if (resources == null) return;

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
        sumSalaryItemMenu.setText(resources.getString("menuItem.sumSalary"));
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

        if (welcomeLabel != null && bootstrapper != null) {
            welcomeLabel.setText(String.format(resources.getString("label.welcome"), bootstrapper.getClientSession().getUserName()));
        }

        if (themeButton != null) {
            themeButton.setText(isDarkMode ? resources.getString("theme.light") : resources.getString("theme.dark"));
        }

        if (consoleTitledPane != null) {
            consoleTitledPane.setText(resources.getString("title.console"));
        }
    }

    public void closeSidePanel() {
        mainBorderPane.setRight(null);
    }

    public void setNotificationListener(NotificationListener notificationListener) {
        this.notificationListener = notificationListener;
    }

    public void setRunnerLoginController(RunnerLoginController runnerLoginController) {
        this.runnerLoginController = runnerLoginController;
    }

    public void setBootstrapper(SystemBootstrapper systemBootstrapper) {
        this.bootstrapper = systemBootstrapper;
    }

    public SystemBootstrapper gerSystemBootstrapper() {
        if (bootstrapper != null) return bootstrapper;
        throw new IllegalArgumentException("Error, SystemBootstrapper no ha sido inicializado");
    }

    public ResourceBundle getResources() {
        return resources;
    }

    private void styleAndTranslateDialog(javafx.scene.control.Dialog<?> dialog) {
        try {
            dialog.getDialogPane().getStylesheets().add(getClass().getResource("/css/dark-theme.css").toExternalForm());
            dialog.getDialogPane().getStyleClass().add("form-panel");
        } catch (Exception ignored) {
            // Silenciado para evitar ensuciar la terminal SSH en Helios
        }

        javafx.scene.control.Button okButton = (javafx.scene.control.Button) dialog.getDialogPane().lookupButton(javafx.scene.control.ButtonType.OK);
        if (okButton != null) {
            okButton.setText(resources.containsKey("button.ok") ? resources.getString("button.ok") : "OK");
        }

        javafx.scene.control.Button cancelButton = (javafx.scene.control.Button) dialog.getDialogPane().lookupButton(javafx.scene.control.ButtonType.CANCEL);
        if (cancelButton != null) {
            cancelButton.setText(resources.containsKey("form.cancel") ? resources.getString("form.cancel") : "Cancel");
        }
    }
}
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Main extends Application {

    private FieldManager fieldManager = new FieldManager();
    private String fieldsFilePath; // Pfad zur aktuellen fields.xml
    private String selectedMonth; // Aktuell ausgewählter Monat
    private TableView<Field> tableView;
    private Map<String, Map<String, String>> growthCalendar; // Karte für Frucht und Status basierend auf Monat
    private final String CONFIG_FILE = "config.properties"; // Datei zur Speicherung von Pfad und Monat

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Feldverwaltung LS22");

        TabPane tabPane = new TabPane();

        // Tab 1: Feldverwaltung
        Tab fieldManagementTab = new Tab("Feldverwaltung");
        fieldManagementTab.setClosable(false);

        tableView = createFieldTableView();

        // Tooltip für die Tabelle
        Tooltip tableTooltip = new Tooltip("Zeigt alle Felder mit Frucht, Status und weiteren Informationen an.");
        Tooltip.install(tableView, tableTooltip);

        Button loadButton = new Button("Datei laden");
        loadButton.setTooltip(new Tooltip("Wähle eine fields.xml-Datei aus, um Felder zu laden."));
        loadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("fields.xml auswählen");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML-Dateien", "*.xml"));
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                fieldsFilePath = file.getAbsolutePath();
                saveConfig();
                fieldManager.loadFields(fieldsFilePath);
                tableView.setItems(FXCollections.observableArrayList(fieldManager.getFields()));
            }
        });

        Button saveButton = new Button("Speichern");
        saveButton.setTooltip(new Tooltip("Speichert die gekauften Felder in der geladenen fields.xml-Datei."));
        saveButton.setOnAction(e -> {
            if (fieldsFilePath != null) {
                fieldManager.savePurchasedFields(fieldsFilePath);
            }
        });

        CheckBox filterPurchasedCheckbox = new CheckBox("Nur gekaufte Felder anzeigen");
        filterPurchasedCheckbox.setTooltip(new Tooltip("Filtern Sie die Tabelle, um nur gekaufte Felder anzuzeigen."));
        filterPurchasedCheckbox.setOnAction(e -> {
            if (filterPurchasedCheckbox.isSelected()) {
                // Zeige nur gekaufte Felder
                List<Field> purchasedFields = fieldManager.filterByPurchasedStatus(true);
                tableView.setItems(FXCollections.observableArrayList(purchasedFields));
            } else {
                // Zeige alle Felder
                tableView.setItems(FXCollections.observableArrayList(fieldManager.getFields()));
            }
        });

        // Monatsauswahl ComboBox mit Tooltip
        ComboBox<String> monthComboBox = new ComboBox<>();
        monthComboBox.setTooltip(new Tooltip("Wähle den aktuellen Monat aus, um die Feldstatus zu aktualisieren."));
        monthComboBox.setItems(FXCollections.observableArrayList(
                "Januar", "Februar", "März", "April", "Mai", "Juni",
                "Juli", "August", "September", "Oktober", "November", "Dezember"
        ));
        monthComboBox.setPromptText("Monat wählen");
        monthComboBox.setOnAction(e -> {
            selectedMonth = monthComboBox.getValue();
            saveConfig();
            updateFieldStatusBasedOnMonth(selectedMonth);
        });

        CheckBox darkModeToggle = new CheckBox("Dunkelmodus aktivieren");
        darkModeToggle.setTooltip(new Tooltip("Aktivieren Sie den Dunkelmodus für die Anwendung."));
        darkModeToggle.setOnAction(e -> {
            if (darkModeToggle.isSelected()) {
                // Dunkelmodus aktivieren
                scene.getStylesheets().clear();
                scene.getStylesheets().add(getClass().getResource("dark-mode.css").toExternalForm());
            } else {
                // Hellmodus aktivieren
                scene.getStylesheets().clear();
                scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            }
        });

        HBox buttonBox = new HBox(10, loadButton, saveButton, monthComboBox, filterPurchasedCheckbox);

        // Tooltip für die Buttons
        Tooltip buttonBoxTooltip = new Tooltip("Verwende die Buttons, um Daten zu laden, zu speichern oder den Monat auszuwählen.");
        Tooltip.install(buttonBox, buttonBoxTooltip);

        VBox controlsLayout = new VBox(10, loadButton, saveButton, monthComboBox, filterPurchasedCheckbox, darkModeToggle);
        controlsLayout.setSpacing(10);
        controlsLayout.setStyle("-fx-padding: 15; -fx-alignment: center-left;");

        // Tab 2: Kalender
        Tab calendarTab = new Tab("Kalender");
        calendarTab.setClosable(false);

        GrowthCalendar growthCalendarView = new GrowthCalendar();
        VBox calendarView = growthCalendarView.getCalendarView();

        // Tooltip für den Kalender-Tab
        Tooltip calendarTooltip = new Tooltip("Zeigt den Wachstums- und Erntekalender für alle Feldfrüchte an.");
        Tooltip.install(calendarView, calendarTooltip);

        calendarTab.setContent(calendarView);

        // Tabs hinzufügen
        tabPane.getTabs().addAll(fieldManagementTab, calendarTab);

        // Szene erstellen
        BorderPane root = new BorderPane();
        root.setCenter(tabPane);

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

        // Initialisiere Wachstums-Kalender und lade gespeicherte Konfiguration
        initializeGrowthCalendar();
        loadConfig();

        // Felder und Monat laden (falls vorhanden)
        if (fieldsFilePath != null && !fieldsFilePath.isEmpty()) {
            fieldManager.loadFields(fieldsFilePath);
            tableView.setItems(FXCollections.observableArrayList(fieldManager.getFields()));
        }
        if (selectedMonth != null && !selectedMonth.isEmpty()) {
            monthComboBox.setValue(selectedMonth);
            updateFieldStatusBasedOnMonth(selectedMonth);
        }
    }

    private TableView<Field> createFieldTableView() {
        TableView<Field> tableView = new TableView<>();
        tableView.setEditable(true);

        TableColumn<Field, Number> idColumn = new TableColumn<>("Feld Nr.");
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()));

        TableColumn<Field, String> fruitColumn = new TableColumn<>("Frucht");
        fruitColumn.setCellValueFactory(cellData -> {
            String englishName = cellData.getValue().getPlannedFruit();
            String germanName = fieldManager.translateFruit(englishName);
            return new SimpleStringProperty(germanName);
        });

        TableColumn<Field, Boolean> purchasedColumn = new TableColumn<>("Gekauft");
        purchasedColumn.setCellValueFactory(cellData -> cellData.getValue().purchasedProperty());
        purchasedColumn.setCellFactory(CheckBoxTableCell.forTableColumn(purchasedColumn));

        TableColumn<Field, String> nextFruitColumn = new TableColumn<>("Folge Frucht");
        nextFruitColumn.setCellValueFactory(cellData -> cellData.getValue().nextFruitProperty());
        nextFruitColumn.setCellFactory(column -> {
            return new TableCell<Field, String>() {
                private final ComboBox<String> comboBox = new ComboBox<>();

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                        setGraphic(null);
                        return;
                    }

                    Field field = (Field) getTableRow().getItem();
                    List<String> nextFruitOptions = fieldManager.getNextFruitOptions(field.getPlannedFruit());
                    comboBox.setItems(FXCollections.observableArrayList(nextFruitOptions));
                    comboBox.setValue(field.getNextFruit());
                    comboBox.setOnAction(e -> field.setNextFruit(comboBox.getValue()));
                    setGraphic(comboBox);
                }
            };
        });

// Setze die Breite der Spalte "Folge Frucht"
        nextFruitColumn.setPrefWidth(120); // Passe die Breite hier nach Bedarf an

        TableColumn<Field, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        // Zellen-Renderer für farbliche Darstellung des Status
        statusColumn.setCellFactory(new Callback<TableColumn<Field, String>, TableCell<Field, String>>() {
            @Override
            public TableCell<Field, String> call(TableColumn<Field, String> param) {
                return new TableCell<Field, String>() {
                    @Override
                    protected void updateItem(String status, boolean empty) {
                        super.updateItem(status, empty);

                        if (empty || status == null) {
                            setText(null);
                            setStyle("");
                        } else {
                            setText(status);

                            // Hintergrundfarbe basierend auf dem Status setzen
                            switch (status) {
                                case "Saatzeit":
                                    setStyle("-fx-background-color: lightgreen; -fx-text-fill: black;");
                                    break;
                                case "Wachstum":
                                    setStyle("-fx-background-color: lightblue; -fx-text-fill: black;");
                                    break;
                                case "Erntezeit":
                                    setStyle("-fx-background-color: yellow; -fx-text-fill: black;");
                                    break;
                                case "Geerntet":
                                    setStyle("-fx-background-color: brown; -fx-text-fill: white;");
                                    break;
                                default:
                                    setStyle("");
                                    break;
                            }
                        }
                    }
                };
            }
        });

        tableView.getColumns().addAll(idColumn, fruitColumn, purchasedColumn, nextFruitColumn, statusColumn);

        return tableView;
    }

    private void initializeGrowthCalendar() {
        // Wachstums-Kalender aus GrowthCalendarInitializer abrufen
        growthCalendar = GrowthCalendarInitializer.initializeGrowthCalendar();
    }

    private void updateFieldStatusBasedOnMonth(String selectedMonth) {
        if (selectedMonth == null || selectedMonth.isEmpty()) {
            return;
        }

        for (Field field : tableView.getItems()) {
            // Fruchtname übersetzen
            String fruit = fieldManager.translateFruit(field.getPlannedFruit());

            // Wachstumsdaten abrufen
            Map<String, String> fruitGrowth = growthCalendar.getOrDefault(fruit, null);
            if (fruitGrowth != null) {
                // Status für den Monat abrufen
                String status = fruitGrowth.getOrDefault(selectedMonth, "Offen");
                field.setStatus(status);
            } else {
                field.setStatus("Offen");
            }
        }

        // Tabelle aktualisieren
        tableView.refresh();
    }

    private void saveConfig() {
        try (FileOutputStream output = new FileOutputStream(CONFIG_FILE)) {
            Properties properties = new Properties();
            properties.setProperty("fieldsFilePath", fieldsFilePath != null ? fieldsFilePath : "");
            properties.setProperty("selectedMonth", selectedMonth != null ? selectedMonth : "");
            properties.store(output, "Konfiguration für LS22 Feldverwaltung");
            System.out.println("Konfiguration gespeichert: " + CONFIG_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadConfig() {
        File configFile = new File(CONFIG_FILE);
        if (!configFile.exists()) {
            System.out.println("Keine gespeicherte Konfiguration gefunden.");
            return;
        }

        try (FileInputStream input = new FileInputStream(CONFIG_FILE)) {
            Properties properties = new Properties();
            properties.load(input);
            fieldsFilePath = properties.getProperty("fieldsFilePath", "");
            selectedMonth = properties.getProperty("selectedMonth", "");
            System.out.println("Konfiguration geladen: fieldsFilePath=" + fieldsFilePath + ", selectedMonth=" + selectedMonth);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
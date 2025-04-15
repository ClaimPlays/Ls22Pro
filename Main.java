import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashMap;
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

        Button loadButton = new Button("Datei laden");
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
        saveButton.setOnAction(e -> {
            if (fieldsFilePath != null) {
                fieldManager.savePurchasedFields(fieldsFilePath);
            }
        });

        // Monatsauswahl ComboBox
        ComboBox<String> monthComboBox = new ComboBox<>();
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

        HBox buttonBox = new HBox(10, loadButton, saveButton, monthComboBox);
        VBox fieldManagementLayout = new VBox(10, tableView, buttonBox);
        fieldManagementTab.setContent(fieldManagementLayout);

        // Tab 2: Kalender
        Tab calendarTab = new Tab("Kalender");
        calendarTab.setClosable(false);

        GrowthCalendar growthCalendarView = new GrowthCalendar();
        VBox calendarView = growthCalendarView.getCalendarView();
        calendarTab.setContent(calendarView);

        // Tabs hinzufügen
        tabPane.getTabs().addAll(fieldManagementTab, calendarTab);

        // Szene erstellen
        BorderPane root = new BorderPane();
        root.setCenter(tabPane);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Initialisiere Wachstums-Kalender und lade gespeicherte Konfiguration
        initializeGrowthCalendar();
        loadConfig();

        // Aktualisiere Tabelle basierend auf geladenem Monat
        if (selectedMonth != null && !selectedMonth.isEmpty()) {
            monthComboBox.setValue(selectedMonth);
            updateFieldStatusBasedOnMonth(selectedMonth);
        }
    }

    private TableView<Field> createFieldTableView() {
        TableView<Field> tableView = new TableView<>();
        tableView.setEditable(true);

        TableColumn<Field, Number> idColumn = new TableColumn<>("ID");
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

        TableColumn<Field, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        tableView.getColumns().addAll(idColumn, fruitColumn, purchasedColumn, nextFruitColumn, statusColumn);

        return tableView;
    }

    private void initializeGrowthCalendar() {
        growthCalendar = new HashMap<>();

        // Beispiel: Wachstumsdaten für Kartoffeln
        Map<String, String> potatoGrowth = new HashMap<>();
        potatoGrowth.put("Januar", "Wachstum");
        potatoGrowth.put("Februar", "Wachstum");
        potatoGrowth.put("März", "Saatzeit");
        potatoGrowth.put("April", "Saatzeit");
        potatoGrowth.put("Mai", "Wachstum");
        potatoGrowth.put("Juni", "Erntezeit");
        potatoGrowth.put("Juli", "Erntezeit");
        potatoGrowth.put("August", "Geerntet");
        potatoGrowth.put("September", "Geerntet");
        potatoGrowth.put("Oktober", "Geerntet");
        potatoGrowth.put("November", "Saatzeit");
        potatoGrowth.put("Dezember", "Wachstum");
        growthCalendar.put("Kartoffeln", potatoGrowth);

        // Weitere Früchte hinzufügen...
    }

    private void updateFieldStatusBasedOnMonth(String selectedMonth) {
        if (selectedMonth == null || selectedMonth.isEmpty()) {
            return;
        }

        for (Field field : tableView.getItems()) {
            String fruit = fieldManager.translateFruit(field.getPlannedFruit());
            Map<String, String> fruitGrowth = growthCalendar.getOrDefault(fruit, new HashMap<>());
            String status = fruitGrowth.getOrDefault(selectedMonth, "Offen");
            field.setStatus(status);
        }

        // Tabelle aktualisieren
        tableView.refresh();
    }

    private void saveConfig() {
        try (FileOutputStream output = new FileOutputStream(CONFIG_FILE)) {
            Properties properties = new Properties();
            properties.setProperty("fieldsFilePath", fieldsFilePath != null ? fieldsFilePath : "");
            properties.setProperty("selectedMonth", selectedMonth != null ? selectedMonth : "");
            properties.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadConfig() {
        try (FileInputStream input = new FileInputStream(CONFIG_FILE)) {
            Properties properties = new Properties();
            properties.load(input);
            fieldsFilePath = properties.getProperty("fieldsFilePath", "");
            selectedMonth = properties.getProperty("selectedMonth", "");
        } catch (IOException e) {
            System.out.println("Keine gespeicherte Konfiguration gefunden.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
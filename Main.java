import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tab;

import java.io.*;
import java.util.ArrayList;
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
    private boolean isDarkMode = false; // Zustand des Dunkelmodus

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Feldverwaltung LS22");

        TabPane tabPane = new TabPane();

        // Tab 1: Feldverwaltung
        Tab fieldManagementTab = new Tab("Feldverwaltung");
        fieldManagementTab.setClosable(false);
        ImageView fieldIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/field.png")));
        fieldIcon.setFitWidth(16); // Icon-Breite
        fieldIcon.setFitHeight(16); // Icon-Höhe
        fieldManagementTab.setGraphic(fieldIcon); // Icon zum Tab hinzufügen

        tableView = createFieldTableView();

        // Tooltip für die Tabelle
        Tooltip tableTooltip = new Tooltip("Zeigt alle Felder mit Frucht, Status und weiteren Informationen an.");
        Tooltip.install(tableView, tableTooltip);

        Button assignFruitButton = new Button("Folge Frucht zuweisen");
        assignFruitButton.setTooltip(new Tooltip("Weise eine Folge Frucht für das ausgewählte Feld zu."));
        assignFruitButton.setOnAction(e -> {
            Field selectedField = tableView.getSelectionModel().getSelectedItem();
            if (selectedField != null) {
                FieldManagementPopup.showPopup(selectedField, fieldManager, () -> tableView.refresh());
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Bitte wählen Sie ein Feld aus!");
                alert.showAndWait();
            }
        });

        // Buttons und andere UI-Elemente
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
        filterPurchasedCheckbox.setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 14px;");
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

        // Neuer Button zum Auswählen der Folge Frucht für das ausgewählte Feld
        Button selectFruitForSelectedFieldButton = new Button("Folge Frucht");
        selectFruitForSelectedFieldButton.setTooltip(new Tooltip("Öffnet ein Fenster zur Auswahl einer Folge Frucht für das ausgewählte Feld."));
        selectFruitForSelectedFieldButton.setOnAction(e -> {
            // Hol das ausgewählte Feld
            Field selectedField = tableView.getSelectionModel().getSelectedItem();
            if (selectedField != null) {
                // Zeige das Popup für das ausgewählte Feld
                FieldManagementPopup.showPopup(selectedField, fieldManager, () -> tableView.refresh());
            } else {
                // Warnmeldung anzeigen, wenn kein Feld ausgewählt ist
                Alert alert = new Alert(Alert.AlertType.WARNING, "Bitte wählen Sie ein Feld aus!");
                alert.showAndWait();
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

        // Label "Aktueller Monat"
        Label currentMonthLabel = new Label("Aktueller Monat: ");
        currentMonthLabel.setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 14px;"); // Optional: Hervorhebung des Labels

        // Label "Aktueller Monat" und Dropdown in eine HBox packen
        HBox monthSelectionBox = new HBox(0); // Abstand von 5px zwischen Label und Dropdown
        monthSelectionBox.getChildren().addAll(currentMonthLabel, monthComboBox);
        monthSelectionBox.setAlignment(Pos.CENTER_LEFT); // Ausrichtung der Elemente nach links

        // Layout mit GridPane
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Layout für die Elemente
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(monthSelectionBox, filterPurchasedCheckbox);
        layout.setStyle("-fx-background-color: rgba(80, 124, 123, 1);"); // Semi-transparenter Hintergrund

        // Buttons und Filter in die obere Zeile des GridPane setzen
        gridPane.add(loadButton, 0, 0);
        gridPane.add(selectFruitForSelectedFieldButton, 1, 0); // Neuer Button für das ausgewählte Feld
        gridPane.add(saveButton, 2, 0);
        gridPane.add(monthSelectionBox, 3, 0); // HBox mit Label und Dropdown direkt positioniert

        // ** Abstand nach unten hinzufügen **
        VBox.setMargin(gridPane, new Insets(10, 0, 20, 0)); // Abstand zwischen Buttons und Tabelle

        // Füge diesen Button zum Layout hinzu, z.B. unterhalb der Tabelle
        VBox.setMargin(assignFruitButton, new Insets(10, 0, 0, 0)); // Optional:

        // Tabelle in die nächste Zeile setzen
        gridPane.add(tableView, 0, 1, 4, 1); // Tabelle über die gesamte Breite

        // Filteroption unter die Tabelle setzen
        GridPane.setMargin(filterPurchasedCheckbox, new Insets(10, 0, 0, 0)); // Abstand zwischen Tabelle und Checkbox
        gridPane.add(filterPurchasedCheckbox, 0, 2, 4, 1); // Checkbox über die gesamte Breite

        // Tab-Inhalt setzen
        VBox fieldManagementLayout = new VBox(10, gridPane);
        fieldManagementTab.setContent(fieldManagementLayout);

        // Tab 2: Kalender
        Tab calendarTab = new Tab("Kalender");
        calendarTab.setClosable(false);
        ImageView calendarIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/calendar.png")));
        calendarIcon.setFitWidth(16); // Icon-Breite
        calendarIcon.setFitHeight(16); // Icon-Höhe
        calendarTab.setGraphic(calendarIcon); // Icon zum Tab hinzufügen

        GrowthCalendar growthCalendarView = new GrowthCalendar();
        VBox calendarView = growthCalendarView.getCalendarView();

        // Tooltip für den Kalender-Tab
        Tooltip calendarTooltip = new Tooltip("Zeigt den Wachstums- und Erntekalender für alle Feldfrüchte an.");
        Tooltip.install(calendarView, calendarTooltip);

        calendarTab.setContent(calendarView);

        SettingsTab settingsTab = new SettingsTab(primaryStage); // Hier wird die primaryStage übergeben
        Tab settingsTabContent = settingsTab.createSettingsTab();

        // Tabs hinzufügen
        tabPane.getTabs().addAll(fieldManagementTab, calendarTab, settingsTabContent);

        // Hintergrundbild festlegen
        Image backgroundImage = new Image(getClass().getResourceAsStream("/images/background.jpg")); // Bild aus dem Ordner "images"
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT, // Wiederholung horizontal
                BackgroundRepeat.NO_REPEAT, // Wiederholung vertikal
                BackgroundPosition.DEFAULT, // Standardposition
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true) // Größe anpassen
        );

        // Hintergrund dem TabPane hinzufügen
        tabPane.setBackground(new Background(background));

        // Szene erstellen
        BorderPane root = new BorderPane();
        root.setCenter(tabPane);

        Scene scene = new Scene(root, 920, 610);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
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
        nextFruitColumn.setCellValueFactory(cellData -> cellData.getValue().nextFruitProperty()); // Bindung an nextFruit

        // Kein Button mehr, nur Text wird in der Spalte angezeigt
        nextFruitColumn.setCellFactory(column -> new TableCell<Field, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item); // Zeige die aktuelle Folge Frucht an
                    setGraphic(null); // Entferne jegliche grafische Komponenten
                }
            }
        });

        TableColumn<Field, String> sowingTimeColumn = new TableColumn<>("Saatzeit der Folge Frucht");
        sowingTimeColumn.setCellValueFactory(cellData -> {
            String nextFruit = cellData.getValue().getNextFruit();
            if (nextFruit == null || nextFruit.isEmpty()) {
                return new SimpleStringProperty("Keine Folge Frucht");
            }

            // Utilize growthCalendar to fetch the sowing period for the selected next fruit
            Map<String, String> fruitGrowth = growthCalendar.get(nextFruit);
            if (fruitGrowth != null) {
                String sowingPeriod = null;
                for (Map.Entry<String, String> entry : fruitGrowth.entrySet()) {
                    if (entry.getValue().equals("Saatzeit")) {
                        if (sowingPeriod == null) {
                            sowingPeriod = entry.getKey();
                        } else {
                            sowingPeriod += ", " + entry.getKey();
                        }
                    }
                }
                return new SimpleStringProperty(sowingPeriod != null ? sowingPeriod : "Keine Saatzeit definiert");
            }
            return new SimpleStringProperty("Unbekannte Frucht");
        });

// Set CellFactory to style the column green if it matches the selected month
        sowingTimeColumn.setCellFactory(column -> new TableCell<Field, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);

                    // If the sowing period contains the selected month, highlight it in green
                    if (selectedMonth != null && item.contains(selectedMonth)) {
                        setStyle("-fx-background-color: lightgreen; -fx-text-fill: black;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        nextFruitColumn.setPrefWidth(110);
        fruitColumn.setPrefWidth(100);
        sowingTimeColumn.setPrefWidth(180);

        TableColumn<Field, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

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

        tableView.getColumns().addAll(idColumn, fruitColumn, purchasedColumn, nextFruitColumn, statusColumn, sowingTimeColumn);

        return tableView;
    }

    private void initializeGrowthCalendar() {
        growthCalendar = GrowthCalendarInitializer.initializeGrowthCalendar();
    }

    private void updateFieldStatusBasedOnMonth(String selectedMonth) {
        if (selectedMonth == null || selectedMonth.isEmpty()) {
            return;
        }

        for (Field field : tableView.getItems()) {
            String fruitToCheck = field.getNextFruit() != null && !field.getNextFruit().isEmpty()
                    ? field.getNextFruit() // Verwende die Folgefrucht, falls vorhanden
                    : fieldManager.translateFruit(field.getPlannedFruit()); // Andernfalls die geplante Frucht

            Map<String, String> fruitGrowth = growthCalendar.getOrDefault(fruitToCheck, null);
            if (fruitGrowth != null) {
                String status = fruitGrowth.getOrDefault(selectedMonth, "--");
                field.setStatus(status);
            } else {
                field.setStatus("--");
            }
        }

        tableView.refresh();
    }

    private void saveConfig() {
        try (FileOutputStream output = new FileOutputStream(CONFIG_FILE)) {
            Properties properties = new Properties();
            properties.setProperty("fieldsFilePath", fieldsFilePath != null ? fieldsFilePath : "");
            properties.setProperty("selectedMonth", selectedMonth != null ? selectedMonth : "");
            properties.store(output, "Konfiguration für LS22 Feldverwaltung");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadConfig() {
        File configFile = new File(CONFIG_FILE);
        if (!configFile.exists()) {
            return;
        }

        try (FileInputStream input = new FileInputStream(CONFIG_FILE)) {
            Properties properties = new Properties();
            properties.load(input);
            fieldsFilePath = properties.getProperty("fieldsFilePath", "");
            selectedMonth = properties.getProperty("selectedMonth", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showSelectedFieldPopup() {
        Field selectedField = tableView.getSelectionModel().getSelectedItem();

        if (selectedField != null) {
            FieldManagementPopup.showPopup(selectedField, fieldManager, () -> tableView.refresh());
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Bitte wählen Sie ein Feld aus!");
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
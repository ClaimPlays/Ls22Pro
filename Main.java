import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.List;

public class Main extends Application {

    private FieldManager fieldManager = new FieldManager();
    private String fieldsFilePath; // Pfad zur aktuellen fields.xml

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Feldverwaltung LS22");

        TableView<Field> tableView = new TableView<>();
        tableView.setEditable(true);

        // Spalten erstellen
        TableColumn<Field, Number> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()));

        TableColumn<Field, String> fruitColumn = new TableColumn<>("Frucht");
        fruitColumn.setCellValueFactory(cellData -> {
            String englishName = cellData.getValue().getPlannedFruit();
            String germanName = fieldManager.translateFruit(englishName); // Übersetzen ins Deutsche
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

        tableView.getColumns().addAll(idColumn, fruitColumn, purchasedColumn, nextFruitColumn);

        // Layout und Buttons
        Button loadButton = new Button("Datei laden");
        loadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("fields.xml auswählen");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML-Dateien", "*.xml"));
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                fieldsFilePath = file.getAbsolutePath();
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

        HBox buttonBox = new HBox(10, loadButton, saveButton);
        VBox layout = new VBox(10, tableView, buttonBox);

        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
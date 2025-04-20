import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.scene.Scene;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.util.List;

public class FieldManagementPopup {

    public static void showPopup(Field field, FieldManager fieldManager, Runnable onSave) {
        // Neues Fenster erstellen
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Folge Frucht auswählen");

        // Überschrift
        Label titleLabel = new Label("Folge Frucht auswählen");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;");

        // Informationstext
        Label infoLabel = new Label("Wähle die Folge Frucht für das Feld Nr. " + field.getId() + ":");
        infoLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");

        // Dropdown für die Auswahl der Folge Frucht
        ComboBox<String> fruitComboBox = new ComboBox<>();
        fruitComboBox.setItems(FXCollections.observableArrayList(
                fieldManager.getNextFruitOptions(field.getPlannedFruit())
        ));
        fruitComboBox.setValue(field.getNextFruit()); // Aktuelle Auswahl setzen
        fruitComboBox.setStyle("-fx-font-size: 14px;");

        // Vorschau-Icon für Frucht
        ImageView fruitPreview = new ImageView();
        fruitPreview.setFitWidth(100); // Breite auf 100 Pixel skalieren
        fruitPreview.setFitHeight(100); // Höhe auf 100 Pixel skalieren
        fruitPreview.setPreserveRatio(true); // Seitenverhältnis beibehalten
        fruitComboBox.setOnAction(e -> {
            String selectedFruit = fruitComboBox.getValue();
            fruitPreview.setImage(getFruitImage(selectedFruit));
        });

// Setze die aktuelle Fruchtvorschau
        fruitPreview.setImage(getFruitImage(field.getNextFruit()));

        // Layout für den Dialog
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));
        layout.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #ddd; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(titleLabel, infoLabel, fruitComboBox, fruitPreview);

        // Button zum Bestätigen
        Button saveButton = new Button("Speichern");
        saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5 10;");
        saveButton.setOnAction(e -> {
            field.setNextFruit(fruitComboBox.getValue());
            fieldManager.autoSaveFields(); // Automatisch speichern
            onSave.run(); // Tabelle aktualisieren
            popupStage.close(); // Fenster schließen
        });

        // Button zum Abbrechen
        Button cancelButton = new Button("Abbrechen");
        cancelButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5 10;");
        cancelButton.setOnAction(e -> popupStage.close());

        // Buttons nebeneinander anordnen
        HBox buttonLayout = new HBox(10, saveButton, cancelButton);
        buttonLayout.setAlignment(Pos.CENTER);
        layout.getChildren().add(buttonLayout);

        // Szene erstellen und anzeigen
        Scene scene = new Scene(layout, 350, 300);
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }

    // Hilfsmethode zum Laden von Fruchtbildern
    private static Image getFruitImage(String fruitName) {
        if (fruitName == null || fruitName.isEmpty()) {
            return null;
        }
        String imagePath = "/images/" + fruitName.toLowerCase() + ".png";
        try {
            Image image = new Image(FieldManagementPopup.class.getResourceAsStream(imagePath));
            if (image.isError()) {
                System.err.println("Fehler beim Laden des Bildes: " + imagePath);
            } else {
                System.out.println("Bild erfolgreich geladen: " + imagePath);
            }
            return image;
        } catch (Exception e) {
            System.err.println("Fehler beim Laden des Bildes: " + imagePath);
            return null;
        }
    }
}
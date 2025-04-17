import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class SettingsTab {

    private final Stage primaryStage;

    public SettingsTab(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Tab createSettingsTab() {
        Tab settingsTab = new Tab("Einstellungen");
        settingsTab.setClosable(false);

        // Dunkelmodus-Toggle
        ToggleButton darkModeToggle = new ToggleButton("Dunkelmodus");
        darkModeToggle.setOnAction(e -> {
            boolean isDarkMode = darkModeToggle.isSelected();
            if (isDarkMode) {
                primaryStage.getScene().getStylesheets().clear();
                primaryStage.getScene().getStylesheets().add(getClass().getResource("dark-mode.css").toExternalForm());
            } else {
                primaryStage.getScene().getStylesheets().clear();
                primaryStage.getScene().getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            }
        });

        // Spracheinstellungen
        ComboBox<String> languageComboBox = new ComboBox<>();
        languageComboBox.getItems().addAll("Deutsch", "Englisch");
        languageComboBox.setPromptText("Sprache wählen");
        languageComboBox.setOnAction(e -> {
            String selectedLanguage = languageComboBox.getValue();
            System.out.println("Gewählte Sprache: " + selectedLanguage);
            // Hier könnte eine Logik zum Wechseln der Sprache hinzugefügt werden.
        });

        // Standard-Dateipfad
        TextField filePathField = new TextField();
        filePathField.setPromptText("Standardpfad für Dateien");
        Button browseButton = new Button("Durchsuchen");
        browseButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Datei auswählen");
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                filePathField.setText(selectedFile.getAbsolutePath());
            }
        });

        // Benachrichtigungseinstellungen
        CheckBox notificationCheckBox = new CheckBox("Benachrichtigungen aktivieren");
        notificationCheckBox.setSelected(true); // Standardmäßig aktiviert

        // Zurücksetzen-Button
        Button resetButton = new Button("Zurücksetzen");
        resetButton.setOnAction(e -> {
            boolean confirmed = showConfirmationDialog("Einstellungen zurücksetzen", "Möchten Sie wirklich alle Einstellungen zurücksetzen?");
            if (confirmed) {
                darkModeToggle.setSelected(false);
                languageComboBox.setValue(null);
                filePathField.clear();
                notificationCheckBox.setSelected(true);
            }
        });

        // Layout für den Tab
        VBox settingsLayout = new VBox(10);
        settingsLayout.getChildren().addAll(
                new Label("Einstellungen:"),
                darkModeToggle,
                new Label("Sprache:"),
                languageComboBox,
                new Label("Standardpfad:"),
                filePathField, browseButton,
                notificationCheckBox,
                resetButton
        );

        settingsTab.setContent(settingsLayout);

        return settingsTab;
    }

    private boolean showConfirmationDialog(String title, String content) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle(title);
        confirmDialog.setContentText(content);

        ButtonType result = confirmDialog.showAndWait().orElse(ButtonType.CANCEL);
        return result == ButtonType.OK;
    }
}
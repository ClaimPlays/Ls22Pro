import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class WorkTab {

    public Tab createWorkTab() {
        Tab workTab = new Tab("Arbeit");
        workTab.setClosable(false);

        // To-Do-Liste erstellen
        ListView<String> todoListView = new ListView<>();
        ObservableList<String> todoItems = FXCollections.observableArrayList(
                "Arbeitsaufgabe 1",
                "Arbeitsaufgabe 2",
                "Arbeitsaufgabe 3"
        );
        todoListView.setItems(todoItems);

        // Eingabefeld für neue Aufgaben
        TextField newTaskField = new TextField();
        newTaskField.setPromptText("Neue Aufgabe hinzufügen");

        // Hinzufügen-Button
        Button addTaskButton = new Button("Hinzufügen");
        addTaskButton.setOnAction(e -> {
            String newTask = newTaskField.getText().trim();
            if (!newTask.isEmpty()) {
                todoItems.add(newTask);
                newTaskField.clear();
            }
        });

        // Entfernen-Button
        Button removeTaskButton = new Button("Entfernen");
        removeTaskButton.setOnAction(e -> {
            String selectedTask = todoListView.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                todoItems.remove(selectedTask);
            }
        });

        // Layout für die To-Do-Liste
        VBox workLayout = new VBox(10);
        workLayout.getChildren().addAll(new Label("To-Do-Liste:"), todoListView, newTaskField, addTaskButton, removeTaskButton);

        workTab.setContent(workLayout);

        return workTab;
    }
}
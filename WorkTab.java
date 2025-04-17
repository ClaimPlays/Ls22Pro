import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class WorkTab {

    private FieldManager fieldManager; // Referenz auf FieldManager

    public WorkTab(FieldManager fieldManager) {
        this.fieldManager = fieldManager;
    }

    public Tab createWorkTab() {
        Tab workTab = new Tab("Arbeit");
        workTab.setClosable(false);

        // Tabelle für die Arbeitsübersicht
        TableView<FieldWorkStep> tableView = new TableView<>();
        TableColumn<FieldWorkStep, String> fieldColumn = new TableColumn<>("Feld Nr.");
        fieldColumn.setCellValueFactory(data -> data.getValue().fieldIdProperty());

        TableColumn<FieldWorkStep, String> stepColumn = new TableColumn<>("Arbeitsschritt");
        stepColumn.setCellValueFactory(data -> data.getValue().workStepProperty());

        TableColumn<FieldWorkStep, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(data -> data.getValue().statusProperty());

        tableView.getColumns().addAll(fieldColumn, stepColumn, statusColumn);

        // Daten laden: Gekaufte Felder und Arbeitsschritte
        ObservableList<FieldWorkStep> workSteps = FXCollections.observableArrayList();

        // Debugging-Ausgabe: Gekaufte Felder
        System.out.println("Gekaufte Felder (Filter):");
        for (Field field : fieldManager.filterByPurchasedStatus(true)) {
            System.out.println("Feld ID=" + field.getId() + ", Purchased=" + field.isPurchased() + ", Status=" + field.getStatus());
            if ("Geerntet".equals(field.getStatus())) {
                workSteps.addAll(generateWorkStepsForField(field));
            }
        }

        tableView.setItems(workSteps);

        // Layout für den Tab
        VBox workLayout = new VBox(10);
        workLayout.getChildren().addAll(new Label("Arbeitsübersicht für gekaufte Felder:"), tableView);

        workTab.setContent(workLayout);

        return workTab;
    }

    private ObservableList<FieldWorkStep> generateWorkStepsForField(Field field) {
        ObservableList<FieldWorkStep> steps = FXCollections.observableArrayList();
        String[] workSteps = {"Kalken", "Mulchen", "Pflügen", "Walzen", "Unkraut Striegel", "Spritzen", "Steine entfernen", "Düngen"};
        for (String step : workSteps) {
            steps.add(new FieldWorkStep(field.getId(), step, "Ausstehend"));
        }
        return steps;
    }
}
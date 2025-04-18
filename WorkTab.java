import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class WorkTab {

    private final FieldManager fieldManager; // Referenz auf FieldManager

    public WorkTab(FieldManager fieldManager) {
        this.fieldManager = fieldManager;
    }

    /**
     * Erstellt den Arbeit-Tab mit einer Tabelle für Arbeitsübersicht.
     * @return Der Arbeit-Tab.
     */
    public Tab createWorkTab() {
        Tab workTab = new Tab("Arbeit");
        workTab.setClosable(false);

        // Tabelle für die Arbeitsübersicht erstellen
        TableView<FieldWorkStep> tableView = createWorkTableView();

        // Daten für die Tabelle laden
        ObservableList<FieldWorkStep> workSteps = loadWorkSteps();
        tableView.setItems(workSteps);

        // Layout für den Tab
        VBox workLayout = new VBox(10);
        workLayout.getChildren().addAll(new Label("Arbeitsübersicht für gekaufte Felder:"), tableView);

        workTab.setContent(workLayout);

        return workTab;
    }

    /**
     * Erstellt die TableView für die Arbeitsübersicht.
     * @return Die konfigurierte TableView.
     */
    private TableView<FieldWorkStep> createWorkTableView() {
        TableView<FieldWorkStep> tableView = new TableView<>();

        // Spalte: Feld Nr.
        TableColumn<FieldWorkStep, String> fieldColumn = new TableColumn<>("Feld Nr.");
        fieldColumn.setCellValueFactory(data -> data.getValue().fieldIdProperty());

        // Spalte: Arbeitsschritt
        TableColumn<FieldWorkStep, String> stepColumn = new TableColumn<>("Arbeitsschritt");
        stepColumn.setCellValueFactory(data -> data.getValue().workStepProperty());

        // Spalte: Status
        TableColumn<FieldWorkStep, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(data -> data.getValue().statusProperty());

        // Spalten hinzufügen
        tableView.getColumns().addAll(fieldColumn, stepColumn, statusColumn);

        return tableView;
    }

    /**
     * Lädt die Arbeitsschritte basierend auf gekauften Feldern und deren Status.
     * @return Eine Liste von Arbeitsschritten.
     */
    private ObservableList<FieldWorkStep> loadWorkSteps() {
        ObservableList<FieldWorkStep> workSteps = FXCollections.observableArrayList();

        // Debugging-Ausgabe: Gekaufte Felder
        System.out.println("Gekaufte Felder (Filter):");
        for (Field field : fieldManager.filterByPurchasedStatus(true)) {
            System.out.println("Feld ID=" + field.getId() + ", Purchased=" + field.isPurchased() + ", Status=" + field.getStatus());

            // Arbeitsschritte nur für bestimmte Status generieren
            if (shouldGenerateWorkSteps(field)) {
                workSteps.addAll(generateWorkStepsForField(field));
            }
        }

        return workSteps;
    }

    /**
     * Überprüft, ob für ein bestimmtes Feld Arbeitsschritte generiert werden sollen.
     * @param field Das zu überprüfende Feld.
     * @return true, wenn Arbeitsschritte generiert werden sollen, sonst false.
     */
    private boolean shouldGenerateWorkSteps(Field field) {
        // Arbeitsschritte nur für Felder mit dem Status "Geerntet" oder "Saatzeit"
        return "Geerntet".equals(field.getStatus()) || "Saatzeit".equals(field.getStatus());
    }

    /**
     * Generiert Arbeitsschritte für ein bestimmtes Feld.
     * @param field Das Feld, für das Arbeitsschritte generiert werden sollen.
     * @return Eine Liste von Arbeitsschritten.
     */
    private ObservableList<FieldWorkStep> generateWorkStepsForField(Field field) {
        ObservableList<FieldWorkStep> steps = FXCollections.observableArrayList();
        String[] workSteps = {"Kalken", "Mulchen", "Pflügen", "Walzen", "Unkraut Striegel", "Spritzen", "Steine entfernen", "Düngen"};

        for (String step : workSteps) {
            steps.add(new FieldWorkStep(field.getId(), step, "Ausstehend"));
        }
        return steps;
    }
}
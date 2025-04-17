import javafx.beans.property.SimpleStringProperty;

public class FieldWorkStep {
    private final SimpleStringProperty fieldId;
    private final SimpleStringProperty workStep;
    private final SimpleStringProperty status;

    public FieldWorkStep(int fieldId, String workStep, String status) {
        this.fieldId = new SimpleStringProperty(String.valueOf(fieldId));
        this.workStep = new SimpleStringProperty(workStep);
        this.status = new SimpleStringProperty(status);
    }

    public SimpleStringProperty fieldIdProperty() {
        return fieldId;
    }

    public SimpleStringProperty workStepProperty() {
        return workStep;
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }
}
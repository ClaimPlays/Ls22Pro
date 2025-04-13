import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class Field {
    private final int id; // ID des Feldes
    private final String plannedFruit; // Aktuelle Frucht
    private final SimpleBooleanProperty purchased; // Ob das Feld gekauft wurde
    private final SimpleStringProperty nextFruit; // Folge Frucht

    public Field(int id, String plannedFruit, boolean purchased, String nextFruit) {
        this.id = id;
        this.plannedFruit = plannedFruit;
        this.purchased = new SimpleBooleanProperty(purchased);
        this.nextFruit = new SimpleStringProperty(nextFruit);
    }

    public int getId() {
        return id;
    }

    public String getPlannedFruit() {
        return plannedFruit;
    }

    public boolean isPurchased() {
        return purchased.get();
    }

    public void setPurchased(boolean purchased) {
        this.purchased.set(purchased);
    }

    public SimpleBooleanProperty purchasedProperty() {
        return purchased;
    }

    public String getNextFruit() {
        return nextFruit.get();
    }

    public void setNextFruit(String nextFruit) {
        this.nextFruit.set(nextFruit);
    }

    public SimpleStringProperty nextFruitProperty() {
        return nextFruit;
    }
}
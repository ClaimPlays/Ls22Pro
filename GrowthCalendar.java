import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class GrowthCalendar {

    public VBox getCalendarView() {
        TableView<CropGrowth> tableView = new TableView<>();

        // Spalten erstellen
        TableColumn<CropGrowth, String> cropColumn = new TableColumn<>("Pflanze");
        cropColumn.setCellValueFactory(new PropertyValueFactory<>("crop"));

        TableColumn<CropGrowth, String> sowingColumn = new TableColumn<>("Saatzeit");
        sowingColumn.setCellValueFactory(new PropertyValueFactory<>("sowingPeriod"));

        TableColumn<CropGrowth, String> harvestColumn = new TableColumn<>("Erntezeit");
        harvestColumn.setCellValueFactory(new PropertyValueFactory<>("harvestPeriod"));

        tableView.getColumns().addAll(cropColumn, sowingColumn, harvestColumn);

        // Daten hinzufügen
        ObservableList<CropGrowth> data = FXCollections.observableArrayList(
                new CropGrowth("Weizen", "März - April", "Juli - August"),
                new CropGrowth("Gerste", "März - April", "Juli - August"),
                new CropGrowth("Raps", "August - September", "Juli - August"),
                new CropGrowth("Hafer", "März - April", "Juli - August"),
                new CropGrowth("Mais", "April - Mai", "Oktober - November"),
                new CropGrowth("Sonnenblumen", "April - Mai", "Oktober - November"),
                new CropGrowth("Sojabohnen", "April - Mai", "Oktober - November"),
                new CropGrowth("Kartoffeln", "März - April", "Juli - August"),
                new CropGrowth("Zuckerrüben", "März - April", "Juli - August"),
                new CropGrowth("Zuckerrohr", "März - April", "Oktober - November"),
                new CropGrowth("Baumwolle", "April - Mai", "Oktober - November"),
                new CropGrowth("Sorghumhirse", "April - Mai", "Oktober - November"),
                new CropGrowth("Trauben", "März - Mai", "September - Oktober"),
                new CropGrowth("Oliven", "März - Mai", "Oktober - November"),
                new CropGrowth("Pappel", "März - April", "Ganzjährig"),
                new CropGrowth("Gras", "Ganzjährig", "Ganzjährig"),
                new CropGrowth("Ölrettich", "März - Mai", "April - Juni")
        );

        tableView.setItems(data);

        VBox vbox = new VBox(tableView);
        return vbox;
    }

    // Innere Klasse für die Daten des Kalenders
    public static class CropGrowth {
        private String crop;
        private String sowingPeriod;
        private String harvestPeriod;

        public CropGrowth(String crop, String sowingPeriod, String harvestPeriod) {
            this.crop = crop;
            this.sowingPeriod = sowingPeriod;
            this.harvestPeriod = harvestPeriod;
        }

        public String getCrop() {
            return crop;
        }

        public void setCrop(String crop) {
            this.crop = crop;
        }

        public String getSowingPeriod() {
            return sowingPeriod;
        }

        public void setSowingPeriod(String sowingPeriod) {
            this.sowingPeriod = sowingPeriod;
        }

        public String getHarvestPeriod() {
            return harvestPeriod;
        }

        public void setHarvestPeriod(String harvestPeriod) {
            this.harvestPeriod = harvestPeriod;
        }
    }
}
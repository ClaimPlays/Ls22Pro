import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.Map;

public class GrowthCalendar {

    public VBox getCalendarView() {
        GridPane grid = new GridPane();
        grid.setGridLinesVisible(true);

        // Initialisiere den Wachstums-Kalender
        Map<String, Map<String, String>> growthCalendar = GrowthCalendarInitializer.initializeGrowthCalendar();

        String[] months = {
                "Januar", "Februar", "März", "April", "Mai", "Juni",
                "Juli", "August", "September", "Oktober", "November", "Dezember"
        };

        // Kopfzeilen (Monate)
        for (int i = 0; i < months.length; i++) {
            Text monthText = new Text(months[i]);
            monthText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            GridPane.setHalignment(monthText, HPos.CENTER);
            grid.add(monthText, i + 1, 0); // Spaltenindex 1-basiert
        }

        int rowIndex = 1; // Startindex für die Zeilen

        // Zeilen für jede Frucht (Saatzeit und Erntezeit separat)
        for (Map.Entry<String, Map<String, String>> entry : growthCalendar.entrySet()) {
            String crop = entry.getKey();
            Map<String, String> growthData = entry.getValue();

            // Hintergrundfarbe für jede zweite Fruchtzeile
            Color backgroundColor = (rowIndex / 2) % 2 == 0 ? Color.GRAY : Color.LIGHTBLUE;

            // Fruchtname mit Icon (eine Zelle für beide Zeilen)
            HBox cropCell = new HBox(5);
            cropCell.setPadding(new Insets(5));
            cropCell.setSpacing(10);
            cropCell.setBackground(new Background(new BackgroundFill(backgroundColor, null, null)));

            ImageView cropIcon = getCropIcon(crop);
            if (cropIcon != null) {
                cropCell.getChildren().add(cropIcon);
            }

            Text cropText = new Text(crop);
            cropText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            cropText.setFill(Color.WHITE); // Textfarbe Weiß für bessere Lesbarkeit auf Dunkelblau
            cropCell.getChildren().add(cropText);

            GridPane.setRowSpan(cropCell, 2); // Überspannt zwei Zeilen
            grid.add(cropCell, 0, rowIndex);

            // Erste Zeile: Saatzeit
            for (int colIndex = 0; colIndex < months.length; colIndex++) {
                String currentMonth = months[colIndex];
                Pane cell = new Pane();
                cell.setPrefSize(60, 25); // Mindestgröße der Zelle
                GridPane.setMargin(cell, new Insets(2)); // Abstand zwischen Zellen

                if (growthData.getOrDefault(currentMonth, "").equals("Saatzeit")) {
                    cell.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, null, null)));
                } else {
                    cell.setBackground(new Background(new BackgroundFill(backgroundColor, null, null))); // Hintergrundfarbe je nach Zeile
                }

                grid.add(cell, colIndex + 1, rowIndex);
            }

            // Zweite Zeile: Erntezeit
            for (int colIndex = 0; colIndex < months.length; colIndex++) {
                String currentMonth = months[colIndex];
                Pane cell = new Pane();
                cell.setPrefSize(60, 25); // Mindestgröße der Zelle
                GridPane.setMargin(cell, new Insets(2)); // Abstand zwischen Zellen

                if (growthData.getOrDefault(currentMonth, "").equals("Erntezeit")) {
                    cell.setBackground(new Background(new BackgroundFill(Color.ORANGE, null, null)));
                } else {
                    cell.setBackground(new Background(new BackgroundFill(backgroundColor, null, null))); // Hintergrundfarbe je nach Zeile
                }

                grid.add(cell, colIndex + 1, rowIndex + 1);
            }

            // Erhöhe den Zeilenindex um 2 (für Saat- und Erntezeile)
            rowIndex += 2;
        }

        // ScrollPane für große Tabellen
        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true); // Passt die Breite an
        scrollPane.setFitToHeight(true); // Passt die Höhe an

        VBox container = new VBox(10, scrollPane);
        container.setPadding(new Insets(10)); // Abstand um den gesamten Kalender
        return container;
    }

    // Hilfsmethode, um Icons für Früchte zu laden
    private ImageView getCropIcon(String crop) {
        try {
            String iconPath = "/images/" + crop.toLowerCase() + ".png";
            Image image = new Image(getClass().getResourceAsStream(iconPath));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(20); // Icon-Größe anpassen
            imageView.setFitHeight(20);
            return imageView;
        } catch (Exception e) {
            System.err.println("Icon für Frucht nicht gefunden: " + crop);
            return null;
        }
    }
}
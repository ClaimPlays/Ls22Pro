import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class GrowthCalendarLoader {

    public static Map<String, Map<String, String>> loadGrowthCalendar(String filePath) {
        Map<String, Map<String, String>> growthCalendar = new HashMap<>();

        try {
            File file = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            document.getDocumentElement().normalize();

            NodeList fruits = document.getElementsByTagName("fruit");

            for (int i = 0; i < fruits.getLength(); i++) {
                Node fruitNode = fruits.item(i);

                if (fruitNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element fruitElement = (Element) fruitNode;
                    String fruitName = fruitElement.getAttribute("name");

                    Map<String, String> growthPeriods = new HashMap<>();
                    NodeList periods = fruitElement.getElementsByTagName("period");

                    for (int j = 0; j < periods.getLength(); j++) {
                        Node periodNode = periods.item(j);

                        if (periodNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element periodElement = (Element) periodNode;
                            String periodIndex = periodElement.getAttribute("index");
                            String plantingAllowed = periodElement.getAttribute("plantingAllowed");

                            if (plantingAllowed != null && plantingAllowed.equals("true")) {
                                growthPeriods.put(periodIndex, "Saatzeit");
                            } else {
                                // Standardwert, falls kein Status definiert ist
                                growthPeriods.put(periodIndex, "Wachstum");
                            }
                        }
                    }

                    growthCalendar.put(fruitName, growthPeriods);
                }
            }
        } catch (Exception e) {
            System.err.println("Fehler beim Laden des Wachstums-Kalenders: " + e.getMessage());
        }

        return growthCalendar;
    }
}
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.*;

public class FieldManager {
    private List<Field> fields;
    private Map<String, String> translationMap; // Für die Übersetzung von Fruchtnamen

    public FieldManager() {
        fields = new ArrayList<>();
        initializeTranslationMap(); // Übersetzungstabelle initialisieren
    }

    // Übersetzung der Fruchtnamen ins Deutsche
    private void initializeTranslationMap() {
        translationMap = new HashMap<>();
        translationMap.put("Wheat", "Weizen");
        translationMap.put("Barley", "Gerste");
        translationMap.put("Canola", "Raps");
        translationMap.put("Corn", "Mais");
        translationMap.put("SugarBeet", "Zuckerrüben");
        translationMap.put("Potato", "Kartoffeln");
        translationMap.put("Sunflower", "Sonnenblumen");
        translationMap.put("Soybean", "Sojabohnen");
    }

    public String translateFruit(String fruitName) {
        return translationMap.getOrDefault(fruitName, fruitName); // Fallback auf Originalname
    }

    // Neue Methode: Gibt eine Liste von möglichen nächsten Früchten basierend auf der geplanten Frucht zurück
    public List<String> getNextFruitOptions(String plannedFruit) {
        List<String> options = new ArrayList<>();
        switch (plannedFruit) {
            case "Wheat":
            case "Weizen":
                options.add("Barley");
                options.add("Canola");
                break;
            case "Barley":
            case "Gerste":
                options.add("Wheat");
                options.add("Corn");
                break;
            case "Canola":
            case "Raps":
                options.add("Sunflower");
                options.add("Soybean");
                break;
            case "Corn":
            case "Mais":
                options.add("Potato");
                options.add("SugarBeet");
                break;
            case "Soybean":
            case "Sojabohnen":
                options.add("Wheat");
                options.add("Barley");
                break;
            default:
                options.add("Unknown");
                break;
        }
        return options;
    }

    // Felder aus einer XML-Datei laden
    public void loadFields(String filePath) {
        try {
            fields.clear(); // Liste zurücksetzen
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(filePath));
            document.getDocumentElement().normalize();
            NodeList nodeList = document.getElementsByTagName("field");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    int id = Integer.parseInt(element.getAttribute("id"));
                    String plannedFruit = element.getAttribute("plannedFruit");
                    fields.add(new Field(id, plannedFruit, false, "")); // Standardmäßig nicht gekauft und keine Folgefrucht
                }
            }

            // Gekaufte Felder und Folgefrüchte aus der entsprechenden Datei laden
            loadPurchasedFields(filePath);

        } catch (Exception e) {
            System.out.println("Fehler beim Laden der Felder: " + e.getMessage());
        }
    }

    // Gekaufte Felder und Folgefrüchte in einer separaten Datei speichern
    public void savePurchasedFields(String filePath) {
        try {
            String purchasedFilePath = filePath.replace(".xml", "_purchased.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element root = document.createElement("gekaufteFelder");
            document.appendChild(root);

            for (Field field : fields) {
                if (field.isPurchased()) {
                    Element fieldElement = document.createElement("field");
                    fieldElement.setAttribute("id", String.valueOf(field.getId()));
                    fieldElement.setAttribute("plannedFruit", field.getPlannedFruit());
                    fieldElement.setAttribute("nextFruit", field.getNextFruit()); // Folge Frucht speichern
                    root.appendChild(fieldElement);
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(purchasedFilePath));
            transformer.transform(source, result);

        } catch (Exception e) {
            System.out.println("Fehler beim Speichern der gekauften Felder: " + e.getMessage());
        }
    }

    // Gekaufte Felder und Folgefrüchte aus der separaten Datei laden
    private void loadPurchasedFields(String filePath) {
        try {
            String purchasedFilePath = filePath.replace(".xml", "_purchased.xml");
            File purchasedFile = new File(purchasedFilePath);

            if (!purchasedFile.exists()) {
                return; // Keine gekauften Felder gespeichert
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(purchasedFile);
            document.getDocumentElement().normalize();
            NodeList nodeList = document.getElementsByTagName("field");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    int id = Integer.parseInt(element.getAttribute("id"));
                    String nextFruit = element.getAttribute("nextFruit"); // Folge Frucht laden

                    // Feld anhand der ID finden und aktualisieren
                    for (Field field : fields) {
                        if (field.getId() == id) {
                            field.setPurchased(true);
                            field.setNextFruit(nextFruit); // Folge Frucht setzen
                            break;
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Fehler beim Laden der gekauften Felder: " + e.getMessage());
        }
    }

    public List<Field> getFields() {
        return fields;
    }

    public List<Field> filterByPurchasedStatus(boolean purchased) {
        List<Field> filteredFields = new ArrayList<>();
        for (Field field : fields) {
            if (field.isPurchased() == purchased) {
                filteredFields.add(field);
            }
        }
        return filteredFields;
    }
}
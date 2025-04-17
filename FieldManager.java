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
        translationMap.put("WHEAT", "Weizen");
        translationMap.put("BARLEY", "Gerste");
        translationMap.put("CANOLA", "Raps");
        translationMap.put("CORN", "Mais");
        translationMap.put("SUGARBEEET", "Zuckerrüben");
        translationMap.put("POTATO", "Kartoffeln");
        translationMap.put("SUNFLOWER", "Sonnenblumen");
        translationMap.put("SOYBEAN", "Sojabohnen");
        translationMap.put("OAT", "Hafer");
        translationMap.put("SORGHUM", "Sorghum");
        translationMap.put("GRASS", "Gras");
        translationMap.put("MAIZE", "Mais");
        translationMap.put("COTTON", "Baumwolle");
        translationMap.put("UNKNOWN", "Unbekannt");
    }

    public String translateFruit(String fruitName) {
        return translationMap.getOrDefault(fruitName.toUpperCase(), "Unbekannt"); // Fallback auf "Unbekannt"
    }

    // Neue Methode: Gibt eine Liste von möglichen nächsten Früchten basierend auf der geplanten Frucht zurück
    public List<String> getNextFruitOptions(String plannedFruit) {
        List<String> options = new ArrayList<>();
        switch (plannedFruit.toUpperCase()) {
            case "WHEAT":
                options.add("Raps");
                options.add("Gerste");
                options.add("Hafer");
                break;
            case "BARLEY":
                options.add("Weizen");
                options.add("Hafer");
                options.add("Raps");
                break;
            case "CANOLA":
                options.add("Weizen");
                options.add("Sonnenblumen");
                options.add("Hafer");
                break;
            case "CORN":
            case "MAIZE":
                options.add("Sojabohnen");
                options.add("Kartoffeln");
                options.add("Gras");
                break;
            case "SOYBEAN":
                options.add("Mais");
                options.add("Sorghum");
                options.add("Weizen");
                break;
            case "SUNFLOWER":
                options.add("Mais");
                options.add("Sojabohnen");
                options.add("Kartoffeln");
                break;
            case "POTATO":
                options.add("Weizen");
                options.add("Zuckerrüben");
                options.add("Sonnenblumen");
                break;
            case "SUGARBEET": // Tippfehler korrigiert
                options.add("Kartoffeln");
                options.add("Gerste");
                break;
            case "OAT":
                options.add("Gerste");
                options.add("Gras");
                options.add("Weizen");
                break;
            case "SORGHUM":
                options.add("Mais");
                options.add("Sojabohnen");
                options.add("Raps");
                break;
            case "GRASS":
                options.add("Weizen");
                options.add("Gerste");
                options.add("Mais");
                break;
            case "COTTON":
                options.add("Sorghum");
                options.add("Sojabohnen");
                break;
            default:
                options.add("UNKNOWN");
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

    public void updateFieldStatus(int fieldId, String newStatus) {
        for (Field field : fields) {
            if (field.getId() == fieldId) {
                field.setStatus(newStatus);
                break;
            }
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
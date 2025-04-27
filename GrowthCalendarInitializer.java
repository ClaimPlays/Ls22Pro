import java.util.HashMap;
import java.util.Map;

public class GrowthCalendarInitializer {

    public static Map<String, Map<String, String>> initializeGrowthCalendar() {
        Map<String, Map<String, String>> growthCalendar = new HashMap<>();

        // Daten für Weizen
        Map<String, String> wheatGrowth = new HashMap<>();
        wheatGrowth.put("März", "Saatzeit");
        wheatGrowth.put("September", "Saatzeit");
        wheatGrowth.put("Oktober", "Saatzeit");
        wheatGrowth.put("November", "Saatzeit");
        wheatGrowth.put("Juli", "Erntezeit");
        wheatGrowth.put("August", "Erntezeit");
        growthCalendar.put("Weizen", wheatGrowth);

        // Daten für Gerste
        Map<String, String> barleyGrowth = new HashMap<>();
        barleyGrowth.put("März", "Saatzeit");
        wheatGrowth.put("September", "Saatzeit");
        barleyGrowth.put("Juni", "Erntezeit");
        barleyGrowth.put("Juli", "Erntezeit");
        barleyGrowth.put("August", "Erntezeit");
        growthCalendar.put("Gerste", barleyGrowth);

        // Daten für Raps
        Map<String, String> canolaGrowth = new HashMap<>();
        canolaGrowth.put("August", "Saatzeit");
        canolaGrowth.put("Juli", "Erntezeit");
        growthCalendar.put("Raps", canolaGrowth);

        // Daten für Hafer
        Map<String, String> oatGrowth = new HashMap<>();
        oatGrowth.put("März", "Saatzeit");
        oatGrowth.put("April", "Saatzeit");
        oatGrowth.put("Sptember", "Erntezeit");
        oatGrowth.put("August", "Erntezeit");
        growthCalendar.put("Hafer", oatGrowth);

        // Daten für Mais
        Map<String, String> cornGrowth = new HashMap<>();
        cornGrowth.put("April", "Saatzeit");
        cornGrowth.put("Mai", "Saatzeit");
        cornGrowth.put("Oktober", "Erntezeit");
        cornGrowth.put("Sptember", "Erntezeit");
        growthCalendar.put("Mais", cornGrowth);

        // Daten für Sonnenblumen
        Map<String, String> sunflowerGrowth = new HashMap<>();
        sunflowerGrowth.put("April", "Saatzeit");
        sunflowerGrowth.put("November", "Saatzeit");
        sunflowerGrowth.put("Oktober", "Erntezeit");
        growthCalendar.put("Sonnenblumen", sunflowerGrowth);

        // Daten für Sojabohnen
        Map<String, String> soybeanGrowth = new HashMap<>();
        soybeanGrowth.put("April", "Saatzeit");
        soybeanGrowth.put("Mai", "Saatzeit");
        soybeanGrowth.put("Oktober", "Erntezeit");
        soybeanGrowth.put("September", "Erntezeit");
        growthCalendar.put("Sojabohnen", soybeanGrowth);

        // Daten für Kartoffeln
        Map<String, String> potatoGrowth = new HashMap<>();
        potatoGrowth.put("Mai", "Saatzeit");
        potatoGrowth.put("April", "Saatzeit");
        potatoGrowth.put("Sptember", "Erntezeit");
        potatoGrowth.put("Oktober", "Erntezeit");
        potatoGrowth.put("November", "Erntezeit");
        growthCalendar.put("Kartoffeln", potatoGrowth);

        // Daten für Zuckerrüben
        Map<String, String> sugarbeetGrowth = new HashMap<>();
        sugarbeetGrowth.put("März", "Saatzeit");
        sugarbeetGrowth.put("April", "Saatzeit");
        sugarbeetGrowth.put("Sebtember", "Erntezeit");
        sugarbeetGrowth.put("Oktober", "Erntezeit");
        sugarbeetGrowth.put("November", "Erntezeit");
        sugarbeetGrowth.put("Dezember", "Erntezeit");
        growthCalendar.put("Zuckerrüben", sugarbeetGrowth);

        // Daten für Zuckerrohr
        Map<String, String> sugarcaneGrowth = new HashMap<>();
        sugarcaneGrowth.put("März", "Saatzeit");
        sugarcaneGrowth.put("April", "Saatzeit");
        sugarcaneGrowth.put("Oktober", "Erntezeit");
        growthCalendar.put("Zuckerrohr", sugarcaneGrowth);

        // Daten für Baumwolle
        Map<String, String> cottonGrowth = new HashMap<>();
        cottonGrowth.put("April", "Saatzeit");
        cottonGrowth.put("Mai", "Saatzeit");
        cottonGrowth.put("Oktober", "Erntezeit");
        growthCalendar.put("Baumwolle", cottonGrowth);

        // Daten für Sorghumhirse
        Map<String, String> sorghumGrowth = new HashMap<>();
        sorghumGrowth.put("April", "Saatzeit");
        sorghumGrowth.put("Mai", "Saatzeit");
        sorghumGrowth.put("August", "Erntezeit");
        sorghumGrowth.put("September", "Erntezeit");
        growthCalendar.put("Sorghumhirse", sorghumGrowth);

        // Daten für Trauben
        Map<String, String> grapeGrowth = new HashMap<>();
        grapeGrowth.put("März", "Saatzeit");
        grapeGrowth.put("Mai", "Saatzeit");
        grapeGrowth.put("September", "Erntezeit");
        growthCalendar.put("Trauben", grapeGrowth);

        // Daten für Oliven
        Map<String, String> oliveGrowth = new HashMap<>();
        oliveGrowth.put("März", "Saatzeit");
        oliveGrowth.put("Mai", "Saatzeit");
        oliveGrowth.put("Oktober", "Erntezeit");
        growthCalendar.put("Oliven", oliveGrowth);

        // Daten für Pappel
        Map<String, String> poplarGrowth = new HashMap<>();
        poplarGrowth.put("Januar", "Saatzeit");
        poplarGrowth.put("Dezember", "Erntezeit");
        growthCalendar.put("Pappel", poplarGrowth);

        // Daten für Gras
        Map<String, String> grassGrowth = new HashMap<>();
        grassGrowth.put("Januar", "Saatzeit");
        grassGrowth.put("Dezember", "Erntezeit");
        growthCalendar.put("Gras", grassGrowth);

        // Daten für Ölrettich
        Map<String, String> oilseedRadishGrowth = new HashMap<>();
        oilseedRadishGrowth.put("März", "Saatzeit");
        oilseedRadishGrowth.put("Oktober", "Erntezeit");
        growthCalendar.put("Ölrettich", oilseedRadishGrowth);

        return growthCalendar;
    }
}
import java.util.HashMap;
import java.util.Map;

public class GrowthCalendarInitializer {

    public static Map<String, Map<String, String>> initializeGrowthCalendar() {
        Map<String, Map<String, String>> growthCalendar = new HashMap<>();

        // Wachstumsdaten für Weizen
        Map<String, String> wheatGrowth = new HashMap<>();
        wheatGrowth.put("Januar", "Geerntet");
        wheatGrowth.put("Februar", "Geerntet");
        wheatGrowth.put("März", "Saatzeit");
        wheatGrowth.put("April", "Saatzeit");
        wheatGrowth.put("Mai", "Wachstum");
        wheatGrowth.put("Juni", "Wachstum");
        wheatGrowth.put("Juli", "Erntezeit");
        wheatGrowth.put("August", "Erntezeit");
        wheatGrowth.put("September", "Geerntet");
        wheatGrowth.put("Oktober", "Geerntet");
        wheatGrowth.put("November", "Geerntet");
        wheatGrowth.put("Dezember", "Geerntet");
        growthCalendar.put("Weizen", wheatGrowth);

        // Wachstumsdaten für Gerste
        Map<String, String> barleyGrowth = new HashMap<>();
        barleyGrowth.put("Januar", "Geerntet");
        barleyGrowth.put("Februar", "Geerntet");
        barleyGrowth.put("März", "Saatzeit");
        barleyGrowth.put("April", "Saatzeit");
        barleyGrowth.put("Mai", "Wachstum");
        barleyGrowth.put("Juni", "Wachstum");
        barleyGrowth.put("Juli", "Erntezeit");
        barleyGrowth.put("August", "Erntezeit");
        barleyGrowth.put("September", "Geerntet");
        barleyGrowth.put("Oktober", "Geerntet");
        barleyGrowth.put("November", "Geerntet");
        barleyGrowth.put("Dezember", "Geerntet");
        growthCalendar.put("Gerste", barleyGrowth);

        // Wachstumsdaten für Raps
        Map<String, String> canolaGrowth = new HashMap<>();
        canolaGrowth.put("Januar", "Geerntet");
        canolaGrowth.put("Februar", "Geerntet");
        canolaGrowth.put("März", "Geerntet");
        canolaGrowth.put("April", "Geerntet");
        canolaGrowth.put("Mai", "Wachstum");
        canolaGrowth.put("Juni", "Geerntet");
        canolaGrowth.put("Juli", "Erntezeit");
        canolaGrowth.put("August", "Saatzeit");
        canolaGrowth.put("September", "Saatzeit");
        canolaGrowth.put("Oktober", "Wachstum");
        canolaGrowth.put("November", "Wachstum");
        canolaGrowth.put("Dezember", "Geerntet");
        growthCalendar.put("Raps", canolaGrowth);

        // Wachstumsdaten für Hafer
        Map<String, String> oatGrowth = new HashMap<>();
        oatGrowth.put("Januar", "Geerntet");
        oatGrowth.put("Februar", "Geerntet");
        oatGrowth.put("März", "Saatzeit");
        oatGrowth.put("April", "Saatzeit");
        oatGrowth.put("Mai", "Wachstum");
        oatGrowth.put("Juni", "Wachstum");
        oatGrowth.put("Juli", "Erntezeit");
        oatGrowth.put("August", "Erntezeit");
        oatGrowth.put("September", "Geerntet");
        oatGrowth.put("Oktober", "Geerntet");
        oatGrowth.put("November", "Geerntet");
        oatGrowth.put("Dezember", "Geerntet");
        growthCalendar.put("Hafer", oatGrowth);

        // Wachstumsdaten für Mais
        Map<String, String> cornGrowth = new HashMap<>();
        cornGrowth.put("Januar", "Geerntet");
        cornGrowth.put("Februar", "Geerntet");
        cornGrowth.put("März", "Geerntet");
        cornGrowth.put("April", "Saatzeit");
        cornGrowth.put("Mai", "Saatzeit");
        cornGrowth.put("Juni", "Wachstum");
        cornGrowth.put("Juli", "Wachstum");
        cornGrowth.put("August", "Wachstum");
        cornGrowth.put("September", "Wachstum");
        cornGrowth.put("Oktober", "Erntezeit");
        cornGrowth.put("November", "Erntezeit");
        cornGrowth.put("Dezember", "Geerntet");
        growthCalendar.put("Mais", cornGrowth);

        // Wachstumsdaten für Sonnenblumen
        Map<String, String> sunflowerGrowth = new HashMap<>();
        sunflowerGrowth.put("Januar", "Geerntet");
        sunflowerGrowth.put("Februar", "Geerntet");
        sunflowerGrowth.put("März", "Geerntet");
        sunflowerGrowth.put("April", "Saatzeit");
        sunflowerGrowth.put("Mai", "Saatzeit");
        sunflowerGrowth.put("Juni", "Wachstum");
        sunflowerGrowth.put("Juli", "Wachstum");
        sunflowerGrowth.put("August", "Wachstum");
        sunflowerGrowth.put("September", "Wachstum");
        sunflowerGrowth.put("Oktober", "Erntezeit");
        sunflowerGrowth.put("November", "Erntezeit");
        sunflowerGrowth.put("Dezember", "Geerntet");
        growthCalendar.put("Sonnenblumen", sunflowerGrowth);

        // Wachstumsdaten für Sojabohnen
        Map<String, String> soybeanGrowth = new HashMap<>();
        soybeanGrowth.put("Januar", "Geerntet");
        soybeanGrowth.put("Februar", "Geerntet");
        soybeanGrowth.put("März", "Geerntet");
        soybeanGrowth.put("April", "Saatzeit");
        soybeanGrowth.put("Mai", "Saatzeit");
        soybeanGrowth.put("Juni", "Wachstum");
        soybeanGrowth.put("Juli", "Wachstum");
        soybeanGrowth.put("August", "Wachstum");
        soybeanGrowth.put("September", "Wachstum");
        soybeanGrowth.put("Oktober", "Erntezeit");
        soybeanGrowth.put("November", "Erntezeit");
        soybeanGrowth.put("Dezember", "Geerntet");
        growthCalendar.put("Sojabohnen", soybeanGrowth);

        // Wachstumsdaten für Kartoffeln
        Map<String, String> potatoGrowth = new HashMap<>();
        potatoGrowth.put("Januar", "Geerntet");
        potatoGrowth.put("Februar", "Geerntet");
        potatoGrowth.put("März", "Saatzeit");
        potatoGrowth.put("April", "Saatzeit");
        potatoGrowth.put("Mai", "Wachstum");
        potatoGrowth.put("Juni", "Erntezeit");
        potatoGrowth.put("Juli", "Erntezeit");
        potatoGrowth.put("August", "Geerntet");
        potatoGrowth.put("September", "Geerntet");
        potatoGrowth.put("Oktober", "Geerntet");
        potatoGrowth.put("November", "Geerntet");
        potatoGrowth.put("Dezember", "Geerntet");
        growthCalendar.put("Kartoffeln", potatoGrowth);

        // Wachstumsdaten für Gras
        Map<String, String> grassGrowth = new HashMap<>();
        grassGrowth.put("Januar", "Wachstum");
        grassGrowth.put("Februar", "Wachstum");
        grassGrowth.put("März", "Wachstum");
        grassGrowth.put("April", "Wachstum");
        grassGrowth.put("Mai", "Wachstum");
        grassGrowth.put("Juni", "Wachstum");
        grassGrowth.put("Juli", "Wachstum");
        grassGrowth.put("August", "Wachstum");
        grassGrowth.put("September", "Wachstum");
        grassGrowth.put("Oktober", "Wachstum");
        grassGrowth.put("November", "Wachstum");
        grassGrowth.put("Dezember", "Wachstum");
        growthCalendar.put("Gras", grassGrowth);

        return growthCalendar;
    }
}
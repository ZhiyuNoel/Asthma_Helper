import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class histData {
//    private ;
    public static void main(String[] args) {
        try {
            // here will be changed to get input from frontend later on
            String siteCode = "BG1"; // Set the SiteCode here
            String speciesCode = "O3"; // Set the SpeciesCode here
            String startDate = "2023-11-27"; // Set the StartDate here
            String endDate = "2023-12-15"; // Set the EndDate here

            // read local json file about site with what species they measure
            JSONParser parser = new JSONParser();
            try {
                Object obj = parser.parse(new FileReader("site_species.json"));
                JSONObject jsonObject = (JSONObject) obj;

                HashMap<String, ArrayList<String>> siteSpeciesMap = new HashMap<>();

                for(Object key : jsonObject.keySet()){
                    String site = (String) key;
                    JSONArray speciesArray = (JSONArray) jsonObject.get(site);
                    ArrayList<String> speciesList = new ArrayList<>();

                    for(Object speciesObj : speciesArray){
                        speciesList.add((String) speciesObj);
                    }

                    siteSpeciesMap.put(site, speciesList);
                }

                if (siteSpeciesMap.containsKey(siteCode) && siteSpeciesMap.get(siteCode).contains(speciesCode)) {
                    // creating url object
                    String url_str = "https://api.erg.ic.ac.uk/AirQuality/Data/SiteSpecies/SiteCode=" + siteCode + "/SpeciesCode=" + speciesCode + "/StartDate=" + startDate + "/EndDate=" + endDate + "/Period=Daily/Units=day/Step=1/Json";
                    //URL latestData_url = new URL("https://api.erg.ic.ac.uk/AirQuality/Daily/MonitoringIndex/Latest/SiteCode=" + siteCode + "/Json");
                    urlReader reader = new urlReader(url_str);
                    JSONObject url_obj = (JSONObject) reader.getURL_obj();


                    JSONObject rawAQData = (JSONObject) url_obj.get("RawAQData");
                    JSONArray dataArray = (JSONArray) rawAQData.get("Data");

                    ArrayList<Double> speciesValueList = new ArrayList<>();
                    for (Object dataObj : dataArray) {
                        JSONObject data = (JSONObject) dataObj;
                        double value = Double.parseDouble((String) data.get("@Value"));
                        speciesValueList.add(value);
                    }

                    // Print or process the speciesValueList as needed
                    for (Double value : speciesValueList) {
                        System.out.println(value);
                    }
                }else{
                    System.out.println("Error: Data for species chosen is not avalaible for this site");
                }

            } catch (Exception e) {
                System.out.println("Error: Not able to read JSON file");
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
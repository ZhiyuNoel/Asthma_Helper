import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class lowDaysMonthlyData {
    // declaring the format we want the 2D array rows in value of hashmap to be
    private static final String[] TARGET_SPECIES = {"NO2", "SO2", "O3", "PM25", "PM10"};
    // declaring the format we want the 2D array columns in value of hashmap to be
    private static final String[] MONTH_KEYS = {"@Month1", "@Month2", "@Month3", "@Month4", "@Month5", "@Month6", "@Month7", "@Month8", "@Month9", "@Month10", "@Month11", "@Month12"};
    private static Integer year = 2023;
    public static void main(String[] args) {
        try {
            // creating hashmap for storing whole data we want
            HashMap<String, ArrayList<ArrayList<Integer>>> siteMonthlyData = new HashMap<>();

            // string of site code fetch from site_codes_only.json
            String[] siteCodes = {"LB6", "MY1", "RB4", "LW2", "RB7", "LW4", "LW6", "LW5", "BG2", "EI1", "BG1", "CE3", "CE2", "HG1", "HG4", "EI8", "BX1", "EA6", "BX2", "EA8", "WM6", "BT5", "GV2", "BT4", "BT6", "BT8", "CR5", "CR8", "GN0", "HP1", "CR7", "EN1", "WA2", "EN4", "GN4", "GR8", "WA7", "EN5", "GN3", "GR7", "GN6", "WA9", "EN7", "GN5", "GR9", "WMD", "WAA", "WAC", "WAB", "TL5", "TL4", "TL6", "BQ7", "IS2", "BQ9", "IS6", "NM2", "NM4", "NM3", "RI2", "RI1", "SK5", "SK6", "SK9", "SK8", "HV1", "HV3", "SKA", "KT4", "SKC", "SKB", "KT6", "KT5", "ST4", "ST6", "ST5", "ST9", "LB4"};
//
//            String[] siteCodes = {"BG1","BQ7"};
            // making connection with url to fetch the JSON object in it
            for (String siteCode : siteCodes) {
                String urlStr = "https://api.erg.ic.ac.uk/AirQuality/Annual/MonitoringReport/SiteCode=" + siteCode + "/Year=" + year.toString() + "/Json";
                urlReader reader = new urlReader(urlStr);
                Object obj = reader.getURL_obj();
                JSONArray dataObject = new JSONArray();
                dataObject.add(obj);

                JSONObject jsonObject = (JSONObject) dataObject.get(0);
                JSONObject siteReport = (JSONObject) jsonObject.get("SiteReport");
                JSONArray reportItems = (JSONArray) siteReport.get("ReportItem");

                // creating the 2D array for sotring values to be put into the final hashmap
                ArrayList<ArrayList<Integer>> monthlyDataForSite = new ArrayList<>();
                for (int i = 0; i < TARGET_SPECIES.length; i++) {
                    monthlyDataForSite.add(initializeZeroList(12)); // Initialize with zeros
                }

//                if there is a reportItems field
                if (reportItems != null) {
                    for (Object reportItemObj : reportItems) {
                        JSONObject reportItem = (JSONObject) reportItemObj;
                        String reportItemName = (String) reportItem.get("@ReportItemName");

                        if ("Low days:".equals(reportItemName)) { // check to get only data from low days
                            String speciesCode = (String) reportItem.get("@SpeciesCode");
                            int speciesIndex = getSpeciesIndex(speciesCode);

                            if (speciesIndex != -1) {
                                ArrayList<Integer> monthlyData = processMonthlyData(reportItem);
                                monthlyDataForSite.set(speciesIndex, monthlyData);
                            }
                        }
                    }
                }

                siteMonthlyData.put(siteCode, monthlyDataForSite);
            }


//             Output to see if data is right
            for (HashMap.Entry<String, ArrayList<ArrayList<Integer>>> entry : siteMonthlyData.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }

            // saving Hashmap as JSON object and to a local JSON file
            JSONObject siteMonthlyLowDaysJson = new JSONObject(siteMonthlyData);
            writeJsonToFile(siteMonthlyLowDaysJson, "Site_Monthly_Low_Days_"+year.toString()+".json");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getSpeciesIndex(String speciesCode) { // func to get species index
        for (int i = 0; i < TARGET_SPECIES.length; i++) {
            if (TARGET_SPECIES[i].equals(speciesCode)) {
                return i;
            }
        }
        return -1; // Return -1 if species not found
    }
//    private static ArrayList<Integer> getMonthlyDataForSpecies(JSONArray reportItems, String species) {
//        ArrayList<Integer> monthlyData = initializeZeroList(MONTH_KEYS.length);
//
//        for (Object reportItemObj : reportItems) {
//            JSONObject reportItem = (JSONObject) reportItemObj;
//            String speciesCode = (String) reportItem.get("@SpeciesCode");
//
//            if (species.equals(speciesCode)) {
//                for (String monthKey : MONTH_KEYS) {
//                    String monthlyValue = (String) reportItem.get(monthKey);
//                    monthlyData.set(getMonthIndex(monthKey), monthlyValue.equals("-999") ? 0 : Integer.parseInt(monthlyValue));
//                }
//                break; // Break after finding the species
//            }
//        }
//        return monthlyData;
//    }
//
//    private static int getMonthIndex(String monthKey) {
//        for (int i = 0; i < MONTH_KEYS.length; i++) {
//            if (MONTH_KEYS[i].equals(monthKey)) {
//                return i;
//            }
//        }
//        return -1; // Return -1 if month not found
//    }

    // method to process monthly data
    private static ArrayList<Integer> processMonthlyData(JSONObject reportItem) {
        ArrayList<Integer> monthlyData = new ArrayList<>();
        for (String monthKey : MONTH_KEYS) {
            String monthlyValue = (String) reportItem.get(monthKey);
            monthlyData.add(monthlyValue.equals("-999") ? 0 : Integer.parseInt(monthlyValue));
        }
        return monthlyData;
    }

    // method to first initialize a list with zeros for each species field and fill them in if needed
    private static ArrayList<Integer> initializeZeroList(int size) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(0);
        }
        return list;
    }
    // func to write to JSON file
    private static void writeJsonToFile(JSONObject jsonObject, String fileName) {
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(jsonObject.toJSONString());
            file.flush();
        } catch (IOException e) {
            System.out.println("Error writing JSONObject to file: " + fileName);
            e.printStackTrace();
        }
    }
}
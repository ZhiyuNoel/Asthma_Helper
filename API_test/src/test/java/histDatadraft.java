//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Scanner;
//
//public class histDatadraft {
////    private ;
//    public static void main(String[] args) {
//        try {
//            String siteCode = "BG1"; // Set the SiteCode here
//            String speciesCode = "NO2"; // Set the SpeciesCode here
//            String startDate = "2023-11-27"; // Set the StartDate here
//            String endDate = "2023-12-15"; // Set the EndDate here
//
//            try {
//                // Check if the speciesCode is present in siteSpeciesMap for the given siteCode
//                if (siteSpeciesMap.containsKey(siteCode) && siteSpeciesMap.get(siteCode).contains(speciesCode)) {
//                    // creating url object
//                    URL historyData_url = new URL("https://api.erg.ic.ac.uk/AirQuality/Data/SiteSpecies/SiteCode=" + siteCode + "/SpeciesCode=" + speciesCode + "/StartDate=" + startDate + "/EndDate=" + endDate + "/Period=Daily/Units=day/Step=1/Json");
////                    URL latestData_url = new URL("https://api.erg.ic.ac.uk/AirQuality/Daily/MonitoringIndex/Latest/SiteCode=" + siteCode + "/Json");
//
//                    HttpURLConnection conn = (HttpURLConnection) historyData_url.openConnection();
//                    conn.setRequestMethod("GET");
//                    conn.connect();
//                    int responseCode = conn.getResponseCode();
//
//                    if (responseCode != 200) {
//                        throw new RuntimeException("HttpResponseCode: " + responseCode);
//                    } else {
//                        StringBuilder informationString = new StringBuilder();
//                        Scanner scanner = new Scanner(historyData_url.openStream());
//
//                        while (scanner.hasNext()) {
//                            informationString.append(scanner.nextLine());
//                        }
//                        scanner.close();
//
//                        JSONParser parser = new JSONParser();
//                        JSONObject jsonResponse = (JSONObject) parser.parse(informationString.toString());
//                        JSONObject rawAQData = (JSONObject) jsonResponse.get("RawAQData");
//                        JSONArray dataArray = (JSONArray) rawAQData.get("Data");
//
//                        ArrayList<Double> speciesValueList = new ArrayList<>();
//                        for (Object dataObj : dataArray) {
//                            JSONObject data = (JSONObject) dataObj;
//                            double value = Double.parseDouble((String) data.get("@Value"));
//                            speciesValueList.add(value);
//                        }
//
//                        // Print or process the speciesValueList as needed
//                        for (Double value : speciesValueList) {
//                            System.out.println(value);
//                        }
//                    }
//                }
//            } catch (Exception e){
//                e.printStackTrace();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
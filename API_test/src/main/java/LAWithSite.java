//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.Scanner;
//
//public class LAWithSite {
//    public LAWithSite(){
//        try {
//            //Public API:
//            //https://www.metaweather.com/api/location/search/?query=<CITY>
//            //https://www.metaweather.com/api/location/44418/
//            // https://api.erg.ic.ac.uk/AirQuality/Daily/MonitoringIndex/GroupName={GROUPNAME}/Date={DATE}
//
////            DateTime date = null;
////            String urlSafeDateString = HttpServerUtility.UrlTokenEncode(date.ToUniversalTime().ToString(System.Globalization.CultureInfo.InvariantCulture).ToCharArray());
////            date = DateTime.Parse(new String(HttpServerUtility.UrlTokenDecode(urlSafeDateString)), System.Globalization.CultureInfo.InvariantCulture).ToLocalTime();
//
////            https://api.erg.ic.ac.uk/AirQuality/Daily/MonitoringIndex/GroupName=London/Date=2023-11-27/Json
////            Greenwich - Westhorne Avenue / Greenwich - Plumstead High Street- seems like a good option
//            URL url = new URL("https://api.erg.ic.ac.uk/AirQuality/Daily/MonitoringIndex/LocalAuthorityId=11/Date=2023-11-27/Json");  // for past date data
//            URL latestLondon_url = new URL("https://api.erg.ic.ac.uk/AirQuality/Daily/MonitoringIndex/Latest/GroupName=London/Json"); // to check for latest data
//
//            // for getting past data
//            HttpURLConnection conn = (HttpURLConnection) latestLondon_url.openConnection();
//            conn.setRequestMethod("GET");
//            conn.connect();
//
//            //Check if connect is made
//            int responseCode = conn.getResponseCode();
//
//            // 200 OK
//            if (responseCode != 200) {
//                throw new RuntimeException("HttpResponseCode: " + responseCode);
//            } else {
//
//                StringBuilder informationString = new StringBuilder();
//                Scanner scanner = new Scanner(latestLondon_url.openStream());
//
//                while (scanner.hasNext()) {
//                    informationString.append(scanner.nextLine());
//                }
//                //Close the scanner
//                scanner.close();
//
//                System.out.println(informationString);
//
//
//                //JSON simple library Setup with Maven is used to convert strings to JSON
//                JSONParser parser = new JSONParser();
//                Object obj  = parser.parse(String.valueOf(informationString));
//                JSONArray dataObject = new JSONArray();
//                dataObject.add(obj);
////                JSONArray dataObject = (JSONArray) parser.parse(String.valueOf(informationString));
//
//                //Get the first JSON object in the JSON array
//                System.out.println(dataObject.get(0));
//
//                JSONObject airQualityIndex = (JSONObject) dataObject.get(0);
//                System.out.println(airQualityIndex.get("DailyAirQualityIndex"));
//
//                JSONObject dailyAirQualityIndex = (JSONObject) airQualityIndex.get("DailyAirQualityIndex");
//                JSONObject localAuthority = (JSONObject) dailyAirQualityIndex.get("LocalAuthority");
//
//                trying to get new json with just name and code
//                JSONObject DAQ_LA_site_main = new JSONObject();
//                JSONArray DAQ_all_LA_array = new JSONArray();
//                JSONObject DAQ_each_LA_obj = new JSONObject();
//                JSONArray DAQ_single_LA_all_site_array = new JSONArray();
//                JSONObject DAQ_each_site_obj = new JSONObject();
//
//
//                for (int i=0; i<=localAuthority.size();i++){
//                    JSONObject localAuth_obj = (JSONObject) localAuthority.get(i);
//                    String localAuth_code =  (String) localAuth_obj.get("@LocalAuthorityCode");
//                    String localAuth_name =  (String) localAuth_obj.get("@LocalAuthorityName");
//                    DAQ_each_LA_obj.put(localAuth_name, localAuth_code);
//
//                    DAQ_all_LA_array.append(DAQ_each_LA_obj);
//                    System.out.println(localAuthority.get(i));
//                }
////                System.out.println(dataObject.get(i));
//
////                JSONObject localAuthority = (JSONObject) dailyAirQualityIndex.get("LocalAuthority");
////                String selectedLocalAuthority = new String("");
//////                if (selectedLocalAuthority != null){
//////                    localAuthority.get(@LocalAuthorityName);
//////                }
//                try {
//                    JSONArray sites = (JSONArray) localAuthority.get("Site");
//
//                    // Find the desired site
//                    JSONObject desiredSite = null;
//
//                    for (int i = 0; i < sites.size(); i++) {
//                        JSONObject site = (JSONObject) sites.get(i);
//                        StringBuilder authorityWithSiteString = new StringBuilder();
//                        authorityWithSiteString.append(localAuthority.get("@LocalAuthorityName"));
//                        if (site.get("@SiteName").equals("Greenwich - Westhorne Avenue")) {
//                            desiredSite = site;
//                            break;
//                        }
//                    }
//
//                    if (desiredSite != null) {
//                        JSONArray speciesArray = (JSONArray) desiredSite.get("Species");
//
//                        // Process each species
//                        for (int i = 0; i < speciesArray.size(); i++) {
//                            JSONObject species = (JSONObject) speciesArray.get(i);
//
//                            // Extract species details
//                            String speciesCode = (String) species.get("@SpeciesCode");
//                            String speciesDescription = (String) species.get("@SpeciesDescription");
//                            String airQualityIndexValue = (String) species.get("@AirQualityIndex");
//                            String airQualityBand = (String) species.get("@AirQualityBand");
//                            String indexSource = (String) species.get("@IndexSource");
//
//                            // Do something with the extracted data (e.g., add to a list or array)
//                            System.out.println("Species Code: " + speciesCode);
//                            System.out.println("Species Description: " + speciesDescription);
//                            System.out.println("Air Quality Index: " + airQualityIndexValue);
//                            System.out.println("Air Quality Band: " + airQualityBand);
//                            System.out.println("Index Source: " + indexSource);
//                            System.out.println("-----------------------------");
//                        }
//                    } else {
//                        System.out.println("Site not found.");
//                    }
//
//                } catch (Exception e) {
//                    // Handle the case where "Site" field is not present in the JSON
//                    System.out.println("Error: Site field not present in the JSON.");
//                    e.printStackTrace();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//}

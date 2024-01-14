import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import org.json.simple.JSONObject;
import java.io.FileWriter;
import java.io.IOException;

public class getNameSite {
    //    public getNameSite(){
    public static void main(String[] args) {
        try { // interact with API ot get the JSON onject
//            https://api.erg.ic.ac.uk/AirQuality/Daily/MonitoringIndex/GroupName=London/Date=2023-11-27/Json
//            Greenwich - Westhorne Avenue / Greenwich - Plumstead High Street- seems like a good option
            String url_str = "https://api.erg.ic.ac.uk/AirQuality/Daily/MonitoringIndex/GroupName=London/Date=2023-11-27/Json";
            urlReader reader = new urlReader(url_str);
            Object obj = reader.getURL_obj();
            JSONArray dataObject = new JSONArray();
            dataObject.add(obj);
//                JSONArray dataObject = (JSONArray) parser.parse(String.valueOf(informationString));


            //Get the first JSON object in the JSON array
            System.out.println(dataObject.get(0));

            JSONObject airQualityIndex = (JSONObject) dataObject.get(0);

            JSONObject dailyAirQualityIndex = (JSONObject) airQualityIndex.get("DailyAirQualityIndex");
            JSONArray localAuthority = (JSONArray) dailyAirQualityIndex.get("LocalAuthority");
//                System.out.println(localAuthority);

            // creating hashmap to store the different name and corresponding codes
            HashMap<String, String> LA_name_code_dict = new HashMap<>(); // local authority name and corresponding code
            HashMap<String, String> site_name_code_dict = new HashMap<>();// site name and corresponding code
            HashMap<String, ArrayList<String>> LA_site_dict = new HashMap<>(); // local authority name and corresponding sites

            HashMap<String, ArrayList<String>> site_species_dict = new HashMap<>();

            for (int i = 0; i < localAuthority.size(); i++) {
                JSONObject localAuth_obj = (JSONObject) localAuthority.get(i);
//                    System.out.println(localAuth_obj);
                String localAuth_code = (String) localAuth_obj.get("@LocalAuthorityCode");
                String localAuth_name = (String) localAuth_obj.get("@LocalAuthorityName");
                LA_name_code_dict.put(localAuth_code, localAuth_name);


                if (localAuth_obj.get("Site") instanceof JSONArray) { // if there is a site and the object is a JSONArray
                    // get JSON array
                    JSONArray sites_arr = (JSONArray) localAuth_obj.get("Site");
                    if (sites_arr != null) {
                        for (int j = 0; j < sites_arr.size(); j++) {
                            JSONObject site = (JSONObject) sites_arr.get(j);
                            String site_code = (String) site.get("@SiteCode");
                            String site_name = (String) site.get("@SiteName");
                            site_name_code_dict.put(site_code, site_name);

                            // code to list species that is measured for each site
                            Object speciesObject = site.get("Species");
                            if (speciesObject instanceof JSONArray) {
                                JSONArray species_arr = (JSONArray) speciesObject;
                                // Process as array
                                ArrayList<String> speciesList = new ArrayList<>();
                                for (int k = 0; k < species_arr.size(); k++) {
                                    JSONObject species = (JSONObject) species_arr.get(k);
                                    String speciesCode = (String) species.get("@SpeciesCode");
                                    speciesList.add(speciesCode);
                                }
                                site_species_dict.put(site_code, speciesList);
                            } else if (speciesObject instanceof JSONObject) {
                                JSONObject singleSpecies = (JSONObject) speciesObject;
                                // Process as a single object
                                String speciesCode = (String) singleSpecies.get("@SpeciesCode");
                                ArrayList<String> singleSpeciesList = new ArrayList<>();
                                singleSpeciesList.add(speciesCode);
                                site_species_dict.put(site_code, singleSpeciesList);
                            }

                            // putting site corresponding to LA
                            if (LA_site_dict.get(localAuth_code) == null) { // if the dict do not have this key value currently
                                LA_site_dict.put(localAuth_code, new ArrayList<String>());   // ArrayList is empty at this point
                                LA_site_dict.get(localAuth_code).add(site_code); // add in the site code for this LA code
                            } else {
                                LA_site_dict.get(localAuth_code).add(site_code); // if LA code key already exist, just add site code
                            }

                        }
                    } else {  // if site object is not a JSONArray
                        // get JSON object
//                        System.out.println("is object");
                        JSONObject sites_obj = (JSONObject) localAuth_obj.get("Site");
                        if (sites_obj != null) {
                            String site_code = (String) sites_obj.get("@SiteCode");
                            String site_name = (String) sites_obj.get("@SiteName");
                            site_name_code_dict.put(site_code, site_name);

                            if (LA_site_dict.get(localAuth_code) == null) { // if the dict do not have this key value currently
                                LA_site_dict.put(localAuth_code, new ArrayList<String>());   // ArrayList is empty at this point
                                LA_site_dict.get(localAuth_code).add(site_code); // add in the site code for this LA code
                            } else {
                                LA_site_dict.get(localAuth_code).add(site_code); // if LA code key already exist, just add site code
                            }
                        }
                    }
                } else {  // for if the local authority do not have any sites
//                        System.out.println("No site for  " + localAuth_name);
                    if (LA_site_dict.get(localAuth_code) == null) { // if the dict do not have this key value currently
                        LA_site_dict.put(localAuth_code, new ArrayList<String>());   // ArrayList is empty at this point
                        LA_site_dict.get(localAuth_code).add("NA"); // add in the site code for this LA code
                    } else {
                        LA_site_dict.get(localAuth_code).add("NA"); // if LA code key already exist, just add site code
                    }
                }

            }

            // Create an ArrayList to store the site names to be used as variable later
            ArrayList<String> siteNames = new ArrayList<>();

            // Add all values from the site_name_code_dict to the ArrayList
            siteNames.addAll(site_name_code_dict.values());


            // Convert HashMaps to JSON objects
            JSONObject LA_NameCode_Json = new JSONObject(LA_name_code_dict);
            JSONObject siteNameCodeJson = new JSONObject(site_name_code_dict);
            JSONObject laSiteJson = new JSONObject(LA_site_dict);
            JSONObject siteSpeciesJson = new JSONObject(site_species_dict);

            // Write each JSON object to a separate file
            writeJsonToFile(LA_NameCode_Json, "LA_name_code.json");
            writeJsonToFile(siteNameCodeJson, "Site_name_code.json");
            writeJsonToFile(laSiteJson, "LA_site_code.json");
            writeJsonToFile(siteSpeciesJson, "site_species.json");

            // ------------------------
            // separate link to get all the sites no matter if they have any monitored air pollutants or not
            // to get all site codes and name
            String url_all_sites = "https://api.erg.ic.ac.uk/AirQuality/Information/MonitoringSites/GroupName=London/Json";
            urlReader reader_all_sites = new urlReader(url_all_sites);
            Object obj_all_sites = reader_all_sites.getURL_obj();
            JSONArray dataObject_all_sites = new JSONArray();
            dataObject_all_sites.add(obj_all_sites);

            HashMap<String, String> all_site_name_code_dict = new HashMap<>();

            JSONObject sitesObject = (JSONObject) dataObject_all_sites.get(0);
            JSONObject sitesContainer = (JSONObject) sitesObject.get("Sites");
            JSONArray sitesArray = (JSONArray) sitesContainer.get("Site");

            for (Object siteObj : sitesArray) {
                JSONObject site = (JSONObject) siteObj;
                String siteName = (String) site.get("@SiteName");
                String siteCode = (String) site.get("@SiteCode");
                all_site_name_code_dict.put(siteCode, siteName);
            }

            JSONObject allsiteNameCodeJson = new JSONObject(all_site_name_code_dict);
            writeJsonToFile(allsiteNameCodeJson, "Site_name_code_all.json");


            // chunk to get list of all site codes
            JSONArray siteCodesArray = new JSONArray();

            for (Object siteObj : sitesArray) {
                JSONObject site = (JSONObject) siteObj;
                String siteCode = (String) site.get("@SiteCode");
                siteCodesArray.add(siteCode);
            }

            JSONObject siteCodesJson = new JSONObject();
            siteCodesJson.put("SiteCodes", siteCodesArray);

            writeJsonToFile(siteCodesJson, "Site_codes_only.json");

//          only save the site code for the one that has species
            JSONParser parser = new JSONParser();

            try (FileReader reader2 = new FileReader("Site_name_code.json")) {
                JSONObject jsonObject = (JSONObject) parser.parse(reader2);
                Set<String> keys = jsonObject.keySet();
                List<String> siteCodes = new ArrayList<>(keys);

                JSONObject outputJson = new JSONObject();
                outputJson.put("siteCodes", siteCodes);

                writeJsonToFile(outputJson, "Site_codes_only_site_with_species.json");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }


    }

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

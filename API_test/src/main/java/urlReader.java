import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class urlReader {
    private String url;
    private Object urlObject;
    public urlReader(String url){
        this.url = url;
        parseURL_obj();
    }

    private void parseURL_obj() {
        try {
//            https://api.erg.ic.ac.uk/AirQuality/Daily/MonitoringIndex/GroupName=London/Date=2023-11-27/Json
//            Greenwich - Westhorne Avenue / Greenwich - Plumstead High Street- seems like a good option
//            URL url = new URL("https://api.erg.ic.ac.uk/AirQuality/Daily/MonitoringIndex/GroupName=London/Date=2023-11-27/Json");
//
//            URL LA11_url = new URL("https://api.erg.ic.ac.uk/AirQuality/Daily/MonitoringIndex/LocalAuthorityId=11/Date=2023-11-27/Json");  // for past date data
//            URL latestLondon_url = new URL("https://api.erg.ic.ac.uk/AirQuality/Daily/MonitoringIndex/Latest/GroupName=London/Json"); // to check for latest data
//            URL historyData_url = new URL("https://api.erg.ic.ac.uk/AirQuality/Data/SiteSpecies/SiteCode=BG1/SpeciesCode=NO2/StartDate=2023-11-27/EndDate=2023-12-15/Period=Daily/Units=day/Step=1/Json");

            // for getting past data
            URL current_url = new URL(this.url);
            HttpURLConnection conn = (HttpURLConnection) current_url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            //Check if connect is made
            int responseCode = conn.getResponseCode();

            // 200 OK
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {

                StringBuilder informationString = new StringBuilder();
                Scanner scanner = new Scanner(current_url.openStream());

                while (scanner.hasNext()) {
                    informationString.append(scanner.nextLine());
                }
                //Close the scanner
                scanner.close();

                System.out.println(informationString);


                //JSON simple library Setup with Maven is used to convert strings to JSON
                JSONParser parser = new JSONParser();
                this.urlObject = parser.parse(String.valueOf(informationString));

            }
        } catch (Exception e) {
            System.out.println("URL not found");
            e.printStackTrace();
        }

    }

    public Object getURL_obj() {
        return this.urlObject;
    }
}

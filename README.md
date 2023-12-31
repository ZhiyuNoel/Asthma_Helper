# Asthma_Helper

**getNameSite** is just a class that was used to run for one time to get the json file of following:
- LA_name_code.json
  -> to get dictionary that map local authority code to their corresponding local authority name  
- Site_name_code.json
  -> to get dictionary that map site code to their corresponding site name  
- LA_site_code.json
  -> to get a dictionary that map local authority code to their corresponding list of site codes 
- Site_species.json
  -> to get dictionary that map site code to their corresponding list of species code

**histData** is a class to generate and return a list of values for the given siteCode, speciesCode, startDate, endDate, that can be changed to be input that are received from the frontend

**siteAPDetails** is a class to return the Air pollution details of different air pollutant monitor and air quality band or level given the chosen site

**urlReader** is a class that helps to handle reading the json object that is fetched from a given link from the API


const fetch = require('node-fetch');
const fs = require('fs');


fetch('https://api.erg.ic.ac.uk/AirQuality/Daily/MonitoringIndex/Latest/GroupName=Essex/Json')
  .then(response => response.text()) // Get the raw response text
  .then(text => {
    console.log(text); // Log the text to inspect it
    // Attempt to parse the text as JSON
    return JSON.parse(text);
  })
  .then(data => {
    fs.writeFileSync('index.json', JSON.stringify(data, null, 2));
    console.log('Data saved to index.json');
  })
  .catch(error => {
    console.error('There was an error fetching or parsing the data:', error);
  });
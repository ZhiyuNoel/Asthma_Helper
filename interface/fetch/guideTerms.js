
const fetch = require('node-fetch');
const fs = require('fs');

fetch('https://api.erg.ic.ac.uk/AirQuality/Information/AirPollutionGuide/Json')
  .then(response => {
    if (!response.ok) {
      return response.text().then(text => {
        throw new Error('Network response was not ok: ' + text); // Log the response body
      });
    }
    return response.json();
  })
  .then(data => {
    fs.writeFileSync('guide.json', JSON.stringify(data, null, 2)); // Pretty-print the JSON
    console.log('data saved to guide.json');
  })
  .catch(error => {
    console.error('There was an error fetching the group data:', error);
  });

const fetch = require('node-fetch'); // You need to install node-fetch if you haven't already
const fs = require('fs');

fetch('https://api.erg.ic.ac.uk/AirQuality/Hourly/Map/Json')
  .then(response => {
    if (!response.ok) {
      throw new Error('Network response was not ok ' + response.statusText);
    }
    return response.json();
  })
  .then(data => {
    // Convert JSON object to string
    const jsonString = JSON.stringify(data, null, 2);
    
    // Write to a new file named data.json
    fs.writeFile('data.json', jsonString, (err) => {
      if (err) throw err;
      console.log('Data written to file');
    });
  })
  .catch(error => {
    console.error('There has been a problem with your fetch operation:', error);
  });
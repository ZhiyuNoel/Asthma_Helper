const fetch = require('node-fetch');
const fs = require('fs');

fetch('https://api.erg.ic.ac.uk/AirQuality/Annual/MonitoringObjective/GroupName=Essex/Json')
  .then(response => response.text()) // Get the raw response text
  .then(text => {
    console.log(text); // Log the raw text
    // Now attempt to parse the text as JSON
    return JSON.parse(text);
  })
  .then(data => {
    // Handle the JSON data
    console.log(data);
  })
  .catch(error => {
    console.error('Error:', error);
  });

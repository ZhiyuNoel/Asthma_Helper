import L from 'leaflet';

// Assuming you have latitude and longitude in your data
const mapData = [
    {
      lat: 51.5074, // Example latitude for London
      lng: -0.1278, // Example longitude for London
      speciesName: "Nitrogen Dioxide",
      measurement: "High", // You would get this from your actual data
      time: "01:00 on Friday 5th January 2024"
    },
    // ... more data points
  ];
  
  // Initialize the map
  const map = L.map('map').setView([51.5074, -0.1278], 10); // Center it on London
  
  // Load and display tile layer on the map
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap contributors'
  }).addTo(map);
  
  // Add markers to the map
  mapData.forEach(dataPoint => {
    L.marker([dataPoint.lat, dataPoint.lng])
      .addTo(map)
      .bindPopup(`<strong>${dataPoint.speciesName}</strong><br>Measurement: ${dataPoint.measurement}<br>Time: ${dataPoint.time}`);
  });
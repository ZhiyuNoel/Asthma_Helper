<template>
    <div ref="map" class="map"></div>
  </template>
  
  <script>
  import L from 'leaflet';
  import 'leaflet/dist/leaflet.css';
  
  export default {
    name: 'LeafletMap',
    mounted() {
      // Once the component is mounted, initialize the map
      this.map = L.map(this.$refs.map).setView([51.505, -0.09], 13);
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://openstreetmap.org/copyright">OpenStreetMap</a> contributors'
      }).addTo(this.map);
    }
  }
  </script>
  
  <style scoped>
  .map {
    height: 400px; /* Set a height for the map */
  }
  </style>
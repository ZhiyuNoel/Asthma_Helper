<template>
  <div>
        <div class="pac-card" id="pac-card" style="width: 470px;">
        <div>
          <div id="title">Autocomplete search</div>
          <div id="type-selector" class="pac-controls">
            <input type="radio" name="type" id="changetype-all" checked="checked" />
            <label for="changetype-all">All</label>

            <input type="radio" name="type" id="changetype-establishment" />
            <label for="changetype-establishment">establishment</label>

            <input type="radio" name="type" id="changetype-address" />
            <label for="changetype-address">address</label>

            <input type="radio" name="type" id="changetype-geocode" />
            <label for="changetype-geocode">geocode</label>

            <input type="radio" name="type" id="changetype-cities" />
            <label for="changetype-cities">(cities)</label>

            <input type="radio" name="type" id="changetype-regions" />
            <label for="changetype-regions">(regions)</label>
          </div>
          <br />
          <div id="strict-bounds-selector" class="pac-controls">
          <input type="checkbox" id="use-location-bias" value="" checked />
          <label for="use-location-bias">Bias to map viewport</label>

          <input type="checkbox" id="use-strict-bounds" value="" />
          <label for="use-strict-bounds">Strict bounds</label>
        </div>
        </div>
        <div id="pac-container">
          <input id="pac-input" type="text" placeholder="Enter a location" />
        </div>
    </div>
  </div>
  <div v-if="circles.length > 0">
    <GoogleMap
    api-key="AIzaSyD2ELEYXQg2JHH231pyB7StYsP88UcYFuE"
    style="width: 100%; height: 700px"
    :center="center"
    :zoom="15"
    >
      <Circle v-for="circle in circles" :key="circle.siteName" :options="circle" />
      <Marker :options="{ position: center }" />
    </GoogleMap>
  </div>
  <div v-else>
    <p>Map Loading</p>
  </div>
</template>
  
<script>
  import { ref,onMounted, computed, watch } from "vue";
  import { GoogleMap, Marker, Polygon, Circle } from 'vue3-google-map'
  import { defineComponent } from "vue";
  import axios from 'axios';
  
  export default defineComponent({
      name: 'MapForPollution',
      props: {
        selectedNumber: Number
      },
      components: {GoogleMap, Marker, Polygon, Circle},
      setup(props){
        const center = { lat: 51.5074, lng: -0.1278 };
        const sitesData = ref([]); // 使用 ref 创建响应式引用
        const selectedNumber = ref(1); // 假设这是一个响应式的选中数值，从 props 或本地状态获得
        const colors = ['#FF0000', '#00FF00', '#0000FF', '#FFFF00', '#FF00FF'];

        const fetchAndInitMonthlyChart = async () => {
          try {
            const airApiURL = `https://api.erg.ic.ac.uk/AirQuality/Daily/MonitoringIndex/GroupName=London/Date=2023-11-27/Json`;
            const response = await axios.get(airApiURL);
            const rawData = response.data.DailyAirQualityIndex.LocalAuthority;

            const pollutantCodes = { 1: "PM10", 2: "PM25", 3: "O3", 4: "NO2", 5: "SO2" };
            const selectedPollutantCode = pollutantCodes[props.selectedNumber];

            sitesData.value = rawData.flatMap(la => {
              if (!la.Site || !Array.isArray(la.Site)) {
                return [];
              }
              return la.Site.filter(site => {
                const speciesList = Array.isArray(site.Species) ? site.Species : [site.Species];
                return speciesList.some(species => species["@SpeciesCode"] === selectedPollutantCode);
              }).map(site => {
                const latitude = parseFloat(site["@Latitude"]);
                const longitude = parseFloat(site["@Longitude"]);
                return {
                  siteName: site["@SiteName"],
                  center: { lat: latitude, lng: longitude },
                };
              });
            });
          } catch (error) {
            console.error("Error fetching data: ", error);
          }
        };

        // 页面加载时获取数据
        onMounted(fetchAndInitMonthlyChart);

        // 监听 selectedNumber 变化
        watch(() => props.selectedNumber, () => {
          fetchAndInitMonthlyChart();
        }, { immediate: true });

        const circles = computed(() => {
        return sitesData.value.map(site => {
          // 获取当前选中数值对应的颜色，确保不会超出数组界限
          const color = colors[props.selectedNumber];
          return {
            center: site.center,
            radius: 200, // 这里固定为200，但您可以根据需要修改
            strokeColor: color,
            strokeOpacity: 0.8,
            strokeWeight: 2,
            fillColor: color,
            fillOpacity: 0.35,
          };
        });
        });
        return { center, circles };
      },
  });
  </script>
  
  <style>
  /* 
   * Always set the map height explicitly to define the size of the div element
   * that contains the map. 
   */
   #map {
    height: 100%;
  }
  
  /* 
   * Optional: Makes the sample page fill the window. 
   */
  html,
  body {
    height: 100%;
    margin: 0;
    padding: 0;
  }
  
  #description {
    font-family: Roboto;
    font-size: 15px;
    font-weight: 300;
  }
  
  #infowindow-content .title {
    font-weight: bold;
  }
  
  #infowindow-content {
    display: none;
  }
  
  #map #infowindow-content {
    display: inline;
  }
  
  .pac-card {
    background-color: #fff;
    border: 0;
    border-radius: 2px;
    box-shadow: 0 1px 4px -1px rgba(0, 0, 0, 0.3);
    margin: 10px;
    padding: 0 0.5em;
    font: 400 18px Roboto, Arial, sans-serif;
    overflow: hidden;
    font-family: Roboto;
    padding: 0;
  }
  
  #pac-container {
    padding-bottom: 12px;
    margin-right: 12px;
  }
  
  .pac-controls {
    display: inline-block;
    padding: 5px 11px;
  }
  
  .pac-controls label {
    font-family: Roboto;
    font-size: 13px;
    font-weight: 300;
  }
  
  #pac-input {
    background-color: #fff;
    font-family: Roboto;
    font-size: 15px;
    font-weight: 300;
    margin-left: 12px;
    padding: 0 11px 0 13px;
    text-overflow: ellipsis;
    width: 400px;
  }
  
  #pac-input:focus {
    border-color: #4d90fe;
  }
  
  #title {
    color: #fff;
    background-color: #4d90fe;
    font-size: 25px;
    font-weight: 500;
    padding: 6px 12px;
  }
  </style>

 
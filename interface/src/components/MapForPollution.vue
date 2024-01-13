<template>
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
        }, { immediate: true});

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
  #map {
    height: 100%;
  }
  
  html, body {
    height: 100%;
    margin: 0;
    padding: 0;
  }
  </style>
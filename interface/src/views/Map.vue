<template>
  <div>
    <el-row class="mb-4">
      <el-button type="primary" round @click="sendNumber(1)" class="button-spacing">PM10</el-button>
      <el-button type="success" round @click="sendNumber(2)" class="button-spacing">PM2.5</el-button>
      <el-button type="info" round @click="sendNumber(3)" class="button-spacing">O3</el-button>
      <el-button type="warning" round @click="sendNumber(4)" class="button-spacing">NO2</el-button>
      <el-button type="danger" round @click="sendNumber(5)" class="button-spacing">SO2</el-button>
    </el-row>
  </div>
<div><br></div>
  <div>
    <MapForPollution :selectedNumber="selectedNumber" />
  </div>
</template>

<script>
import MapForPollution from '@/components/MapForPollution.vue';
// import MapSearch from '@/components/MapSearch.vue';

export default {
  name: 'Map',
  components: {
    MapForPollution
  },
  data() {
    return {
      selectedNumber: 1, // 初始化为默认值，比如 PM10
      Pollution: "PM10"
    };
  },
  methods: {
    sendNumber(number) {
      this.selectedNumber = number; // 更新 selectedNumber
      // 更新 Pollution 文本
      switch (number) {
        case 1:
          this.Pollution = "PM10";
          break;
        case 2:
          this.Pollution = "PM2.5";
          break;
        case 3:
          this.Pollution = "O3";
          break;
        case 4:
          this.Pollution = "NO2";
          break;
        case 5:
          this.Pollution = "SO2";
          break;
        default:
          this.Pollution = "Unknown";
      }
    }
  }
};
</script>

<style>
.right-aligned-text {
  text-align: right;
  font-size: 18px;
}
.button-spacing {
  margin-right: 10px;
}
</style>

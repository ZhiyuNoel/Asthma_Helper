<template>
    <div class="settings-row">
      <h2>Settings</h2>
      <form @submit.prevent="saveSettings">
        <el-row :gutter="20">
          <el-col :span="6"><label for="inhalerType">Select Additional Inhaler (if required) :</label></el-col>
          <el-col :span="6"><select id="inhalerType" v-model="selectedInhalerType">
            <option value="none">None</option>
            <option value="preventer">Preventer</option>
            <option value="combination">Combination</option>
          </select></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="6"><label for="relieverDoses">Total Reliever Doses:</label></el-col>
          <el-col :span="6"><input type="number" id="relieverDoses" v-model.number="relieverDoses"></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="6"><label for="abnormalFrequency">Abnormal Frequency Threshold:</label></el-col>
           <el-col :span="6"><input type="number" id="abnormalFrequency" v-model.number="abnormalFrequency"></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="6"><label for="relieverLowDoseThreshold">Reliever Low Dose Threshold (%):</label></el-col>
          <el-col :span="6"><input type="number" id="relieverLowDoseThreshold" v-model.number="relieverLowDoseThreshold"></el-col>
        </el-row>
        <h3>Combination/Preventer Settings</h3>
      <el-row :gutter="20">
        <el-col :span="6"><label for="preventerDoses">Total Doses:</label></el-col>
        <el-col :span="6"><input type="number" id="preventerDoses" v-model.number="preventerDoses"></el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="6"><label for="morningNumberOfPuffs">Morning Dose Number of Puffs:</label></el-col>
        <el-col :span="6"><input type="number" id="morningNumberOfPuffs" v-model.number="morningNumberOfPuffs"></el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="6"><label for="morningTime">Morning Dose Time:</label></el-col>
        <el-col :span="6"><input type="time" id="morningTime" v-model="morningTime"></el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="6"><label for="eveningNumberOfPuffs">Evening Dose Number of Puffs:</label></el-col>
        <el-col :span="6"><input type="number" id="eveningNumberOfPuffs" v-model.number="eveningNumberOfPuffs"></el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="6"><label for="eveningTime">Evening Dose Time:</label></el-col>
        <el-col :span="6"><input type="time" id="eveningTime" v-model="eveningTime"></el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="6"><label for="preventerLowDoseThreshold">Low Dose Threshold (%):</label></el-col>
        <el-col :span="6"><input type="number" id="preventerLowDoseThreshold" v-model.number="preventerLowDoseThreshold"></el-col>
      </el-row>
      <button type="submit">Save</button>
      </form>


<!--      <form @submit.prevent="saveSettings">-->
<!--            <div>-->
<!--                <label for="inhalerType">Select Additional Inhaler (if required) :</label>-->
<!--                <select id="inhalerType" v-model="selectedInhalerType">-->
<!--                    <option value="none">None</option>-->
<!--                    <option value="preventer">Preventer</option>-->
<!--                    <option value="combination">Combination</option>-->
<!--                </select>-->
<!--            </div>-->
<!--            <div>-->
<!--                <label for="relieverDoses">Total Reliever Doses:</label>-->
<!--                <input type="number" id="relieverDoses" v-model.number="relieverDoses">-->
<!--            </div>-->
<!--            <div>-->
<!--                <label for="abnormalFrequency">Abnormal Frequency Threshold:</label>-->
<!--                <input type="number" id="abnormalFrequency" v-model.number="abnormalFrequency">-->
<!--            </div>-->
<!--            <div>-->
<!--                <label for="relieverLowDoseThreshold">Reliever Low Dose Threshold (%):</label>-->
<!--                <input type="number" id="relieverLowDoseThreshold" v-model.number="relieverLowDoseThreshold">-->
<!--            </div>-->

<!--            <div v-if="selectedInhalerType === 'preventer' || selectedInhalerType === 'combination'">-->
<!--                 <h3>Combination/Preventer Settings</h3>-->
<!--                <div>-->
<!--                    <label for="preventerDoses">Total Doses:</label>-->
<!--                    <input type="number" id="preventerDoses" v-model.number="preventerDoses">-->
<!--                </div>-->
<!--                <div>-->
<!--                    <label for="morningNumberOfPuffs">Morning Dose Number of Puffs:</label>-->
<!--                    <input type="number" id="morningNumberOfPuffs" v-model.number="morningNumberOfPuffs">-->
<!--                </div>-->
<!--                <div>-->
<!--                    <label for="morningTime">Morning Dose Time:</label>-->
<!--                    <input type="time" id="morningTime" v-model="morningTime">-->
<!--                </div>-->
<!--                <div>-->
<!--                    <label for="eveningNumberOfPuffs">Evening Dose Number of Puffs:</label>-->
<!--                    <input type="number" id="eveningNumberOfPuffs" v-model.number="eveningNumberOfPuffs">-->
<!--                </div>-->
<!--                <div>-->
<!--                    <label for="eveningTime">Evening Dose Time:</label>-->
<!--                    <input type="time" id="eveningTime" v-model="eveningTime">-->
<!--                </div>-->
<!--                <div>-->
<!--                    <label for="preventerLowDoseThreshold">Low Dose Threshold (%):</label>-->
<!--                    <input type="number" id="preventerLowDoseThreshold" v-model.number="preventerLowDoseThreshold">-->
<!--                </div>-->
<!--            </div>-->

<!--            <button type="submit">Save</button>-->
<!--        </form>-->
    </div>
</template>

<script>

export default {
  data() {
    return {
      relieverDoses: this.getStoredDoses('relieverDoses', 200),
      abnormalFrequency: parseInt(localStorage.getItem('abnormalFrequency') || 3), 
      preventerDoses: this.getStoredDoses('preventerDoses', 200),
      morningNumberOfPuffs: parseInt(localStorage.getItem('morningNumberOfPuffs') || 2),
      eveningNumberOfPuffs: parseInt(localStorage.getItem('eveningNumberOfPuffs') || 2),
      morningTime: localStorage.getItem('morningTime') || '9:00am',
      eveningTime: localStorage.getItem('eveningTime') || '9:00pm',
      relieverLowDoseThreshold: parseInt(localStorage.getItem('relieverLowDoseThreshold') || 20),
      preventerLowDoseThreshold: parseInt(localStorage.getItem('preventerLowDoseThreshold') || 20),
      selectedInhalerType: localStorage.getItem('selectedInhalerType') || 'none',
    };
  },
  methods: {
    getStoredDoses(key, defaultValue) {
      const value = localStorage.getItem(key);
      return value !== null ? parseInt(value, 10) : defaultValue;
    },
    saveSettings() {
      localStorage.setItem('relieverDoses', this.relieverDoses.toString());
      localStorage.setItem('abnormalFrequency', this.abnormalFrequency.toString());
      localStorage.setItem('preventerDoses', this.preventerDoses.toString());
      localStorage.setItem('morningNumberOfPuffs', this.morningNumberOfPuffs.toString());
      localStorage.setItem('eveningNumberOfPuffs', this.eveningNumberOfPuffs.toString());
      localStorage.setItem('relieverDosesRemaining', this.relieverDoses.toString());
      localStorage.setItem('preventerDosesRemaining', this.preventerDoses.toString());
      localStorage.setItem('morningTime', this.morningTime.toString());
      localStorage.setItem('eveningTime', this.eveningTime.toString());
      localStorage.setItem('relieverLowDoseThreshold', this.relieverLowDoseThreshold.toString());
      localStorage.setItem('preventerLowDoseThreshold', this.preventerLowDoseThreshold.toString());
      localStorage.setItem('selectedInhalerType', this.selectedInhalerType);

      window.dispatchEvent(new Event('localStorageChanged'));

      // Notify the user of successful save
      alert('Settings saved!');
    }
  }
}

</script>

<style scoped>
.settings-row {
    margin-bottom: 20px; 
}
</style>

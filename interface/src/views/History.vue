<template>
  <div class="common-layout">
    <el-container>
      <el-header><h2>Historical Air Quality Graph</h2></el-header>
      <el-header>This section presents the historical levels of pollutants in the selected monitoring sites across London in the periods of weekly, monthly, and 3-monthly.
        Datas are fetched from London Air Quality Network <a href="https://londonair.org.uk/LondonAir/Default.aspx">(LAQN)</a>.</el-header>
      <el-container>
        <el-container>
          <el-main>
            <h3>Select a monitoring site (region):</h3>
              <select id="siteSelect" v-model="selectedSite">
                <option v-for="(name, code) in sites" :key="name" :value="code">
                  {{ name }}
                </option>
              </select>
            <div><h3>Past Week</h3></div>
            <div style="margin-top: 50px;"><canvas id="weeklyChart" style="width: 50%; height: 100%;"></canvas></div>
            <div><h3>Past Month</h3></div>
            <div style="margin-top: 50px;"><canvas id="monthlyChart" style="width: 300px; height: 90px;"></canvas></div>
            <div><h3>Past 3 Months</h3></div>
            <div><canvas id="3monthlyChart" style="width: 300px; height: 90px;"></canvas></div>
          </el-main>
        </el-container>
        <el-aside>
          <h4>Suggested Pollutant Level</h4>
          <el-table :data="[
  {
    factors: 'O3',
    level: '&lt; 50 ugm-3',
  },
  {
    factors: 'NO2',
    level: ' &lt; 101 ugm-3',
  },
  {
    factors: 'SO2',
    level: '&lt; 133 ugm-3',
  },
  {
    factors: 'PM2.5',
    level: '&lt; 18 ugm-3',
  },
  {
    factors: 'PM10',
    level: '&lt; 25 ugm-3',
  },
]"  border stripe style="width: 100%">
            <el-table-column prop="factors" label="Pollutant" width="120" />
            <el-table-column prop="level" label="Level"  />
          </el-table>
        </el-aside>
      </el-container>
    </el-container>
  </div>
</template>



<style scoped="scoped"
  lang="scss">
$width: 100%;
$height: 100%;
$background-color: #5251b1;
$header-color: #fff;
$header-height: 60px;

.common-layout {
    width: $width;
    height: $height;
    p {
      font-family: 'Open Sans', sans-serif;
      color: #333;
      font-size: 15px;
    }

    //.el-container{
    //  display: flex;
    //  justify-content: space-between;
    //}

    .el-header{
      width:100%;
    }
    .el-aside{
      width:20%;
    }
    .el-main{
      width:80%;
    }

    .el-footer{
      width:100%;
    }
}
</style>



<script>
import Chart from 'chart.js/auto';
import { initCustomFormatter } from 'vue';
import axios from 'axios';



let data = () => {
  return {
    collapsed: false,
    systemName: 'Asthma Helper',
    userName: 'User',
    currentDate: null,
    sites: {"LB6":"Lambeth - Streatham Green","MY1":"Westminster - Marylebone Road","RB4":"Redbridge - Gardner Close","LW2":"Lewisham - New Cross","RB7":"Redbridge - Ley Street","LW4":"Lewisham - Loampit Vale","LW6":"Lewisham -Laurence House Catford","LW5":"Lewisham - Deptford","BG2":"Barking and Dagenham - Scrattons Farm","EI1":"Ealing - Western Avenue","BG1":"Barking and Dagenham - Rush Green","CE3":"Regent Street (The Crown Estate)","CE2":"Waterloo Place (The Crown Estate)","HG1":"Haringey - Haringey Town Hall","HG4":"Haringey  - Priory Park South","EI8":"Ealing - Horn Lane TEOM","BX1":"Bexley - Slade Green","EA6":"Ealing - Hanger Lane Gyratory","BX2":"Bexley - Belvedere","EA8":"Ealing - Horn Lane","WM6":"Westminster - Oxford Street","BT5":"Brent - Neasden Lane","GV2":"Westminster - Duke Street (Grosvenor)","BT4":"Brent - Ikea","BT6":"Brent - John Keble Primary School","BT8":"Brent - ARK Franklin Primary Academy","CR5":"Croydon - Norbury","CR8":"Croydon - Norbury Manor","GN0":"Greenwich - A206 Burrage Grove","HP1":"Lewisham - Honor Oak Park","CR7":"Croydon - Purley Way A23","EN1":"Enfield - Bush Hill Park","WA2":"Wandsworth - Wandsworth Town Hall","EN4":"Enfield - Derby Road","GN4":"Greenwich - Fiveways Sidcup Rd A20","GR8":"Greenwich - Woolwich Flyover","WA7":"Wandsworth - Putney High Street","EN5":"Enfield - Bowes Primary School","GN3":"Greenwich - Plumstead High Street","GR7":"Greenwich - Blackheath","GN6":"Greenwich - John Harrison Way","WA9":"Wandsworth - Putney","EN7":"Enfield - Prince of Wales School","GN5":"Greenwich - Trafalgar Road (Hoskins St)","GR9":"Greenwich - Westhorne Avenue","WMD":"Westminster - Elizabeth Bridge","WAA":"Wandsworth - Battersea","WAC":"Wandsworth - Lavender Hill (Clapham Jct)","WAB":"Wandsworth - Tooting High Street","TL5":"Newham - Hoola Tower","TL4":"Greenwich - Tunnel Avenue","TL6":"Newham - Britannia Gate","BQ7":"Bexley - Belvedere West","IS2":"Islington - Holloway Road","BQ9":"Bexley - Slade Green FIDAS","IS6":"Islington - Arsenal","NM2":"Newham - Cam Road","NM4":"Newham - East Ham Town Hall","NM3":"Newham - Wren Close","RI2":"Richmond Upon Thames - Barnes Wetlands","RI1":"Richmond Upon Thames - Castelnau","SK5":"Southwark - A2 Old Kent Road","SK6":"Southwark - Elephant and Castle","SK9":"Southwark - A2 Old Kent Road FIDAS","SK8":"Southwark - Tower Bridge Road","HV1":"Havering - Rainham","HV3":"Havering - Romford","SKA":"Southwark - Lower Road","KT4":"Kingston Upon Thames - Tolworth Broadway","SKC":"Southwark - South Circular Road","SKB":"Southwark - Vicarage Grove","KT6":"Kingston Upon Thames - Kingston Vale","KT5":"Kingston Upon Thames - Cromwell Road","ST4":"Sutton - Wallington","ST6":"Sutton - Worcester Park","ST5":"Sutton - Beddington Lane north","ST9":"Sutton - Beddington Village","LB4":"Lambeth - Brixton Road"},
    selectedSite: ''
  }
}

export default {
  data: data,
  name: "History",
  mounted() {
    //initialize all three charts with null labels and datasets
    this.initWeeklyChart();
    this.initMonthlyChart();
    this.init3MonthlyChart();
  },

  methods: {

    initWeeklyChart() {
      //initialize chart with null datas
      //same for three charts
      const ctx = document.getElementById('weeklyChart').getContext('2d');
      this.weeklyChart = new Chart(ctx, {type: 'line',
        data: {labels: null,datasets: null},
        options: {
          plugins: {title: {display: true,text: `Weekly Chart ${this.selectedSite}`}},
          scales: {y: {beginAtZero: true}}
        }
      });
    },

    initMonthlyChart() {
      const ctx = document.getElementById('monthlyChart').getContext('2d');
      this.monthlyChart = new Chart(ctx, {
        type: 'line',
        data: {labels: null,datasets: null},
        options: {
          plugins: {title: {display: true,text: `Monthly Chart ${this.selectedSite}`}},
          scales: {y: {beginAtZero: true}}
        }
      });
    },
    
    init3MonthlyChart() {
      const ctx = document.getElementById('3monthlyChart').getContext('2d');
      this.tmonthlyChart = new Chart(ctx, {
        type: 'line',
        data: {labels: null, datasets: null},
        options: {
          plugins: {title: {display: true, text: `3 Monthly Chart ${this.selectedSite}`}},
          scales: {y: {beginAtZero: true}}
        } 
      }); 
    }, 

    updateWeeklyChart() {
      
      //update chart with fetched data
      //same for three charts except different past date

      this.currentDate = this.getCurrentDate();
      var pastDate = this.getPastDate(7);

      //insert selectedSite, currentDate, and pastDate to API urls. Fetch all five pollutants
      var SO2_link= `https://api.erg.ic.ac.uk/AirQuality/Data/SiteSpecies/SiteCode=${this.selectedSite}/SpeciesCode=SO2/StartDate=${pastDate}/EndDate=${this.currentDate}/Period=daily/Units=hour/Step=1/Json`;
      var O3_link= `https://api.erg.ic.ac.uk/AirQuality/Data/SiteSpecies/SiteCode=${this.selectedSite}/SpeciesCode=O3/StartDate=${pastDate}/EndDate=${this.currentDate}/Period=daily/Units=hour/Step=1/Json`;
      var NO2_link= `https://api.erg.ic.ac.uk/AirQuality/Data/SiteSpecies/SiteCode=${this.selectedSite}/SpeciesCode=NO2/StartDate=${pastDate}/EndDate=${this.currentDate}/Period=daily/Units=hour/Step=1/Json`;
      var PM10_link= `https://api.erg.ic.ac.uk/AirQuality/Data/SiteSpecies/SiteCode=${this.selectedSite}/SpeciesCode=PM10/StartDate=${pastDate}/EndDate=${this.currentDate}/Period=daily/Units=hour/Step=1/Json`;
      var PM25_link= `https://api.erg.ic.ac.uk/AirQuality/Data/SiteSpecies/SiteCode=${this.selectedSite}/SpeciesCode=PM25/StartDate=${pastDate}/EndDate=${this.currentDate}/Period=daily/Units=hour/Step=1/Json`;

      Promise.all([
        axios.get(SO2_link).catch(err => ({ error: true, data: null })),
        axios.get(NO2_link).catch(err => ({ error: true, data: null })),
        axios.get(O3_link).catch(err => ({ error: true, data: null })),
        axios.get(PM10_link).catch(err => ({ error: true, data: null })),
        axios.get(PM25_link).catch(err => ({ error: true, data: null }))
      ]).then(responses => {

        var datasets = [];
        var labels = [];

        //check x-axis (date) for all pollutants, select the longest x-axis
        responses.forEach(response => {
          if (response && !response.error && response.data && response.data.RawAQData && response.data.RawAQData.Data) {
            const currentLabels = response.data.RawAQData.Data.map(item => item["@MeasurementDateGMT"].split(" ")[0]);
            if (currentLabels.length > labels.length) {
              labels = currentLabels;
            }
          }
        });

        //check whether each pollutant is available or null. If not null, push to datasets.
        if (responses[0] && !responses[0].error) {
          datasets.push({
            label: 'SO2',
            data: responses[0].data.RawAQData.Data.map(item => parseFloat(item["@Value"])),
            backgroundColor: 'rgba(255, 99, 132, 0.2)',
            borderColor: 'rgba(255, 99, 132, 1)',
            borderWidth: 1
          });
        }
        if (responses[1] && !responses[1].error) {
          datasets.push({
            label: 'NO2',
            data: responses[1].data.RawAQData.Data.map(item => parseFloat(item["@Value"])),
            backgroundColor: 'rgba(200, 200, 0, 0.2)',
            borderColor: 'rgba(200, 200, 0, 1)',
            borderWidth: 1
          });
        }
        if (responses[2] && !responses[2].error) {
          datasets.push({
            label: 'O3',
            data: responses[2].data.RawAQData.Data.map(item => parseFloat(item["@Value"])),
            backgroundColor: 'rgba(0, 0, 200, 0.2)',
            borderColor: 'rgba(0, 0, 200, 1)',
            borderWidth: 1
          });
        }
        if (responses[3] && !responses[3].error) {
          datasets.push({
            label: 'PM10',
            data: responses[3].data.RawAQData.Data.map(item => parseFloat(item["@Value"])),
            backgroundColor: 'rgba(0, 200, 0, 0.2)',
            borderColor: 'rgba(0, 200, 0, 1)',
            borderWidth: 1
          });
        }
        if (responses[4] && !responses[4].error) {
          datasets.push({
            label: 'PM25',
            data: responses[4].data.RawAQData.Data.map(item => parseFloat(item["@Value"])),
            backgroundColor: 'rgba(128, 0, 132, 0.2)',
            borderColor: 'rgba(128, 0, 132, 1)',
            borderWidth: 1
          });
        }
        
        //update chart's dataset and labels. update figure. 
        this.weeklyChart.data.datasets =datasets;
        this.weeklyChart.data.labels = labels;
        this.weeklyChart.update();
  
    }).catch(error => {
        console.error("Error in Promise.all: ", error);
      });
     
    },
  
    updateMonthlyChart() {
      this.currentDate = this.getCurrentDate();
      var pastDate = this.getPastDate(30);
      var SO2_link= `https://api.erg.ic.ac.uk/AirQuality/Data/SiteSpecies/SiteCode=${this.selectedSite}/SpeciesCode=SO2/StartDate=${pastDate}/EndDate=${this.currentDate}/Period=daily/Units=hour/Step=1/Json`;
      var O3_link= `https://api.erg.ic.ac.uk/AirQuality/Data/SiteSpecies/SiteCode=${this.selectedSite}/SpeciesCode=O3/StartDate=${pastDate}/EndDate=${this.currentDate}/Period=daily/Units=hour/Step=1/Json`;
      var NO2_link= `https://api.erg.ic.ac.uk/AirQuality/Data/SiteSpecies/SiteCode=${this.selectedSite}/SpeciesCode=NO2/StartDate=${pastDate}/EndDate=${this.currentDate}/Period=daily/Units=hour/Step=1/Json`;
      var PM10_link= `https://api.erg.ic.ac.uk/AirQuality/Data/SiteSpecies/SiteCode=${this.selectedSite}/SpeciesCode=PM10/StartDate=${pastDate}/EndDate=${this.currentDate}/Period=daily/Units=hour/Step=1/Json`;
      var PM25_link= `https://api.erg.ic.ac.uk/AirQuality/Data/SiteSpecies/SiteCode=${this.selectedSite}/SpeciesCode=PM25/StartDate=${pastDate}/EndDate=${this.currentDate}/Period=daily/Units=hour/Step=1/Json`;

      Promise.all([
        axios.get(SO2_link).catch(err => ({ error: true, data: null })),
        axios.get(NO2_link).catch(err => ({ error: true, data: null })),
        axios.get(O3_link).catch(err => ({ error: true, data: null })),
        axios.get(PM10_link).catch(err => ({ error: true, data: null })),
        axios.get(PM25_link).catch(err => ({ error: true, data: null }))
      ]).then(responses => {
        var datasets = [];
        var labels = [];
        responses.forEach(response => {
          if (response && !response.error && response.data && response.data.RawAQData && response.data.RawAQData.Data) {
            const currentLabels = response.data.RawAQData.Data.map(item => item["@MeasurementDateGMT"].split(" ")[0]);
            if (currentLabels.length > labels.length) {
              labels = currentLabels;
            }
          }
        });

        if (responses[0] && !responses[0].error) {
          datasets.push({
            label: 'SO2',
            data: responses[0].data.RawAQData.Data.map(item => parseFloat(item["@Value"])),
            backgroundColor: 'rgba(255, 99, 132, 0.2)',
            borderColor: 'rgba(255, 99, 132, 1)',
            borderWidth: 1
          });
        }
        if (responses[1] && !responses[1].error) {
          datasets.push({
            label: 'NO2',
            data: responses[1].data.RawAQData.Data.map(item => parseFloat(item["@Value"])),
            backgroundColor: 'rgba(200, 200, 0, 0.2)',
            borderColor: 'rgba(200, 200, 0, 1)',
            borderWidth: 1
          });
        }
        if (responses[2] && !responses[2].error) {
          datasets.push({
            label: 'O3',
            data: responses[2].data.RawAQData.Data.map(item => parseFloat(item["@Value"])),
            backgroundColor: 'rgba(0, 0, 200, 0.2)',
            borderColor: 'rgba(0, 0, 200, 1)',
            borderWidth: 1
          });
        }
        if (responses[3] && !responses[3].error) {
          datasets.push({
            label: 'PM10',
            data: responses[3].data.RawAQData.Data.map(item => parseFloat(item["@Value"])),
            backgroundColor: 'rgba(0, 200, 0, 0.2)',
            borderColor: 'rgba(0, 200, 0, 1)',
            borderWidth: 1
          });
        }
        if (responses[4] && !responses[4].error) {
          datasets.push({
            label: 'PM25',
            data: responses[4].data.RawAQData.Data.map(item => parseFloat(item["@Value"])),
            backgroundColor: 'rgba(128, 0, 132, 0.2)',
            borderColor: 'rgba(128, 0, 132, 1)',
            borderWidth: 1
          });
        }

      this.monthlyChart.data.datasets =datasets;
      this.monthlyChart.data.labels = labels;
      this.monthlyChart.update();

      }).catch(error => {
        console.error("Error in Promise.all: ", error);
      });
      
    },

  update3MonthlyChart() {
    this.currentDate = this.getCurrentDate();
    var pastDate = this.getPastDate(90);
    var SO2_link= `https://api.erg.ic.ac.uk/AirQuality/Data/SiteSpecies/SiteCode=${this.selectedSite}/SpeciesCode=SO2/StartDate=${pastDate}/EndDate=${this.currentDate}/Period=daily/Units=hour/Step=1/Json`;
    var O3_link= `https://api.erg.ic.ac.uk/AirQuality/Data/SiteSpecies/SiteCode=${this.selectedSite}/SpeciesCode=O3/StartDate=${pastDate}/EndDate=${this.currentDate}/Period=daily/Units=hour/Step=1/Json`;
    var NO2_link= `https://api.erg.ic.ac.uk/AirQuality/Data/SiteSpecies/SiteCode=${this.selectedSite}/SpeciesCode=NO2/StartDate=${pastDate}/EndDate=${this.currentDate}/Period=daily/Units=hour/Step=1/Json`;
    var PM10_link= `https://api.erg.ic.ac.uk/AirQuality/Data/SiteSpecies/SiteCode=${this.selectedSite}/SpeciesCode=PM10/StartDate=${pastDate}/EndDate=${this.currentDate}/Period=daily/Units=hour/Step=1/Json`;
    var PM25_link= `https://api.erg.ic.ac.uk/AirQuality/Data/SiteSpecies/SiteCode=${this.selectedSite}/SpeciesCode=PM25/StartDate=${pastDate}/EndDate=${this.currentDate}/Period=daily/Units=hour/Step=1/Json`;

    Promise.all([
      axios.get(SO2_link).catch(err => ({ error: true, data: null })),
      axios.get(NO2_link).catch(err => ({ error: true, data: null })),
      axios.get(O3_link).catch(err => ({ error: true, data: null })),
      axios.get(PM10_link).catch(err => ({ error: true, data: null })),
      axios.get(PM25_link).catch(err => ({ error: true, data: null }))
    ]).then(responses => {
      var datasets = [];
      var labels = [];
      responses.forEach(response => {
        if (response && !response.error && response.data && response.data.RawAQData && response.data.RawAQData.Data) {
          const currentLabels = response.data.RawAQData.Data.map(item => item["@MeasurementDateGMT"].split(" ")[0]);
          if (currentLabels.length > labels.length) {
            labels = currentLabels;
          }
        }
      });

      if (responses[0] && !responses[0].error) {
        datasets.push({
          label: 'SO2',
          data: responses[0].data.RawAQData.Data.map(item => parseFloat(item["@Value"])),
          backgroundColor: 'rgba(255, 99, 132, 0.2)',
          borderColor: 'rgba(255, 99, 132, 1)',
          borderWidth: 1
        });
      }
      if (responses[1] && !responses[1].error) {
        datasets.push({
          label: 'NO2',
          data: responses[1].data.RawAQData.Data.map(item => parseFloat(item["@Value"])),
          backgroundColor: 'rgba(200, 200, 0, 0.2)',
          borderColor: 'rgba(200, 200, 0, 1)',
          borderWidth: 1
        });
      }
      if (responses[2] && !responses[2].error) {
        datasets.push({
          label: 'O3',
          data: responses[2].data.RawAQData.Data.map(item => parseFloat(item["@Value"])),
          backgroundColor: 'rgba(0, 0, 200, 0.2)',
          borderColor: 'rgba(0, 0, 200, 1)',
          borderWidth: 1
        });
      }
      if (responses[3] && !responses[3].error) {
        datasets.push({
          label: 'PM10',
          data: responses[3].data.RawAQData.Data.map(item => parseFloat(item["@Value"])),
          backgroundColor: 'rgba(0, 200, 0, 0.2)',
          borderColor: 'rgba(0, 200, 0, 1)',
          borderWidth: 1
        });
      }
      if (responses[4] && !responses[4].error) {
        datasets.push({
          label: 'PM25',
          data: responses[4].data.RawAQData.Data.map(item => parseFloat(item["@Value"])),
          backgroundColor: 'rgba(128, 0, 132, 0.2)',
          borderColor: 'rgba(128, 0, 132, 1)',
          borderWidth: 1
        });
      }
      

      this.tmonthlyChart.data.datasets =datasets;
      this.tmonthlyChart.data.labels = labels;
      this.tmonthlyChart.update();

      }).catch(error => {console.error("Error in Promise.all: ", error);});
      
    },  
    
  getCurrentDate() {
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0'); 
    const day = String(today.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
    },
    
  getPastDate(num) {
    const today = new Date();
    today.setDate(today.getDate() - num);

    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0'); 
    const day = String(today.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
    },
    handleChangeSelect() {
    this.weeklyChartUpdated = false; 
  }
  },


  watch: {

    //monitor user selection. update chart when selectedSite changes. 
    selectedSite() {
      this.updateWeeklyChart();
      this.updateMonthlyChart();
      this.update3MonthlyChart();
  }
  }
}

</script>

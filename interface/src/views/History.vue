<template>
<el-main class="main">
  <div class="panel">
    <select id="siteSelect" v-model="selectedSite">
      <option v-for="(name, code) in sites" :key="name" :value="code">
        {{ name }}
      </option>
    </select>
      <div>
        <h3>Past Week</h3> 
      </div>
      <div style="margin-top: 50px;">
          <canvas id="weeklyChart" style="width: 300px; height: 90px;"></canvas>  
      </div>

      <div>
        <h3>Past Month</h3> 
      </div>
      <div style="margin-top: 50px;">
          <canvas id="monthlyChart" style="width: 300px; height: 90px;"></canvas>  
      </div>

      <div>
        <h3>Past 3 Months</h3>
      </div>
      <div>
        <canvas id="3monthlyChart" style="width: 300px; height: 90px;"></canvas>  
      </div>

  </div>
  </el-main>
</template>


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
    this.fetchAndInitWeeklyChart();
    this.fetchAndInitMonthlyChart();
    this.fetchAndInit3MonthlyChart();
  },

  methods: {

    goToHistory() {
      this.$router.push({ name: 'history' });
    },

    fetchAndInitWeeklyChart() {
      this.currentDate = this.getCurrentDate();
      var pastDate = this.getPastDate(7);
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
        
        
        const ctx = document.getElementById('weeklyChart').getContext('2d');
        this.weeklyChart = new Chart(ctx, {type: 'line',
          data: {labels: labels,datasets: datasets},
          options: {
            plugins: {title: {display: true,text: `Weekly Chart ${this.selectedSite}`}},
            scales: {y: {beginAtZero: true}}
          }
        });

    }).catch(error => {
        console.error("Error in Promise.all: ", error);
      });
     
    },
    

    updateWeeklyChart() {
      this.currentDate = this.getCurrentDate();
      var pastDate = this.getPastDate(7);
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
        

        this.weeklyChart.data.datasets =datasets;
        this.weeklyChart.data.labels = labels;
        this.weeklyChart.update();
        
    }).catch(error => {
        console.error("Error in Promise.all: ", error);
      });
     
    },

   

    fetchAndInitMonthlyChart() {
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

          const ctx = document.getElementById('monthlyChart').getContext('2d');

          this.monthlyChart = new Chart(ctx, {
            type: 'line',
            data: {labels: labels,datasets: datasets},
            options: {
              plugins: {
                  title: {display: true,text: `Monthly Chart ${this.selectedSite}`}
              },
              scales: {y: {beginAtZero: true}}
            }
          });

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


    fetchAndInit3MonthlyChart() {
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
        

          const ctx = document.getElementById('3monthlyChart').getContext('2d');
          this.tmonthlyChart = new Chart(ctx, {
            type: 'line',
            data: {labels: labels, datasets: datasets},
            options: {
              plugins: {title: {display: true, text: `3 Monthly Chart ${this.selectedSite}`}},
              scales: {y: {beginAtZero: true}}
            } 
          }); 

        }).catch(error => {console.error("Error in Promise.all: ", error);});
        
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



  normalizeData(data) {
      const maxVal = Math.max(...data);
      return data.map(val => val / maxVal);
    },
    
  getCurrentDate() {
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0'); // 月份是从0开始的
    const day = String(today.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
    },
    
  getPastDate(num) {
    const today = new Date();
    today.setDate(today.getDate() - num);

    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0'); // 月份是从 0 开始的
    const day = String(today.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
    },
    handleChangeSelect() {
    this.weeklyChartUpdated = false; // 每次更改 select 时将标志设置为 false
    // 其他处理逻辑
  }
  },


  watch: {
    selectedSite() {

      this.updateWeeklyChart();
      this.updateMonthlyChart();
      this.update3MonthlyChart();

      //this.fetchAndInitMonthlyChart();
      //this.fetchAndInit3MonthlyChart();
  }
  }
}


</script>

<style scoped="scoped"
  lang="scss">
$width: 100%;
$height: 100%;
$background-color: #5251b1;
$header-color: #fff;
$header-height: 60px;

.container {
    position: absolute;
    top: 0;
    bottom: 0;
    width: 100%;
    .el-aside {
        .el-header {
            line-height: $header-height;
            background-color: $background-color;
            color: $header-color;
            text-align: center;
        }
        .el-container {
            height: $height;
            .el-main {
                padding: 0;
            }
        }
    }

    .main {
        width: $width;
        height: $height;
    }

    .menu-button {
        width: 14px;
        cursor: pointer;
    }

    .userinfo-inner {
        cursor: pointer;
    }

    .el-menu {
        height: $height;
    }

    .header {
        background-color: $background-color;
        color: $header-color;
        text-align: center;
        line-height: $header-height;
        padding: 0;

        .header-title {
            text-align: left;
            span {
                padding: 0 20px;
            }
        }
    }

    .system-name {
        font-size: large;
        font-weight: bold;
    }
}
</style>
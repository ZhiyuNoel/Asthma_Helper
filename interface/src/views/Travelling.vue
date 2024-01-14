<template>
  <section>
        <el-main class="main">
          <!-- <div>
            <select id="siteSelect" v-model="selectedSite">
              <option v-for="(name, code) in sites" :key="code" :value="code">
                {{ name }}
              </option>
            </select>
          </div> -->
          <div>
            Top favourable sites to live in based on Percentage of Days with Low DAQI index (Up to 10)<br />
            Based on Species:
            <select id="speciesSelectTop" v-model="selectedTopSpecies">
              <option v-for="(name, code) in species" :key="code" :value="code">
                {{ name }}
              </option>
            </select>
            and Month:
            <select id="monthSelectedTop" v-model="selectedTopMonth">
              <option v-for="(name, code) in months" :key="code" :value="code">
                {{ name }}
              </option>
            </select>
          Up to:
          <select id="choseTopNumber" v-model="choseTopNumber">
              <option v-for="(name) in topNumber">
                {{ name }}
              </option>
            </select></div>
          Species Chosen for Top 10: {{ this.selectedTopSpecies }} 
          Month Chosen for Top 10: {{ this.selectedTopMonth }} <br />
          <div>
              <div v-for="item  in this.top10details">
              {{ item }}
              </div>
              <div>
              <p>
                According to Species:
            <select id="speciesSelectCompare" v-model="selectedSpeciesCompare">
              <option v-for="(name, code) in species" :key="code" :value="code">
                {{ name }}
              </option>
            </select>
          <br />
          And looking between 
          
            <select id="siteSelect1" v-model="selectedSite1">
              <option v-for="(name, code) in sites" :key="code" :value="code">
                {{ name }}
              </option>
            </select>

          and 

            <select id="siteSelect2" v-model="selectedSite2">
              <option v-for="(name, code) in sites" :key="code" :value="code">
                {{ name }}
              </option>
            </select></p>
          </div>

            <!-- <p v-html="this.top10details"></p> -->
            First Site Chosen: {{ this.selectedSite1 }} <br />
            Second Site Chosen: {{ this.selectedSite2 }} <br />
            Species Chosen: {{ this.selectedSpeciesCompare }} <br />
            <!-- Index NO2: {{ NO2Index }} <br />
            Index SO2: {{ SO2Index }} <br />
            Index O3: {{ O3Index }} <br />
            Index PM25: {{ PM25Index }} <br />
            Index PM10: {{ PM10Index }} <br /> -->
            <!-- Percentage: {{ this.percentages[1] }} <br />
            Notice about air pollution across all sites: {{ this.message }} <br />
            Top 10 sites: {{ this.returnList }} <br />
            Top 10 sites and values: {{ this.keyValues }} <br /> -->

            {{ this.comparisonResult }}
            <p v-html="this.comparisonResult"></p>
          </div>
          <transition name="fade" mode="out-in">
            <router-view></router-view>
          </transition>
        </el-main>
  </section>
  </template>
  
  <script>
  import axios from 'axios';
  import Site_Monthly_Low_Days from '@/assets/Site_Monthly_Low_Days.json';
  let data = () => {
    return {
      collapsed: false,
      systemName: 'Asthma Helper',
      userName: 'User',
      speciesAnnualLowDaysData:{"LB6":[253,0,0,0,232],"MY1":[320,276,180,0,0],"RB4":[363,0,0,0,290],"LW2":[345,0,0,177,330],"RB7":[242,0,292,311,273],"LW4":[305,0,0,0,354],"LW6":[324,0,0,0,0],"LW5":[335,0,0,343,0],"BG2":[353,0,0,0,251],"EI1":[357,0,0,0,247],"BG1":[318,322,0,0,0],"CE3":[306,0,0,340,341],"CE2":[348,0,266,321,343],"HG1":[301,0,0,0,0],"HG4":[345,0,211,0,0],"EI8":[0,0,0,0,353],"BX1":[346,340,68,0,0],"EA6":[360,0,0,0,222],"BX2":[363,0,0,361,362],"EA8":[363,0,0,0,67],"WM6":[364,0,0,0,337],"BT5":[365,0,0,147,324],"GV2":[351,0,0,0,0],"BT4":[239,0,138,362,344],"BT6":[316,0,0,92,178],"BT8":[224,0,0,128,230],"CR5":[361,0,0,0,0],"CR8":[0,0,0,343,0],"GN0":[355,0,0,131,351],"HP1":[361,0,337,364,365],"CR7":[279,0,0,0,0],"EN1":[359,0,0,0,0],"WA2":[353,0,0,0,0],"EN4":[340,0,0,0,0],"GN4":[333,0,0,0,336],"GR8":[200,0,0,55,238],"WA7":[170,0,0,0,331],"EN5":[365,0,0,0,274],"GN3":[365,0,363,203,355],"GR7":[353,0,0,0,348],"GN6":[365,0,0,229,323],"WA9":[274,0,0,0,308],"EN7":[354,0,0,0,0],"GN5":[227,0,0,0,286],"GR9":[365,0,4,303,360],"WMD":[363,0,0,350,0],"WAA":[268,0,0,0,307],"WAC":[248,0,0,0,356],"WAB":[298,0,0,0,354],"TL5":[324,0,0,0,0],"TL4":[340,0,0,0,0],"TL6":[326,0,0,312,0],"BQ7":[346,0,343,362,362],"IS2":[295,0,0,0,196],"BQ9":[0,0,0,345,357],"IS6":[358,0,0,0,260],"NM2":[359,0,0,360,365],"NM4":[361,0,0,144,0],"NM3":[355,0,0,361,358],"RI2":[263,0,86,0,338],"RI1":[355,0,0,0,352],"SK5":[357,0,0,0,260],"SK6":[284,0,334,323,329],"SK9":[0,0,0,351,350],"SK8":[363,0,0,359,359],"HV1":[358,0,0,336,338],"HV3":[363,0,0,0,326],"SKA":[345,0,0,356,357],"KT4":[345,0,0,195,0],"SKC":[355,0,0,346,347],"SKB":[359,0,0,311,311],"KT6":[364,0,0,0,312],"KT5":[363,0,0,0,336],"ST4":[360,0,0,0,337],"ST6":[365,0,0,0,360],"ST5":[352,0,0,361,348],"ST9":[362,0,0,0,272],"LB4":[355,0,0,299,294]},
      speciesMonthlyLowDaysData: Site_Monthly_Low_Days,
      sites:{"LB6":"Lambeth - Streatham Green","MY1":"Westminster - Marylebone Road","RB4":"Redbridge - Gardner Close","LW2":"Lewisham - New Cross","RB7":"Redbridge - Ley Street","LW4":"Lewisham - Loampit Vale","LW6":"Lewisham -Laurence House Catford","LW5":"Lewisham - Deptford","BG2":"Barking and Dagenham - Scrattons Farm","EI1":"Ealing - Western Avenue","BG1":"Barking and Dagenham - Rush Green","CE3":"Regent Street (The Crown Estate)","CE2":"Waterloo Place (The Crown Estate)","HG1":"Haringey - Haringey Town Hall","HG4":"Haringey  - Priory Park South","EI8":"Ealing - Horn Lane TEOM","BX1":"Bexley - Slade Green","EA6":"Ealing - Hanger Lane Gyratory","BX2":"Bexley - Belvedere","EA8":"Ealing - Horn Lane","WM6":"Westminster - Oxford Street","BT5":"Brent - Neasden Lane","GV2":"Westminster - Duke Street (Grosvenor)","BT4":"Brent - Ikea","BT6":"Brent - John Keble Primary School","BT8":"Brent - ARK Franklin Primary Academy","CR5":"Croydon - Norbury","CR8":"Croydon - Norbury Manor","GN0":"Greenwich - A206 Burrage Grove","HP1":"Lewisham - Honor Oak Park","CR7":"Croydon - Purley Way A23","EN1":"Enfield - Bush Hill Park","WA2":"Wandsworth - Wandsworth Town Hall","EN4":"Enfield - Derby Road","GN4":"Greenwich - Fiveways Sidcup Rd A20","GR8":"Greenwich - Woolwich Flyover","WA7":"Wandsworth - Putney High Street","EN5":"Enfield - Bowes Primary School","GN3":"Greenwich - Plumstead High Street","GR7":"Greenwich - Blackheath","GN6":"Greenwich - John Harrison Way","WA9":"Wandsworth - Putney","EN7":"Enfield - Prince of Wales School","GN5":"Greenwich - Trafalgar Road (Hoskins St)","GR9":"Greenwich - Westhorne Avenue","WMD":"Westminster - Elizabeth Bridge","WAA":"Wandsworth - Battersea","WAC":"Wandsworth - Lavender Hill (Clapham Jct)","WAB":"Wandsworth - Tooting High Street","TL5":"Newham - Hoola Tower","TL4":"Greenwich - Tunnel Avenue","TL6":"Newham - Britannia Gate","BQ7":"Bexley - Belvedere West","IS2":"Islington - Holloway Road","BQ9":"Bexley - Slade Green FIDAS","IS6":"Islington - Arsenal","NM2":"Newham - Cam Road","NM4":"Newham - East Ham Town Hall","NM3":"Newham - Wren Close","RI2":"Richmond Upon Thames - Barnes Wetlands","RI1":"Richmond Upon Thames - Castelnau","SK5":"Southwark - A2 Old Kent Road","SK6":"Southwark - Elephant and Castle","SK9":"Southwark - A2 Old Kent Road FIDAS","SK8":"Southwark - Tower Bridge Road","HV1":"Havering - Rainham","HV3":"Havering - Romford","SKA":"Southwark - Lower Road","KT4":"Kingston Upon Thames - Tolworth Broadway","SKC":"Southwark - South Circular Road","SKB":"Southwark - Vicarage Grove","KT6":"Kingston Upon Thames - Kingston Vale","KT5":"Kingston Upon Thames - Cromwell Road","ST4":"Sutton - Wallington","ST6":"Sutton - Worcester Park","ST5":"Sutton - Beddington Lane north","ST9":"Sutton - Beddington Village","LB4":"Lambeth - Brixton Road"},
      species:{"NO2":"Nitrogen Dioxide","SO2":"Sulfur Dioxide","O3":"Ozone","PM25":"Particulate Matter 2.5","PM10":"Particulate Matter 10"},
      speciesIndexer:{"NO2":0, "SO2":1, "O3":2, "PM25":3, "PM10":4},
      months:{0:"Jan", 1:"Feb", 2:"Mar", 3:"Apr", 4:"May", 5:"June", 6:"July", 7:"Aug", 8:"Sep", 9:"Oct", 10:"Nov", 11:"Dec"},
      monthDays: [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31],
      selectedSite1: '',
      selectedSite2: '',
      selectedTopSpecies: null,
      selectedSpeciesCompare: null,
      selectedTopMonth: null,
      message: '',
      comparisonResult: '',
      choseTopNumber: null,
      topNumber: [3,5,10],
      site_Selected:'',
      top10details: [
      "1st -",
      "2nd -",
      "3rd -",
      "4th -",
      "5th -",
      "6th -",
      "7th -",
      "8th -",
      "9th -",
      "10th -"
    ],
    }
  }
  
  export default {
    name: 'Travelling',
    data: data,
    methods: {
      checkIndexForSelectedSite(){
        this.site_Selected = this.selectedSite1 
      },

    checkTopByMonth(){
      // console.log("speciesMonthlyLowDaysData",this.speciesMonthlyLowDaysData["BG1"][0][0]);
      var keyValues = [];
      if (this.selectedTopSpecies != null && this.selectedTopMonth != null) {
        for (var key in this.speciesMonthlyLowDaysData) {
          var monthData = this.speciesMonthlyLowDaysData[key][this.speciesIndexer[this.selectedTopSpecies]][this.selectedTopMonth];
          keyValues.push([key, monthData]);
        }
          
      console.log("month data",monthData)

      keyValues.sort(function compare(kv1, kv2) {
        // This comparison function has 3 return cases:
        // - Negative number: kv1 should be placed BEFORE kv2
        // - Positive number: kv1 should be placed AFTER kv2
        // - Zero: they are equal, any order is ok between these 2 items
        return kv1[1] - kv2[1]
      }).reverse()
      // console.log(keyValues)
      var returnSiteCodeList = []
      var returnSiteNameList = []
      for (var i = 0; i < keyValues.length; i++){
        if(i<this.choseTopNumber){
          returnSiteCodeList.push(keyValues[i][0]);
          var siteName = this.sites[keyValues[i][0]];
          returnSiteNameList.push(siteName)
        }
      }
      // console.log(returnSiteNameList)

      var messages = [];
      // Loop through the top 10 sites in the returnList
      for (var i = 0; i < returnSiteNameList.length; i++) {
        var siteName = returnSiteNameList[i]; // Site name from returnSiteNameList
        var siteCode = returnSiteCodeList[i]; // Site code from returnSiteCodeList
        
        // Find the low days value for the site
        var lowDaysValue = this.speciesMonthlyLowDaysData[siteCode][this.speciesIndexer[this.selectedTopSpecies]][this.selectedTopMonth];
        
        // Calculate the percentage of low index days
        var percentageLowDays = (lowDaysValue / this.monthDays[this.selectedTopMonth] * 100).toFixed(0); // Calculate percentage and round to nearest whole number
        if (percentageLowDays ==0){
          break;
        }
        // Create the message string
        if (i+1 == 1){var message = `${i+1}st - \t ${siteName} (${percentageLowDays}%)`;}
        else if (i+1 == 2){var message = `${i+1}nd - \t ${siteName} (${percentageLowDays}%)`;}
        else if (i+1 == 3){var message = `${i+1}rd - \t ${siteName} (${percentageLowDays}%)`;}
        else{var message = `${i+1}th - \t ${siteName} (${percentageLowDays}%)`;}
        messages.push(message);
      }
      this.top10details = messages;

      console.log(messages); // Output the messages
      
      }
    },
    compareBetterSite(){
      const site1Data = this.speciesAnnualLowDaysData[this.selectedSite1];
      const site2Data = this.speciesAnnualLowDaysData[this.selectedSite2];
      const species = this.selectedSpeciesCompare;
      const speciesIndex = this.speciesIndexer[this.selectedSpeciesCompare];
      console.log(site1Data)

      if (site1Data && site2Data && species !== undefined) {
      const site1Value = site1Data[speciesIndex] || 0;
      const site2Value = site2Data[speciesIndex] || 0;

      // Calculate the percentages
      const total = site1Value + site2Value;
      const site1Percentage = total > 0 ? (site1Value / total) * 100 : 0;
      const site2Percentage = total > 0 ? (site2Value / total) * 100 : 0;

      // Determine which site is better or if they are equally good
      if (site1Percentage > site2Percentage) {
        var siteName1 = this.sites[this.selectedSite1]
        // this.comparisonResult = `For species ${species}, ${siteName1} is a better site.`;
        this.comparisonResult = `For species ${species}, ${siteName1} is a better site to live in based on past data.`;
      } else if (site1Percentage < site2Percentage) {
        var siteName2 = this.sites[this.selectedSite2]
        // this.comparisonResult = `For species ${species}, ${siteName2} is a better site.`;
        this.comparisonResult = `For species ${species}, ${siteName2} is a better site to live in based on past data.`;
      } else {
        var siteName1 = this.sites[this.selectedSite1]
        var siteName2 = this.sites[this.selectedSite2]
        // this.comparisonResult = `For species ${species}, both ${siteName1} and ${siteName2} are equally good.`;
        this.comparisonResult = `For species ${species}, both ${siteName1} and ${siteName2} are equally good to live in.`;
      }
      } else {
        this.comparisonResult = 'Please select both sites and a species to compare.';
      }
    },
    updateTopMonth() {
      // handle changes in selectedTopSpecies
      console.log("Selected Species:", this.selectedTopSpecies);
      console.log("Month Index:", this.selectedTopMonth);
      this.checkTopByMonth(); 
    },
    updateCompare() {
      // handle changes in selectedSpeciesCompare
      console.log("Selected Species:", this.selectedSpeciesCompare);
      console.log("Selected Site 1:", this.selectedSite1);
      console.log("Selected Site 2:", this.selectedSite2);
      this.compareBetterSite(); 
    }},
    created(){

    },
    mounted() {
      // this.checkForValuesGreaterThanThresholds();
      // this.calculatePercentageOfIndices();
      // this.updateTop10();
      this.updateTopMonth();
      this.updateCompare();
      // this.checkTop10();
    },
  
    watch: {
      selectedTopSpecies(newVal, oldVal) {
      if (newVal !== oldVal) {
        this.updateTopMonth();
      }},
      selectedTopMonth(newVal, oldVal) {
      if (newVal !== oldVal) {
        this.updateTopMonth();
      }},
      choseTopNumber(newVal, oldVal) {
      if (newVal !== oldVal) {
        this.updateTopMonth();
      }},
      // selectedSite1() { this.compareBetterSite(); },
      // selectedSite2() { this.compareBetterSite(); },
      // selectedSpeciesCompare() { this.compareBetterSite(); },
      selectedSite1(newVal, oldVal) {
      if (newVal !== oldVal) {
        this.updateCompare();
      }},
      selectedSite2(newVal, oldVal) {
      if (newVal !== oldVal) {
        this.updateCompare();
      }},
      selectedSpeciesCompare(newVal, oldVal) {
      if (newVal !== oldVal) {
        this.updateCompare();
      }},
    }
  }

  </script>
  
  <style scoped="scoped"
    lang="scss">
    .tab {
      margin-left: 20px; /* This creates a space equivalent to a tab */
    }
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
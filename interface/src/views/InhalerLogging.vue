<template>
  <div v-if="currentView === 'default'">

    <!-- Default view content (Inhaler Logging Page)-->
    <div class="inhaler-logging">
      <div class="cards-container">

        <div class="card-section">
          <div class="card reliever">
            <h2>Reliever</h2>
            <p>{{ puffsToday }} puffs today</p>
            <p>{{ puffsThisWeek }} puffs this week</p>
            <p class="frequency-label"><strong>Frequency</strong></p>
            <p class="frequency-value" :class="{ 'abnormal-frequency': puffsThisWeek > abnormalFrequencyThreshold }">{{ frequencyStatus }}</p>
            <div class="dose-info">
              <div class="dose-remaining">
                  <strong>Remaining</strong>
                  <div>{{ reliever.dosesRemaining }}/{{ reliever.totalDoses }} doses</div>
              </div>
              <div class="progress-bar">
                  <div class="progress-overlay" :style="reliever.getOverlayStyle()"></div>
              </div>
            </div>
            <button @click="logPuff">Tap to log</button>
            <div v-if="reliever.dosesRemaining === 0">
              <button @click="addNewReliever">Add New Inhaler</button>
            </div>
          </div>
        </div>

        <!-- Preventer Section (shown only if needed) -->
        <div v-if="selectedInhalerType === 'preventer'" class="card-section">
          <div class="card preventer">
            <h2>Preventer</h2>
            <div @click="toggleDose('morningDose')" class="dose">
              <span>
                <i class="fas fa-sun"></i>
                {{ morningDose.numberOfPuffs }} puffs at {{ morningDose.time }} 
              </span>
              <div v-if="morningDose.taken" class="overlay">Taken</div>
            </div>

            <div @click="toggleDose('eveningDose')" class="dose">
              <span>
                <i class="fas fa-moon"></i>
                {{ eveningDose.numberOfPuffs }} puffs at {{ eveningDose.time }} 
              </span>
              <div v-if="eveningDose.taken" class="overlay">Taken</div>
            </div>
            <div class="dose-info">
              <div class="dose-remaining">
                  <strong>Remaining</strong>
                  <div>{{ preventer.dosesRemaining }}/{{ preventer.totalDoses }} doses</div>
              </div>
              <div class="progress-bar">
                  <div class="progress-overlay" :style="preventer.getOverlayStyle()"></div>
              </div>
              <div v-if="preventer.dosesRemaining === 0">
                <button @click="addNewPreventer">Add New Inhaler</button>
              </div>
            </div>
          </div>
        </div>

        <!-- Combination  Section (shown only if needed) -->
        <div v-if="selectedInhalerType === 'combination'" class="card-section">
          <div class="card combination">
            <h2>Combination</h2>
            <div @click="toggleDose('morningDose')" class="dose">
              <span>
                <i class="fas fa-sun"></i>
                {{ morningDose.numberOfPuffs }} puffs at {{ morningDose.time }}
              </span>
              <div v-if="morningDose.taken" class="overlay">Taken</div>
            </div>

            <div @click="toggleDose('eveningDose')" class="dose">
              <span>
                <i class="fas fa-moon"></i>
                {{ eveningDose.numberOfPuffs }} puffs at {{ eveningDose.time }}
              </span>
              <div v-if="eveningDose.taken" class="overlay">Taken</div>
            </div>
            <div class="dose-info">
              <div class="dose-remaining">
                  <strong>Remaining</strong>
                  <div>{{ preventer.dosesRemaining }}/{{ preventer.totalDoses }} doses</div>
              </div>
              <div class="progress-bar">
                  <div class="progress-overlay" :style="preventer.getOverlayStyle()"></div>
              </div>
            </div>
            <div v-if="preventer.dosesRemaining === 0">
              <button @click="addNewPreventer">Add New Inhaler</button>
            </div>
          </div>
      </div>
    </div>

    <div v-if="warningMessage" class="warning-box">
      <strong>!!Warning!!</strong>
      <p v-html="warningMessage"></p> 
    </div>
  </div>

  <button @click="showLogHistory" class="log-history-btn">Logging History</button>
  <component
    v-if="currentComponent"
    :is="currentComponent"
    @log-deleted="handleLogDeletion"
    @change-view="handleChangeView"
  ></component>

  <button @click="resetEverything">Reset Everything (Dev)</button>
</div>

<LogHistory
    v-if="currentView === 'logHistory'"
    @log-deleted="handleLogDeletion"
    @change-view="handleChangeView"
  ></LogHistory>

</template>


<script>
import LogHistory from '@/components/LogHistory.vue'

// Object constructor for DoseTracker
export class DoseTracker {
  constructor(initialDoses) {
  this.totalDoses = initialDoses;
  this.dosesRemaining = initialDoses;
  }
  logPuff(numberOfPuffs) {
    if (this.dosesRemaining >= numberOfPuffs) {
      this.dosesRemaining -= numberOfPuffs;
      return true;
    } else {
      alert('No remaining doses!');
      return false;
    }
  }
  resetIfNewDay() {
    const today = new Date();
    if (today.toDateString() !== this.lastResetDate) {
      this.dosesRemaining = this.totalDoses;
      this.lastResetDate = today.toDateString();
    }
  }
  getOverlayStyle() {
    const ratio = Math.min(Math.max((this.totalDoses - this.dosesRemaining) / this.totalDoses, 0), 1);
    return {
    width: `${ratio * 100}%`,
    backgroundColor: ratio < 1 ? 'white' : 'white', 
    };
  }
}


export default {
  components: {
    LogHistory
  },

  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.currentView = 'default';
    });
  },
  
  beforeRouteUpdate(to, from, next) {
    this.currentView = 'default';
    next();
  },

  data() {
    return {
      selectedInhalerType: this.getStoredSetting('selectedInhalerType', 'none'),
      puffsToday: this.getStoredSetting('puffsToday', 0),
      puffsThisWeek: this.getStoredSetting('puffsThisWeek', 0),
      lastResetDay: this.getStoredSetting('lastResetDay', new Date().getDay()),
      reliever: new DoseTracker(this.getStoredSetting('relieverDoses', 1)),
      abnormalFrequencyThreshold: this.getStoredSetting('abnormalFrequency', 3),
      preventer: new DoseTracker(this.getStoredSetting('preventerDoses', 1)),
      dosesRemaining: this.getStoredSetting('dosesRemaining', 1),
      morningDose: {
        numberOfPuffs: this.getStoredSetting('morningNumberOfPuffs', 2),
        time: this.getStoredSetting('morningTime', '9:00am'),
        taken: this.getStoredSetting('morningDoseTaken', false)
      },
      eveningDose: {
        numberOfPuffs: this.getStoredSetting('eveningNumberOfPuffs', 2),
        time: this.getStoredSetting('eveningTime', '9:00pm'),
        taken: this.getStoredSetting('eveningDoseTaken', false)
      },
      preventerDosesRemaining: this.getStoredSetting('preventerDosesRemaining', 1),
      lowDoseReliever: false,
      lowDosePreventer: false,
      currentView: 'default'
    };
  },

  computed: {
    currentComponent() {
      if (this.currentView === 'logHistory') {
        return LogHistory;
      } else {
        // If no specific component, return null or a default component
        return null; // or return DefaultComponent if it's defined
      }
    },
    frequencyStatus() {
      return this.puffsThisWeek > this.abnormalFrequencyThreshold ? 'Abnormal' : 'Normal';
    },
    overlayStyle() {
      const ratio = 1 - (this.dosesRemaining / this.totalDoses);
      return {
        width: `${ratio * 100}%`,
        backgroundColor: 'white', 
      };
    },
    
    warningMessage() {
    let messages = [];
     const lowDoseThresholdReliever = this.reliever.totalDoses * (parseInt(localStorage.getItem('relieverLowDoseThreshold') || 20) / 100);
      const lowDoseThresholdPreventer = this.preventer.totalDoses * (parseInt(localStorage.getItem('preventerLowDoseThreshold') || 20) / 100);

      if (this.puffsThisWeek > this.abnormalFrequencyThreshold) {
        messages.push('Please book a GP appointment as soon as possible!');
      }
      if (this.reliever.dosesRemaining < lowDoseThresholdReliever) { 
        messages.push('Time to pick up a new reliever inhaler!');
      }
      if (this.preventer.dosesRemaining < lowDoseThresholdPreventer) {
        const inhalerTypeMessage = this.selectedInhalerType === 'preventer' ? 'Time to pick up a new preventer inhaler!' : 'Time to pick up a new combination inhaler!';
        messages.push(inhalerTypeMessage);
      }
      return messages.join('<br>');
    },
  },

  created() {
    console.log("InhalerLogging - Created Hook: State loaded");
    this.loadState();
    this.refreshDoses();
    this.updateDoses();
    this.updateMorningAndEveningDoses(); 

    window.addEventListener('storage', this.localStorageChanged);
    window.addEventListener('localStorageChanged', this.handleLocalStorageChange);
  },

  methods: {
    handleChangeView(newView) {
      this.currentView = newView;
    },
    showLogHistory() {
      this.currentView = 'logHistory';
    },
    handleLogDeletion() {
      console.log("handleLogDeletion");
      this.puffsToday -= 1;
      this.puffsThisWeek -= 1;
      this.reliever.dosesRemaining += 1;
      this.saveState();
    },
    
    handleLocalStorageChange() {
        this.loadState();
        this.updateMorningAndEveningDoses();
    },

    updateMorningAndEveningDoses() {
      this.morningDose.numberOfPuffs = this.getStoredSetting('morningNumberOfPuffs', 2);
      this.morningDose.time = this.getStoredSetting('morningTime', '9:00am');
      this.eveningDose.numberOfPuffs = this.getStoredSetting('eveningNumberOfPuffs', 2);
      this.eveningDose.time = this.getStoredSetting('eveningTime', '9:00pm');
    },

    resetEverything() {
      // Reset the local component data
      this.selectedInhalerType = 'none';
      this.puffsToday = 0;
      this.puffsThisWeek = 0;
      this.lastResetDay = new Date().getDay();
      this.reliever = new DoseTracker(this.getStoredSetting('relieverDoses', 1));
      this.preventer = new DoseTracker(this.getStoredSetting('preventerDoses', 1));

      // Clear or reset localStorage items
      localStorage.setItem('relieverDosesRemaining', JSON.stringify(1)); // Reset to default
      localStorage.setItem('logHistory', JSON.stringify([])); // Clear log history
      localStorage.setItem('puffsToday', JSON.stringify(0)); // Reset puffs today
      localStorage.setItem('puffsThisWeek', JSON.stringify(0)); // Reset puffs this week

      // Save the updated state
      this.saveState();
    },

    getStoredSetting(key, defaultValue) {
      const value = localStorage.getItem(key);
      if (value === null) {
        return defaultValue;
      }
      try {
        // Try parsing as JSON
        return JSON.parse(value);
      } catch (e) {
        console.error(`Error parsing ${key} from localStorage:`, e);
        // If parsing fails, return the string value itself
        return value;
      }
    },

    saveState() {
      // Save the current state to localStorage
      const state = {
        selectedInhalerType: this.selectedInhalerType,
        puffsToday: this.puffsToday,
        puffsThisWeek: this.puffsThisWeek,
        lastResetDay: this.lastResetDay,
        relieverDoses: this.reliever.dosesRemaining,
        preventerDoses: this.preventer.dosesRemaining,
        morningDose: this.morningDose,
        eveningDose: this.eveningDose,
      };

      console.log("Saving state:", state);
      localStorage.setItem('inhalerState', JSON.stringify(state));
    },

    loadState() {
    // Load the state from localStorage
      const savedState = localStorage.getItem('inhalerState');
      if (savedState) {
        const state = JSON.parse(savedState);
        console.log("Loaded state:", state);
        this.puffsToday = state.puffsToday;
        this.puffsThisWeek = state.puffsThisWeek;
        this.reliever.dosesRemaining = state.relieverDoses;
        this.preventer.dosesRemaining = state.preventerDoses;
        this.morningDose = state.morningDose;
        this.eveningDose = state.eveningDose;
      }
      this.updateMorningAndEveningDoses();
    },

    localStorageChanged(event) {
      console.log("Local storage changed:", event.key);
      // Check if the changed key is one of the relevant keys
      if (event.key === 'relieverDoses' || event.key === 'preventerDoses') { 
        const customEvent = new Event('localStorageChanged');
        window.dispatchEvent(customEvent);
      }
    },


    logPuff() {
      const logSuccess = this.reliever.logPuff(1);
      if (logSuccess) {
        this.puffsToday += 1;
        this.puffsThisWeek += 1;

        let logHistory = JSON.parse(localStorage.getItem('logHistory')) || [];
        logHistory.push({
          type: 'reliever',
          time: new Date().toISOString(),
          action: 'clicked' 
        });

        localStorage.setItem('logHistory', JSON.stringify(logHistory));
        this.saveState();
      }
    },

    getSetting(key, defaultValue) {
      const value = localStorage.getItem(key);
      return value !== null ? value : defaultValue;
    },

    refreshDoses() {
      this.morningDose.numberOfPuffs = parseInt(this.getSetting('morningNumberOfPuffs', '2'), 10);
      this.morningDose.time = this.getSetting('morningTime');
      this.eveningDose.numberOfPuffs = parseInt(this.getSetting('eveningNumberOfPuffs', '2'), 10);
      this.eveningDose.time = this.getSetting('eveningTime');
    },

    getStoredDoses(key, defaultValue) {
      const storedDoses = localStorage.getItem(key);
      return storedDoses ? parseInt(storedDoses, 10) : defaultValue;
    },
    
    updateDoses() {
      this.reliever.totalDoses = this.getStoredDoses('relieverDoses', 1);
      this.preventer.totalDoses = this.getStoredDoses('preventerDoses', 1);
    },
    
    toggleDose(doseType) {
      const dose = this[doseType];
      const wasTaken = dose.taken;
      dose.taken = !dose.taken;

      if (dose.taken) {
        if (this.preventer.dosesRemaining >= dose.numberOfPuffs) {
          this.preventer.dosesRemaining -= dose.numberOfPuffs;
        } else {
          alert('No sufficient doses remaining!');
          dose.taken = false; // Reset the taken state if there are no sufficient doses
          return;
        }
      } else {
        this.preventer.dosesRemaining += dose.numberOfPuffs;
      }

      this.logDose(doseType, dose.taken ? 'clicked' : 'unclicked', wasTaken);
      this.saveState();
    },

    logDose(timeOfDay, action, wasTaken) {
      const now = new Date().toISOString();
      let logHistory = JSON.parse(localStorage.getItem('logHistory')) || [];
      let doseType = this.selectedInhalerType; 

      logHistory.push({
        type: doseType,
        time: now,
        timeOfDay: timeOfDay,
        action: action
      });

      localStorage.setItem('logHistory', JSON.stringify(logHistory));
    },

    checkAndResetCounts: function() {
        const today = new Date();
        const lastUsedDate = new Date(localStorage.getItem('lastUsedDate'));
        const lastUsedWeek = new Date(localStorage.getItem('lastUsedWeek'));

        // Check and reset daily count
        if (today.toDateString() !== lastUsedDate.toDateString()) {
            localStorage.setItem('puffsToday', 0); // Reset daily count
            localStorage.setItem('lastUsedDate', today.toISOString()); // Update last used date
        }

        // Check and reset weekly count
        if (this.getWeekNumber(today) !== this.getWeekNumber(lastUsedWeek)) {
            localStorage.setItem('puffsThisWeek', 0); // Reset weekly count
            localStorage.setItem('lastUsedWeek', today.toISOString()); // Update last used week
        }
    },

    getWeekNumber: function(date) {
        const startOfYear = new Date(date.getFullYear(), 0, 1);
        const pastDaysOfYear = (date - startOfYear) / 86400000;
        return Math.ceil((pastDaysOfYear + startOfYear.getDay() + 1) / 7);
    },

    addNewReliever() {
      this.reliever.dosesRemaining = this.getStoredDoses('relieverDoses'); // Reset to initial value
      this.saveState(); // Save the updated state
    },

    addNewPreventer() {
      this.preventer.dosesRemaining = this.getStoredDoses('preventerDoses'); // Reset to initial value
      this.morningDose.taken = false;
      this.eveningDose.taken = false;
      this.saveState(); // Save the updated state
    },
    
  },
  
  watch: {
    '$route'() {
      this.loadState();
    },
    'localStorageChanged'() {
      this.loadState();
    }
  },

  mounted() {
    console.log("InhalerLogging - Mounted Hook: Component mounted");
    this.loadState();
    this.checkAndResetCounts();
    window.addEventListener('storage', this.handleStorageChange);
  },

  updated() {
    console.log("InhalerLogging - Updated Hook: Component updated");
  },

  beforeDestroy() {
    console.log("InhalerLogging - BeforeDestroy Hook: Cleaning up");
    clearInterval(this.interval);
    window.removeEventListener('storage', this.localStorageChanged);
    window.removeEventListener('storage', this.handleStorageChange);
  }
};
</script>

<style scoped>
.inhaler-logging {
  display: flex;
  flex-direction: column; 
  align-items: stretch; 
}

.cards-container {
  display: flex;
  flex-direction: row;
  width: 100%;
  justify-content: space-between;
}

.card-section {
  flex: 1; 
  padding: 10px;
  box-sizing: border-box;
}

.card {
  flex-grow: 1;
  margin: 10px;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
}

.reliever {
  background-color: #e0f7fa;
}

.preventer {
  background-color: #ffe0b2;
}

.combination {
    background-color: #E5d3f2;
}

.dose {
  cursor: pointer;
  display: flex;
  position: relative;
  justify-content: left;
  align-items: center; 
  height: 85px; 
  margin: 10px 0; 
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 255, 0, 0.5); 
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
}

button {
  margin-top: 10px;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  background-color: #26c6da;
  color: white;
  cursor: pointer;
}

button:hover {
  background-color: #00acc1;
}

.fas.fa-sun, .fas.fa-moon {
  margin-right: 5px;
}

.card > * {
  margin-bottom: 10px;
}

.card > *:last-child {
  margin-bottom: 0;
}

.progress-bar {
  width: 100%;
  height: 20px;
  background-image: linear-gradient(to left, red, orange, yellow, green);
  border-radius: 10px;
  overflow: hidden;
}

.progress-overlay {
  height: 100%;
  transition: width 0.3s ease;
}

.frequency-label,
.frequency-value {
  margin-top: 10px; 
}

.abnormal-frequency {
  color: red; 
  font-weight: bold; 
}

.warning-box-section {
  width: 100%; 
  display: flex;
  justify-content: center; 
}

.warning-box {
  background-color: #f8d7da;
  color: #721c24;
  padding: 20px;
  margin-top: 20px;
  text-align: center;
  border-radius: 5px;
  width: 95%; 
}

.log-history-btn {
  position: absolute;
  bottom: 20px; 
  right: 20px; 
  padding: 10px 20px;
  background-color: #a792ba; 
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  z-index: 10; 
}

.log-history-btn:hover {
  background-color: #754599;
}

</style>
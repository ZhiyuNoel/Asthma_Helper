<template>
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
        </div>
      </div>

      <!-- Preventer Section (shown only if needed) -->
      <div v-if="selectedInhalerType === 'preventer'" class="card-section">
        <div class="card-section">
          <div class="card preventer">
            <h2>Preventer</h2>
            <div @click="toggleMorningDose" class="dose">
              <span>
                <i class="fas fa-sun"></i>
                {{ morningDose.numberOfPuffs }} puffs at {{ morningDose.time }}
              </span>
              <div v-if="morningDose.taken" class="overlay">Taken</div>
            </div>

            <div @click="toggleEveningDose" class="dose">
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
          </div>
        </div>
      </div>

      <!-- Combination  Section (shown only if needed) -->
      <div v-if="selectedInhalerType === 'combination'" class="card-section">
        <div class="card combination">
          <h2>Combination</h2>
          <div @click="toggleMorningDose" class="dose">
            <span>
              <i class="fas fa-sun"></i>
              {{ morningDose.numberOfPuffs }} puffs at {{ morningDose.time }}
            </span>
            <div v-if="morningDose.taken" class="overlay">Taken</div>
          </div>

          <div @click="toggleEveningDose" class="dose">
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
        </div>
      </div>
    </div>

    <div v-if="warningMessage" class="warning-box">
      <strong>!!Warning!!</strong>
      <p v-html="warningMessage"></p> <!-- Use v-html here -->
    </div>
  </div>

  <button @click="goToLogHistory" class="log-history-btn">Logging History</button>
  <LogHistory @log-deleted="handleLogDeletion" />
</template>


<script>
// Object constructor for DoseTracker
function DoseTracker(initialDoses) {
  this.totalDoses = initialDoses;
  this.dosesRemaining = initialDoses;
}

// Methods for the DoseTracker object
DoseTracker.prototype = {
  logPuff: function(numberOfPuffs) {
    if (this.dosesRemaining >= numberOfPuffs) {
      this.dosesRemaining -= numberOfPuffs;
    } else {
      alert('No remaining doses!');
    }
  },
  resetIfNewDay: function() {
    const today = new Date();
    if (today.toDateString() !== this.lastResetDate) {
      this.dosesRemaining = this.totalDoses;
      this.lastResetDate = today.toDateString();
    }
  },
  getOverlayStyle: function() {
    const ratio = (this.totalDoses - this.dosesRemaining) / this.totalDoses;
    return {
      width: `${ratio * 100}%`,
      backgroundColor: 'white',
    };
  }
};

export default {
  data() {
    return {
      selectedInhalerType: this.getStoredSetting('selectedInhalerType', 'none'),
      puffsToday: this.getStoredSetting('puffsToday', 0),
      puffsThisWeek: this.getStoredSetting('puffsThisWeek', 0),
      lastResetDay: this.getStoredSetting('lastResetDay', new Date().getDay()),
      reliever: new DoseTracker(this.getStoredSetting('relieverDoses', 200)),
      abnormalFrequencyThreshold: this.getStoredSetting('abnormalFrequency', 3),
      preventer: new DoseTracker(this.getStoredSetting('preventerDoses', 200)),
      dosesRemaining: this.getStoredSetting('dosesRemaining', 200),
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
      preventerDosesRemaining: 200,
      lowDoseReliever: false,
      lowDosePreventer: false,
      lastResetDay: new Date().getDay(),
    };
  },

  computed: {
    frequencyStatus() {
      return this.puffsThisWeek > this.abnormalFrequencyThreshold ? 'Abnormal' : 'Normal';
    },
    overlayStyle() {
      const ratio = 1 - (this.dosesRemaining / this.totalDoses);
      return {
        width: `${ratio * 100}%`,
        backgroundColor: 'white', // This is the color that will cover the gradient.
      };
    },
    showWarning() {
      return this.puffsThisWeek > 3;
    },
    showLowDoseWarning() {
      return this.lowDoseReliever || this.lowDosePreventer;
    },
    warningMessage() {
    let messages = [];
     const lowDoseThresholdReliever = this.reliever.totalDoses * (parseInt(localStorage.getItem('relieverLowDoseThreshold') || 20) / 100);
      const lowDoseThresholdPreventer = this.preventer.totalDoses * (parseInt(localStorage.getItem('preventerLowDoseThreshold') || 20) / 100);

      if (this.puffsThisWeek > this.abnormalFrequencyThreshold) {
        messages.push('Please book a GP appointment as soon as possible!');
      }
      if (this.reliever.dosesRemaining < lowDoseThresholdReliever) { // Replace with your threshold
        messages.push('Time to pick up a new reliever!');
      }
      if (this.preventer.dosesRemaining < lowDoseThresholdPreventer) {
        messages.push('Time to pick up a new preventer!');
      }
      return messages.join('<br>');
    },
  },

  created() {
    this.loadState();
    this.refreshDoses();
    this.updateDoses();
  },

  methods: {
    handleLogDeletion(puffsDeleted) {
      this.puffsToday = Math.max(this.puffsToday - puffsDeleted, 0);
      this.puffsThisWeek = Math.max(this.puffsThisWeek - puffsDeleted, 0);
      this.reliever.dosesRemaining = this.getStoredRelieverDoses(); // Updating doses from localStorage
      this.saveState(); // Save the updated state
    },
    
    getStoredRelieverDoses() {
      const storedValue = localStorage.getItem('relieverDosesRemaining');
      return storedValue ? parseInt(JSON.parse(storedValue), 10) : this.reliever.totalDoses;
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
        // Other properties to save
      };
      localStorage.setItem('inhalerState', JSON.stringify(state));
    },

    loadState() {
    // Load the state from localStorage
      const savedState = localStorage.getItem('inhalerState');
      if (savedState) {
        const state = JSON.parse(savedState);
        // Assign saved state to data properties
        this.puffsToday = state.puffsToday;
        this.puffsThisWeek = state.puffsThisWeek;
        this.reliever.dosesRemaining = state.relieverDoses;
        this.preventer.dosesRemaining = state.preventerDoses;
        this.morningDose = state.morningDose;
        this.eveningDose = state.eveningDose;
      }
      const savedRelieverDoses = localStorage.getItem('relieverDosesRemaining');
      if (savedRelieverDoses) {
        this.reliever.dosesRemaining = JSON.parse(savedRelieverDoses);
      }
    },


    logPuff() {
      this.reliever.logPuff(1);
      this.puffsToday += 1;
      this.puffsThisWeek += 1;

      // Record the timestamp and other relevant information
      let logHistory = JSON.parse(localStorage.getItem('logHistory')) || [];
      logHistory.push({
        type: 'reliever',
        time: new Date().toISOString(),
        action: 'clicked' // Assuming every tap is a 'clicked' action
      });

      localStorage.setItem('logHistory', JSON.stringify(logHistory));
      this.saveState();
    },

    getSetting(key, defaultValue) {
      const value = localStorage.getItem(key);
      return value !== null ? value : defaultValue;
    },

    refreshDoses() {
      this.morningDose.numberOfPuffs = parseInt(this.getSetting('morningNumberOfPuffs', '2'), 10);
      this.morningDose.time = this.getSetting('morningTime', '9:00am');
      this.eveningDose.numberOfPuffs = parseInt(this.getSetting('eveningNumberOfPuffs', '2'), 10);
      this.eveningDose.time = this.getSetting('eveningTime', '9:00pm');
    },

    getStoredDoses(key, defaultValue) {
      const storedDoses = localStorage.getItem(key);
      return storedDoses ? parseInt(storedDoses, 10) : defaultValue;
    },
    
    updateDoses() {
      this.reliever.totalDoses = this.getStoredDoses('relieverDoses', 200);
      this.preventer.totalDoses = this.getStoredDoses('preventerDoses', 200);
    },
    // Use the DoseTracker instance methods to toggle morning and evening doses
    
    toggleMorningDose() {
      const wasTaken = this.morningDose.taken;
      this.morningDose.taken = !this.morningDose.taken;
      if (this.morningDose.taken) {
        if (this.preventer.dosesRemaining >= this.morningDose.numberOfPuffs) {
          this.preventer.dosesRemaining -= this.morningDose.numberOfPuffs;
        } else {
          alert('No sufficient doses remaining!');
          this.morningDose.taken = false; // Reset the taken state if there are no sufficient doses
          return;
        }
      } else {
        this.preventer.dosesRemaining += this.morningDose.numberOfPuffs;
      }
      this.logDose('morning', this.morningDose.taken ? 'clicked' : 'unclicked', wasTaken);
      this.saveState();
    },

    toggleEveningDose() {
      const wasTaken = this.eveningDose.taken;
      this.eveningDose.taken = !this.eveningDose.taken;
      if (this.eveningDose.taken) {
        if (this.preventer.dosesRemaining >= this.eveningDose.numberOfPuffs) {
          this.preventer.dosesRemaining -= this.eveningDose.numberOfPuffs;
        } else {
          alert('No sufficient doses remaining!');
          this.eveningDose.taken = false; // Reset the taken state if there are no sufficient doses
          return;
        }
      } else {
        this.preventer.dosesRemaining += this.eveningDose.numberOfPuffs;
      }
      this.logDose('evening', this.eveningDose.taken ? 'clicked' : 'unclicked', wasTaken);
      this.saveState();
    },

    logDose(timeOfDay, action, wasTaken) {
      const now = new Date().toISOString();
      let logHistory = JSON.parse(localStorage.getItem('logHistory')) || [];
      let doseType = this.selectedInhalerType; // 'preventer' or 'combination'

      logHistory.push({
        type: doseType,
        time: now,
        timeOfDay: timeOfDay,
        action: action
      });

      localStorage.setItem('logHistory', JSON.stringify(logHistory));
    },

    resetPuffsDaily() {
      const today = new Date();
      if (this.lastResetDay !== today.getDay()) {
        this.puffsToday = 0;
        this.lastResetDay = today.getDay();
        this.saveState(); // Save the reset state
      }
    },

    resetPuffsWeekly() {
      const today = new Date();
      if (today.getDay() === 0 && this.lastResetWeek !== today.getDay()) {
        this.puffsThisWeek = 0;
        this.lastResetWeek = today.getDay();
        this.saveState(); // Save the reset state
      }
    },

    checkResets() {
      this.resetPuffsDaily();
      this.resetPuffsWeekly();
    },

    goToLogHistory() {
      this.$router.push({ name: 'LogHistory' }); // Use the name of the route
    },
  },
  
  watch: {
    '$route'() {
      this.loadState();
    },
  },

  mounted() {
    this.interval = setInterval(() => {
      this.checkResets();
    }, 60000); // Every minute
    this.loadState();
  },

  beforeDestroy() {
    clearInterval(this.interval);
  }
};
</script>

<style scoped>
.inhaler-logging {
  display: flex;
  flex-direction: column; /* Stack children vertically */
  align-items: stretch; /* Stretch children to match the parent's width */
}

.cards-container {
  display: flex;
  flex-direction: row;
  width: 100%;
  justify-content: space-between;
}

.card-section {
  flex: 1; /* This will ensure both sections take equal space */
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
  position: relative;
}

.combination {
    background-color: #E5d3f2;
}

.dose {
  cursor: pointer;
  display: flex;
  position: relative;
  justify-content: left;
  align-items: center; /* Center items vertically */
  height: 85px; /* Increase the height */
  margin: 10px 0; /* Add some margin top and bottom */
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 255, 0, 0.5); /* Semi-transparent green overlay */
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

/* Ensure the buttons and paragraphs are spaced evenly */
.card > * {
  margin-bottom: 10px;
}

/* Make sure the last child doesn't have a margin-bottom */
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
  margin-top: 10px; /* Adjust as needed */
}

.frequency-value {
  margin-top: 10px; /* Adjust as needed */
}

.abnormal-frequency {
  color: red; /* Red color for abnormal */
  font-weight: bold; /* Bold text for abnormal */
}

.warning-box-section {
  width: 100%; /* Full width */
  display: flex;
  justify-content: center; /* Center the warning box horizontally */
}

.warning-box {
  background-color: #f8d7da;
  color: #721c24;
  padding: 20px;
  margin-top: 20px;
  text-align: center;
  border-radius: 5px;
  width: 95%; /* Span full width */
}

.log-history-btn {
  position: absolute;
  bottom: 20px; /* Adjust as needed */
  right: 20px; /* Adjust as needed */
  padding: 10px 20px;
  background-color: #FFC0CB; /* Pink background */
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  z-index: 10; /* Ensure it's above other elements */
}

.log-history-btn:hover {
  background-color: #EAB3B3; /* Slightly darker pink on hover */
}


</style>
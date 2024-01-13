<template>
  <div class="log-history">
    <h2>Log History</h2>
    <div class="log-columns">
      <div class="log-column">
        <h3>Reliever Log</h3>
        <ul>
          <li v-for="(log, index) in relieverLogs" :key="`reliever-${index}`">
            {{ new Date(log.time).toLocaleString() }} - {{ log.action }}
            <button @click="deleteLog(log, index)">Delete</button>
          </li>
        </ul>
      </div>
      <div class="log-column">
        <h3>Preventer/Combination Log</h3>
        <ul>
          <li v-for="(log, index) in preventerCombinationLogs" :key="`preventer-combination-${index}`">
            {{ new Date(log.time).toLocaleString() }} - {{ log.type }} - {{ log.timeOfDay }} - {{ log.action }}
          </li>
        </ul>
      </div>
    </div>
    <button @click="clearLogHistory" class="clear-log-btn">Clear Logging History</button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      logs: JSON.parse(localStorage.getItem('logHistory')) || []
    };
  },
  computed: {
    relieverLogs() {
      return this.logs.filter(log => log.type === 'reliever');
    },
    preventerCombinationLogs() {
      return this.logs.filter(log => log.type !== 'reliever');
    },
  },

  methods: {
    deleteLog(log, index) {
      if (log.type === 'reliever' && log.action === 'clicked') {
        // Decrement the remaining doses
        const updatedDoses = this.getStoredRelieverDoses() - 1;
        localStorage.setItem('relieverDosesRemaining', JSON.stringify(updatedDoses));
        
        // Emit an event to notify the parent component
        this.$emit('log-deleted', 1); // Emitting 1 puff deleted
      }

      // Remove the specific log
      this.logs.splice(index, 1);
      localStorage.setItem('logHistory', JSON.stringify(this.logs));
    },

    getStoredRelieverDoses() {
      const storedValue = localStorage.getItem('relieverDosesRemaining');
      return storedValue ? parseInt(JSON.parse(storedValue), 10) : 200; // Default to 200 if not set
    },

    getStoredSetting(key, defaultValue) {
      const value = localStorage.getItem(key);
      return value !== null ? JSON.parse(value) : defaultValue;
    },

    saveRelieverDoses() {
      localStorage.setItem('relieverDosesRemaining', JSON.stringify(this.relieverDosesRemaining));
    },

    clearLogHistory() {
      if (window.confirm("Are you sure you want to clear the logging history? This action cannot be undone.")) {
        localStorage.setItem('logHistory', JSON.stringify([])); 
        this.logs = []; 
        alert("Logging history has been cleared.");
      }
    },
  },
}

</script>

<style scoped>

.log-columns {
    display: flex;
    justify-content: space-between;
  }

  .log-column {
    flex: 1;
    padding: 0 20px;
  }

.clear-log-btn {
  /* Styling for your clear button */
  margin-top: 20px;
  padding: 10px 20px;
  background-color: #ffcccc; /* Example color */
  color: #333;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.clear-log-btn:hover {
  background-color: #ff9999; /* Darker color on hover */
}

  .log-column button {
    margin-left: 10px;
    padding: 5px 10px;
    background-color: #ff6666;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }

  .log-column button:hover {
    background-color: #cc0000;
  }
</style>
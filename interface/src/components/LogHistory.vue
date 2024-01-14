<template>
  <div class="log-history">
    <h2>Log History</h2>     <button @click="backToHome" class="back-home-btn">Back</button>
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
            {{ new Date(log.time).toLocaleString() }} - {{ log.timeOfDay }} - {{ log.type }} - {{ log.action }}
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
    //Filters and returns only the log entries where the type is 'reliever' from the array of logs.
      return this.logs.filter(log => log.type === 'reliever');
    },
    preventerCombinationLogs() {
    //Filters and returns the log entries that are not of type 'reliever', which includes preventer and combination logs.
      return this.logs.filter(log => log.type !== 'reliever');
    },
  },

  methods: {
    backToHome() {
    //Emits a 'change-view' event to the parent component to switch the current view back to the default home view.
      this.$emit('change-view', 'default'); // Emit an event to request view change
    },
    deleteLog(log, index) {
      console.log("Deleting log:", log);
      if (window.confirm('Are you sure you want to delete this log?')) {
        const index = this.logs.findIndex(l => l === log);

        // Log before deletion
        console.log("Before deletion:", this.logs.length);

        if (log.type === 'reliever') {
        // Update the logs array by removing the item at the specified index
          this.logs.splice(index, 1);
        }

        // Save updated logs to localStorage
        localStorage.setItem('logHistory', JSON.stringify(this.logs));

        // Emit an event to notify the parent component
        this.$emit('log-deleted');

        // Log after deletion
        console.log("After deletion:", this.logs.length);
      }
    },
    clearLogHistory() {
    //Clears the entire log history after confirming with the user.
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
    max-height: 400px; /* Set a fixed maximum height */
    overflow-y: auto; /* Enable vertical scrolling */
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

  .back-home-btn{
  /* Styling for your clear button */
  margin-top: 20px;
  padding: 10px 20px;
  background-color: #ffcccc; /* Example color */
  color: #333;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
</style>
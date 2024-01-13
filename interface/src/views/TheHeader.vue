<template>
  <header class="header">
    <h1>Asthma Helper</h1>
    <div class="time-display">{{ currentTime }}</div>
    <div>Hello: User</div>
  </header>
</template>

<script>
export default {
  data() {
    return {
      currentTime: new Date().toLocaleString(),
    };
  },
  created() {
    setInterval(() => {
      this.currentTime = new Date().toLocaleString();
    }, 1000);
  },
};
</script>

<style scoped>
.header {
  display: flex;
  align-items: center;
  background-color: #4C7BF5;
  padding: 1rem;
  color: white;
}

.time-display {
  font-size: 0.9rem;
}
</style>

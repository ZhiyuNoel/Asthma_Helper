<template>
  <section>
    <el-container class="container">
      <!-- Header -->
      <el-header class="header">
        <img src="../assets/logo_asthma.png" alt="Logo" class="logo" />
        <span class="system-name">{{ systemName }}</span>
        <div class="header-info">
          <span class="date-time">{{ currentDate }} {{ currentTime }}</span>
          <span class="user-info">Hello, {{ userName }}</span>
        </div>
      </el-header>

      <!-- Layout for sidebar and main content -->
      <el-container class="layout-content">
        <!-- Sidebar -->
        <el-aside class="sidebar">
          <el-menu class="main-menu">
            <el-menu-item index="1-1">
              <router-link to="/dashboard">Dashboard</router-link>
            </el-menu-item>
            <el-menu-item index="1-2">
              <router-link to="/inhaler-logging">Inhaler Logging</router-link>
            </el-menu-item>
            <el-menu-item index="1-3">
              <router-link to="/map">Map</router-link>
            </el-menu-item>
            <el-menu-item index="1-4">
              <router-link to="/history">History</router-link>
            </el-menu-item>
            <el-menu-item index="1-5">
              <router-link to="/travelling">Travelling</router-link>
            </el-menu-item>
            <el-menu-item index="1-6">
              <router-link to="/settings">Settings</router-link>
            </el-menu-item>
          </el-menu>
        </el-aside>

        <!-- Main Content -->
        <el-main class="main">
          <transition name="fade" mode="out-in">
            <router-view></router-view>
          </transition>
        </el-main>
      </el-container>
    </el-container>
  </section>
</template>


<script>
export default {
  data() {
    return {
      systemName: 'Asthma Helper',
      userName: 'User',
      currentDate: new Date().toLocaleDateString(),
      currentTime: new Date().toLocaleTimeString(),
    };
  },
  methods: {
    updateDateTime() {
      this.currentDate = new Date().toLocaleDateString();
      this.currentTime = new Date().toLocaleTimeString();
    }
  },
  mounted() {
    this.interval = setInterval(this.updateDateTime, 1000);
  },
  beforeDestroy() {
    clearInterval(this.interval);
  }
}
</script>

<style scoped lang="scss">
$width: 100%;
$height: 100%;
$background-color: #5251b1;
$header-color: #fff;
$header-height: 60px;

.container {
  position: relative;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;

  .layout-content {
    display: flex; 
    height: calc(100% - #{$header-height}); 
  }

  .sidebar {
    width: 200px; // fixed width for sidebar
  }

  .header {
    background-color: $background-color;
    color: $header-color;
    width: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 20px;

    .header-left {
      display: flex;
      align-items: center;
    }

    .logo {
      height: 40px; // larger logo height
      width: auto; // to maintain aspect ratio
    }

    .system-name {
      font-size: large;
      font-weight: bold;
    }

    .header-right {
      display: flex;
      align-items: center;
      justify-content: flex-end;

      .date-time {
        margin-right: 10px;
      }
      .user-info {
        margin-left: 10px; 
      }
    }
  }

  .main {
    width: calc(100% - 200px); // main content width
    overflow-y: auto; // for scrolling
  }
}

</style>

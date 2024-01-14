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
          <el-menu :default-active="activeIndex" class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose" :router="true">
            <el-menu-item index="1" @click="toInhaler"><el-icon><House /></el-icon>InhalerLogging</el-menu-item>
            <el-menu-item index="2" @click="toMap"><el-icon><MapLocation /></el-icon>Map</el-menu-item>
            <el-menu-item index="3" @click="toHistory"><el-icon><PieChart /></el-icon>History</el-menu-item>
            <el-menu-item index="4" @click="toAdvice"><el-icon><ChatSquare /></el-icon>Advice</el-menu-item>
            <el-menu-item index="5" @click="toSetting"><el-icon><Setting /></el-icon>Settings</el-menu-item>
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
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

export default {
  data() {
    return {
      systemName: 'Asthma Helper',
      userName: 'User',
      currentDate: new Date().toLocaleDateString(),
      currentTime: new Date().toLocaleTimeString(),
      activeIndex: "1"
    };
  const app = createApp(App)
  for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
  },
  methods: {
    updateDateTime() {
      this.currentDate = new Date().toLocaleDateString();
      this.currentTime = new Date().toLocaleTimeString();
    },
    toInhaler(){
      this.$router.push({ name: "InhalerLogging"})
      this.activeIndex = "1"
    },
    toMap(){
      this.$router.push({ name:"Map"})
      this.activeIndex = "2"
    },
    toHistory(){
      this.$router.push({ name:"History"})
      this.activeIndex = "3"
    },
    toAdvice(){
      this.$router.push({ name:"Travelling"})
      this.activeIndex = "4"
    },
    toSetting(){
      this.$router.push({ name:"Settings"})
      this.activeIndex = "5"
    }

  },
  mounted() {
    this.interval = setInterval(this.updateDateTime, 1000);
  },
  beforeDestroy() {
    clearInterval(this.interval);
  },
}
</script>

<style scoped lang="scss">
$width: 100%;
$height: 100%;
$background-color: #5251b1;
$header-color: #fff;
$header-height: 60px;

.el-menu-vertical-demo .el-menu-item.is-active {
  background-color: #3399ff; 
  color: #fff ;
}


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


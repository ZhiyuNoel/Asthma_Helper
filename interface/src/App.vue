<template>
  <div id="app">
    <main-page />
  </div>
</template>

<script>
import MainPage from './page/mainPage.vue'; // Ensure this path is correct

export default {
  name: 'App',
  components: {
    MainPage // Registering MainPage component
  }
}
</script>
   
  <style lang="scss">
  body {
      margin: 0;
      padding: 0;
      font-family: Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB, Microsoft YaHei, SimSun, sans-serif;
      font-size: 14px;
      -webkit-font-smoothing: antialiased;
  }
   
  #app {
      position: absolute;
      top: 0;
      bottom: 0;
      width: 100%;
  }
   
  .el-submenu [class^=fa] {
      vertical-align: baseline;
      margin-right: 10px;
  }
   
  .el-menu-item [class^=fa] {
      vertical-align: baseline;
      margin-right: 10px;
  }
   
  .toolbar {
      background: #f2f2f2;
      padding: 10px;
      margin: 10px 0;
      .el-form-item {
          margin-bottom: 10px;
      }
  }
   
  .fade-enter-active,
  .fade-leave-active {
      transition: all 0.2s ease;
  }
   
  .fade-enter,
  .fade-leave-active {
      opacity: 0;
  }
  </style>
   
  <style lang="scss">
  body {
      margin: 0;
      padding: 0;
      font-family: Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB, Microsoft YaHei, SimSun, sans-serif;
      font-size: 14px;
      -webkit-font-smoothing: antialiased;
  }
   
  #app {
      position: absolute;
      top: 0;
      bottom: 0;
      width: 100%;
  }
   
  .el-submenu [class^=fa] {
      vertical-align: baseline;
      margin-right: 10px;
  }
   
  .el-menu-item [class^=fa] {
      vertical-align: baseline;
      margin-right: 10px;
  }
   
  .toolbar {
      background: #f2f2f2;
      padding: 10px;
      margin: 10px 0;
      .el-form-item {
          margin-bottom: 10px;
      }
  }
   
  .fade-enter-active,
  .fade-leave-active {
      transition: all 0.2s ease;
  }
   
  .fade-enter,
  .fade-leave-active {
      opacity: 0;
  }
  </style>
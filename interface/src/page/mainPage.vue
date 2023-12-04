<template>
    <section>
      <el-container class="container">
        <!--左边-->
        <el-aside :width="collapsed? '65px' : '200px' ">
          <el-container>
            <el-header>
              <span class="menu-button" v-if="collapsed" @click.prevent="collapsed=!collapsed">
                <i class="fa fa-align-justify"></i>
              </span>
              <span v-else class="system-name">{{systemName}}</span>
            </el-header>
            <el-main>
              <el-menu :collapse="collapsed">
                <el-submenu index="1">
                  <template slot="title">
                    <i class="el-icon-menu"></i>
                    <span>菜单一</span>
                  </template>
                  <el-menu-item-group>
                    <el-menu-item index="1-1">Dashboard</el-menu-item>
                    <el-menu-item index="1-2">Inhaler Logging</el-menu-item>
                    <el-menu-item index="1-3">Map</el-menu-item>
                    <el-menu-item index="1-4">History</el-menu-item>
                    <el-menu-item index="1-5">Traveling</el-menu-item>
                  </el-menu-item-group>
                </el-submenu>
              </el-menu>
            </el-main>
          </el-container>
        </el-aside>
        <!--内容-->
        <el-container>
          <!--页眉-->
          <el-header class="header">
            <el-row>
              <el-col :span="18" class="header-title">
                <img src="../assets/logo_asthma.png" alt="Your Image Description" style="height: 20px; width: 20px;"/> <!-- 添加图片 -->
                <span v-if="collapsed" class="system-name">{{systemName}}</span>
                <span v-else class="menu-button" @click.prevent="collapsed=!collapsed">
                  <i class="fa fa-align-justify"></i>
                </span>
              </el-col>
              <el-col :span="6"><span class="el-dropdown-link userinfo-inner">Hello: {{userName}}</span></el-col>
            </el-row>
          </el-header>
          <!--中间-->
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
    let data = () => {
      return {
        collapsed: false,
        systemName: 'Asthma Helper',
        userName: 'User'
      }
    }
    
    export default {
      data: data,
      methods: {
    
      },
      mounted: function() {
    
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
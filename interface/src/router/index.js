import { createRouter, createWebHistory } from 'vue-router'
import mainPage from '../page/mainPage.vue'
import InhalerLogging from '@/views/InhalerLogging.vue';
import Map from '@/views/Map.vue';
import History from '@/views/History.vue';
import Travelling from '@/views/Travelling.vue';
import Settings from '@/views/Settings.vue';
import LogHistory from '@/components/LogHistory.vue';

const routes = [
  {
    path: '/',
    name: 'home',
    component: mainPage
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  },
  {
    path: '/inhaler-logging',
    name: 'InhalerLogging',
    component: InhalerLogging,
  },
  {
    path: '/map',
    name: 'Map',
    component: Map,
  },
  {
    path: '/history',
    name: 'History',
    component: History,
  },
  {
    path: '/travelling',
    name: 'Travelling',
    component: Travelling,
  },
  {
    path: '/settings',
    name: 'Settings',
    component: Settings
  },
  {
    path: '/log-history',
    name: 'LogHistory',
    component: LogHistory
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
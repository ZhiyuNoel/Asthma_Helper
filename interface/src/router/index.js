import { createRouter, createWebHistory } from 'vue-router'
import mainPage from '../page/mainPage.vue'

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
    path: '/hisotry',
    name: 'history',
    component: () => import(/* webpackChunkName: "history" */ '../page/historyPage.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
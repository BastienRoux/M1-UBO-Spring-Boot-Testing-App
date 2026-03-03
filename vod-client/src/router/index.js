import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'

import Home from '../views/Home.vue'
import FilmList from '../views/FilmList.vue'
import FilmDetail from '../views/FilmDetail.vue'
import ArtistList from '../views/ArtistList.vue'
import ArtistDetail from '../views/ArtistDetail.vue'
import MyReservations from '../views/MyReservations.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import AdminPanel from '../views/AdminPanel.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: FilmList
  },
  {
    path: '/films',
    name: 'FilmList',
    component: FilmList
  },
  {
    path: '/films/:id',
    name: 'FilmDetail',
    component: FilmDetail
  },
  {
    path: '/artists',
    name: 'ArtistList',
    component: ArtistList
  },
  {
    path: '/artists/:id',
    name: 'ArtistDetail',
    component: ArtistDetail
  },
  {
    path: '/my-reservations',
    name: 'MyReservations',
    component: MyReservations,
    meta: { requiresAuth: true }
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/admin',
    name: 'AdminPanel',
    component: AdminPanel,
    meta: { requiresAuth: true, requiresAdmin: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// Navigation guards
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next('/login')
  } else if (to.meta.requiresAdmin && !authStore.isAdmin) {
    next('/')
  } else {
    next()
  }
})

export default router

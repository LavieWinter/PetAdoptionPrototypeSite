import { createRouter, createWebHashHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import store from '../store'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/login',
    name: 'login',
    component: () => import(/* webpackChunkName: "login" */ '../views/LoginView.vue')
  },
  {
    path: '/register',
    name: 'register',
    component: () => import(/* webpackChunkName: "register" */ '../views/RegisterView.vue')
  },
  {
    path: '/register-person',
    name: 'register-person',
    component: () =>
      import(/* webpackChunkName: "register-person" */ '../views/RegisterTutorView.vue')
  },
  {
    path: '/register-org',
    name: 'register-org',
    component: () =>
      import(/* webpackChunkName: "register-person" */ '../views/RegisterTutorView.vue')
  },
  {
    path: '/ongs',
    name: 'ongs',
    component: () => import(/* webpackChunkName: "ongs" */ '../views/HomeView.vue')
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const authed = store.getters['user/isAuthed']
  if (to.meta?.requiresAuth && !authed) {
    next({ name: 'login', query: { redirect: to.fullPath } })
  } else {
    next()
  }
})

export default router

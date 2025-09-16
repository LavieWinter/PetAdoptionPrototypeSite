// src/main.js
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify'
import { loadFonts } from './plugins/webfontloader'

loadFonts()
;(async () => {
  const app = createApp(App)

  app.use(store)

  // tenta hidratar o perfil se já houver token salvo
  try {
    await store.dispatch('user/init')
  } catch (_) {
    // ignora erro (token inválido/expirado)
  }

  app.use(router)
  app.use(vuetify)
  app.mount('#app')
})()

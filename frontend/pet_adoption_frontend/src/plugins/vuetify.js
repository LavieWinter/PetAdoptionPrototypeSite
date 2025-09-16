// Styles
import '@mdi/font/css/materialdesignicons.css'
import 'vuetify/styles'

// Vuetify
import { createApp } from 'vue'
import { createVuetify } from 'vuetify'
import { VMaskInput } from 'vuetify/labs/VMaskInput'
import { VFileUpload } from 'vuetify/labs/VFileUpload'

const petAdoptionTheme = {
  dark: false,
  colors: {
    primary: '#8A9A5B',
    secondary: '#4A5429',
    terciary: '#ACC196',
    inputBackground: '#C3D4B0',
    textColor: '#40412A',
    background: '#D4E4C4',
    link: '#00A86B'
  }
}

export default createVuetify({
  theme: {
    defaultTheme: 'petAdoptionTheme',
    themes: {
      petAdoptionTheme
    }
  },
  icons: {
    defaultSet: 'mdi'
  },
  components: {
    VMaskInput,
    VFileUpload
  }
})
// https://vuetifyjs.com/en/introduction/why-vuetify/#feature-guides

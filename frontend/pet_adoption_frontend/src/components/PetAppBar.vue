<template>
        <v-app-bar class="px-md-4" color="secondary" flat>
      <template #prepend>
        <v-app-bar-nav-icon v-if="$vuetify.display.smAndDown" @click="openDrawer" />
      </template>

      <v-img class="me-sm-8" max-width="40" :src="logo" />
      PetAdoption
      <v-spacer></v-spacer>

      <template v-if="$vuetify.display.mdAndUp">
        <v-btn v-for="(item, i) in items" :key="i" :active="i === 0" class="me-2 text-none" slim v-bind="item" />
      </template>

      <v-spacer />

      <template #append>

        <v-btn class="ms-1" icon>
          <v-avatar image="https://cdn.vuetifyjs.com/images/john.png" />

          <v-menu activator="parent" origin="top">
            <v-list>
              <v-list-item @click="openDialog" link title="Atualizar Perfil" />
              <v-list-item link title="Sair" @click="logout" />
            </v-list>
          </v-menu>
        </v-btn>
      </template>
    </v-app-bar>
</template>
<script setup>
import logo from '@/assets/logoPet.png'
import { useStore } from 'vuex'
import { ref } from 'vue'
import { useRouter } from 'vue-router'
const router = useRouter()
const store = useStore()
const props = defineProps({
  dialogOpen: {
    type: Boolean,
    required: true
  }
})

const emit = defineEmits(['update:dialogOpen', 'update:drawer'])
const openDrawer = () => {
  emit('update:drawer', true)
}

const openDialog = () => {
  emit('update:dialogOpen', true)
}

async function logout() {
  await store.dispatch('user/logout')
  router.push('/login')
}


const items = [
  {
    text: 'Home'
  },
  {
    text: 'Adotar animal'
  },
  {
    text: 'ONGs'
  },
  {
    text: 'Meus Pets'
  },
  {
    text: 'Sobre nós'
  }
]


</script>
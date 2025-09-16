<template>
  <v-layout class="min-height-100vh" height="100%">
    <v-app-bar class="px-md-4" color="secondary" flat>
      <template #prepend>
        <v-app-bar-nav-icon v-if="$vuetify.display.smAndDown" @click="drawer = !drawer" />
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
              <v-list-item @click="dialogOpen = true" link title="Atualizar Perfil" />
              <v-list-item link title="Sair" @click="logout" />
            </v-list>
          </v-menu>
        </v-btn>
      </template>
    </v-app-bar>
    <NavigationDrawer v-model="drawer" />
    <v-main>
      <v-container fluid>
        <v-infinite-scroll class="w-100" @load="loadMore">
          
          
          <v-row dense>
            <v-col v-for="card in cards" :key="card.name" :cols="card.flex">
              <Pet :card="card" />
            </v-col>
            <!-- Skeletons enquanto carrega -->
            <v-col v-if="loading" cols="12">
              <v-row dense>
                <v-col cols="12" sm="6" md="4" v-for="i in 3" :key="'sk-' + i">
                  <v-skeleton-loader type="image, article" class="rounded-lg" />
                </v-col>
              </v-row>
            </v-col>
            <v-col cols="12" class="text-center">
              <!-- <v-btn v-if="!loading && hasMore" variant="text" @click="done('ok')" class="opacity-70">
                Carregar mais
              </v-btn> -->

              <div v-if="!hasMore" class="text-medium-emphasis py-6">
                Não há mais pets para mostrar.
              </div>
            </v-col>
          </v-row>
        </v-infinite-scroll>
        <v-alert v-if="error" type="error" variant="tonal" class="mb-4" density="comfortable">
          {{ error }}
        </v-alert>
      </v-container>
      <ProfileDialog v-model="dialogOpen" :open="dialogOpen" @save="handleSaveProfile" @close="dialogOpen = false" />
    </v-main>
  </v-layout>
</template>
<script setup>
import { shallowRef, ref, onMounted } from 'vue'
import logo from '@/assets/logoPet.png'
import Pet from '@/components/Pet.vue'
import ProfileDialog from '@/components/ProfileDialog.vue'
import { listPets } from '@/services/pets' // função fictícia para listar pets
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import NavigationDrawer from '@/components/NavigationDrawer.vue'

const drawer = shallowRef(true)
const dialogOpen = ref(false)
const save = ref(false)
const page = ref(0)        // página atual (0-based)
const size = ref(9)        // quantos por requisição
const hasMore = ref(true)  // a API ainda tem itens?
const loading = ref(false)
const error = ref('')
const router = useRouter()
const store = useStore()




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
    text: 'Requisições'
  }
]


async function handleSaveProfile(payload) {
  saving.value = true
  try {
    // await api.updateProfile(payload)
    open.value = false
  } finally {
    saving.value = false
  }
}

const cards = ref([
  // {
  //   name: 'Marshmellow',
  //   size: 'Grande',
  //   caracteristicas: [
  //     { title: 'Brincalhão', color: 'red' },
  //     { title: 'Sociável', color: 'brown' },
  //     { title: 'Protetor', color: 'light-blue' }
  //   ],
  //   src: 'https://images.unsplash.com/photo-1507146426996-ef05306b995a?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80',
  //   flex: 4,
  //   sex: 'F',
  //   ong: 'Projeto Vida Animal',
  //   place: 'Sumare, SP',
  //   weight: '25kg',
  //   age: '2 anos',
  //   breed: 'Golden Retriever',
  //   description:
  //     'Marshmellow é uma cachorrinha muito amigável e brincalhona. Ela adora correr e brincar com outros cães. Está procurando um lar amoroso onde possa receber muita atenção e carinho.',
  //   needs: 'Exercício diário, alimentação balanceada, visitas regulares ao veterinário.'
  // },
  // {
  //   name: 'Olaf',
  //   size: 'Pequeno',
  //   caracteristicas: [
  //     { title: 'Independente', color: 'purple' },
  //     { title: 'Calmo', color: 'blue' },
  //     { title: 'Seletivo', color: 'green' }
  //   ],
  //   src: 'https://images.unsplash.com/photo-1507146426996-ef05306b995a?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80',
  //   flex: 4,
  //   sex: 'M',
  //   ong: 'SOS Animal',
  //   place: 'Hortolandia, SP',
  //   weight: '8kg',
  //   age: '4 anos',
  //   breed: 'Shih Tzu',
  //   description:
  //     'Olaf é um cãozinho tranquilo e independente. Ele gosta de passar o tempo sozinho, mas também aprecia momentos de carinho. Está em busca de um lar onde possa ter seu espaço e receber atenção quando desejar.',
  //   needs: 'Ambiente calmo, alimentação adequada, cuidados básicos de higiene.'
  // },
  // {
  //   name: 'Paçoca',
  //   size: 'Grande',
  //   caracteristicas: [
  //     { title: 'Carinhoso', color: 'red' },
  //     { title: 'Energético', color: 'yellow' },
  //     { title: 'Protetor', color: 'light-blue' }
  //   ],
  //   src: 'https://images.unsplash.com/photo-1507146426996-ef05306b995a?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80',
  //   flex: 4,
  //   sex: 'F',
  //   ong: 'Projeto Vida Animal',
  //   place: 'Sumare, SP',
  //   weight: '30kg',
  //   age: '3 anos',
  //   breed: 'Pastor Alemão',
  //   description:
  //     'Paçoca é uma cadelinha cheia de energia e amor para dar. Ela adora brincar e correr, mas também é muito protetora com sua família. Está procurando um lar ativo onde possa receber muita atenção e exercícios.',
  //   needs: 'Exercício diário, socialização, alimentação balanceada.'
  // },
  // {
  //   name: 'Simba',
  //   size: 'Médio',
  //   caracteristicas: [
  //     { title: 'Independente', color: 'purple' },
  //     { title: 'Sociável', color: 'brown' },
  //     { title: 'Carinhoso', color: 'red' }
  //   ],
  //   src: 'https://images.unsplash.com/photo-1507146426996-ef05306b995a?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80',
  //   flex: 4,
  //   sex: 'M',
  //   ong: 'SOS Animal',
  //   place: 'Hortolandia, SP',
  //   weight: '15kg',
  //   age: '5 anos',
  //   breed: 'Beagle',
  //   description:
  //     'Simba é um cãozinho amigável e sociável. Ele gosta de interagir com outros cães e pessoas, mas também aprecia momentos de independência. Está em busca de um lar onde possa ter equilíbrio entre socialização e tempo sozinho.',
  //   needs: 'Socialização, alimentação adequada, cuidados básicos de saúde.'
  // },
  // {
  //   name: 'Luna',
  //   size: 'Pequeno',
  //   caracteristicas: [
  //     { title: 'Carinhosa', color: 'red' },
  //     { title: 'Independente', color: 'purple' },
  //     { title: 'Curiosa', color: 'orange' }
  //   ],
  //   src: 'https://images.unsplash.com/photo-1518791841217-8f162f1e1131?auto=format&fit=crop&w=1170&q=80',
  //   flex: 4,
  //   sex: 'F',
  //   ong: 'Gatinhos do Bem',
  //   place: 'Campinas, SP',
  //   weight: '4kg',
  //   age: '1 ano',
  //   breed: 'SRD (Gato)',
  //   description:
  //     'Luna é uma gatinha meiga e curiosa. Adora explorar a casa e tirar sonecas no sol. Procura uma família com carinho e paciência.',
  //   needs: 'Ambiente seguro, arranhador, alimentação balanceada.'
  // },
  // {
  //   name: 'Thor',
  //   size: 'Grande',
  //   caracteristicas: [
  //     { title: 'Energético', color: 'yellow' },
  //     { title: 'Brincalhão', color: 'red' },
  //     { title: 'Protetor', color: 'light-blue' }
  //   ],
  //   src: 'https://images.unsplash.com/photo-1517849845537-4d257902454a?auto=format&fit=crop&w=1170&q=80',
  //   flex: 4,
  //   sex: 'M',
  //   ong: 'Cão Feliz',
  //   place: 'Paulínia, SP',
  //   weight: '28kg',
  //   age: '3 anos',
  //   breed: 'Labrador Retriever',
  //   description:
  //     'Thor é um cão muito ativo e leal. Ama acompanhar a família em passeios e brincadeiras ao ar livre.',
  //   needs: 'Exercício diário, enriquecimento ambiental, socialização.'
  // },
  // {
  //   name: 'Mel',
  //   size: 'Médio',
  //   caracteristicas: [
  //     { title: 'Sociável', color: 'brown' },
  //     { title: 'Calma', color: 'blue' },
  //     { title: 'Carinhosa', color: 'red' }
  //   ],
  //   src: 'https://images.unsplash.com/photo-1511044568932-338cba0ad803?auto=format&fit=crop&w=1170&q=80',
  //   flex: 4,
  //   sex: 'F',
  //   ong: 'Miados & Ronrons',
  //   place: 'Sumaré, SP',
  //   weight: '5kg',
  //   age: '2 anos',
  //   breed: 'Siamês (Gato)',
  //   description:
  //     'Mel é doce e companheira. Se dá bem com pessoas e outros gatos, perfeita para um lar tranquilo.',
  //   needs: 'Caixa de areia limpa, brinquedos interativos, escovação ocasional.'
  // },
  // {
  //   name: 'Bento',
  //   size: 'Médio',
  //   caracteristicas: [
  //     { title: 'Sociável', color: 'brown' },
  //     { title: 'Obediente', color: 'green' },
  //     { title: 'Brincalhão', color: 'red' }
  //   ],
  //   src: 'https://images.unsplash.com/photo-1558944351-c7a7a74a5b1f?auto=format&fit=crop&w=1170&q=80',
  //   flex: 4,
  //   sex: 'M',
  //   ong: 'Projeto Vida Animal',
  //   place: 'Hortolândia, SP',
  //   weight: '12kg',
  //   age: '2 anos',
  //   breed: 'Vira-lata (SRD)',
  //   description:
  //     'Bento é um amigão equilibrado: adora brincar, mas sabe relaxar com a família. Ótimo para primeiros tutores.',
  //   needs: 'Passeios regulares, rotina de treinos leves, alimentação adequada.'
  // },
  // {
  //   name: 'Nala',
  //   size: 'Pequeno',
  //   caracteristicas: [
  //     { title: 'Carinhosa', color: 'red' },
  //     { title: 'Sossegada', color: 'blue' },
  //     { title: 'Dócil', color: 'green' }
  //   ],
  //   src: 'https://images.unsplash.com/photo-1510070009289-b5bc34383727?auto=format&fit=crop&w=1170&q=80',
  //   flex: 4,
  //   sex: 'F',
  //   ong: 'Gatinhos do Bem',
  //   place: 'Campinas, SP',
  //   weight: '3.5kg',
  //   age: '2 anos',
  //   breed: 'Persa (Gato)',
  //   description:
  //     'Nala adora colo e momentos tranquilos. Ideal para um lar sereno e cheio de afeto.',
  //   needs: 'Escovação frequente, ambiente calmo, consultas veterinárias regulares.'
  // },
  // {
  //   name: 'Chico',
  //   size: 'Pequeno',
  //   caracteristicas: [
  //     { title: 'Companheiro', color: 'brown' },
  //     { title: 'Calmo', color: 'blue' },
  //     { title: 'Simpático', color: 'orange' }
  //   ],
  //   src: 'https://images.unsplash.com/photo-1552053831-71594a27632d?auto=format&fit=crop&w=1170&q=80',
  //   flex: 4,
  //   sex: 'M',
  //   ong: 'SOS Animal',
  //   place: 'Americana, SP',
  //   weight: '9kg',
  //   age: '6 anos',
  //   breed: 'Pug',
  //   description:
  //     'Chico é um senhor simpático e tranquilo. Gosta de uma boa soneca e de acompanhar a família pela casa.',
  //   needs: 'Controle de peso, passeios curtos e frequentes, atenção com respiração.'
  // }
])


async function loadMore({ done }) {
  if (!hasMore.value || loading.value) {
    done('ok')
    return
  }
  loading.value = true
  error.value = ''
  try {
    const { items, last, page: currentPage } = await listPets({ page: page.value, size: size.value })
    if (Array.isArray(items) && items.length) {
      cards.value.push(...items)
      page.value = (currentPage ?? page.value) + 1   // prepara a próxima página
      hasMore.value = !last
      done(last ? 'empty' : 'ok')
    } else {
      hasMore.value = false
      error.value = 'Nenhum pet encontrado.'
      done('empty')
    }
  } catch (e) {
    error.value = 'Falha ao carregar pets.'
    done('error')
  } finally {
    loading.value = false
  }
}

async function logout() {
  await store.dispatch('user/logout')
  router.push('/login')
}

onMounted(() => {
  loadMore()
})
</script>
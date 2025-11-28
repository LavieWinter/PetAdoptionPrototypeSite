<template>
  <v-layout class="min-height-100vh" height="100%">
    <AppBar :dialogOpen="dialogOpen" @update:dialogOpen="dialogOpen = $event" :drawer="drawer"
      @update:drawer="drawer = $event" />
    <NavigationDrawer v-model="drawer" />
    <v-main>
      <v-container fluid>
        <v-infinite-scroll class="w-100" @load="loadMore">
          <v-row dense>
            <v-col v-for="card in cards" :key="card.name" cols="4">
              <!-- {{  router.currentRoute.value.name }} -->
              <Pet v-if="router.currentRoute.value.name == 'home' || router.currentRoute.value.name == 'pets'" :card="card" :loading="isAdopting" @adopt="onAdopt"/>
              <Ong v-if="router.currentRoute.value.name == 'ongs'" :card="ongCard" />
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
              <template v-slot:empty>
                <div class="text-medium-emphasis py-6 text-center">
                  Não há mais pets para mostrar.
                </div>
              </template>
            </v-col>
          </v-row>
        </v-infinite-scroll>
        <v-alert v-if="error" type="error" variant="tonal" class="mb-4" density="comfortable">
          {{ error }}
        </v-alert>
        <v-snackbar v-model="successAdopt" color="success" timeout="3000">
     {{ successMessage }}
  </v-snackbar>
      </v-container>
      <ProfileDialog v-model="dialogOpen" :open="dialogOpen" @save="handleSaveProfile" @close="dialogOpen = false" />
      
    </v-main>

  </v-layout>
</template>
<script setup>
import { shallowRef, ref, onMounted, watch } from 'vue'
import Pet from '@/components/Pet.vue'
import Ong from '@/components/Ong.vue'
import ProfileDialog from '@/components/ProfileDialog.vue'
import AppBar from '@/components/PetAppBar.vue'
import { listPets, listAvailablePets } from '@/services/pets' // função fictícia para listar pets
import { useRouter, useRoute } from 'vue-router'
import { useStore } from 'vuex'
import NavigationDrawer from '@/components/NavigationDrawer.vue'
import { listOrgs } from '@/services/orgs' // <--- Importe a função criada acima
import { createAdoptionApplication } from '@/services/adoption' // Importe o serviço novo

const drawer = shallowRef(true)
const dialogOpen = ref(false)
const page = ref(0)        // página atual (0-based)
const size = ref(9)        // quantos por requisição
const hasMore = ref(true)  // a API ainda tem itens?
const loading = ref(false)
const error = ref('')
const router = useRouter()
const store = useStore()
const cards = ref([])
const route = useRoute()
const isAdopting = ref(false) // Estado de loading para o botão
const successMessage = ref('')
const successAdopt = ref(false)

async function handleSaveProfile(payload) {
  saving.value = true
  try {
    // await api.updateProfile(payload)
    open.value = false
  } finally {
    saving.value = false
  }
}


const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';
const ongCard = ref([
  {
    name: 'Marshmellow',
    size: 'Grande',
    caracteristicas: [
      { title: 'Brincalhão', color: 'red' },
      { title: 'Sociável', color: 'brown' },
      { title: 'Protetor', color: 'light-blue' }
    ],
    src: 'https://images.unsplash.com/photo-1507146426996-ef05306b995a?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80',
    flex: 4,
    sex: 'F',
    ong: 'Projeto Vida Animal',
    place: 'Sumare, SP',
    weight: '25kg',
    age: '2 anos',
    breed: 'Golden Retriever',
    description:
      'Marshmellow é uma cachorrinha muito amigável e brincalhona. Ela adora correr e brincar com outros cães. Está procurando um lar amoroso onde possa receber muita atenção e carinho.',
    needs: 'Exercício diário, alimentação balanceada, visitas regulares ao veterinário.'
  },
])

const SIZE_MAP = {
  'SMALL': 'Pequeno',
  'MEDIUM': 'Médio',
  'LARGE': 'Grande'
}

const SPECIES_MAP = {
  'CAT': 'Gato',
  'DOG': 'Cão'
}

// --- WATCHER DE ROTA ---
// Isso é crucial: quando mudar de 'pets' para 'ongs', limpa tudo.
watch(() => route.name, (newRoute) => {
  cards.value = []
  page.value = 0
  hasMore.value = true
  error.value = ''
  loading.value = false
  
  // O v-infinite-scroll tentará carregar automaticamente ao resetar, 
  // mas garantimos a chamada se a lista estiver vazia
  loadMore({ done: () => {} })
})

// === FUNÇÃO PRINCIPAL DO SCROLL ===
async function loadMore({ done = () => {} } = {}) {
  if (loading.value) {
    if(done) done('ok')
    return
  }
  
  if (!hasMore.value) {
    if(done) done('empty')
    return
  }

  loading.value = true
  error.value = ''

  try {
    const currentRouteName = route.name

    if (currentRouteName === 'ongs') {
      await fetchOrgs(done)
    } else {
      // Padrão para 'home' ou 'pets'
      await fetchPets(done)
    }
  } catch (e) {
    error.value = 'Falha ao carregar dados.'
    console.error(e)
    if(done) done('error')
  } finally {
    loading.value = false
  }
}

// === LÓGICA DE ONGS ===
async function fetchOrgs(done) {
  // Chama o serviço
  const { items, last } = await listOrgs({ page: page.value, size: size.value })

  if (Array.isArray(items) && items.length) {
    const mappedOrgs = items.map(org => {
      return {
        id: org.id,
        // OrgResponseDTO: { id, name, email, phone, city, neighborhood, uf... }
        name: org.name,
        // Como o back não tem imagem de ONG, usamos um placeholder
        src: 'https://cdn-icons-png.flaticon.com/512/10009/10009983.png', 
        
        // Formata localização
        place: org.city && org.uf ? `${org.city}, ${org.uf}` : 'Localização não informada',
        neighborhood: org.neighborhood,
        
        // Contatos
        email: org.email,
        phone: org.phone,
        
        // Propriedade para controle de layout (grid)
        flex: 4 
      }
    })

    cards.value.push(...mappedOrgs)
    page.value += 1
    hasMore.value = !last // Se 'last' for true (veio menos itens que o size), paramos.
    done(last ? 'empty' : 'ok')
  } else {
    hasMore.value = false
    done('empty')
  }
}

// === LÓGICA DE PETS (A sua existente) ===
async function fetchPets(done) {
  const { items, last, page: currentPage } = await listAvailablePets({ page: page.value, size: size.value })
    
  if (Array.isArray(items) && items.length) {
    const mappedPets = items.map(pet => {
      const traits = []
      if (pet.goodWithOtherAnimals) traits.push({ title: 'Sociável', color: 'green' })
      if (pet.requiresConstantCare) traits.push({ title: 'Cuidados Constantes', color: 'orange' })
      if (pet.hasOngoingTreatment) traits.push({ title: 'Em Tratamento', color: 'blue' })
      if (pet.hasSpecialNeeds) traits.push({ title: pet.hasSpecialNeeds, color: 'red' })
      if (pet.hasChronicDisease) traits.push({ title: `Condição: ${pet.hasChronicDisease}`, color: 'purple' })

      const needsText = [
          pet.hasSpecialNeeds ? `Necessidades: ${pet.hasSpecialNeeds}` : null,
          pet.hasOngoingTreatment ? 'Possui tratamento em andamento' : null,
          pet.requiresConstantCare ? 'Requer supervisão constante' : null
      ].filter(Boolean).join(', ') || 'Nenhuma necessidade especial informada.'

      return {
        ...pet,
        src: pet.petImage 
          ? `${API_BASE_URL}${pet.petImage}` 
          : 'https://images.unsplash.com/photo-1543466835-00a7907e9de1?auto=format&fit=crop&w=500&q=60',
        sizeFormatted: SIZE_MAP[pet.size] || pet.size,
        speciesFormatted: SPECIES_MAP[pet.species] || pet.species,
        caracteristicas: traits,
        place: 'Campinas, SP', 
        ong: 'ONG Parceira',  
        age: 'Não inf.',      
        weight: 'Não inf.',    
        description: pet.petDescription || 'Este pet ainda não tem uma descrição detalhada.',
        needs: needsText,
        flex: 4
      }
    })

    cards.value.push(...mappedPets)
    page.value = (currentPage ?? page.value) + 1
    hasMore.value = !last
    done(last ? 'empty' : 'ok')
  } else {
    hasMore.value = false
    done('empty')
  }
}

async function logout() {
  await store.dispatch('user/logout')
  router.push('/login')
}

onMounted(() => {
  loadMore()
})

async function onAdopt(pet) {
  // 1. Verificação básica de login (se necessário)
  // const store = useStore();
  // if (!store.getters['user/isAuthenticated']) {
  //    router.push('/login'); 
  //    return;
  // }

  isAdopting.value = true
  error.value = ''
  successMessage.value = ''

  try {
    // 2. Chama o Backend
    await createAdoptionApplication(pet.id)
    
    // 3. Sucesso
    console.log('Pedido de adoção criado com sucesso!')
    successMessage.value = `Pedido enviado para ${pet.name}! Acompanhe em "Meus Pets".`
    successAdopt.value = true
    
    // Fecha o modal após um breve delay ou imediatamente
    isSelected.value = false 
    
    // Opcional: Redirecionar para uma página de sucesso ou lista de aplicações
    // router.push('/minhas-adocoes')

  } catch (e) {
    console.error(e)
    // Tratamento de erro básico
    if (e.response && e.response.status === 401) {
       error.value = 'Você precisa estar logado para adotar.'
       router.push('/login')
    } else {
       error.value = 'Erro ao solicitar adoção. Tente novamente.'
    }
  } finally {
    isAdopting.value = false
  }
}

</script>
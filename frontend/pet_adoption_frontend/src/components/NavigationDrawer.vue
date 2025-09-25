<template>
  <!-- Filtros -->
  <v-navigation-drawer v-model="drawer" location="left" :width="360" :permanent="$vuetify.display.mdAndUp"
    color="primary" elevation="0">
    <div class="px-4 py-4">
      <div class="text-center text-white font-weight-bold text-h6 mb-4">
        Filtros
      </div>

      <!-- UF / Cidade / Buscar -->
      <v-row class="mb-3" dense>
        <v-col cols="5">
          <v-select v-model="filters.uf" :items="ufs" label="UF" variant="solo" bg-color="inputBackground"
            density="comfortable" rounded="lg" hide-details />
        </v-col>
        <v-col cols="7">
          <v-select v-model="filters.city" :items="cities" :loading="citiesLoading" label="Cidade" variant="solo"
            bg-color="inputBackground" density="comfortable" rounded="lg" hide-details
            :disabled="!filters.uf || citiesLoading" />
        </v-col>
      </v-row>

      <!-- Espécie -->
      <v-select v-model="filters.species" :items="species" label="Espécie" variant="solo" bg-color="inputBackground"
        density="comfortable" rounded="lg" hide-details class="mb-3" />

      <!-- Idade aproximada -->
      <v-select v-model="filters.age" :items="ages" label="Idade aproximada" variant="solo" bg-color="inputBackground"
        density="comfortable" rounded="lg" hide-details class="mb-3" />

      <!-- Porte -->
      <v-select v-model="filters.size" :items="sizes" label="Porte do animal" variant="solo" bg-color="inputBackground"
        density="comfortable" rounded="lg" hide-details class="mb-3" />

      <!-- Raça -->
      <v-select v-model="filters.breed" :items="breeds" label="Raça do animal" variant="solo" bg-color="inputBackground"
        density="comfortable" rounded="lg" hide-details class="mb-3" />

      <!-- Gênero -->
      <v-select v-model="filters.gender" :items="genders" label="Gênero do animal" variant="solo"
        bg-color="inputBackground" density="comfortable" rounded="lg" hide-details class="mb-4" />

      <!-- ONG -->
      <v-select v-model="filters.org" :items="ongs" label="ONG" variant="solo" bg-color="inputBackground"
        density="comfortable" rounded="lg" hide-details class="mb-6" />

      <!-- Características -->
      <v-combobox v-model="filters.traits" v-model:search="traitsSearch" :items="traitsFiltered" label="Características"
        multiple chips closable-chips clearable hide-selected hide-details variant="solo" bg-color="inputBackground"
        density="comfortable" rounded="lg" class="mb-4">
        <!-- chip de seleção -->
        <template #selection="{ item, index, props }">
          <v-chip v-bind="props" class="ma-1" size="small" color="secondary" variant="tonal" closable
            @click:close="removeTrait(index)">
            {{ item.title ?? item.title }}
          </v-chip>
        </template>

        <!-- item da lista -->
        <template #item="{ item }">
          <v-list-item>
            <v-list-item-title v-html="highlight(item.title ?? item, traitsSearch)" />
          </v-list-item>
        </template>
      </v-combobox>

      <!-- Necessidades -->
      <v-combobox v-model="filters.needs" v-model:search="needsSearch" :items="needsFiltered" label="Necessidades"
        multiple chips closable-chips clearable hide-selected hide-details variant="solo" bg-color="inputBackground"
        density="comfortable" rounded="lg" class="mb-6">
        <template #selection="{ item, index, props }">
          <v-chip v-bind="props" class="ma-1" size="small" color="secondary" variant="tonal" closable
            @click:close="removeNeed(index)">
            {{ item.title ?? item.title }}
          </v-chip>
        </template>

        <template #item="{ item }">
          <v-list-item>
            <v-list-item-title v-html="highlight(item.title ?? item.title, needsSearch)" />
          </v-list-item>
        </template>
      </v-combobox>


      <v-row dense>
        <v-col cols="6">
          <v-btn block rounded="lg" variant="text" class="text-white text-none" @click="clearFilters">
            Limpar
          </v-btn>
        </v-col>
        <v-col cols="6">
          <v-btn block rounded="lg" color="secondary" class="text-none" @click="applyFilters">
            Aplicar
          </v-btn>
        </v-col>
      </v-row>
    </div>
  </v-navigation-drawer>

</template>

<script setup>
import { ref, watch, computed } from 'vue'

const drawer = ref(true)

const filters = ref({
  uf: 'PE',
  city: '',
  species: 'Cachorro e Gato',
  age: null,
  size: null,
  breed: null,
  gender: null,
  org: null,
  traits: [],     // <- múltiplas
  needs: []       // <- múltiplas
})

const ufs = ['AC', 'AL', 'AM', 'AP', 'BA', 'CE', 'DF', 'ES', 'GO', 'MA', 'MG', 'MS', 'MT', 'PA', 'PB', 'PE', 'PI', 'PR', 'RJ', 'RN', 'RO', 'RR', 'RS', 'SC', 'SE', 'SP', 'TO']

// cidades por UF (exemplo). Integre com IBGE se quiser.
const CITIES_BY_UF = {
  PE: ['Recife', 'Olinda', 'Jaboatão dos Guararapes', 'Paulista', 'Caruaru', 'Petrolina'],
  SP: ['São Paulo', 'Campinas', 'Santo André', 'Sorocaba', 'Ribeirão Preto'],
  RJ: ['Rio de Janeiro', 'Niterói', 'Duque de Caxias', 'Nova Iguaçu']
}

const cities = ref([])
const citiesLoading = ref(false)

async function loadCities(uf) {
  citiesLoading.value = true
  try {
    const res = await fetch(`https://servicodados.ibge.gov.br/api/v1/localidades/estados/${uf}/municipios`)
    const json = await res.json()
    cities.value = json.map(c => c.nome)
  } finally {
    citiesLoading.value = false
    // se cidade atual não pertence à nova lista, limpa
    if (!cities.value.includes(filters.value.city)) {
      filters.value.city = ''
    }
  }
}

watch(() => filters.value.uf, (uf) => {
  if (uf) loadCities(uf)
}, { immediate: true })

const species = ['Cachorro', 'Gato', 'Cachorro e Gato']
const ages = ['Filhote', 'Jovem', 'Adulto', 'Sênior']
const sizes = ['Pequeno', 'Médio', 'Grande']
const breeds = ['Sem raça definida', 'SRD', 'Labrador', 'Golden', 'Siamês', 'Persa']
const genders = ['Macho', 'Fêmea']
const ongs = ['Casa dos peludos', 'Projeto Vida Animal', 'Gatinhos do Bem', 'Cão Feliz']

const traits = [
  'Brincalhão', 'Sociável', 'Calmo', 'Energético', 'Protetor', 'Carinhoso', 'Independente'
]
const needsOptions = [
  'Exercício diário', 'Visitas ao vet', 'Alimentação especial', 'Medicação contínua'
]

const traitsSearch = ref('')
const needsSearch  = ref('')

function applyFilters() {
  // emita/consuma conforme sua arquitetura
  console.log('Aplicar filtros', { ...filters.value })
}

const traitsFiltered = computed(() => {
  const q = traitsSearch.value.trim().toLowerCase()
  const base = q ? traits.filter(t => t.toLowerCase().includes(q)) : traits
  return base.filter(t => !filters.value.traits.includes(t))
})

const needsFiltered = computed(() => {
  const q = needsSearch.value.trim().toLowerCase()
  const base = q ? needsOptions.filter(n => n.toLowerCase().includes(q)) : needsOptions
  return base.filter(n => !filters.value.needs.includes(n))
})

function removeTrait(index) {
  filters.value.traits.splice(index, 1)
}
function removeNeed(index) {
  filters.value.needs.splice(index, 1)
}

// opcional: highlight do texto buscado
function highlight(text, query) {
  if (!query) return text
  const q = query.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')
  return text.replace(new RegExp(`(${q})`, 'ig'), '<mark>$1</mark>')
}

function clearFilters() {
  filters.value = {
    uf: filters.value.uf, // mantém UF atual
    city: '',
    species: 'Cachorro e Gato',
    age: null,
    size: null,
    breed: null,
    gender: null,
    org: null,
    traits: [],
    needs: []
  }
}
</script>

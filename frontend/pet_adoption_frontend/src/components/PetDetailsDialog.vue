<template>
  <v-dialog v-model="model" max-width="980" scrollable transition="fade-transition">
    <v-card class="rounded-xl overflow-hidden">
      <v-row class="ma-0" no-gutters>
        <!-- Foto grande -->
        <v-col cols="12" md="6" class="bg-grey-darken-3">
          <v-img :src="pet.src" :alt="pet.name" height="100%" class="h-100" cover />
        </v-col>

        <!-- Painel de detalhes -->
        <v-col cols="12" md="6" class="pa-6 pa-md-7">
          <div class="d-flex align-center justify-space-between mb-3">
            <div class="d-flex align-center ga-2">
              <h2 class="text-h5 text-h4-md font-weight-bold">{{ pet.name }}</h2>
              <v-icon :color="genderColor" size="20">{{ genderIcon }}</v-icon>
            </div>

            <v-btn icon variant="text" density="comfortable" @click="model = false">
              <v-icon>mdi-close</v-icon>
            </v-btn>
          </div>

          <!-- grade de atributos principais -->
          <v-row class="text-body-2 text-medium-emphasis">
            <v-col cols="6">
              <div class="text-caption">Peso</div>
              <div class="text-high-emphasis">{{ pet.weight ?? '-' }}</div>
            </v-col>
            <v-col cols="6">
              <div class="text-caption">Idade aproximada</div>
              <div class="text-high-emphasis">{{ pet.age ?? '-' }}</div>
            </v-col>
            <v-col cols="6">
              <div class="text-caption">Raça</div>
              <div class="text-high-emphasis">{{ pet.breed ?? '-' }}</div>
            </v-col>
            <v-col cols="6">
              <div class="text-caption">Porte</div>
              <div class="text-high-emphasis">{{ pet.sizeFormatted ?? '-' }}</div>
            </v-col>
          </v-row>

          <!-- blocos descritivos -->
          <div class="mt-3">
            <div class="text-caption text-medium-emphasis">Características</div>
            <div class="d-flex flex-wrap">
              <v-chip
                v-for="(c, i) in pet.caracteristicas"
                :key="i"
                :color="c.color"
                size="small"
                class="ma-1"
                variant="flat"
                text-color="black"
              >
                {{ c.title }}
              </v-chip>
              <span v-if="!pet.caracteristicas?.length" class="text-caption">
                Nenhuma característica específica listada.
              </span>
            </div>
          </div>

          <v-row class="mt-3 text-body-2">
            <v-col cols="12" md="6">
              <div class="text-caption text-medium-emphasis">ONG</div>
              <div class="text-high-emphasis">{{ pet.ong ?? '-' }}</div>
            </v-col>
            <v-col cols="12" md="6">
              <div class="text-caption text-medium-emphasis">Localização</div>
              <div class="text-high-emphasis">{{ pet.place ?? '-' }}</div>
            </v-col>
          </v-row>

          <v-row class="mt-3 text-body-2">
            <v-col cols="12" md="6">
              <div class="text-caption text-medium-emphasis">Necessidades</div>
              <div class="text-high-emphasis">{{ pet.needs ?? '-' }}</div>
            </v-col>
          </v-row>

          <div class="mt-3">
            <div class="text-caption text-medium-emphasis">Descrição</div>
            <div class="text-body-2 text-high-emphasis">
              {{ pet.description ?? '-' }}
            </div>
          </div>

          <!-- CTA -->
          <v-btn
            color="primary"
            class="mt-6"
            size="large"
            block
            rounded="lg"
            @click="$emit('adopt', pet)"
          >
            Achei meu amigo!
          </v-btn>
        </v-col>
      </v-row>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  modelValue: { type: Boolean, default: false },
  pet: {
    type: Object,
    required: true
    // campos esperados (exemplo):
    // { name, src, gender: 'F'|'M', weight, age, breed, size,
    //   traits, ngo, needs, location, description }
  }
})
const emit = defineEmits(['update:modelValue', 'adopt'])

const model = computed({
  get: () => props.modelValue,
  set: (v) => emit('update:modelValue', v)
})

const genderIcon = computed(() =>
  props.pet?.sex === 'M' ? 'mdi-gender-male' : 'mdi-gender-female'
)
const genderColor = computed(() => (props.pet?.sex === 'M' ? 'blue' : 'pink'))
</script>

<style scoped>
.h-100 {
  height: 100%;
}

.text-h4-md {
  font-size: 1.75rem;
  line-height: 1.2;
}

@media (min-width: 960px) {
  .text-h4-md {
    font-size: 2rem;
  }
}
</style>

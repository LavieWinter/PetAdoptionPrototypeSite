<template>
  <v-card hover link @click="isSelected = !isSelected">
    <v-img
      :src="card.src"
      class="align-end"
      gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
      height="200px"
      cover
    >
      <v-card-title class="text-white">
        {{ card.name }} 
        <v-icon v-if="card.sex === 'M'" color="blue">mdi-gender-male</v-icon>
        <v-icon v-else color="pink">mdi-gender-female</v-icon>
        Porte {{ card.sizeFormatted }}
      </v-card-title>
    </v-img>
    <v-card-text>
      <div>
        <v-chip
          v-for="(caracteristica, i) in card.caracteristicas"
          :key="i"
          :color="caracteristica.color"
          class="ma-1"
          text-color="black"
          size="x-small"
          variant="flat"
        >
          {{ caracteristica.title }}
        </v-chip>
      </div>
    </v-card-text>
    <v-card-subtitle class="mb-4">{{ card.ong }}, {{ card.place }}</v-card-subtitle>
  </v-card>
  <v-fade-transition hide-on-leave>
    <PetDetailsDialog v-model="isSelected" :pet="card" @adopt="onAdopt" />
  </v-fade-transition>
</template>
<script setup>
import { ref } from 'vue'
import PetDetailsDialog from '@/components/PetDetailsDialog.vue'

const props = defineProps({
  card: { type: Object, required: true }
})

const isSelected = ref(false)
function onAdopt(pet) {
  // ação ao clicar no CTA (ex.: navegar para "iniciar adoção")
  console.log('Quero adotar:', pet.name)
}
</script>

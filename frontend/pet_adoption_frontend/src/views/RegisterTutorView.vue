<template>
  <v-main>
    <v-container class="py-2" fluid>
      <v-row justify="center">
        <v-col cols="12" md="10" lg="9">
          <v-sheet class="pa-6 pa-md-10" rounded="xl" elevation="0" color="background">
            <TutorSignupForm
              v-if="router.currentRoute.value.path === '/register-person'"
              @success="goHome"
            />
            <OrgSignupForm
              v-else-if="router.currentRoute.value.path === '/register-org'"
              @success="goHome"
            />
          </v-sheet>
        </v-col>
      </v-row>
    </v-container>
  </v-main>
</template>

<script setup>
import { useRouter, useRoute } from 'vue-router'
import TutorSignupForm from '@/components/TutorSignupForm.vue'
import OrgSignupForm from '@/components/OrgSignupForm.vue'

const router = useRouter()
function goHome() {
  router.push('/') // redirecione para onde você quiser após sucesso
}
</script>
<style scoped>
.link {
  color: rgb(var(--v-theme-link));
  text-decoration: none;
  font-weight: 600;
}
.link:hover {
  text-decoration: underline;
}
</style>

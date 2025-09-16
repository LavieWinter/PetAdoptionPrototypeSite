<template>
  <v-main>
    <v-container class="vh-page" fluid>
      <v-row class="h-100 align-center" justify="center">
        <v-col cols="12" md="10" lg="9">
          <v-sheet rounded="xl" elevation="0" color="background" class="pa-6 pa-md-10">
            <!-- Alerts -->

            <v-row align="center">
              <!-- Imagem -->
              <v-col cols="12" md="6" class="d-flex justify-center mb-8 mb-md-0">
                <v-img
                  :src="kittenUrl"
                  alt="Gatinho fofo"
                  width="553"
                  height="829"
                  class="rounded-xl kitty-shadow"
                  cover
                />
              </v-col>

              <!-- Ações -->
              <v-col cols="12" md="6" class="d-flex flex-column align-center px-md-8 gap-y">
                <v-btn
                  block
                  size="large"
                  variant="flat"
                  color="inputBackground"
                  class="text-primary mb-4 action-btn"
                  rounded="lg"
                  @click="goRegisterPerson"
                >
                  Sou pessoa física
                </v-btn>

                <v-btn
                  block
                  size="large"
                  variant="flat"
                  color="inputBackground"
                  class="text-primary mb-8 action-btn"
                  rounded="lg"
                  @click="goRegisterOrg"
                >
                  Faço parte de uma instituição
                </v-btn>

                <div class="text-body-2 mb-10">
                  Já possui uma conta?
                  <RouterLink class="link" to="/login">Faça login</RouterLink>
                </div>

                <v-btn
                  class="text-medium-emphasis"
                  variant="text"
                  append-icon="mdi-open-in-new"
                  size="small"
                  @click="goHelpLostPet"
                >
                  Ajude na busca pelo amigo perdido de alguém
                </v-btn>
              </v-col>
            </v-row>
          </v-sheet>
        </v-col>
      </v-row>
    </v-container>
  </v-main>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter, RouterLink } from 'vue-router'
import kittenUrl from '@/assets/gatoLogin.jpg'
import { login, register } from '@/services/auth'

const router = useRouter()

const email = ref('')
const password = ref('')
const loading = ref(false)
const errorMsg = ref('')
const successMsg = ref('')

function goRegisterPerson() {
  // Ajuste a rota quando tiver a tela
  router.push('/register-person')
}
function goRegisterOrg() {
  // Ajuste a rota quando tiver a tela
  router.push('/register-org')
}
function goHelpLostPet() {
  window.open('https://example.org/busca-amigo-perdido', '_blank')
}
</script>

<style scoped>
/* ocupa a altura toda da janela e alinha verticalmente */
.vh-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
}

/* sombra suave na imagem pra destacar do fundo */
.kitty-shadow {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}

/* deixa os botões mais “gordinhos” como no mock */
.action-btn {
  height: 48px;
  letter-spacing: 0.4px;
  font-weight: 600;
}

/* link com cor de ênfase do tema */
.link {
  color: rgb(var(--v-theme-link));
  text-decoration: none;
  font-weight: 600;
}
.link:hover {
  text-decoration: underline;
}

.gap-y > * + * {
  margin-top: 48px; /* aumenta o espaçamento vertical entre filhos */
}
</style>

<template>
  <v-main class="">
    <v-container class="py-2" fluid>
      <v-row justify="center">
        <v-col cols="12" md="10" lg="9">
          <v-sheet class="pa-6 pa-md-10" rounded="xl" elevation="0" color="background">
            <v-row>
              <v-col cols="12" class="text-center mb-6">
                <v-alert
                  v-if="errorMsg"
                  type="error"
                  variant="tonal"
                  class="mt-4"
                  density="compact"
                >
                  {{ errorMsg }}
                </v-alert>

                <v-alert
                  v-if="successMsg"
                  type="success"
                  variant="tonal"
                  class="mt-4"
                  density="compact"
                >
                  {{ successMsg }}
                </v-alert>
              </v-col>
            </v-row>
            <v-row no-gutters>
              <!-- Imagem -->
              <v-col
                cols="12"
                md="6"
                class="pr-md-6 pb-6 pb-md-6 d-flex align-center justify-center"
              >
                <v-img
                  :src="kittenUrl"
                  alt="Cute kitten"
                  height="829px"
                  width="553px"
                  class="rounded-xl kitty-shadow"
                  cover
                />
              </v-col>

              <!-- Formulário -->
              <v-col cols="12" md="6" class="d-flex flex-column justify-center pa-8">
                <div class="d-flex align-top justify-space-between mb-2">
                  <h1 class="font-weight-bold mb-16 login-title">Bem Vindo de Volta!</h1>
                </div>

                <v-form @submit.prevent="onSubmit" ref="formRef" validate-on="submit">
                  <v-label class="text-caption mb-1 d-block label-text">Email</v-label>
                  <v-text-field
                    v-model="form.email"
                    type="email"
                    placeholder="nome@email.com"
                    variant="solo"
                    density="comfortable"
                    bg-color="inputBackground"
                    rounded="lg"
                    hide-details="auto"
                    class="mb-4"
                    :rules="[rules.required, rules.email]"
                  />

                  <v-label class="text-caption mb-1 d-block label-text">Senha</v-label>
                  <v-text-field
                    v-model="form.password"
                    :type="showPass ? 'text' : 'password'"
                    placeholder="************"
                    variant="solo"
                    density="comfortable"
                    bg-color="inputBackground"
                    rounded="lg"
                    hide-details="auto"
                    class="mb-6"
                    :append-inner-icon="showPass ? 'mdi-eye-off-outline' : 'mdi-eye-outline'"
                    @click:append-inner="showPass = !showPass"
                    :rules="[rules.required, rules.min6]"
                  />

                  <v-btn
                    type="submit"
                    color="primary"
                    size="large"
                    class="mb-3"
                    rounded="lg"
                    block
                    :loading="loading"
                  >
                    Login
                  </v-btn>

                  <v-btn
                    variant="flat"
                    color="background"
                    size="large"
                    class="text-primary"
                    rounded="lg"
                    block
                    @click="goRegister"
                  >
                    Ainda não possui uma conta?
                    <br />
                    Cadastre-se agora
                  </v-btn>
                </v-form>

                <div class="text-caption mt-8 d-flex align-center">
                  <v-btn
                    class="text-medium-emphasis"
                    variant="text"
                    append-icon="mdi-open-in-new"
                    size="small"
                    block
                  >
                    Ajude na busca pelo amigo perdido de alguém
                  </v-btn>
                </div>
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
import { useRouter } from 'vue-router'
import kittenUrl from '@/assets/gatoLogin.jpg'
import { login, register } from '@/services/auth'
import { useStore } from 'vuex'

const router = useRouter()
const showPass = ref(false)
const formRef = ref(null)
const form = ref({ email: '', password: '' })
const loading = ref(false)
const errorMsg = ref('')
const successMsg = ref('')
const store = useStore()

const rules = {
  required: (v) => (!!v && v.length > 0) || 'Obrigatório',
  email: (v) => /.+@.+\..+/.test(v) || 'Email inválido',
  min6: (v) => (v?.length ?? 0) >= 6 || 'Mínimo 6 caracteres'
}

async function onSubmit() {
  const result = await formRef.value.validate()
  if (!result.valid) return
  onLogin()
}

function goRegister() {
  router.push('/register')
}

async function onLogin() {
  errorMsg.value = ''
  successMsg.value = ''
  loading.value = true
  try {
    // await login(form.value.email.trim().toLowerCase(), form.value.password)
    await store.dispatch('user/login', {
      email: form.value.email.trim().toLowerCase(),
      password: form.value.password
    })
    outer.push('/')
  } catch (err) {
    errorMsg.value = mapError(err)
  } finally {
    loading.value = false
  }
}

function mapError(err) {
  const status = err?.response?.status
  if (status === 401) return 'Credenciais inválidas.'
  if (status === 403) return 'Acesso não autorizado.'
  if (status === 409) return 'Esse email já está cadastrado.'
  if (status === 400) return 'Dados inválidos. Verifique os campos.'
  return 'Falha ao comunicar com o servidor.'
}
</script>

<style scoped>
.login-title {
  margin-bottom: 64px !important;
  font-family: 'Nunito', sans-serif;
  font-size: 54px;
  font-weight: 700;
  color: rgb(var(--v-theme-textColor));
}

.kitty-shadow {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}

.label-text {
  font-family: 'Nunito', sans-serif;
  font-size: 18px;
  font-weight: 600;
  color: rgb(var(--v-theme-textColor));
  line-height: 100%;
  letter-spacing: 0;
  margin-bottom: 16px !important;
}

@media (min-width: 960px) {
  .text-h3-md {
    font-size: 2rem;
    line-height: 1.2;
  }
}
</style>

<template>
  <v-row>
    <v-col cols="12" class="text-center mb-6">
      <v-alert v-if="errorMsg" type="error" variant="tonal" class="mt-4" density="compact">
        {{ errorMsg }}
      </v-alert>

      <v-alert v-if="successMsg" type="success" variant="tonal" class="mt-4" density="compact">
        {{ successMsg }}
      </v-alert>
    </v-col>
  </v-row>
  <v-row no-gutters>
    <!-- imagem -->
    <v-col cols="12" md="6" class="pr-md-6 pb-6 pb-md-6 d-flex align-center justify-center">
      <v-img
        :src="dogUrl"
        alt="Cute kitten"
        height="829px"
        width="553px"
        class="rounded-xl kitty-shadow"
        cover
      />
    </v-col>
    <v-col cols="12" md="6" class="d-flex flex-column justify-center pa-8">
      <!-- conteúdo -->
      <div class="d-flex align-center justify-space-between mb-4">
        <h2 class="text-h5 text-md-h6 text-high-emphasis mb-0">
          {{ step === 1 ? 'Crie sua conta' : 'Informações pessoais' }}
        </h2>

        <!-- indicador simples de etapa -->
        <div class="text-caption text-medium-emphasis">{{ step }} / 2</div>
      </div>

      <v-form ref="formRef" @submit.prevent="onSubmit">
        <!-- janela de passos -->
        <v-window v-model="step" class="mb-6">
          <!-- PASSO 1 -->
          <v-window-item :value="1">
            <v-row>
              <v-col cols="12" md="6">
                <v-text-field
                  v-model="form.firstName"
                  label="Primeiro nome"
                  :rules="[rules.required]"
                  color="textColor"
                  bg-color="inputBackground"
                  variant="solo"
                  rounded="lg"
                  density="comfortable"
                />
              </v-col>

              <v-col cols="12" md="6">
                <v-text-field
                  v-model="form.lastName"
                  label="Último nome"
                  :rules="[rules.required]"
                  color="primary"
                  bg-color="inputBackground"
                  variant="solo"
                  rounded="lg"
                  density="comfortable"
                />
              </v-col>

              <v-col cols="12">
                <v-text-field
                  v-model="form.email"
                  label="Email"
                  :rules="[rules.required, rules.email]"
                  color="primary"
                  bg-color="inputBackground"
                  variant="solo"
                  rounded="lg"
                  density="comfortable"
                />
              </v-col>

              <v-col cols="12" md="6">
                <v-mask-input
                  v-model="form.cpf"
                  label="CPF"
                  placeholder="000.000.000-00"
                  :rules="[rules.required, rules.cpf]"
                  color="primary"
                  bg-color="inputBackground"
                  variant="solo"
                  rounded="lg"
                  density="comfortable"
                  mask="###.###.###-##"
                  maxlength="14"
                />
              </v-col>

              <v-col cols="12" md="6">
                <v-mask-input
                  v-model="form.phone"
                  label="Celular"
                  placeholder="(99) 99999-9999"
                  :rules="[rules.required]"
                  mask="(##) #####-####"
                  color="primary"
                  bg-color="inputBackground"
                  variant="solo"
                  rounded="lg"
                  density="comfortable"
                />
              </v-col>

              <v-col cols="12" md="6">
                <v-text-field
                  v-model="form.password"
                  :type="showPass ? 'text' : 'password'"
                  label="Senha"
                  :append-inner-icon="showPass ? 'mdi-eye-off' : 'mdi-eye'"
                  @click:append-inner="showPass = !showPass"
                  :rules="[rules.required, rules.min6]"
                  color="primary"
                  bg-color="inputBackground"
                  variant="solo"
                  rounded="lg"
                  density="comfortable"
                />
              </v-col>

              <v-col cols="12" md="6">
                <v-text-field
                  v-model="form.confirmPassword"
                  :type="showPass2 ? 'text' : 'password'"
                  label="Confirme sua senha"
                  :append-inner-icon="showPass2 ? 'mdi-eye-off' : 'mdi-eye'"
                  @click:append-inner="showPass2 = !showPass2"
                  :rules="[rules.required, (v) => v === form.password || 'Senhas diferentes']"
                  color="primary"
                  bg-color="inputBackground"
                  variant="solo"
                  rounded="lg"
                  density="comfortable"
                />
              </v-col>
            </v-row>
          </v-window-item>

          <!-- PASSO 2 -->
          <v-window-item :value="2">
            <v-row>
              <v-col cols="12">
                <v-text-field
                  v-model="form.ongName"
                  label="Nome da ONG"
                  :rules="[rules.required]"
                  color="textColor"
                  bg-color="inputBackground"
                  variant="solo"
                  rounded="lg"
                  density="comfortable"
                />
              </v-col>
              <v-col cols="12">
                <v-text-field
                  v-model="form.ongEmail"
                  label="Email"
                  :rules="[rules.required, rules.email]"
                  color="primary"
                  bg-color="inputBackground"
                  variant="solo"
                  rounded="lg"
                  density="comfortable"
                />
              </v-col>

              <v-col cols="12" md="6">
                <v-mask-input
                  v-model="form.orgPhone"
                  label="Telefone"
                  placeholder="(99) 99999-9999"
                  :rules="[rules.required]"
                  mask="(##) #####-####"
                  color="primary"
                  bg-color="inputBackground"
                  variant="solo"
                  rounded="lg"
                  density="comfortable"
                />
              </v-col>

              <v-col cols="12" md="6">
                <v-mask-input
                  v-model="form.cnpj"
                  label="CNPJ"
                  placeholder="00.000.000/0000-00"
                  :rules="[rules.cnpj]"
                  color="primary"
                  bg-color="inputBackground"
                  variant="solo"
                  rounded="lg"
                  density="comfortable"
                  mask="##.###.###/####-##"
                />
              </v-col>
              <v-col cols="12" md="6">
                <v-text-field
                  v-model="form.cep"
                  label="CEP"
                  placeholder="00000-000"
                  :rules="[rules.required, rules.cep]"
                  color="primary"
                  bg-color="inputBackground"
                  variant="solo"
                  density="comfortable"
                  :loading="cepLoading"
                  :append-inner-icon="cepLoading ? '' : 'mdi-magnify'"
                  @click:append-inner="onFetchCep"
                  @blur="onFetchCep"
                />
              </v-col>

              <v-col cols="12" md="6">
                <v-text-field
                  v-model="form.street"
                  label="Logradouro"
                  placeholder="Rua das flores"
                  :rules="[rules.required]"
                  color="primary"
                  bg-color="inputBackground"
                  variant="solo"
                  rounded="lg"
                  density="comfortable"
                />
              </v-col>

              <v-col cols="12" md="6">
                <v-text-field
                  v-model="form.neighborhood"
                  label="Bairro"
                  :rules="[rules.required]"
                  color="primary"
                  variant="solo"
                  bg-color="inputBackground"
                  density="comfortable"
                />
              </v-col>
              <v-col cols="12" md="3">
                <v-text-field
                  v-model="form.number"
                  label="Número"
                  placeholder="999"
                  :rules="[rules.required]"
                  color="primary"
                  bg-color="inputBackground"
                  variant="solo"
                  rounded="lg"
                  density="comfortable"
                />
              </v-col>

              <v-col cols="12" md="3">
                <v-select
                  v-model="form.houseType"
                  :items="houseTypes"
                  label="Tipo de casa"
                  :rules="[rules.required]"
                  color="primary"
                  bg-color="inputBackground"
                  variant="solo"
                  rounded="lg"
                  density="comfortable"
                />
              </v-col>

              <v-col cols="12" md="3">
                <v-select
                  v-model="form.uf"
                  :items="ufs"
                  label="UF"
                  :rules="[rules.required]"
                  color="primary"
                  bg-color="inputBackground"
                  variant="solo"
                  rounded="lg"
                  density="comfortable"
                />
              </v-col>

              <v-col cols="12" md="9">
                <v-text-field
                  v-model="form.city"
                  label="Cidade"
                  :rules="[rules.required]"
                  color="primary"
                  bg-color="inputBackground"
                  variant="solo"
                  rounded="lg"
                  density="comfortable"
                />
              </v-col>
              <v-col cols="12">
                <v-label class="mb-4">Logo</v-label>
                <v-file-upload
                  clearable
                  color="inputBackground"
                  density="compact"
                  variant="compact"
                  scrim="primary"
                  title="Arraste e solte o arquivo"
                ></v-file-upload>
              </v-col>
            </v-row>
          </v-window-item>
        </v-window>

        <!-- ações -->
        <div class="d-flex ga-4">
          <v-btn
            v-if="step > 1"
            bg-color="inputBackground"
            variant="solo"
            rounded="lg"
            color="primary"
            @click="previousStep"
            :loading="loading"
          >
            ← Anterior
          </v-btn>

          <v-spacer />

          <v-btn
            v-if="step < 2"
            color="primary"
            variant="flat"
            @click="nextStep"
            :loading="loading"
          >
            Próximo →
          </v-btn>

          <v-btn v-else color="primary" variant="flat" type="submit" :loading="loading">
            Cadastrar
          </v-btn>
        </div>

        <div class="text-caption mt-6 text-center">
          Já possui uma conta?
          <RouterLink class="link" to="/login">Faça login</RouterLink>
        </div>
      </v-form>
    </v-col>
  </v-row>
</template>

<script setup>
import { ref } from 'vue'
import { register } from '@/services/auth' // você já tem este serviço
import { validateCPF, validateCNPJ } from '@/utils/validateTaxId'
import { maskCPF, maskPhoneBR } from '@/utils/masks'
import dogUrl from '@/assets/cachorroLogin.jpg'
import axios from 'axios'

const emit = defineEmits(['success'])
const cepLoading = ref(false)

const step = ref(1)
const formRef = ref(null)
const loading = ref(false)
const errorMsg = ref('')
const successMsg = ref('')
const showPass = ref(false)
const showPass2 = ref(false)

const houseTypes = ['Casa', 'Apartamento', 'Chácara']
const ufs = [
  'AC',
  'AL',
  'AP',
  'AM',
  'BA',
  'CE',
  'DF',
  'ES',
  'GO',
  'MA',
  'MT',
  'MS',
  'MG',
  'PA',
  'PB',
  'PR',
  'PE',
  'PI',
  'RJ',
  'RN',
  'RS',
  'RO',
  'RR',
  'SC',
  'SP',
  'SE',
  'TO'
]

// objeto único com todos os dados
const form = ref({
  firstName: '',
  lastName: '',
  email: '',
  cpf: '',
  phone: '',
  password: '',
  confirmPassword: '',
  cep: '',
  street: '',
  neighborhood: '',
  number: '',
  houseType: '',
  uf: '',
  city: ''
})

const rules = {
  required: (v) => (!!v && String(v).length > 0) || 'Obrigatório',
  email: (v) => /.+@.+\..+/.test(v) || 'Email inválido',
  min6: (v) => (v?.length ?? 0) >= 6 || 'Mínimo 6 caracteres',
  cpf: (v) => validateCPF(v) || 'CPF inválido',
  cnpj: (v) => '' || validateCNPJ(v) || 'CNPJ inválido',
  cep: (v) => {
    const only = (v || '').replace(/\D/g, '')
    return only.length === 8 || 'CEP inválido'
  }
}

async function onFetchCep() {
  // só busca se houver 8 dígitos
  const raw = (form.value.cep || '').replace(/\D/g, '')
  if (raw.length !== 8 || cepLoading.value) return
  try {
    cepLoading.value = true
    errorMsg.value = ''

    const { data } = await axios.get(`https://viacep.com.br/ws/${raw}/json/`)
    if (data?.erro) {
      throw new Error('CEP não encontrado')
    }

    // preenche os campos
    form.value.street = data.logradouro || ''
    form.value.neighborhood = data.bairro || ''
    form.value.city = data.localidade || ''
    form.value.uf = data.uf || ''
    // opcional: data.complemento
  } catch (e) {
    errorMsg.value = 'CEP inválido ou não encontrado.'
  } finally {
    cepLoading.value = false
  }
}

async function nextStep() {
  errorMsg.value = ''
  const validNow = await validateCurrentStep()
  if (validNow) step.value = 2
}

function previousStep() {
  errorMsg.value = ''
  step.value = 1
}

async function validateCurrentStep() {
  // valida somente os campos visíveis
  const { valid } = await formRef.value.validate()
  return valid
}

async function onSubmit() {
  errorMsg.value = ''
  successMsg.value = ''
  loading.value = true

  try {
    // valida o passo 2 antes de enviar
    const ok = await validateCurrentStep()
    if (!ok) return

    // monte o payload aceito pelo backend (ex.: /api/auth/register)
    const payload = {
      email: form.value.email.trim().toLowerCase(),
      password: form.value.password,
      name: `${form.value.firstName} ${form.value.lastName}`.trim(),
      phone: form.value.phone,
      roles: ['DOADOR'] // opcional — backend já força ADOTANTE
      // demais campos ficam para uma futura rota de perfil/endereço
      // street, number, houseType, uf, city, cpf...
    }

    await register(payload)
    successMsg.value = 'Cadastro realizado! Você já está autenticado.'
    emit('success', payload) // opcional: avisa o pai para redirecionar
  } catch (err) {
    const st = err?.response?.status
    if (st === 409) errorMsg.value = 'Esse email já está cadastrado.'
    else if (st === 400) errorMsg.value = 'Dados inválidos. Verifique os campos.'
    else errorMsg.value = 'Falha ao comunicar com o servidor.'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
/* respira um pouco entre os grupos */
.v-window {
  --v-window-transition: 180ms;
}

.kitty-shadow {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}

.link {
  color: rgb(var(--v-theme-link));
  text-decoration: none;
  font-weight: 600;
}
.link:hover {
  text-decoration: underline;
}
</style>

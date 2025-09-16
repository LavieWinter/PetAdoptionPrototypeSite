<template>
    <v-dialog v-model="open" max-width="720" persistent>
        <v-card rounded="xl" elevation="10">
            <v-card-text class="pa-6">
                <!-- close -->
                <div class="d-flex justify-end">
                    <v-btn icon variant="text" @click="open = false">
                        <v-icon>mdi-close</v-icon>
                    </v-btn>
                </div>

                <!-- avatar -->
                <div class="d-flex flex-column align-center mb-6">
                    <v-avatar size="88" class="elevation-2">
                        <v-img :src="form.avatar || defaultAvatar" alt="Avatar" />
                    </v-avatar>

                    <v-btn class="mt-2" density="comfortable" size="small" variant="text"
                        @click="$refs.avatarInput.click()">
                        <v-icon start>mdi-pencil</v-icon>
                        Alterar foto
                    </v-btn>
                    <input ref="avatarInput" type="file" accept="image/*" class="d-none" @change="pickAvatar" />
                </div>
                <!-- form -->
                <v-form ref="formRef" @submit.prevent="onSubmit" v-model="isValid" validate-on="input">
                    <v-row dense>

                        <v-col cols="12" md="6">

                            <!-- Nome -->
                            <v-text-field v-model="form.name" label="Nome" variant="solo" density="comfortable"
                                class="mb-3" rounded="lg" :rules="[rules.required]" color="primary" />

                        </v-col>
                        <v-col cols="12" md="6">

                            <!-- Sobrenome -->
                            <v-text-field v-model="form.lastName" label="Sobrenome" variant="solo" density="comfortable"
                                class="mb-3" rounded="lg" :rules="[rules.required]" color="primary" />

                        </v-col>
                        <v-col cols="12" md="6">

                            <!-- Email -->
                            <v-text-field v-model="form.email" label="E-mail" variant="solo" density="comfortable"
                                class="mb-3" rounded="lg" :rules="[rules.required]" color="primary" disabled />

                        </v-col>
                        <v-col cols="12" md="6">

                            <!-- CPF -->
                            <v-mask-input v-model="form.cpf" label="CPF" placeholder="000.000.000-00"
                                :rules="[rules.required, rules.cpf]" color="primary" variant="solo" rounded="lg"
                                density="comfortable" mask="###.###.###-##" maxlength="14" disabled />

                        </v-col>
                        <v-col cols="12" md="6">
                            <!-- Telefone -->
                            <v-mask-input v-model="form.phone" label="Celular" placeholder="(99) 99999-9999"
                                :rules="[rules.required]" mask="(##) #####-####" color="primary" variant="solo"
                                rounded="lg" density="comfortable" />
                        </v-col>


                        <!-- CEP -->
                        <v-col cols="12" md="6">
                            <v-mask-input v-model="form.cep" label="CEP" placeholder="00000-000"
                                :rules="[rules.required, rules.cep]" color="primary" mask="#####-###" variant="solo"
                                density="comfortable" :loading="cepLoading" rounded="lg"
                                :append-inner-icon="cepLoading ? '' : 'mdi-magnify'" @click:append-inner="onFetchCep"
                                @blur="onFetchCep" />
                        </v-col>
                        <v-col cols="12" md="6">

                            <v-text-field v-model="form.street" label="Rua" class="mb-3" variant="solo" rounded="lg"
                                color="primary" density="comfortable" />
                        </v-col>
                        <v-col cols="12" md="6">
                            
                            <v-text-field v-model="form.number" label="Número" class="mb-3" variant="solo" rounded="lg"
                            color="primary" density="comfortable" />
                        </v-col>
                        <v-col cols="12" md="2">
                            <v-select v-model="form.state" label="UF" class="mb-3" variant="solo" rounded="lg"
                                color="primary" density="comfortable" :items="ufList" />
                        </v-col>
                        <v-col cols="12" md="6">
                            <v-text-field v-model="form.neighborhood" label="Bairro" class="mb-3" variant="solo"
                                rounded="lg" color="primary" density="comfortable" />
                        </v-col>
                        <v-col cols="12" md="4">
                            <v-text-field v-model="form.city" label="Cidade" class="mb-3" variant="solo" rounded="lg"
                                color="primary" density="comfortable" />
                        </v-col>
                    </v-row>

                    <!-- Endereço -->
                </v-form>

                <v-row class="mt-6" dense>
                    <v-col cols="12">
                        <v-btn color="primary" block size="large" rounded="lg" :loading="loading" @click="onSave">
                            Salvar
                        </v-btn>
                    </v-col>
                </v-row>
            </v-card-text>
        </v-card>
    </v-dialog>
</template>

<script setup>
import { ref, watch, computed } from 'vue'
import { useStore } from 'vuex'

const store = useStore()
//const open = ref(false) // controle externo/interno
const loading = ref(false)
const formRef = ref(null)


const props = defineProps({
  modelValue: { type: Boolean, default: false },
  loading: { type: Boolean, default: false }
})
const emit = defineEmits(['update:modelValue', 'save', 'close'])
const defaultAvatar = 'https://cdn.vuetifyjs.com/images/john.png'

const open = computed({
  get: () => props.modelValue,
  set: v => emit('update:modelValue', v)
})


const form = ref({
    name: '',
    phone: '',
    cep: '',
    street: '',
    number: '',
    neighborhood: '',
    city: '',
    state: ''
})

const rules = {
    required: v => !!v || 'Obrigatório'
}

// quando abrir o dialog, popular com os dados da store
watch(open, (v) => {
    if (v) {
        const profile = store.state.user.profile || {}
        form.value = {
            name: profile.name || '',
            phone: profile.phone || '',
            cep: profile.cep || '',
            street: profile.street || '',
            number: profile.number || '',
            neighborhood: profile.neighborhood || '',
            city: profile.city || '',
            state: profile.state || ''
        }
    }
})

const ufList = [
  'AC','AL','AP','AM','BA','CE','DF','ES','GO','MA','MT','MS','MG',
  'PA','PB','PR','PE','PI','RJ','RN','RS','RO','RR','SC','SP','SE','TO'
]
const housingTypes = ['Apartamento', 'Casa', 'Kitnet', 'Chácara', 'Outro']
const cepLoading = ref(false)

async function fetchAddress() {
    const cep = form.value.cep?.replace(/\D/g, '')
    if (cep?.length !== 8) return
    try {
        const resp = await fetch(`https://viacep.com.br/ws/${cep}/json/`)
        const data = await resp.json()
        if (!data.erro) {
            form.value.street = data.logradouro
            form.value.neighborhood = data.bairro
            form.value.city = data.localidade
            form.value.state = data.uf
        }
    } catch (err) {
        console.error('Erro ao buscar CEP', err)
    }
}

async function onSubmit() {
    const valid = await formRef.value.validate()
    if (!valid.valid) return
    loading.value = true
    try {
        await store.dispatch('user/updateProfile', form.value)
        open.value = false
    } finally {
        loading.value = false
    }
}

const avatarInput = ref(null)
function pickAvatar(e) {
  const file = e?.target?.files?.[0]
  if (!file) return
  const reader = new FileReader()
  reader.onload = () => {
    form.value.avatar = reader.result // dataURL (ou envie o File no save)
  }
  reader.readAsDataURL(file)
}

async function onSave() {
  const ok = await formRef.value?.validate()
  if (!ok?.valid) return
  emit('save', { ...form.value })
}
function onClose() {
  emit('close')
  open.value = false
}

</script>

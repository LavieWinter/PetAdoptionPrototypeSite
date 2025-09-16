import api from '@/plugins/axios'

export async function getMe() {
  const { data } = await api.get('/me')
  return data
}

export async function updateMe(payload) {
  const { data } = await api.put('/me', payload)
  return data
}

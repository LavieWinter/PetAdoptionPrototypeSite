import api from '@/plugins/axios'
import { TOKEN_KEY } from '@/plugins/axios'


export async function getMe() {
  let token = localStorage.getItem(TOKEN_KEY)
  let header = {
    Authorization: `Bearer ${token}`
  }
  const { data } = await api.get('/auth/me', { headers: header })
  return data
}

export async function updateMe(payload) {
  const { data } = await api.put('/auth/change-profile', payload)
  return data
}

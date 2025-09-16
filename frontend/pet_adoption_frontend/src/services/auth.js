import api, { TOKEN_KEY } from '../plugins/axios'

function extractToken(resp) {
  if (resp?.data?.accessToken) return resp.data.accessToken

  // 2) Header Authorization: Bearer xxx
  const h = resp?.headers?.authorization || resp?.headers?.Authorization
  if (h && h.startsWith('Bearer ')) return h.substring(7)

  return null
}

export async function login(email, password) {
  const resp = await api.post('/auth/login', { email, password }) // axios rejeita no 401
  const token = extractToken(resp)
  if (!token) throw new Error('TOKEN_MISSING')
  localStorage.setItem(TOKEN_KEY, token)
  return resp.data
}

export async function register({ email, password, name, phone, roles }) {
  const resp = await api.post('/auth/register', {
    email,
    password,
    name,
    phone,
    roles
  })
  const token = extractToken(resp)
  if (token) {
    localStorage.setItem(TOKEN_KEY, token)
  }
  return resp.data
}

export function logout() {
  localStorage.removeItem(TOKEN_KEY)
}

export function getToken() {
  return localStorage.getItem(TOKEN_KEY)
}

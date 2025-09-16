import axios from 'axios'

export const TOKEN_KEY = 'accessToken'

const api = axios.create({
  baseURL: (import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080') + '/api'
  //baseURL: '/api',
})

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// api.interceptors.response.use(
//   (res) => res,
//   (err) => {
//     if (err?.response?.status === 401) {
//       localStorage.removeItem(TOKEN_KEY)
//       if (location.pathname !== '/login') location.href = '/login'
//     }
//     return Promise.reject(err)
//   }
// )

export default api

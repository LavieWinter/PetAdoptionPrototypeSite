// src/services/orgs.js (Exemplo)
import api, { TOKEN_KEY } from '@/plugins/axios';

export async function listOrgs({ page, size }) {
  let header = {
    Authorization: `Bearer ${TOKEN_KEY}`
  }
  const response = await api.get(`${import.meta.env.VITE_API_BASE_URL}/api/orgs`, {
    params: { page, size },
    headers: header
  })
  
  // Tratamento para infinite scroll manual (já que o back retorna Lista e não Page)
  const items = response.data
  const last = items.length < size // Se vier menos que o tamanho da pág, acabou
  
  return { items, last }
}
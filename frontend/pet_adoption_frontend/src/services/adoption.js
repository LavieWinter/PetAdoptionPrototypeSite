import api, { TOKEN_KEY } from '@/plugins/axios';


const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

// Certifique-se de que o axios esteja enviando o token no header Authorization.
// Se você configurou um interceptor global no axios, isso já deve estar automático.
// Caso contrário, você precisará passar { headers: { Authorization: ... } }

export async function createAdoptionApplication(petId) {
  // O backend espera { petId: UUID, useDefaultPreferences: boolean }
  const payload = {
    petId: petId,
    useDefaultPreferences: true // Assumindo true por padrão
  }
  
  const response = await api.post(`${API_BASE_URL}/api/adoptions/applications`, payload)
  return response.data
}
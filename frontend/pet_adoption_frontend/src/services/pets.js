// src/services/pets.js
import api, { TOKEN_KEY } from '@/plugins/axios';

// MOCK de pets com paginação fake

// gera uma imagem diferente por índice
const imgDog = (i) =>
  `https://images.unsplash.com/photo-1517849845537-4d257902454a?auto=format&fit=crop&w=1170&q=80&sig=${i}`;
const imgCat = (i) =>
  `https://images.unsplash.com/photo-1518791841217-8f162f1e1131?auto=format&fit=crop&w=1170&q=80&sig=${i}`;

// util rápido
const pick = (arr, i) => arr[i % arr.length];

// cria um pet “verossímil”
function makePet(i) {
  const isCat = i % 3 === 0; // 1/3 são gatos
  const sex = i % 2 === 0 ? 'M' : 'F';
  const namesDog = ['Thor', 'Mel', 'Bento', 'Chico', 'Simba', 'Paçoca', 'Olaf', 'Marshmellow'];
  const namesCat = ['Luna', 'Nala', 'Mimi', 'Tom', 'Zelda', 'Pipoca', 'Frajola', 'Mingau'];

  const name = isCat ? pick(namesCat, i) : pick(namesDog, i);
  const size = isCat ? 'Pequeno' : pick(['Pequeno', 'Médio', 'Grande'], i);
  const caracteristicasBase = [
    { title: 'Brincalhão', color: 'red' },
    { title: 'Sociável', color: 'brown' },
    { title: 'Protetor', color: 'light-blue' },
    { title: 'Energético', color: 'yellow' },
    { title: 'Calmo', color: 'blue' },
    { title: 'Independente', color: 'purple' },
    { title: 'Dócil', color: 'green' },
  ];

  const caracteristicas = [
    pick(caracteristicasBase, i),
    pick(caracteristicasBase, i + 1),
    pick(caracteristicasBase, i + 2),
  ];

  return {
    id: `mock-${i}`,
    name,
    size,
    caracteristicas,
    src: isCat ? imgCat(i) : imgDog(i),
    flex: 4,
    sex,
    ong: pick(
      ['Projeto Vida Animal', 'SOS Animal', 'Miados & Ronrons', 'Gatinhos do Bem', 'Cão Feliz'],
      i
    ),
    place: pick(['Sumaré, SP', 'Hortolândia, SP', 'Campinas, SP', 'Americana, SP', 'Paulínia, SP'], i),
    weight: isCat ? `${3 + (i % 4)}kg` : `${8 + (i % 25)}kg`,
    age: `${1 + (i % 10)} anos`,
    breed: isCat ? pick(['SRD (Gato)', 'Siamês', 'Persa', 'Maine Coon'], i) : pick(
      ['Vira-lata (SRD)', 'Beagle', 'Labrador', 'Golden Retriever', 'Shih Tzu', 'Pastor Alemão'],
      i
    ),
    description: isCat
      ? 'Gatinho dócil e curioso, adora um cantinho ensolarado. Procura um lar com carinho e paciência.'
      : 'Cão amigável e brincalhão, perfeito para famílias que gostam de passear e se divertir.',
    needs: 'Rotina de cuidados, alimentação balanceada, carinho e atenção.'
  };
}

// dataset “infinito” (ex.: 120 itens)
const TOTAL = 120;
const DB = Array.from({ length: TOTAL }, (_, i) => makePet(i));

// pequena latência pra parecer real
const delay = (ms) => new Promise((r) => setTimeout(r, ms));

/**
 * listPets({ page, size })
 * retorna { items, last, page }
 */
export async function listPets({ page = 0, size = 10 } = {}) {
  //await delay(400 + Math.random() * 400); // 400–800ms
  let token = localStorage.getItem(TOKEN_KEY)
  let header = {
    Authorization: `Bearer ${token}`
  }
  const { data } = await api.get(`/pets?page=${page}&size=${size}`, { headers: header })
  const items = data;
  const start = page * size;
  const end = start + size;
  console.log(`Fetched pets from API: page ${page}, size ${size}, items ${items}`);
  return { items, end, page };
}

/**
 * listPets({ page, size })
 * retorna { items, last, page }
 */
export async function listAvailablePets({ page = 0, size = 10 } = {}) {
  //await delay(400 + Math.random() * 400); // 400–800ms
  let token = localStorage.getItem(TOKEN_KEY)
  let header = {
    Authorization: `Bearer ${token}`
  }
  const { data } = await api.get(`/pets/status/AVAILABLE?page=${page}&size=${size}`, { headers: header })
  const items = data;
  const start = page * size;
  const end = start + size;
  console.log(`Fetched pets from API: page ${page}, size ${size}, items ${items}`);
  return { items, end, page };
}

export async function getPetById(id) {
  let token = localStorage.getItem(TOKEN_KEY)
  let header = {
    Authorization: `Bearer ${token}`
  }
  const { data } = await api.get(`/pets/${id}`, { headers: header })
  return data;
}

export async function adoptPet(id) {
  let token = localStorage.getItem(TOKEN_KEY)
  let header = {
    Authorization: `Bearer ${token}`
  }
  const { data } = await api.post(`/pets/${id}/adopt`, {}, { headers: header })
  return data;
}

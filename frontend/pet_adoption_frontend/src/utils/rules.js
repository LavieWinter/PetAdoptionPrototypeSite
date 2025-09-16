// src/utils/rules.js
import { onlyDigits } from '@/utils/masks'

export const rules = {
  required: (v) => !!(v && String(v).trim()) || 'Obrigatório',
  cpf: (v) => {
    const d = onlyDigits(v)
    return (d.length === 11 && validate_cpf(d)) || 'CPF inválido'
  },
  cellphone: (v) => {
    const d = onlyDigits(v)
    return d.length === 10 || d.length === 11 || 'Telefone inválido'
  }
}

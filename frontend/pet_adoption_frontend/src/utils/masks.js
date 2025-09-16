// src/utils/masks.js
export function onlyDigits(v = '') {
  return (v || '').toString().replace(/\D+/g, '')
}

export function maskCPF(v = '') {
  const d = onlyDigits(v).slice(0, 11)
  const p1 = d.slice(0, 3)
  const p2 = d.slice(3, 6)
  const p3 = d.slice(6, 9)
  const p4 = d.slice(9, 11)
  let out = p1
  if (p2) out += '.' + p2
  if (p3) out += '.' + p3
  if (p4) out += '-' + p4
  return out
}

export function maskPhoneBR(v = '') {
  // (99) 99999-9999 (11 dígitos). Se vier 10 dígitos, formata (99) 9999-9999
  const d = onlyDigits(v).slice(0, 11)
  const hasNine = d.length > 10
  const ddd = d.slice(0, 2)
  const p1 = d.slice(2, hasNine ? 7 : 6)
  const p2 = d.slice(hasNine ? 7 : 6, hasNine ? 11 : 10)

  let out = ddd ? `(${ddd}` : ''
  if (ddd && d.length >= 2) out += ') '
  if (p1) out += p1
  if (p2) out += '-' + p2
  return out
}

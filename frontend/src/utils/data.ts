export function paraIso(dataBr: string): string {
  const [dia, mes, ano] = dataBr.split('/')
  return `${ano}-${mes.padStart(2, '0')}-${dia.padStart(2, '0')}`
}

export function paraBr(dataIso: string): string {
  const [ano, mes, dia] = dataIso.split('-')
  return `${dia}/${mes}/${ano}`
}

export function aplicarMascaraData(valor: string): string {
  const digitos = valor.replace(/\D/g, '').slice(0, 8)
  return [digitos.slice(0, 2), digitos.slice(2, 4), digitos.slice(4, 8)].filter(Boolean).join('/')
}

export function dataCompleta(valor: string): boolean {
  return /^\d{2}\/\d{2}\/\d{4}$/.test(valor)
}

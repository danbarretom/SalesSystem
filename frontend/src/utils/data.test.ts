import { describe, expect, it } from 'vitest'
import { aplicarMascaraData, dataCompleta, paraBr, paraIso } from './data'

describe('paraIso', () => {
  it('converte dd/mm/aaaa para aaaa-mm-dd', () => {
    expect(paraIso('05/03/2026')).toBe('2026-03-05')
  })
})

describe('paraBr', () => {
  it('converte aaaa-mm-dd para dd/mm/aaaa', () => {
    expect(paraBr('2026-03-05')).toBe('05/03/2026')
  })
})

describe('aplicarMascaraData', () => {
  it('insere as barras conforme os dígitos são digitados', () => {
    expect(aplicarMascaraData('0')).toBe('0')
    expect(aplicarMascaraData('05')).toBe('05')
    expect(aplicarMascaraData('0503')).toBe('05/03')
    expect(aplicarMascaraData('05032026')).toBe('05/03/2026')
  })

  it('ignora caracteres que não são dígitos', () => {
    expect(aplicarMascaraData('05/03/2026')).toBe('05/03/2026')
    expect(aplicarMascaraData('ab05cd03ef2026')).toBe('05/03/2026')
  })

  it('corta em 8 dígitos', () => {
    expect(aplicarMascaraData('050320269999')).toBe('05/03/2026')
  })
})

describe('dataCompleta', () => {
  it('reconhece uma data no formato dd/mm/aaaa como completa', () => {
    expect(dataCompleta('05/03/2026')).toBe(true)
  })

  it('rejeita datas parciais ou em outro formato', () => {
    expect(dataCompleta('05/03/202')).toBe(false)
    expect(dataCompleta('05/03')).toBe(false)
    expect(dataCompleta('2026-03-05')).toBe(false)
    expect(dataCompleta('')).toBe(false)
  })
})

import { describe, expect, it, vi } from 'vitest'
import { render, screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { CampoData } from './CampoData'

describe('CampoData', () => {
  it('mostra a data já preenchida em dd/mm/aaaa a partir do valor ISO recebido', () => {
    render(
      <CampoData id="data" label="Data" valorIso="2026-03-05" onChange={vi.fn()} />,
    )

    expect(screen.getByLabelText('Data')).toHaveValue('05/03/2026')
  })

  it('aplica a máscara enquanto o usuário digita e só avisa o valor ISO quando a data fica completa', async () => {
    const usuario = userEvent.setup()
    const onChange = vi.fn()

    render(<CampoData id="data" label="Data" valorIso="" onChange={onChange} />)

    const campo = screen.getByLabelText('Data')
    await usuario.type(campo, '05032026')

    expect(campo).toHaveValue('05/03/2026')
    expect(onChange).toHaveBeenLastCalledWith('2026-03-05')
  })

  it('não chama onChange com valor pronto enquanto a data está incompleta', async () => {
    const usuario = userEvent.setup()
    const onChange = vi.fn()

    render(<CampoData id="data" label="Data" valorIso="" onChange={onChange} />)

    await usuario.type(screen.getByLabelText('Data'), '0503')

    expect(onChange).toHaveBeenLastCalledWith('')
  })
})

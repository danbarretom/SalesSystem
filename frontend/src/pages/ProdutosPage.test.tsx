import { beforeEach, describe, expect, it, vi } from 'vitest'
import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { MemoryRouter } from 'react-router-dom'
import { ProdutosPage } from './ProdutosPage'
import { produtoApi } from '../api/produtoApi'
import { ApiError } from '../api/http'
import type { ProdutoResponse } from '../types/produto'

vi.mock('../api/produtoApi', () => ({
  produtoApi: {
    listar: vi.fn(),
    listarEstoqueBaixo: vi.fn(),
    buscarPorId: vi.fn(),
    criar: vi.fn(),
    atualizar: vi.fn(),
    deletar: vi.fn(),
  },
}))

const produtoExemplo: ProdutoResponse = {
  codigoProduto: 1,
  descricaoProduto: 'Mouse Gamer',
  valorCompra: 50,
  valorVenda: 100,
  estoqueAtual: 10,
  estoqueMinimo: 2,
}

function renderPagina() {
  return render(
    <MemoryRouter>
      <ProdutosPage />
    </MemoryRouter>,
  )
}

beforeEach(() => {
  vi.mocked(produtoApi.listar).mockReset()
  vi.mocked(produtoApi.deletar).mockReset()
})

describe('ProdutosPage', () => {
  it('mostra a mensagem de carregamento e depois a tabela com os produtos', async () => {
    vi.mocked(produtoApi.listar).mockResolvedValue([produtoExemplo])

    renderPagina()

    expect(screen.getByText('Carregando produtos...')).toBeInTheDocument()

    expect(await screen.findByText('Mouse Gamer')).toBeInTheDocument()
    expect(screen.getByText('R$ 100,00')).toBeInTheDocument()
  })

  it('mostra mensagem de lista vazia quando não há produtos', async () => {
    vi.mocked(produtoApi.listar).mockResolvedValue([])

    renderPagina()

    expect(await screen.findByText('Nenhum produto cadastrado.')).toBeInTheDocument()
  })

  it('mostra a mensagem de erro vinda da API quando a busca falha', async () => {
    vi.mocked(produtoApi.listar).mockRejectedValue(
      new ApiError(500, {
        timestamp: '2026-01-01T00:00:00',
        status: 500,
        erro: 'Erro interno',
        mensagem: 'Ocorreu um erro inesperado. Tente novamente mais tarde.',
        detalhes: [],
      }),
    )

    renderPagina()

    expect(
      await screen.findByText('Ocorreu um erro inesperado. Tente novamente mais tarde.'),
    ).toBeInTheDocument()
  })

  it('remove o produto da lista após excluir com confirmação', async () => {
    vi.mocked(produtoApi.listar).mockResolvedValue([produtoExemplo])
    vi.mocked(produtoApi.deletar).mockResolvedValue(undefined)
    vi.spyOn(window, 'confirm').mockReturnValue(true)

    const usuario = userEvent.setup()
    renderPagina()

    await screen.findByText('Mouse Gamer')
    await usuario.click(screen.getByRole('button', { name: 'Excluir' }))

    await waitFor(() => {
      expect(produtoApi.deletar).toHaveBeenCalledWith(1)
    })
    expect(screen.queryByText('Mouse Gamer')).not.toBeInTheDocument()
  })
})

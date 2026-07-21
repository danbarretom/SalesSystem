import { useState, type FormEvent } from 'react'
import { useNavigate } from 'react-router-dom'
import { produtoApi } from '../api/produtoApi'
import { ApiError } from '../api/http'
import type { ProdutoRequest } from '../types/produto'

const valoresIniciais: ProdutoRequest = {
  descricaoProduto: '',
  valorCompra: 0,
  valorVenda: 0,
  estoqueAtual: 0,
  estoqueMinimo: 0,
}

export function ProdutoFormPage() {
  const navigate = useNavigate()
  const [dados, setDados] = useState<ProdutoRequest>(valoresIniciais)
  const [salvando, setSalvando] = useState(false)
  const [mensagemErro, setMensagemErro] = useState<string | null>(null)
  const [detalhesErro, setDetalhesErro] = useState<string[]>([])

  function atualizarCampo<K extends keyof ProdutoRequest>(campo: K, valor: ProdutoRequest[K]) {
    setDados((atual) => ({ ...atual, [campo]: valor }))
  }

  async function handleSubmit(event: FormEvent) {
    event.preventDefault()
    setSalvando(true)
    setMensagemErro(null)
    setDetalhesErro([])

    try {
      await produtoApi.criar(dados)
      navigate('/produtos')
    } catch (e) {
      if (e instanceof ApiError) {
        setMensagemErro(e.erro.mensagem)
        setDetalhesErro(e.erro.detalhes)
      } else {
        setMensagemErro('Erro ao salvar o produto')
      }
    } finally {
      setSalvando(false)
    }
  }

  return (
    <div className="p-6">
      <h1 className="mb-4 text-2xl font-semibold text-gray-800">Novo produto</h1>

      <form onSubmit={handleSubmit} className="max-w-md space-y-4">
        <div>
          <label htmlFor="descricaoProduto" className="block text-sm font-medium text-gray-700">
            Descrição
          </label>
          <input
            id="descricaoProduto"
            type="text"
            required
            maxLength={255}
            value={dados.descricaoProduto}
            onChange={(e) => atualizarCampo('descricaoProduto', e.target.value)}
            className="mt-1 w-full rounded border border-gray-300 px-3 py-2 text-sm focus:border-blue-500 focus:outline-none"
          />
        </div>

        <div>
          <label htmlFor="valorCompra" className="block text-sm font-medium text-gray-700">
            Valor de compra
          </label>
          <input
            id="valorCompra"
            type="number"
            required
            min={0}
            step="0.01"
            value={dados.valorCompra}
            onChange={(e) => atualizarCampo('valorCompra', Number(e.target.value))}
            className="mt-1 w-full rounded border border-gray-300 px-3 py-2 text-sm focus:border-blue-500 focus:outline-none"
          />
        </div>

        <div>
          <label htmlFor="valorVenda" className="block text-sm font-medium text-gray-700">
            Valor de venda
          </label>
          <input
            id="valorVenda"
            type="number"
            required
            min={0.01}
            step="0.01"
            value={dados.valorVenda}
            onChange={(e) => atualizarCampo('valorVenda', Number(e.target.value))}
            className="mt-1 w-full rounded border border-gray-300 px-3 py-2 text-sm focus:border-blue-500 focus:outline-none"
          />
        </div>

        <div>
          <label htmlFor="estoqueAtual" className="block text-sm font-medium text-gray-700">
            Estoque atual
          </label>
          <input
            id="estoqueAtual"
            type="number"
            required
            min={0}
            value={dados.estoqueAtual}
            onChange={(e) => atualizarCampo('estoqueAtual', Number(e.target.value))}
            className="mt-1 w-full rounded border border-gray-300 px-3 py-2 text-sm focus:border-blue-500 focus:outline-none"
          />
        </div>

        <div>
          <label htmlFor="estoqueMinimo" className="block text-sm font-medium text-gray-700">
            Estoque mínimo
          </label>
          <input
            id="estoqueMinimo"
            type="number"
            required
            min={0}
            value={dados.estoqueMinimo}
            onChange={(e) => atualizarCampo('estoqueMinimo', Number(e.target.value))}
            className="mt-1 w-full rounded border border-gray-300 px-3 py-2 text-sm focus:border-blue-500 focus:outline-none"
          />
        </div>

        {mensagemErro && (
          <div className="rounded border border-red-200 bg-red-50 p-3 text-sm text-red-700">
            <p>{mensagemErro}</p>
            {detalhesErro.length > 0 && (
              <ul className="mt-1 list-disc pl-5">
                {detalhesErro.map((detalhe) => (
                  <li key={detalhe}>{detalhe}</li>
                ))}
              </ul>
            )}
          </div>
        )}

        <div className="flex gap-3">
          <button
            type="submit"
            disabled={salvando}
            className="rounded bg-blue-600 px-4 py-2 text-sm font-medium text-white hover:bg-blue-700 disabled:opacity-50"
          >
            {salvando ? 'Salvando...' : 'Salvar'}
          </button>
          <button
            type="button"
            onClick={() => navigate('/produtos')}
            className="rounded border border-gray-300 px-4 py-2 text-sm font-medium text-gray-700 hover:bg-gray-50"
          >
            Cancelar
          </button>
        </div>
      </form>
    </div>
  )
}

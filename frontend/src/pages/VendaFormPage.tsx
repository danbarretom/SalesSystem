import { useEffect, useState, type FormEvent } from 'react'
import { useNavigate } from 'react-router-dom'
import { vendaApi } from '../api/vendaApi'
import { clienteApi } from '../api/clienteApi'
import { produtoApi } from '../api/produtoApi'
import { ApiError } from '../api/http'
import { CampoData } from '../components/CampoData'
import type { ClienteResponse } from '../types/cliente'
import type { ProdutoResponse } from '../types/produto'
import type { ItemVendaRequest, TipoVenda, VendaRequest } from '../types/venda'

interface ItemFormulario {
  id: string
  codigoProduto: number | ''
  quantidade: number
}

function criarItemVazio(): ItemFormulario {
  return { id: crypto.randomUUID(), codigoProduto: '', quantidade: 1 }
}

export function VendaFormPage() {
  const navigate = useNavigate()
  const [clientes, setClientes] = useState<ClienteResponse[]>([])
  const [produtos, setProdutos] = useState<ProdutoResponse[]>([])
  const [tipoVenda, setTipoVenda] = useState<TipoVenda>('A_VISTA')
  const [codigoCliente, setCodigoCliente] = useState<number | ''>('')
  const [dataVencimento, setDataVencimento] = useState('')
  const [itens, setItens] = useState<ItemFormulario[]>([criarItemVazio()])
  const [salvando, setSalvando] = useState(false)
  const [mensagemErro, setMensagemErro] = useState<string | null>(null)
  const [detalhesErro, setDetalhesErro] = useState<string[]>([])

  useEffect(() => {
    clienteApi.listar().then(setClientes).catch(() => setClientes([]))
    produtoApi.listar().then(setProdutos).catch(() => setProdutos([]))
  }, [])

  function atualizarItemProduto(id: string, valor: string) {
    setItens((atual) =>
      atual.map((item) => (item.id === id ? { ...item, codigoProduto: Number(valor) || '' } : item)),
    )
  }

  function atualizarItemQuantidade(id: string, valor: string) {
    setItens((atual) =>
      atual.map((item) => (item.id === id ? { ...item, quantidade: Number(valor) } : item)),
    )
  }

  function adicionarItem() {
    setItens((atual) => [...atual, criarItemVazio()])
  }

  function removerItem(id: string) {
    setItens((atual) => atual.filter((item) => item.id !== id))
  }

  async function handleSubmit(event: FormEvent) {
    event.preventDefault()
    setSalvando(true)
    setMensagemErro(null)
    setDetalhesErro([])

    const dados: VendaRequest = {
      tipoVenda,
      codigoCliente: tipoVenda === 'A_PRAZO' && codigoCliente !== '' ? codigoCliente : undefined,
      dataVencimento: tipoVenda === 'A_PRAZO' ? dataVencimento : undefined,
      itens: itens
        .filter((item) => item.codigoProduto !== '')
        .map(
          (item): ItemVendaRequest => ({
            codigoProduto: item.codigoProduto as number,
            quantidade: item.quantidade,
          }),
        ),
    }

    try {
      await vendaApi.registrar(dados)
      navigate('/vendas')
    } catch (e) {
      if (e instanceof ApiError) {
        setMensagemErro(e.erro.mensagem)
        setDetalhesErro(e.erro.detalhes)
      } else {
        setMensagemErro('Erro ao registrar a venda')
      }
    } finally {
      setSalvando(false)
    }
  }

  return (
    <div className="p-6">
      <h1 className="mb-4 text-2xl font-semibold text-gray-800">Nova venda</h1>

      <form onSubmit={handleSubmit} className="max-w-xl space-y-4">
        <div>
          <label htmlFor="tipoVenda" className="block text-sm font-medium text-gray-700">
            Tipo de venda
          </label>
          <select
            id="tipoVenda"
            value={tipoVenda}
            onChange={(e) => setTipoVenda(e.target.value as TipoVenda)}
            className="mt-1 w-full rounded border border-gray-300 px-3 py-2 text-sm focus:border-blue-500 focus:outline-none"
          >
            <option value="A_VISTA">À vista</option>
            <option value="A_PRAZO">A prazo</option>
          </select>
        </div>

        {tipoVenda === 'A_PRAZO' && (
          <>
            <div>
              <label htmlFor="cliente" className="block text-sm font-medium text-gray-700">
                Cliente
              </label>
              <select
                id="cliente"
                required
                value={codigoCliente}
                onChange={(e) => setCodigoCliente(Number(e.target.value) || '')}
                className="mt-1 w-full rounded border border-gray-300 px-3 py-2 text-sm focus:border-blue-500 focus:outline-none"
              >
                <option value="">Selecione um cliente</option>
                {clientes.map((cliente) => (
                  <option key={cliente.codigoCliente} value={cliente.codigoCliente}>
                    {cliente.nomeCliente}
                  </option>
                ))}
              </select>
            </div>

            <CampoData
              id="dataVencimento"
              label="Data de vencimento"
              valorIso={dataVencimento}
              onChange={setDataVencimento}
              required
            />
          </>
        )}

        <div>
          <div className="mb-2 flex items-center justify-between">
            <span className="block text-sm font-medium text-gray-700">Itens</span>
            <button
              type="button"
              onClick={adicionarItem}
              className="text-sm font-medium text-blue-600 hover:text-blue-700"
            >
              + adicionar item
            </button>
          </div>

          <div className="space-y-2">
            {itens.map((item) => (
              <div key={item.id} className="flex gap-2">
                <select
                  required
                  value={item.codigoProduto}
                  onChange={(e) => atualizarItemProduto(item.id, e.target.value)}
                  className="flex-1 rounded border border-gray-300 px-3 py-2 text-sm focus:border-blue-500 focus:outline-none"
                >
                  <option value="">Selecione um produto</option>
                  {produtos.map((produto) => (
                    <option key={produto.codigoProduto} value={produto.codigoProduto}>
                      {produto.descricaoProduto} —{' '}
                      {produto.valorVenda.toLocaleString('pt-BR', {
                        style: 'currency',
                        currency: 'BRL',
                      })}
                    </option>
                  ))}
                </select>
                <input
                  type="number"
                  required
                  min={1}
                  value={item.quantidade}
                  onChange={(e) => atualizarItemQuantidade(item.id, e.target.value)}
                  className="w-24 rounded border border-gray-300 px-3 py-2 text-sm focus:border-blue-500 focus:outline-none"
                />
                <button
                  type="button"
                  onClick={() => removerItem(item.id)}
                  disabled={itens.length === 1}
                  className="rounded border border-gray-300 px-3 py-2 text-sm text-gray-600 hover:bg-gray-50 disabled:opacity-50"
                >
                  Remover
                </button>
              </div>
            ))}
          </div>
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
            {salvando ? 'Salvando...' : 'Registrar venda'}
          </button>
          <button
            type="button"
            onClick={() => navigate('/vendas')}
            className="rounded border border-gray-300 px-4 py-2 text-sm font-medium text-gray-700 hover:bg-gray-50"
          >
            Cancelar
          </button>
        </div>
      </form>
    </div>
  )
}

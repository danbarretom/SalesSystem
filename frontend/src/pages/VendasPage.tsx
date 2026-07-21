import { useEffect, useState, type FormEvent } from 'react'
import { Link } from 'react-router-dom'
import { vendaApi } from '../api/vendaApi'
import { ApiError } from '../api/http'
import { CampoData } from '../components/CampoData'
import type { VendaResponse } from '../types/venda'

const rotuloTipoVenda: Record<VendaResponse['tipoVenda'], string> = {
  A_VISTA: 'À vista',
  A_PRAZO: 'A prazo',
}

function formatarMoeda(valor: number) {
  return valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
}

function formatarData(data: string) {
  return new Date(data).toLocaleDateString('pt-BR', { timeZone: 'UTC' })
}

export function VendasPage() {
  const [vendas, setVendas] = useState<VendaResponse[]>([])
  const [carregando, setCarregando] = useState(true)
  const [erro, setErro] = useState<string | null>(null)
  const [inicio, setInicio] = useState('')
  const [fim, setFim] = useState('')

  useEffect(() => {
    carregarTodas()
  }, [])

  function carregarTodas() {
    setCarregando(true)
    setErro(null)
    vendaApi
      .listar()
      .then(setVendas)
      .catch((e: unknown) =>
        setErro(e instanceof ApiError ? e.erro.mensagem : 'Erro ao carregar vendas'),
      )
      .finally(() => setCarregando(false))
  }

  function buscarPorPeriodo(event: FormEvent) {
    event.preventDefault()
    setCarregando(true)
    setErro(null)
    vendaApi
      .buscarPorPeriodo(inicio, fim)
      .then(setVendas)
      .catch((e: unknown) =>
        setErro(e instanceof ApiError ? e.erro.mensagem : 'Erro ao buscar vendas por período'),
      )
      .finally(() => setCarregando(false))
  }

  function limparFiltro() {
    setInicio('')
    setFim('')
    carregarTodas()
  }

  return (
    <div className="p-6">
      <div className="mb-4 flex items-center justify-between">
        <h1 className="text-2xl font-semibold text-gray-800">Vendas</h1>
        <Link
          to="/vendas/nova"
          className="rounded bg-blue-600 px-4 py-2 text-sm font-medium text-white hover:bg-blue-700"
        >
          Nova venda
        </Link>
      </div>

      <form onSubmit={buscarPorPeriodo} className="mb-4 flex items-end gap-2">
        <CampoData id="inicio" label="De" valorIso={inicio} onChange={setInicio} required />
        <CampoData id="fim" label="Até" valorIso={fim} onChange={setFim} required />
        <button
          type="submit"
          className="rounded border border-gray-300 px-4 py-2 text-sm font-medium text-gray-700 hover:bg-gray-50"
        >
          Buscar
        </button>
        <button
          type="button"
          onClick={limparFiltro}
          className="text-sm font-medium text-gray-500 hover:text-gray-700"
        >
          Limpar filtro
        </button>
      </form>

      {carregando ? (
        <p className="text-gray-500">Carregando vendas...</p>
      ) : erro ? (
        <p className="text-red-600">{erro}</p>
      ) : vendas.length === 0 ? (
        <p className="text-gray-500">Nenhuma venda registrada.</p>
      ) : (
        <table className="w-full border-collapse text-left text-sm">
          <thead>
            <tr className="border-b border-gray-200 text-gray-500">
              <th className="py-2 pr-4">ID</th>
              <th className="py-2 pr-4">Data</th>
              <th className="py-2 pr-4">Tipo</th>
              <th className="py-2 pr-4">Cliente</th>
              <th className="py-2 pr-4">Vencimento</th>
              <th className="py-2 pr-4">Itens</th>
              <th className="py-2 pr-4">Valor total</th>
            </tr>
          </thead>
          <tbody>
            {vendas.map((venda) => (
              <tr key={venda.id} className="border-b border-gray-100">
                <td className="py-2 pr-4">{venda.id}</td>
                <td className="py-2 pr-4">{formatarData(venda.dataVenda)}</td>
                <td className="py-2 pr-4">{rotuloTipoVenda[venda.tipoVenda]}</td>
                <td className="py-2 pr-4">{venda.cliente?.nomeCliente ?? '-'}</td>
                <td className="py-2 pr-4">
                  {venda.dataVencimento ? formatarData(venda.dataVencimento) : '-'}
                </td>
                <td className="py-2 pr-4">{venda.itens.length}</td>
                <td className="py-2 pr-4">{formatarMoeda(venda.valorTotal)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  )
}

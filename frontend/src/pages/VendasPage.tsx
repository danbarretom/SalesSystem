import { useEffect, useState } from 'react'
import { vendaApi } from '../api/vendaApi'
import { ApiError } from '../api/http'
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

  useEffect(() => {
    vendaApi
      .listar()
      .then(setVendas)
      .catch((e: unknown) =>
        setErro(e instanceof ApiError ? e.erro.mensagem : 'Erro ao carregar vendas'),
      )
      .finally(() => setCarregando(false))
  }, [])

  if (carregando) {
    return <p className="p-6 text-gray-500">Carregando vendas...</p>
  }

  if (erro) {
    return <p className="p-6 text-red-600">{erro}</p>
  }

  return (
    <div className="p-6">
      <h1 className="mb-4 text-2xl font-semibold text-gray-800">Vendas</h1>

      {vendas.length === 0 ? (
        <p className="text-gray-500">Nenhuma venda registrada.</p>
      ) : (
        <table className="w-full border-collapse text-left text-sm">
          <thead>
            <tr className="border-b border-gray-200 text-gray-500">
              <th className="py-2 pr-4">ID</th>
              <th className="py-2 pr-4">Data</th>
              <th className="py-2 pr-4">Tipo</th>
              <th className="py-2 pr-4">Cliente</th>
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

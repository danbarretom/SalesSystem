import { useEffect, useState } from 'react'
import { produtoApi } from '../api/produtoApi'
import { ApiError } from '../api/http'
import type { ProdutoResponse } from '../types/produto'

export function ProdutosPage() {
  const [produtos, setProdutos] = useState<ProdutoResponse[]>([])
  const [carregando, setCarregando] = useState(true)
  const [erro, setErro] = useState<string | null>(null)

  useEffect(() => {
    produtoApi
      .listar()
      .then(setProdutos)
      .catch((e: unknown) =>
        setErro(e instanceof ApiError ? e.erro.mensagem : 'Erro ao carregar produtos'),
      )
      .finally(() => setCarregando(false))
  }, [])

  if (carregando) {
    return <p className="p-6 text-gray-500">Carregando produtos...</p>
  }

  if (erro) {
    return <p className="p-6 text-red-600">{erro}</p>
  }

  return (
    <div className="p-6">
      <h1 className="mb-4 text-2xl font-semibold text-gray-800">Produtos</h1>

      {produtos.length === 0 ? (
        <p className="text-gray-500">Nenhum produto cadastrado.</p>
      ) : (
        <table className="w-full border-collapse text-left text-sm">
          <thead>
            <tr className="border-b border-gray-200 text-gray-500">
              <th className="py-2 pr-4">Código</th>
              <th className="py-2 pr-4">Descrição</th>
              <th className="py-2 pr-4">Valor de venda</th>
              <th className="py-2 pr-4">Estoque</th>
            </tr>
          </thead>
          <tbody>
            {produtos.map((produto) => (
              <tr key={produto.codigoProduto} className="border-b border-gray-100">
                <td className="py-2 pr-4">{produto.codigoProduto}</td>
                <td className="py-2 pr-4">{produto.descricaoProduto}</td>
                <td className="py-2 pr-4">
                  {produto.valorVenda.toLocaleString('pt-BR', {
                    style: 'currency',
                    currency: 'BRL',
                  })}
                </td>
                <td className="py-2 pr-4">{produto.estoqueAtual}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  )
}

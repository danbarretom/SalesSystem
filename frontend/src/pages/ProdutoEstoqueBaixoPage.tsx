import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { produtoApi } from '../api/produtoApi'
import { ApiError } from '../api/http'
import type { ProdutoResponse } from '../types/produto'

export function ProdutoEstoqueBaixoPage() {
  const [produtos, setProdutos] = useState<ProdutoResponse[]>([])
  const [carregando, setCarregando] = useState(true)
  const [erro, setErro] = useState<string | null>(null)

  useEffect(() => {
    produtoApi
      .listarEstoqueBaixo()
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
      <div className="mb-4 flex items-center justify-between">
        <h1 className="text-2xl font-semibold text-gray-800">Estoque baixo</h1>
        <Link to="/produtos" className="text-sm font-medium text-blue-600 hover:text-blue-700">
          Voltar pra todos os produtos
        </Link>
      </div>

      {produtos.length === 0 ? (
        <p className="text-gray-500">Nenhum produto com estoque abaixo do mínimo.</p>
      ) : (
        <table className="w-full border-collapse text-left text-sm">
          <thead>
            <tr className="border-b border-gray-200 text-gray-500">
              <th className="py-2 pr-4">Código</th>
              <th className="py-2 pr-4">Descrição</th>
              <th className="py-2 pr-4">Estoque atual</th>
              <th className="py-2 pr-4">Estoque mínimo</th>
            </tr>
          </thead>
          <tbody>
            {produtos.map((produto) => (
              <tr key={produto.codigoProduto} className="border-b border-gray-100">
                <td className="py-2 pr-4">{produto.codigoProduto}</td>
                <td className="py-2 pr-4">{produto.descricaoProduto}</td>
                <td className="py-2 pr-4 text-red-600">{produto.estoqueAtual}</td>
                <td className="py-2 pr-4">{produto.estoqueMinimo}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  )
}

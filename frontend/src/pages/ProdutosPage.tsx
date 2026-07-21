import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { produtoApi } from '../api/produtoApi'
import { ApiError } from '../api/http'
import type { ProdutoResponse } from '../types/produto'

export function ProdutosPage() {
  const [produtos, setProdutos] = useState<ProdutoResponse[]>([])
  const [carregando, setCarregando] = useState(true)
  const [erro, setErro] = useState<string | null>(null)

  useEffect(() => {
    carregarProdutos()
  }, [])

  function carregarProdutos() {
    setCarregando(true)
    produtoApi
      .listar()
      .then(setProdutos)
      .catch((e: unknown) =>
        setErro(e instanceof ApiError ? e.erro.mensagem : 'Erro ao carregar produtos'),
      )
      .finally(() => setCarregando(false))
  }

  async function handleExcluir(produto: ProdutoResponse) {
    if (!window.confirm(`Excluir o produto "${produto.descricaoProduto}"?`)) return

    try {
      await produtoApi.deletar(produto.codigoProduto)
      setProdutos((atual) => atual.filter((p) => p.codigoProduto !== produto.codigoProduto))
    } catch (e) {
      window.alert(e instanceof ApiError ? e.erro.mensagem : 'Erro ao excluir o produto')
    }
  }

  if (carregando) {
    return <p className="p-6 text-gray-500">Carregando produtos...</p>
  }

  if (erro) {
    return <p className="p-6 text-red-600">{erro}</p>
  }

  return (
    <div className="p-6">
      <div className="mb-4 flex items-center justify-between">
        <h1 className="text-2xl font-semibold text-gray-800">Produtos</h1>
        <div className="flex items-center gap-4">
          <Link
            to="/produtos/estoque-baixo"
            className="text-sm font-medium text-gray-600 hover:text-gray-900"
          >
            Ver estoque baixo
          </Link>
          <Link
            to="/produtos/novo"
            className="rounded bg-blue-600 px-4 py-2 text-sm font-medium text-white hover:bg-blue-700"
          >
            Novo produto
          </Link>
        </div>
      </div>

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
              <th className="py-2 pr-4">Ações</th>
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
                <td className="py-2 pr-4">
                  <Link
                    to={`/produtos/${produto.codigoProduto}/editar`}
                    className="mr-3 text-blue-600 hover:text-blue-700"
                  >
                    Editar
                  </Link>
                  <button
                    type="button"
                    onClick={() => handleExcluir(produto)}
                    className="text-red-600 hover:text-red-700"
                  >
                    Excluir
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  )
}

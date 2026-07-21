import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { produtoApi } from '../api/produtoApi'
import { clienteApi } from '../api/clienteApi'
import { vendaApi } from '../api/vendaApi'
import { ApiError } from '../api/http'

interface Resumo {
  totalProdutos: number
  totalClientes: number
  totalVendas: number
  produtosEstoqueBaixo: number
  valorTotalVendido: number
}

function formatarMoeda(valor: number) {
  return valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
}

interface CartaoResumoProps {
  titulo: string
  valor: number | string
  link?: string
  destaque?: boolean
}

function CartaoResumo({ titulo, valor, link, destaque }: CartaoResumoProps) {
  const conteudo = (
    <div
      className={`rounded border p-4 ${
        destaque ? 'border-red-200 bg-red-50' : 'border-gray-200 bg-white'
      }`}
    >
      <p className="text-sm text-gray-500">{titulo}</p>
      <p className={`text-2xl font-semibold ${destaque ? 'text-red-600' : 'text-gray-800'}`}>
        {valor}
      </p>
    </div>
  )

  if (!link) {
    return conteudo
  }

  return (
    <Link to={link} className="block hover:opacity-80">
      {conteudo}
    </Link>
  )
}

export function DashboardPage() {
  const [resumo, setResumo] = useState<Resumo | null>(null)
  const [carregando, setCarregando] = useState(true)
  const [erro, setErro] = useState<string | null>(null)

  useEffect(() => {
    Promise.all([
      produtoApi.listar(),
      clienteApi.listar(),
      vendaApi.listar(),
      produtoApi.listarEstoqueBaixo(),
    ])
      .then(([produtos, clientes, vendas, estoqueBaixo]) => {
        setResumo({
          totalProdutos: produtos.length,
          totalClientes: clientes.length,
          totalVendas: vendas.length,
          produtosEstoqueBaixo: estoqueBaixo.length,
          valorTotalVendido: vendas.reduce((soma, venda) => soma + venda.valorTotal, 0),
        })
      })
      .catch((e: unknown) =>
        setErro(e instanceof ApiError ? e.erro.mensagem : 'Erro ao carregar o resumo'),
      )
      .finally(() => setCarregando(false))
  }, [])

  if (carregando) {
    return <p className="p-6 text-gray-500">Carregando resumo...</p>
  }

  if (erro || !resumo) {
    return <p className="p-6 text-red-600">{erro ?? 'Erro ao carregar o resumo'}</p>
  }

  return (
    <div className="p-6">
      <h1 className="mb-4 text-2xl font-semibold text-gray-800">Início</h1>

      <div className="grid grid-cols-2 gap-4 sm:grid-cols-3 lg:grid-cols-5">
        <CartaoResumo titulo="Produtos" valor={resumo.totalProdutos} link="/produtos" />
        <CartaoResumo titulo="Clientes" valor={resumo.totalClientes} link="/clientes" />
        <CartaoResumo titulo="Vendas" valor={resumo.totalVendas} link="/vendas" />
        <CartaoResumo
          titulo="Estoque baixo"
          valor={resumo.produtosEstoqueBaixo}
          link="/produtos/estoque-baixo"
          destaque={resumo.produtosEstoqueBaixo > 0}
        />
        <CartaoResumo titulo="Total vendido" valor={formatarMoeda(resumo.valorTotalVendido)} />
      </div>
    </div>
  )
}

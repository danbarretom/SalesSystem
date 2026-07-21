import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { clienteApi } from '../api/clienteApi'
import { ApiError } from '../api/http'
import type { ClienteResponse } from '../types/cliente'

export function ClientesPage() {
  const [clientes, setClientes] = useState<ClienteResponse[]>([])
  const [carregando, setCarregando] = useState(true)
  const [erro, setErro] = useState<string | null>(null)

  useEffect(() => {
    clienteApi
      .listar()
      .then(setClientes)
      .catch((e: unknown) =>
        setErro(e instanceof ApiError ? e.erro.mensagem : 'Erro ao carregar clientes'),
      )
      .finally(() => setCarregando(false))
  }, [])

  if (carregando) {
    return <p className="p-6 text-gray-500">Carregando clientes...</p>
  }

  if (erro) {
    return <p className="p-6 text-red-600">{erro}</p>
  }

  return (
    <div className="p-6">
      <div className="mb-4 flex items-center justify-between">
        <h1 className="text-2xl font-semibold text-gray-800">Clientes</h1>
        <Link
          to="/clientes/novo"
          className="rounded bg-blue-600 px-4 py-2 text-sm font-medium text-white hover:bg-blue-700"
        >
          Novo cliente
        </Link>
      </div>

      {clientes.length === 0 ? (
        <p className="text-gray-500">Nenhum cliente cadastrado.</p>
      ) : (
        <table className="w-full border-collapse text-left text-sm">
          <thead>
            <tr className="border-b border-gray-200 text-gray-500">
              <th className="py-2 pr-4">Código</th>
              <th className="py-2 pr-4">Nome</th>
              <th className="py-2 pr-4">Endereço</th>
              <th className="py-2 pr-4">Telefone</th>
            </tr>
          </thead>
          <tbody>
            {clientes.map((cliente) => (
              <tr key={cliente.codigoCliente} className="border-b border-gray-100">
                <td className="py-2 pr-4">{cliente.codigoCliente}</td>
                <td className="py-2 pr-4">{cliente.nomeCliente}</td>
                <td className="py-2 pr-4">{cliente.enderecoCliente}</td>
                <td className="py-2 pr-4">{cliente.telefoneCliente}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  )
}

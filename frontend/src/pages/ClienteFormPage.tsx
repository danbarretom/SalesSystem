import { useState, type FormEvent } from 'react'
import { useNavigate } from 'react-router-dom'
import { clienteApi } from '../api/clienteApi'
import { ApiError } from '../api/http'
import type { ClienteRequest } from '../types/cliente'

const valoresIniciais: ClienteRequest = {
  nomeCliente: '',
  enderecoCliente: '',
  telefoneCliente: '',
}

export function ClienteFormPage() {
  const navigate = useNavigate()
  const [dados, setDados] = useState<ClienteRequest>(valoresIniciais)
  const [salvando, setSalvando] = useState(false)
  const [mensagemErro, setMensagemErro] = useState<string | null>(null)
  const [detalhesErro, setDetalhesErro] = useState<string[]>([])

  function atualizarCampo<K extends keyof ClienteRequest>(campo: K, valor: ClienteRequest[K]) {
    setDados((atual) => ({ ...atual, [campo]: valor }))
  }

  async function handleSubmit(event: FormEvent) {
    event.preventDefault()
    setSalvando(true)
    setMensagemErro(null)
    setDetalhesErro([])

    try {
      await clienteApi.criar(dados)
      navigate('/clientes')
    } catch (e) {
      if (e instanceof ApiError) {
        setMensagemErro(e.erro.mensagem)
        setDetalhesErro(e.erro.detalhes)
      } else {
        setMensagemErro('Erro ao salvar o cliente')
      }
    } finally {
      setSalvando(false)
    }
  }

  return (
    <div className="p-6">
      <h1 className="mb-4 text-2xl font-semibold text-gray-800">Novo cliente</h1>

      <form onSubmit={handleSubmit} className="max-w-md space-y-4">
        <div>
          <label htmlFor="nomeCliente" className="block text-sm font-medium text-gray-700">
            Nome
          </label>
          <input
            id="nomeCliente"
            type="text"
            required
            maxLength={255}
            value={dados.nomeCliente}
            onChange={(e) => atualizarCampo('nomeCliente', e.target.value)}
            className="mt-1 w-full rounded border border-gray-300 px-3 py-2 text-sm focus:border-blue-500 focus:outline-none"
          />
        </div>

        <div>
          <label htmlFor="enderecoCliente" className="block text-sm font-medium text-gray-700">
            Endereço
          </label>
          <input
            id="enderecoCliente"
            type="text"
            required
            maxLength={255}
            value={dados.enderecoCliente}
            onChange={(e) => atualizarCampo('enderecoCliente', e.target.value)}
            className="mt-1 w-full rounded border border-gray-300 px-3 py-2 text-sm focus:border-blue-500 focus:outline-none"
          />
        </div>

        <div>
          <label htmlFor="telefoneCliente" className="block text-sm font-medium text-gray-700">
            Telefone
          </label>
          <input
            id="telefoneCliente"
            type="text"
            required
            maxLength={255}
            value={dados.telefoneCliente}
            onChange={(e) => atualizarCampo('telefoneCliente', e.target.value)}
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
            onClick={() => navigate('/clientes')}
            className="rounded border border-gray-300 px-4 py-2 text-sm font-medium text-gray-700 hover:bg-gray-50"
          >
            Cancelar
          </button>
        </div>
      </form>
    </div>
  )
}

import { http } from './http'
import type { ClienteRequest, ClienteResponse } from '../types/cliente'

const BASE_PATH = '/api/clientes'

export const clienteApi = {
  listar: () => http.get<ClienteResponse[]>(BASE_PATH),
  buscarPorId: (id: number) => http.get<ClienteResponse>(`${BASE_PATH}/${id}`),
  criar: (dados: ClienteRequest) => http.post<ClienteResponse>(BASE_PATH, dados),
  atualizar: (id: number, dados: ClienteRequest) =>
    http.put<ClienteResponse>(`${BASE_PATH}/${id}`, dados),
  deletar: (id: number) => http.delete(`${BASE_PATH}/${id}`),
}

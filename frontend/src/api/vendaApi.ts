import { http } from './http'
import type { VendaRequest, VendaResponse } from '../types/venda'

const BASE_PATH = '/api/vendas'

export const vendaApi = {
  listar: () => http.get<VendaResponse[]>(BASE_PATH),
  registrar: (dados: VendaRequest) => http.post<VendaResponse>(BASE_PATH, dados),
  buscarPorPeriodo: (inicio: string, fim: string) =>
    http.get<VendaResponse[]>(`${BASE_PATH}/periodo?inicio=${inicio}&fim=${fim}`),
}

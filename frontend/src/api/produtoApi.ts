import { http } from './http'
import type { ProdutoRequest, ProdutoResponse } from '../types/produto'

const BASE_PATH = '/api/produtos'

export const produtoApi = {
  listar: () => http.get<ProdutoResponse[]>(BASE_PATH),
  listarEstoqueBaixo: () => http.get<ProdutoResponse[]>(`${BASE_PATH}/estoque-baixo`),
  buscarPorId: (id: number) => http.get<ProdutoResponse>(`${BASE_PATH}/${id}`),
  criar: (dados: ProdutoRequest) => http.post<ProdutoResponse>(BASE_PATH, dados),
  atualizar: (id: number, dados: ProdutoRequest) =>
    http.put<ProdutoResponse>(`${BASE_PATH}/${id}`, dados),
  deletar: (id: number) => http.delete(`${BASE_PATH}/${id}`),
}

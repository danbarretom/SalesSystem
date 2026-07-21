import type { ClienteResponse } from './cliente'
import type { ProdutoResponse } from './produto'

export type TipoVenda = 'A_VISTA' | 'A_PRAZO'

export interface ItemVendaRequest {
  codigoProduto: number
  quantidade: number
}

export interface ItemVendaResponse {
  id: number
  produto: ProdutoResponse
  quantidade: number
  subtotal: number
}

export interface VendaRequest {
  tipoVenda: TipoVenda
  codigoCliente?: number
  dataVencimento?: string
  itens: ItemVendaRequest[]
}

export interface VendaResponse {
  id: number
  dataVenda: string
  cliente: ClienteResponse | null
  valorTotal: number
  dataVencimento: string | null
  tipoVenda: TipoVenda
  itens: ItemVendaResponse[]
}

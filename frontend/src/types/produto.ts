export interface ProdutoRequest {
  descricaoProduto: string
  valorCompra: number
  valorVenda: number
  estoqueAtual: number
  estoqueMinimo: number
}

export interface ProdutoResponse {
  codigoProduto: number
  descricaoProduto: string
  valorCompra: number
  valorVenda: number
  estoqueAtual: number
  estoqueMinimo: number
}

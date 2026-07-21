package com.portifolio.sistema_vendas.exception;

public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public static RecursoNaoEncontradoException paraId(String entidade, Object id) {
        return new RecursoNaoEncontradoException(entidade + " não encontrado com o código: " + id);
    }
}

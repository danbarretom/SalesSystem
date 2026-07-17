package com.portifolio.sistema_vendas.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResposta> tratarRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {
        ErroResposta erro = new ErroResposta(HttpStatus.NOT_FOUND.value(), "Recurso não encontrado", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResposta> tratarRegraNegocio(RegraNegocioException ex) {
        ErroResposta erro = new ErroResposta(HttpStatus.BAD_REQUEST.value(), "Regra de negócio violada", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResposta> tratarValidacao(MethodArgumentNotValidException ex) {
        List<String> detalhes = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();
        ErroResposta erro = new ErroResposta(HttpStatus.BAD_REQUEST.value(), "Dados inválidos", "Um ou mais campos são inválidos", detalhes);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErroResposta> tratarParametroAusente(MissingServletRequestParameterException ex) {
        ErroResposta erro = new ErroResposta(HttpStatus.BAD_REQUEST.value(), "Parâmetro obrigatório ausente", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResposta> tratarTipoDeParametroInvalido(MethodArgumentTypeMismatchException ex) {
        String mensagem = "Parâmetro '" + ex.getName() + "' recebeu um valor inválido: " + ex.getValue();
        ErroResposta erro = new ErroResposta(HttpStatus.BAD_REQUEST.value(), "Parâmetro inválido", mensagem);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResposta> tratarCorpoDaRequisicaoInvalido(HttpMessageNotReadableException ex) {
        ErroResposta erro = new ErroResposta(HttpStatus.BAD_REQUEST.value(), "Corpo da requisição inválido",
                "O corpo da requisição está ausente ou mal formatado.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ErroResposta> tratarConflito(OptimisticLockingFailureException ex) {
        ErroResposta erro = new ErroResposta(HttpStatus.CONFLICT.value(), "Conflito de concorrência",
                "O recurso foi alterado por outra operação enquanto esta requisição era processada. Tente novamente.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErroResposta> tratarRotaInexistente(NoResourceFoundException ex) {
        ErroResposta erro = new ErroResposta(HttpStatus.NOT_FOUND.value(), "Recurso não encontrado",
                "Nenhum endpoint corresponde a essa URL.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResposta> tratarErroGenerico(Exception ex) {
        log.error("Erro interno inesperado", ex);
        ErroResposta erro = new ErroResposta(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno",
                "Ocorreu um erro inesperado. Tente novamente mais tarde.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }
}

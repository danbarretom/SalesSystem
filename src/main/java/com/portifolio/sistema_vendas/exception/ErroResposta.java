package com.portifolio.sistema_vendas.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErroResposta(
        LocalDateTime timestamp,
        int status,
        String erro,
        String mensagem,
        List<String> detalhes
) {
    public ErroResposta(int status, String erro, String mensagem) {
        this(LocalDateTime.now(), status, erro, mensagem, List.of());
    }

    public ErroResposta(int status, String erro, String mensagem, List<String> detalhes) {
        this(LocalDateTime.now(), status, erro, mensagem, detalhes);
    }
}

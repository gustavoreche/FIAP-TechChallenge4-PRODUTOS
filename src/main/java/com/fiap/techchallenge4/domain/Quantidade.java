package com.fiap.techchallenge4.domain;

import lombok.Getter;

import java.util.Objects;

@Getter
public class Quantidade {
    private Long numero;

    public Quantidade(final Long numero) {
        if (Objects.isNull(numero) || (numero <= 0 || numero > 1000)) {
            throw new IllegalArgumentException("QUANTIDADE NAO PODE SER NULO OU MENOR E IGUAL A ZERO E MAIOR QUE 1000!");
        }

        this.numero = numero;
    }

}

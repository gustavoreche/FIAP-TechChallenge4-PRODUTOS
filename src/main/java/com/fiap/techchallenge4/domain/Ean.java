package com.fiap.techchallenge4.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Builder
public class Ean {
    private Long numero;

    public Ean(final Long numero) {
        if (Objects.isNull(numero) || numero <= 0) {
            throw new IllegalArgumentException("EAN NAO PODE SER NULO OU MENOR E IGUAL A ZERO!");
        }

        this.numero = numero;
    }

}

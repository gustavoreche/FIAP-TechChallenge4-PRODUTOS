package com.fiap.techchallenge4.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Produto {
    private long ean;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private long quantidade;
    private LocalDateTime dataDeCriacao;
}

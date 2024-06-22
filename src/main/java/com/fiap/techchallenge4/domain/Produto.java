package com.fiap.techchallenge4.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Builder
public class Produto {
    private long ean;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private long quantidade;

    public Produto(final Long ean,
                   final String nome,
                   final String descricao,
                   final BigDecimal preco,
                   final Long quantidade) {
        if (Objects.isNull(ean) || ean <= 0) {
            throw new IllegalArgumentException("EAN NAO PODE SER NULO OU MENOR E IGUAL A ZERO!");
        }

        if (Objects.isNull(nome) || nome.isEmpty()) {
            throw new IllegalArgumentException("NOME NAO PODE SER NULO OU VAZIO!");
        }
        if (nome.length() < 3 || nome.length() > 50) {
            throw new IllegalArgumentException("O NOME deve ter no mínimo 3 letras e no máximo 50 letras");
        }

        if (Objects.isNull(descricao) || descricao.isEmpty()) {
            throw new IllegalArgumentException("DESCRICAO NAO PODE SER NULO OU VAZIO!");
        }
        if (descricao.length() < 5 || descricao.length() > 50) {
            throw new IllegalArgumentException("A DESCRICAO deve ter no mínimo 5 letras e no máximo 50 letras");
        }

        if (Objects.isNull(preco) || preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("PRECO NAO PODE SER NULO OU MENOR E IGUAL A ZERO!");
        }

        if (Objects.isNull(quantidade) || (quantidade <= 0 || quantidade > 1000)) {
            throw new IllegalArgumentException("QUANTIDADE NAO PODE SER NULO OU MENOR E IGUAL A ZERO E MAIOR QUE 1000!");
        }

        this.ean = ean;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidade = quantidade;
    }

}

package com.fiap.techchallenge4.useCase;

import com.fiap.techchallenge4.infrastructure.controller.dto.AtualizaProdutoDTO;
import com.fiap.techchallenge4.infrastructure.controller.dto.CriaProdutoDTO;
import com.fiap.techchallenge4.infrastructure.controller.dto.ProdutoDTO;

public interface ProdutoUseCase {

    void importa();

    boolean cadastra(final CriaProdutoDTO produto);

    boolean atualiza(final Long ean,
                     final AtualizaProdutoDTO dadosProduto);

    boolean deleta(final Long ean);

    ProdutoDTO busca(final Long ean);

    Boolean temEstoque(final Long ean,
                       final Long quantidade);
}

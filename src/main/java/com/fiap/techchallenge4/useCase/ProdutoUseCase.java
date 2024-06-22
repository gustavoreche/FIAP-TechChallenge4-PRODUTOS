package com.fiap.techchallenge4.useCase;

import com.fiap.techchallenge4.infrastructure.controller.dto.AtualizaProdutoDTO;
import com.fiap.techchallenge4.infrastructure.controller.dto.CriaProdutoDTO;

public interface ProdutoUseCase {

    void importa();

    boolean cadastra(final CriaProdutoDTO produto);

    boolean atualiza(final Long ean,
                     final AtualizaProdutoDTO dadosProduto);
}

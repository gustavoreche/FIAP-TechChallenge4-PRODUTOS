package com.fiap.techchallenge4.useCase;

import com.fiap.techchallenge4.infrastructure.controller.dto.CriaProdutoDTO;

public interface ProdutoUseCase {

    void importa();

    boolean cadastra(CriaProdutoDTO produto);
}

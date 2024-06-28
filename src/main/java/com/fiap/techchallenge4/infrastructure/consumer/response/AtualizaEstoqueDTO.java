package com.fiap.techchallenge4.infrastructure.consumer.response;

import com.fiap.techchallenge4.domain.StatusEstoqueEnum;

public record AtualizaEstoqueDTO(
		Long ean,
		Long quantidade,
		String statusEstoque
) {}

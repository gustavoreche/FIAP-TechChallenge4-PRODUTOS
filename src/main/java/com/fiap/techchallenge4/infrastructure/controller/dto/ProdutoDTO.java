package com.fiap.techchallenge4.infrastructure.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProdutoDTO(

		Long ean,
		String nome,
		String descricao,
		BigDecimal preco,
		Long quantidade,
		LocalDateTime dataDeCriacao
) {}

package com.fiap.techchallenge4.infrastructure.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

public record AtualizaProdutoDTO(

		@JsonInclude(JsonInclude.Include.NON_NULL)
		String nome,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		String descricao,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		BigDecimal preco,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		Long quantidade
) {}

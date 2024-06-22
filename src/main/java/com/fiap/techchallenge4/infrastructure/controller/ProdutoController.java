package com.fiap.techchallenge4.infrastructure.controller;

import com.fiap.techchallenge4.infrastructure.controller.dto.CriaProdutoDTO;
import com.fiap.techchallenge4.useCase.ProdutoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.fiap.techchallenge4.infrastructure.controller.ProdutoController.URL_PRODUTO;

@Tag(
		name = "Produtos",
		description = "Serviço para realizar o gerenciamento de produtos no sistema"
)
@RestController
@RequestMapping(URL_PRODUTO)
public class ProdutoController {

	public static final String URL_PRODUTO = "/produto";
	public static final String URL_PRODUTO_IMPORTA = URL_PRODUTO + "/importa";

	private final ProdutoUseCase service;

	public ProdutoController(final ProdutoUseCase service) {
		this.service = service;
	}

	@Operation(
			summary = "Serviço para importar produtos"
	)
	@PostMapping("/importa")
	public ResponseEntity<Void> importa() {
		this.service.importa();
		return ResponseEntity
				.status(HttpStatus.OK)
				.build();
	}

	@Operation(
			summary = "Serviço para cadastrar um produto"
	)
	@PostMapping
	public ResponseEntity<Void> cadastra(@RequestBody @Valid final CriaProdutoDTO dadosProduto) {
		final var cadastrou = this.service.cadastra(dadosProduto);
		if(cadastrou) {
			return ResponseEntity
					.status(HttpStatus.CREATED)
					.build();
		}
		return ResponseEntity
				.status(HttpStatus.CONFLICT)
				.build();
	}

//	@Operation(
//			summary = """
//					Serviço que busca os restaurantes. Veja os detalhes:
//					""",
//			description = """
//					Busque as informações pelos seguintes parâmetros:
//
//						- nome (se não passar um nome, pesquisa por todos os restaurantes)
//						- localização (esse parâmetro contêm 4 itens, podendo ser informado, apenas um, dois, todos, ou nenhum. Se não passar algum parâmetro da localização, esse filtro não será levado em consideração)
//						- tipo de cozinha (se não passar um tipo de cozinha, pesquisa por todos os tipos de cozinha)
//
//					"""
//	)
//	@GetMapping
//	public ResponseEntity<List<ExibeBuscaRestauranteDTO>> busca(@RequestParam(required = false) final String nome,
//																@RequestParam(required = false) @Schema(example = "14012-456") final String cep,
//																@RequestParam(required = false) final String bairro,
//																@RequestParam(required = false) final String cidade,
//																@RequestParam(required = false) final String estado,
//																@RequestParam(required = false) final TipoCozinhaEnum tipoCozinha) {
//		var busca = this.service.busca(
//				nome,
//				cep,
//				bairro,
//				cidade,
//				estado,
//				tipoCozinha
//		);
//		var status = busca.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
//		return ResponseEntity
//				.status(status)
//				.body(busca);
//	}

}

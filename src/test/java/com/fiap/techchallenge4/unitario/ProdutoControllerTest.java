package com.fiap.techchallenge4.unitario;

import com.fiap.techchallenge4.infrastructure.controller.ProdutoController;
import com.fiap.techchallenge4.infrastructure.controller.dto.AtualizaProdutoDTO;
import com.fiap.techchallenge4.infrastructure.controller.dto.CriaProdutoDTO;
import com.fiap.techchallenge4.infrastructure.controller.dto.ProdutoDTO;
import com.fiap.techchallenge4.useCase.impl.ProdutoUseCaseImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;

public class ProdutoControllerTest {

    @Test
    public void importa_deveRetornar200_salvaNaBaseDeDados() {
        // preparação
        var service = Mockito.mock(ProdutoUseCaseImpl.class);
        Mockito.doNothing()
                .when(service)
                .importa();

        var controller = new ProdutoController(service);

        // execução
        var produto = controller.importa();

        // avaliação
        Assertions.assertEquals(HttpStatus.OK, produto.getStatusCode());
    }

    @Test
    public void importa_deveRetornar500_naoSalvaNaBaseDeDados() {
        // preparação
        var service = Mockito.mock(ProdutoUseCaseImpl.class);
        Mockito.doThrow(
                new RuntimeException("QUALQUER EXCECAO!!")
        )
                .when(service)
                .importa();

        var controller = new ProdutoController(service);

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, controller::importa);
    }

    @Test
    public void cadastra_deveRetornar201_salvaNaBaseDeDados() {
        // preparação
        var service = Mockito.mock(ProdutoUseCaseImpl.class);
        Mockito.when(service.cadastra(
                                any(CriaProdutoDTO.class)
                        )
                )
                .thenReturn(
                        true
                );

        var controller = new ProdutoController(service);

        // execução
        var produto = controller.cadastra(
                new CriaProdutoDTO(
                        7894900011517L,
                        "Produto Teste",
                        "Descrição do Produto Teste",
                        new BigDecimal("100"),
                        100L
                )
        );

        // avaliação
        Assertions.assertEquals(HttpStatus.CREATED, produto.getStatusCode());
    }

    @Test
    public void cadastra_deveRetornar409_naoSalvaNaBaseDeDados() {
        // preparação
        var service = Mockito.mock(ProdutoUseCaseImpl.class);
        Mockito.when(service.cadastra(
                        any(CriaProdutoDTO.class)
                        )
                )
                .thenReturn(
                        false
                );

        var controller = new ProdutoController(service);

        // execução
        var produto = controller.cadastra(
                new CriaProdutoDTO(
                        7894900011517L,
                        "Produto Teste",
                        "Descrição do Produto Teste",
                        new BigDecimal("100"),
                        100L
                )
        );

        // avaliação
        Assertions.assertEquals(HttpStatus.CONFLICT, produto.getStatusCode());
    }

    @Test
    public void atualiza_deveRetornar200_salvaNaBaseDeDados() {
        // preparação
        var service = Mockito.mock(ProdutoUseCaseImpl.class);
        Mockito.when(service.atualiza(
                                any(Long.class),
                                any(AtualizaProdutoDTO.class)
                        )
                )
                .thenReturn(
                        true
                );

        var controller = new ProdutoController(service);

        // execução
        var produto = controller.atualiza(
                7894900011517L,
                new AtualizaProdutoDTO(
                        "Produto Teste",
                        "Descrição do Produto Teste",
                        new BigDecimal("100"),
                        100L
                )
        );

        // avaliação
        Assertions.assertEquals(HttpStatus.OK, produto.getStatusCode());
    }

    @Test
    public void atualiza_deveRetornar204_naoSalvaNaBaseDeDados() {
        // preparação
        var service = Mockito.mock(ProdutoUseCaseImpl.class);
        Mockito.when(service.atualiza(
                                any(Long.class),
                                any(AtualizaProdutoDTO.class)
                        )
                )
                .thenReturn(
                        false
                );

        var controller = new ProdutoController(service);

        // execução
        var produto = controller.atualiza(
                7894900011517L,
                new AtualizaProdutoDTO(
                        "Produto Teste",
                        "Descrição do Produto Teste",
                        new BigDecimal("100"),
                        100L
                )
        );

        // avaliação
        Assertions.assertEquals(HttpStatus.NO_CONTENT, produto.getStatusCode());
    }

    @Test
    public void deleta_deveRetornar200_deletaNaBaseDeDados() {
        // preparação
        var service = Mockito.mock(ProdutoUseCaseImpl.class);
        Mockito.when(service.deleta(
                                any(Long.class)
                        )
                )
                .thenReturn(
                        true
                );

        var controller = new ProdutoController(service);

        // execução
        var produto = controller.deleta(
                7894900011517L
        );

        // avaliação
        Assertions.assertEquals(HttpStatus.OK, produto.getStatusCode());
    }

    @Test
    public void deleta_deveRetornar204_naoDeletaNaBaseDeDados() {
        // preparação
        var service = Mockito.mock(ProdutoUseCaseImpl.class);
        Mockito.when(service.deleta(
                                any(Long.class)
                        )
                )
                .thenReturn(
                        false
                );

        var controller = new ProdutoController(service);

        // execução
        var produto = controller.deleta(
                7894900011517L
        );

        // avaliação
        Assertions.assertEquals(HttpStatus.NO_CONTENT, produto.getStatusCode());
    }

    @Test
    public void busca_deveRetornar200_buscaNaBaseDeDados() {
        // preparação
        var service = Mockito.mock(ProdutoUseCaseImpl.class);
        Mockito.when(service.busca(
                        7894900011517L
                        )
                )
                .thenReturn(
                        new ProdutoDTO(
                                7894900011517L,
                                "Produto Teste",
                                "Descrição do Produto Teste",
                                new BigDecimal("100"),
                                100L,
                                LocalDateTime.now()
                        )
                );

        var controller = new ProdutoController(service);

        // execução
        var produto = controller.busca(
                7894900011517L
        );

        // avaliação
        Assertions.assertEquals(HttpStatus.OK, produto.getStatusCode());
    }

    @Test
    public void busca_deveRetornar204_naoEncontraNaBaseDeDados() {
        // preparação
        var service = Mockito.mock(ProdutoUseCaseImpl.class);
        Mockito.when(service.busca(
                        7894900011517L
                        )
                )
                .thenReturn(
                        null
                );

        var controller = new ProdutoController(service);

        // execução
        var produto = controller.busca(
                7894900011517L
        );

        // avaliação
        Assertions.assertEquals(HttpStatus.NO_CONTENT, produto.getStatusCode());
    }

    @Test
    public void temEstoque_deveRetornar200_estoqueOk_buscaNaBaseDeDados() {
        // preparação
        var service = Mockito.mock(ProdutoUseCaseImpl.class);
        Mockito.when(service.temEstoque(
                7894900011517L,1L
                        )
                )
                .thenReturn(
                        true
                );

        var controller = new ProdutoController(service);

        // execução
        var produto = controller.temEstoque(
                7894900011517L,
                1L
        );

        // avaliação
        Assertions.assertEquals(HttpStatus.OK, produto.getStatusCode());
    }

    @Test
    public void temEstoque_deveRetornar200_estoqueNaoOk_buscaNaBaseDeDados() {
        // preparação
        var service = Mockito.mock(ProdutoUseCaseImpl.class);
        Mockito.when(service.temEstoque(
                                7894900011517L,1L
                        )
                )
                .thenReturn(
                        false
                );

        var controller = new ProdutoController(service);

        // execução
        var produto = controller.temEstoque(
                7894900011517L,
                1L
        );

        // avaliação
        Assertions.assertEquals(HttpStatus.OK, produto.getStatusCode());
    }

    @Test
    public void temEstoque_deveRetornar204_naoEncontraNaBaseDeDados() {
        // preparação
        var service = Mockito.mock(ProdutoUseCaseImpl.class);
        Mockito.when(service.temEstoque(
                                7894900011517L,
                1L
                        )
                )
                .thenReturn(
                        null
                );

        var controller = new ProdutoController(service);

        // execução
        var produto = controller.temEstoque(
                7894900011517L,
                1L
        );

        // avaliação
        Assertions.assertEquals(HttpStatus.NO_CONTENT, produto.getStatusCode());
    }

    @ParameterizedTest
    @MethodSource("requestValidandoCampos")
    public void cadastra_camposInvalidos_naoBuscaNaBaseDeDados(Long ean,
                                                               String nome,
                                                               String descricao,
                                                               BigDecimal preco,
                                                               Long quantidade) {
        // preparação
        var service = Mockito.mock(ProdutoUseCaseImpl.class);
        Mockito.doThrow(
                        new IllegalArgumentException("Campos inválidos!")
                )
                .when(service)
                .cadastra(
                        any(CriaProdutoDTO.class)
                );

        var controller = new ProdutoController(service);

        // execução e avaliação
        var excecao = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            controller.cadastra(
                    new CriaProdutoDTO(
                            ean,
                            nome,
                            descricao,
                            preco,
                            quantidade
                    )
            );
        });
    }

    @ParameterizedTest
    @MethodSource("requestValidandoCampos")
    public void atualiza_camposInvalidos_naoBuscaNaBaseDeDados(Long ean,
                                                               String nome,
                                                               String descricao,
                                                               BigDecimal preco,
                                                               Long quantidade) {
        // preparação
        var service = Mockito.mock(ProdutoUseCaseImpl.class);
        Mockito.doThrow(
                        new IllegalArgumentException("Campos inválidos!")
                )
                .when(service)
                .atualiza(
                        any(Long.class),
                        any(AtualizaProdutoDTO.class)
                );

        var controller = new ProdutoController(service);

        // execução e avaliação
        var excecao = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            controller.atualiza(
                    Objects.isNull(ean) ? -1L : ean,
                    new AtualizaProdutoDTO(
                            nome,
                            descricao,
                            preco,
                            quantidade
                    )
            );
        });
    }

    @ParameterizedTest
    @ValueSource(longs = {
            -1L,
            0
    })
    public void deleta_camposInvalidos_naoDeletaNaBaseDeDados(Long ean) {
        // preparação
        var service = Mockito.mock(ProdutoUseCaseImpl.class);
        Mockito.doThrow(
                        new IllegalArgumentException("Campos inválidos!")
                )
                .when(service)
                .deleta(
                        any(Long.class)
                );

        var controller = new ProdutoController(service);

        // execução e avaliação
        var excecao = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            controller.deleta(
                    ean
            );
        });
    }

    @ParameterizedTest
    @ValueSource(longs = {
            -1L,
            0
    })
    public void busca_camposInvalidos_naoBuscaNaBaseDeDados(Long ean) {
        // preparação
        var service = Mockito.mock(ProdutoUseCaseImpl.class);
        Mockito.doThrow(
                        new IllegalArgumentException("Campos inválidos!")
                )
                .when(service)
                .busca(
                        any(Long.class)
                );

        var controller = new ProdutoController(service);

        // execução e avaliação
        var excecao = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            controller.busca(
                    ean
            );
        });
    }

    @ParameterizedTest
    @MethodSource("requestValidandoDoisCampos")
    public void atualiza_camposInvalidos_naoBuscaNaBaseDeDados(Long ean,
                                                               Long quantidade) {
        // preparação
        var service = Mockito.mock(ProdutoUseCaseImpl.class);
        Mockito.doThrow(
                        new IllegalArgumentException("Campos inválidos!")
                )
                .when(service)
                .temEstoque(
                        any(Long.class),
                        any(Long.class)
                );

        var controller = new ProdutoController(service);

        // execução e avaliação
        var excecao = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            controller.temEstoque(
                    Objects.isNull(ean) ? -1L : ean,
                    Objects.isNull(quantidade) ? -1L : quantidade
            );
        });
    }

    private static Stream<Arguments> requestValidandoCampos() {
        return Stream.of(
                Arguments.of(null, "Nome de teste", "Descricao teste", new BigDecimal("100"), 100L),
                Arguments.of(-1L, "Nome de teste", "Descricao teste", new BigDecimal("100"), 100L),
                Arguments.of(0L, "Nome de teste", "Descricao teste", new BigDecimal("100"), 100L),
                Arguments.of(123456789L, null, "Descricao teste", new BigDecimal("100"), 100L),
                Arguments.of(123456789L, "", "Descricao teste", new BigDecimal("100"), 100L),
                Arguments.of(123456789L, " ", "Descricao teste", new BigDecimal("100"), 100L),
                Arguments.of(123456789L, "ab", "Descricao teste", new BigDecimal("100"), 100L),
                Arguments.of(123456789L, "aaaaaaaaaabbbbbbbbbbaaaaaaaaaabbbbbbbbbbaaaaaaaaaab", "Descricao teste", new BigDecimal("100"), 100L),
                Arguments.of(123456789L, "Nome de teste", null, new BigDecimal("100"), 100L),
                Arguments.of(123456789L, "Nome de teste", "", new BigDecimal("100"), 100L),
                Arguments.of(123456789L, "Nome de teste", " ", new BigDecimal("100"), 100L),
                Arguments.of(123456789L, "Nome de teste", "ab", new BigDecimal("100"), 100L),
                Arguments.of(123456789L, "Nome de teste", "aaaaaaaaaabbbbbbbbbbaaaaaaaaaabbbbbbbbbbaaaaaaaaaab", new BigDecimal("100"), 100L),
                Arguments.of(123456789L, "Nome de teste", "Descricao teste", null, 100L),
                Arguments.of(123456789L, "Nome de teste", "Descricao teste", BigDecimal.ZERO, 100L),
                Arguments.of(123456789L, "Nome de teste", "Descricao teste", new BigDecimal("-1"), 100L),
                Arguments.of(123456789L, "Nome de teste", "Descricao teste", new BigDecimal("100"), null),
                Arguments.of(123456789L, "Nome de teste", "Descricao teste", new BigDecimal("100"), -1L),
                Arguments.of(123456789L, "Nome de teste", "Descricao teste", new BigDecimal("100"), 0L),
                Arguments.of(123456789L, "Nome de teste", "Descricao teste", new BigDecimal("100"), 1001L)
        );
    }

    private static Stream<Arguments> requestValidandoDoisCampos() {
        return Stream.of(
                Arguments.of(null, 100L),
                Arguments.of(-1L, 100L),
                Arguments.of(0L, 100L),
                Arguments.of(123456789L, null),
                Arguments.of(123456789L, -1L),
                Arguments.of(123456789L, 0L),
                Arguments.of(123456789L, 1001L)
        );
    }

}

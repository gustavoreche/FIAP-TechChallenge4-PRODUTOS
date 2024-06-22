package com.fiap.techchallenge4.unitario;

import com.fiap.techchallenge4.infrastructure.controller.dto.AtualizaProdutoDTO;
import com.fiap.techchallenge4.infrastructure.controller.dto.CriaProdutoDTO;
import com.fiap.techchallenge4.infrastructure.model.ProdutoEntity;
import com.fiap.techchallenge4.infrastructure.repository.ProdutoRepository;
import com.fiap.techchallenge4.useCase.impl.ProdutoUseCaseImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ProdutoUseCaseTest {

    @Test
    public void cadastra_salvaNaBaseDeDados() {
        // preparação
        var repository = Mockito.mock(ProdutoRepository.class);
        var jobLauncher = Mockito.mock(JobLauncher.class);
        var importaProdutosJob = Mockito.mock(Job.class);

        Mockito.when(repository.save(Mockito.any()))
                .thenReturn(
                        new ProdutoEntity(
                                7894900011517L,
                                "Produto Teste",
                                "Descrição do Produto Teste",
                                new BigDecimal("100"),
                                100,
                                LocalDateTime.now()
                        )
                );

        var service = new ProdutoUseCaseImpl(repository, jobLauncher, importaProdutosJob);

        // execução
        service.cadastra(
                new CriaProdutoDTO(
                        7894900011517L,
                        "Produto Teste",
                        "Descrição do Produto Teste",
                        new BigDecimal("100"),
                        100L
                )
        );

        // avaliação
        verify(repository, times(1)).save(Mockito.any());
    }

    @Test
    public void cadastra_produtoJaCadastrado_naoSalvaNaBaseDeDados() {
        // preparação
        var repository = Mockito.mock(ProdutoRepository.class);
        var jobLauncher = Mockito.mock(JobLauncher.class);
        var importaProdutosJob = Mockito.mock(Job.class);

        Mockito.when(repository.findById(Mockito.any()))
                .thenReturn(
                        Optional.of(
                                new ProdutoEntity(
                                        7894900011517L,
                                        "Produto Teste",
                                        "Descrição do Produto Teste",
                                        new BigDecimal("100"),
                                        100,
                                        LocalDateTime.now()
                                )
                        )
                );

        var service = new ProdutoUseCaseImpl(repository, jobLauncher, importaProdutosJob);

        // execução
        service.cadastra(
                new CriaProdutoDTO(
                        7894900011517L,
                        "Produto Teste",
                        "Descrição do Produto Teste",
                        new BigDecimal("100"),
                        100L
                )
        );

        // avaliação
        verify(repository, times(1)).findById(Mockito.any());
        verify(repository, times(0)).save(Mockito.any());
    }

    @Test
    public void atualiza_salvaNaBaseDeDados() {
        // preparação
        var repository = Mockito.mock(ProdutoRepository.class);
        var jobLauncher = Mockito.mock(JobLauncher.class);
        var importaProdutosJob = Mockito.mock(Job.class);

        Mockito.when(repository.save(Mockito.any()))
                .thenReturn(
                        new ProdutoEntity(
                                7894900011517L,
                                "Produto Teste",
                                "Descrição do Produto Teste",
                                new BigDecimal("100"),
                                100,
                                LocalDateTime.now()
                        )
                );
        Mockito.when(repository.findById(Mockito.any()))
                .thenReturn(
                        Optional.of(new ProdutoEntity(
                                7894900011517L,
                                "Produto Teste",
                                "Descrição do Produto Teste",
                                new BigDecimal("100"),
                                100,
                                LocalDateTime.now()
                        ))
                );

        var service = new ProdutoUseCaseImpl(repository, jobLauncher, importaProdutosJob);

        // execução
        service.atualiza(
                7894900011517L,
                new AtualizaProdutoDTO(
                        "Produto Teste",
                        "Descrição do Produto Teste",
                        new BigDecimal("100"),
                        100L
                )
        );

        // avaliação
        verify(repository, times(1)).save(Mockito.any());
    }

    @Test
    public void atualiza_produtoNaoEstaCadastrado_naoSalvaNaBaseDeDados() {
        // preparação
        var repository = Mockito.mock(ProdutoRepository.class);
        var jobLauncher = Mockito.mock(JobLauncher.class);
        var importaProdutosJob = Mockito.mock(Job.class);

        Mockito.when(repository.findById(Mockito.any()))
                .thenReturn(
                        Optional.of(
                                new ProdutoEntity(
                                        7894900011517L,
                                        "Produto Teste",
                                        "Descrição do Produto Teste",
                                        new BigDecimal("100"),
                                        100,
                                        LocalDateTime.now()
                                )
                        )
                );
        Mockito.when(repository.findById(Mockito.any()))
                .thenReturn(
                        Optional.empty()
                );

        var service = new ProdutoUseCaseImpl(repository, jobLauncher, importaProdutosJob);

        // execução
        service.atualiza(
                7894900011517L,
                new AtualizaProdutoDTO(
                        "Produto Teste",
                        "Descrição do Produto Teste",
                        new BigDecimal("100"),
                        100L
                )
        );

        // avaliação
        verify(repository, times(1)).findById(Mockito.any());
        verify(repository, times(0)).save(Mockito.any());
    }

    @ParameterizedTest
    @MethodSource("requestValidandoCampos")
    public void cadastra_camposInvalidos_naoSalvaNaBaseDeDados(Long ean,
                                                               String nome,
                                                               String descricao,
                                                               BigDecimal preco,
                                                               Long quantidade) {
        // preparação
        var repository = Mockito.mock(ProdutoRepository.class);
        var jobLauncher = Mockito.mock(JobLauncher.class);
        var importaProdutosJob = Mockito.mock(Job.class);

        Mockito.when(repository.save(Mockito.any()))
                .thenReturn(
                        new ProdutoEntity(
                                7894900011517L,
                                "Produto Teste",
                                "Descrição do Produto Teste",
                                new BigDecimal("100"),
                                100,
                                LocalDateTime.now()
                        )
                );

        var service = new ProdutoUseCaseImpl(repository, jobLauncher, importaProdutosJob);

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            service.cadastra(
                    new CriaProdutoDTO(
                            ean,
                            nome,
                            descricao,
                            preco,
                            quantidade
                    )
            );
        });
        verify(repository, times(0)).save(Mockito.any());
    }

    @ParameterizedTest
    @MethodSource("requestValidandoCampos")
    public void atualiza_camposInvalidos_naoSalvaNaBaseDeDados(Long ean,
                                                               String nome,
                                                               String descricao,
                                                               BigDecimal preco,
                                                               Long quantidade) {
        // preparação
        var repository = Mockito.mock(ProdutoRepository.class);
        var jobLauncher = Mockito.mock(JobLauncher.class);
        var importaProdutosJob = Mockito.mock(Job.class);

        Mockito.when(repository.save(Mockito.any()))
                .thenReturn(
                        new ProdutoEntity(
                                7894900011517L,
                                "Produto Teste",
                                "Descrição do Produto Teste",
                                new BigDecimal("100"),
                                100,
                                LocalDateTime.now()
                        )
                );
        Mockito.when(repository.findById(Mockito.any()))
                .thenReturn(
                        Optional.of(new ProdutoEntity(
                                7894900011517L,
                                "Produto Teste",
                                "Descrição do Produto Teste",
                                new BigDecimal("100"),
                                100,
                                LocalDateTime.now()
                        ))
                );

        var service = new ProdutoUseCaseImpl(repository, jobLauncher, importaProdutosJob);

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            service.atualiza(
                    ean,
                    new AtualizaProdutoDTO(
                            nome,
                            descricao,
                            preco,
                            quantidade
                    )
            );
        });
        verify(repository, times(0)).save(Mockito.any());
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

}

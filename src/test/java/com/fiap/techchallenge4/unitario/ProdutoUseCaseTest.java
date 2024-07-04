package com.fiap.techchallenge4.unitario;

import com.fiap.techchallenge4.domain.StatusEstoqueEnum;
import com.fiap.techchallenge4.infrastructure.consumer.response.AtualizaEstoqueDTO;
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
import org.junit.jupiter.params.provider.ValueSource;
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

    @Test
    public void deleta_deletaNaBaseDeDados() {
        // preparação
        var repository = Mockito.mock(ProdutoRepository.class);
        var jobLauncher = Mockito.mock(JobLauncher.class);
        var importaProdutosJob = Mockito.mock(Job.class);

        Mockito.doNothing().when(repository).deleteById(Mockito.any());
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
        service.deleta(
                7894900011517L
        );

        // avaliação
        verify(repository, times(1)).findById(Mockito.any());
        verify(repository, times(1)).deleteById(Mockito.any());
    }

    @Test
    public void deleta_produtoNaoEstaCadastrado_naoDeletaNaBaseDeDados() {
        // preparação
        var repository = Mockito.mock(ProdutoRepository.class);
        var jobLauncher = Mockito.mock(JobLauncher.class);
        var importaProdutosJob = Mockito.mock(Job.class);

        Mockito.when(repository.findById(Mockito.any()))
                .thenReturn(
                        Optional.empty()
                );

        var service = new ProdutoUseCaseImpl(repository, jobLauncher, importaProdutosJob);

        // execução
        service.deleta(
                7894900011517L
        );

        // avaliação
        verify(repository, times(1)).findById(Mockito.any());
        verify(repository, times(0)).deleteById(Mockito.any());
    }

    @Test
    public void busca_buscaNaBaseDeDados() {
        // preparação
        var repository = Mockito.mock(ProdutoRepository.class);
        var jobLauncher = Mockito.mock(JobLauncher.class);
        var importaProdutosJob = Mockito.mock(Job.class);

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
        service.busca(
                7894900011517L
        );

        // avaliação
        verify(repository, times(1)).findById(Mockito.any());
    }

    @Test
    public void busca_produtoNaoEstaCadastrado_naoEncontraNaBaseDeDados() {
        // preparação
        var repository = Mockito.mock(ProdutoRepository.class);
        var jobLauncher = Mockito.mock(JobLauncher.class);
        var importaProdutosJob = Mockito.mock(Job.class);

        Mockito.when(repository.findById(Mockito.any()))
                .thenReturn(
                        Optional.empty()
                );

        var service = new ProdutoUseCaseImpl(repository, jobLauncher, importaProdutosJob);

        // execução
        service.busca(
                7894900011517L
        );

        // avaliação
        verify(repository, times(1)).findById(Mockito.any());
    }

    @Test
    public void temEstoque_quantidadeIgual_buscaNaBaseDeDados() {
        // preparação
        var repository = Mockito.mock(ProdutoRepository.class);
        var jobLauncher = Mockito.mock(JobLauncher.class);
        var importaProdutosJob = Mockito.mock(Job.class);

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
        Boolean response = service.temEstoque(
                7894900011517L,
                100L
        );

        // avaliação
        verify(repository, times(1)).findById(Mockito.any());
        Assertions.assertTrue(response);
    }

    @Test
    public void temEstoque_quantidadeMenor_buscaNaBaseDeDados() {
        // preparação
        var repository = Mockito.mock(ProdutoRepository.class);
        var jobLauncher = Mockito.mock(JobLauncher.class);
        var importaProdutosJob = Mockito.mock(Job.class);

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
        Boolean response = service.temEstoque(
                7894900011517L,
                99L
        );

        // avaliação
        verify(repository, times(1)).findById(Mockito.any());
        Assertions.assertTrue(response);
    }

    @Test
    public void temEstoque_quantidadeMaior_buscaNaBaseDeDados() {
        // preparação
        var repository = Mockito.mock(ProdutoRepository.class);
        var jobLauncher = Mockito.mock(JobLauncher.class);
        var importaProdutosJob = Mockito.mock(Job.class);

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
        Boolean response = service.temEstoque(
                7894900011517L,
                101L
        );

        // avaliação
        verify(repository, times(1)).findById(Mockito.any());
        Assertions.assertFalse(response);
    }

    @Test
    public void temEstoque_produtoNaoEstaCadastrado_naoEncontraNaBaseDeDados() {
        // preparação
        var repository = Mockito.mock(ProdutoRepository.class);
        var jobLauncher = Mockito.mock(JobLauncher.class);
        var importaProdutosJob = Mockito.mock(Job.class);

        Mockito.when(repository.findById(Mockito.any()))
                .thenReturn(
                        Optional.empty()
                );

        var service = new ProdutoUseCaseImpl(repository, jobLauncher, importaProdutosJob);

        // execução
        service.temEstoque(
                7894900011517L,
                1L
        );

        // avaliação
        verify(repository, times(1)).findById(Mockito.any());
    }

    @Test
    public void atualizaEstoque_retira_quantidadeIgual_buscaNaBaseDeDados() {
        // preparação
        var repository = Mockito.mock(ProdutoRepository.class);
        var jobLauncher = Mockito.mock(JobLauncher.class);
        var importaProdutosJob = Mockito.mock(Job.class);

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
        service.atualizaEstoque(
                new AtualizaEstoqueDTO(
                        7894900011517L,
                        100L,
                        StatusEstoqueEnum.RETIRA_DO_ESTOQUE.name()
                )
        );

        // avaliação
        verify(repository, times(1)).findById(Mockito.any());
        verify(repository, times(1)).save(Mockito.any());
    }

    @Test
    public void atualizaEstoque_retira_quantidadeMenor_buscaNaBaseDeDados() {
        // preparação
        var repository = Mockito.mock(ProdutoRepository.class);
        var jobLauncher = Mockito.mock(JobLauncher.class);
        var importaProdutosJob = Mockito.mock(Job.class);

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
        service.atualizaEstoque(
                new AtualizaEstoqueDTO(
                        7894900011517L,
                        99L,
                        StatusEstoqueEnum.RETIRA_DO_ESTOQUE.name()
                )
        );

        // avaliação
        verify(repository, times(1)).findById(Mockito.any());
        verify(repository, times(1)).save(Mockito.any());
    }

    @Test
    public void atualizaEstoque_retira_quantidadeMaior_buscaNaBaseDeDados() {
        // preparação
        var repository = Mockito.mock(ProdutoRepository.class);
        var jobLauncher = Mockito.mock(JobLauncher.class);
        var importaProdutosJob = Mockito.mock(Job.class);

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
        service.atualizaEstoque(
                new AtualizaEstoqueDTO(
                        7894900011517L,
                        101L,
                        StatusEstoqueEnum.RETIRA_DO_ESTOQUE.name()
                )
        );

        // avaliação
        verify(repository, times(1)).findById(Mockito.any());
        verify(repository, times(0)).save(Mockito.any());
    }

    @Test
    public void atualizaEstoque_retira_produtoNaoEstaCadastrado_naoEncontraNaBaseDeDados() {
        // preparação
        var repository = Mockito.mock(ProdutoRepository.class);
        var jobLauncher = Mockito.mock(JobLauncher.class);
        var importaProdutosJob = Mockito.mock(Job.class);

        Mockito.when(repository.findById(Mockito.any()))
                .thenReturn(
                        Optional.empty()
                );

        var service = new ProdutoUseCaseImpl(repository, jobLauncher, importaProdutosJob);

        // execução e avaliação
        service.atualizaEstoque(
                new AtualizaEstoqueDTO(
                        7894900011517L,
                        101L,
                        StatusEstoqueEnum.RETIRA_DO_ESTOQUE.name()
                )
        );

        // avaliação
        verify(repository, times(1)).findById(Mockito.any());
        verify(repository, times(0)).save(Mockito.any());
    }

    @Test
    public void atualizaEstoque_volta_buscaNaBaseDeDados() {
        // preparação
        var repository = Mockito.mock(ProdutoRepository.class);
        var jobLauncher = Mockito.mock(JobLauncher.class);
        var importaProdutosJob = Mockito.mock(Job.class);

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
        service.atualizaEstoque(
                new AtualizaEstoqueDTO(
                        7894900011517L,
                        99L,
                        StatusEstoqueEnum.VOLTA_PARA_O_ESTOQUE.name()
                )
        );

        // avaliação
        verify(repository, times(1)).findById(Mockito.any());
        verify(repository, times(1)).save(Mockito.any());
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

    @ParameterizedTest
    @ValueSource(longs = {
            -1000,
            -1L,
            0
    })
    public void deleta_camposInvalidos_naoDeletaNaBaseDeDados(Long ean) {
        // preparação
        var repository = Mockito.mock(ProdutoRepository.class);
        var jobLauncher = Mockito.mock(JobLauncher.class);
        var importaProdutosJob = Mockito.mock(Job.class);

        var service = new ProdutoUseCaseImpl(repository, jobLauncher, importaProdutosJob);

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            service.deleta(
                    ean == -1000 ? null : ean
            );
        });
        verify(repository, times(0)).findById(Mockito.any());
        verify(repository, times(0)).deleteById(Mockito.any());
    }

    @ParameterizedTest
    @ValueSource(longs = {
            -1000,
            -1L,
            0
    })
    public void busca_camposInvalidos_naoBuscaNaBaseDeDados(Long ean) {
        // preparação
        var repository = Mockito.mock(ProdutoRepository.class);
        var jobLauncher = Mockito.mock(JobLauncher.class);
        var importaProdutosJob = Mockito.mock(Job.class);

        var service = new ProdutoUseCaseImpl(repository, jobLauncher, importaProdutosJob);

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            service.busca(
                    ean == -1000 ? null : ean
            );
        });
        verify(repository, times(0)).findById(Mockito.any());
        verify(repository, times(0)).deleteById(Mockito.any());
    }

    @ParameterizedTest
    @MethodSource("requestValidandoDoisCampos")
    public void temEstoque_camposInvalidos_naoBuscaNaBaseDeDados(Long ean,
                                                                 Long quantidade) {
        // preparação
        var repository = Mockito.mock(ProdutoRepository.class);
        var jobLauncher = Mockito.mock(JobLauncher.class);
        var importaProdutosJob = Mockito.mock(Job.class);

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
            service.temEstoque(
                    ean,
                    quantidade
            );
        });
        verify(repository, times(0)).findById(Mockito.any());
    }

    @ParameterizedTest
    @MethodSource("requestValidandoTresCampos")
    public void atualizaEstoque_camposInvalidos_naoEntraNoFluxo(Long ean,
                                                                Long quantidade,
                                                                String statusEstoque) {
        // preparação
        var repository = Mockito.mock(ProdutoRepository.class);
        var jobLauncher = Mockito.mock(JobLauncher.class);
        var importaProdutosJob = Mockito.mock(Job.class);

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
            service.atualizaEstoque(
                    new AtualizaEstoqueDTO(
                            ean,
                            quantidade,
                            statusEstoque
                    )
            );
        });
        verify(repository, times(0)).findById(Mockito.any());
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

    private static Stream<Arguments> requestValidandoTresCampos() {
        return Stream.of(
                Arguments.of(null, 100L, StatusEstoqueEnum.RETIRA_DO_ESTOQUE.name()),
                Arguments.of(-1L, 100L, StatusEstoqueEnum.RETIRA_DO_ESTOQUE.name()),
                Arguments.of(0L, 100L, StatusEstoqueEnum.RETIRA_DO_ESTOQUE.name()),
                Arguments.of(123456789L, null, StatusEstoqueEnum.RETIRA_DO_ESTOQUE.name()),
                Arguments.of(123456789L, -1L, StatusEstoqueEnum.RETIRA_DO_ESTOQUE.name()),
                Arguments.of(123456789L, 0L, StatusEstoqueEnum.RETIRA_DO_ESTOQUE.name()),
                Arguments.of(123456789L, 1001L, StatusEstoqueEnum.RETIRA_DO_ESTOQUE.name()),
                Arguments.of(123456789L, 100L, null),
                Arguments.of(123456789L, 100L, ""),
                Arguments.of(123456789L, 100L, " "),
                Arguments.of(123456789L, 100L, "teste")
        );
    }

}

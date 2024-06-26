package com.fiap.techchallenge4.integrados;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge4.infrastructure.consumer.response.BaixaNoEstoqueDTO;
import com.fiap.techchallenge4.infrastructure.model.ProdutoEntity;
import com.fiap.techchallenge4.infrastructure.repository.ProdutoRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.function.StreamBridge;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConsumerBaixaNoEstoqueIT {

    @Autowired
    private StreamBridge streamBridge;

    @Autowired
    ProdutoRepository produtoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void inicializaLimpezaDoDatabase() {
        this.produtoRepository.deleteAll();
    }

    @AfterAll
    void finalizaLimpezaDoDatabase() {
        this.produtoRepository.deleteAll();
    }

    @Test
    public void baixaNoEstoque_quantidadeIgual_buscaNaBaseDeDados() throws ExecutionException, InterruptedException {

        this.produtoRepository.save(ProdutoEntity.builder()
                .ean(2222222222L)
                .nome("Laranja")
                .descricao("Fruta")
                .preco(new BigDecimal("3.00"))
                .quantidade(10)
                .dataDeCriacao(LocalDateTime.now())
                .build());

        var producer = CompletableFuture.runAsync(() -> {
            this.streamBridge
                    .send("produto-atualiza-estoque", new BaixaNoEstoqueDTO(2222222222L, 10L));
        });

        producer.get();
        Thread.sleep(2000);

        var produto = this.produtoRepository.findAll().get(0);

        Assertions.assertEquals(0, produto.getQuantidade());
    }

    @Test
    public void baixaNoEstoque_quantidadeMenor_buscaNaBaseDeDados() throws ExecutionException, InterruptedException {

        this.produtoRepository.save(ProdutoEntity.builder()
                .ean(2222222222L)
                .nome("Laranja")
                .descricao("Fruta")
                .preco(new BigDecimal("3.00"))
                .quantidade(10)
                .dataDeCriacao(LocalDateTime.now())
                .build());

        var producer = CompletableFuture.runAsync(() -> {
            this.streamBridge
                    .send("produto-atualiza-estoque", new BaixaNoEstoqueDTO(2222222222L, 9L));
        });

        producer.get();
        Thread.sleep(2000);

        var produto = this.produtoRepository.findAll().get(0);

        Assertions.assertEquals(1, produto.getQuantidade());
    }

    @Test
    public void baixaNoEstoque_quantidadeMaior_buscaNaBaseDeDados() throws ExecutionException, InterruptedException {

        this.produtoRepository.save(ProdutoEntity.builder()
                .ean(2222222222L)
                .nome("Laranja")
                .descricao("Fruta")
                .preco(new BigDecimal("3.00"))
                .quantidade(10)
                .dataDeCriacao(LocalDateTime.now())
                .build());

        var producer = CompletableFuture.runAsync(() -> {
            this.streamBridge
                    .send("produto-atualiza-estoque", new BaixaNoEstoqueDTO(2222222222L, 11L));
        });

        producer.get();
        Thread.sleep(2000);

        var produto = this.produtoRepository.findAll().get(0);

        Assertions.assertEquals(10, produto.getQuantidade());
    }

}

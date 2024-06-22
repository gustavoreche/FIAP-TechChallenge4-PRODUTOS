package com.fiap.techchallenge4.unitario;

import com.fiap.techchallenge4.domain.Produto;
import com.fiap.techchallenge4.domain.batch.ProcessadorProduto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class ProcessadorProdutoTest {

    @Test
    public void process_sucesso() throws Exception {
        // preparação
        var processadorProduto = new ProcessadorProduto();

        // execução
        var produto = Produto.builder()
                .ean(7891234560L)
                .nome("Produto Teste")
                .descricao("Descrição do Produto Teste")
                .preco(new BigDecimal("10.0"))
                .quantidade(100)
                .build();
        var produtoEntity = processadorProduto.process(produto);

        // avaliação
        Assertions.assertNotNull(produtoEntity);
        Assertions.assertEquals(7891234560L, produtoEntity.getEan());
        Assertions.assertEquals("Produto Teste", produtoEntity.getNome());
        Assertions.assertEquals("Descrição do Produto Teste", produtoEntity.getDescricao());
        Assertions.assertEquals(new BigDecimal("10.0"), produtoEntity.getPreco());
        Assertions.assertEquals(100, produtoEntity.getQuantidade());
        Assertions.assertNotNull(produtoEntity.getDataDeCriacao());
    }

    @Test
    public void process_error() {
        // preparação
        var processadorProduto = new ProcessadorProduto();

        // execução e avaliação
        Assertions.assertThrows(Exception.class, () -> {
            processadorProduto.process(null);
        });
    }

}

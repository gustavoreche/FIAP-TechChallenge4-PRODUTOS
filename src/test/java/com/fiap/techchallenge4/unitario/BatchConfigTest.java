package com.fiap.techchallenge4.unitario;

import com.fiap.techchallenge4.domain.Produto;
import com.fiap.techchallenge4.infrastructure.batch.BatchConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;

public class BatchConfigTest {

    @Test
    public void reader_sucesso() throws Exception {
        // preparação
        var batchConfig = new BatchConfig();

        // execução
        var itemReader = batchConfig.reader();
        ((FlatFileItemReader<Produto>)itemReader).open(new ExecutionContext());
        var produto = itemReader.read();
        var count = 0;
        do {
            // avaliação
            Assertions.assertNotNull(produto);
            Assertions.assertTrue(produto.getEan() > 0);
            Assertions.assertNotNull(produto.getNome());
            Assertions.assertNotNull(produto.getPreco());
            Assertions.assertTrue(produto.getQuantidade() > 0);
            count++;
            produto = itemReader.read();
        } while (produto != null);

        Assertions.assertEquals(10, count);

    }

}

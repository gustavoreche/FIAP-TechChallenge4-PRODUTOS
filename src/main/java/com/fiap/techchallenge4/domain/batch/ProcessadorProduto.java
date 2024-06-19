package com.fiap.techchallenge4.domain.batch;

import com.fiap.techchallenge4.domain.Produto;
import com.fiap.techchallenge4.infrastructure.model.ProdutoEntity;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;

public class ProcessadorProduto implements ItemProcessor<Produto, ProdutoEntity> {

    @Override
    public ProdutoEntity process(Produto item) throws Exception {
        return ProdutoEntity.builder()
                .ean(item.getEan())
                .nome(item.getNome())
                .descricao(item.getDescricao())
                .preco(item.getPreco())
                .quantidade(item.getQuantidade())
                .dataDeCriacao(LocalDateTime.now())
                .build();
    }
}

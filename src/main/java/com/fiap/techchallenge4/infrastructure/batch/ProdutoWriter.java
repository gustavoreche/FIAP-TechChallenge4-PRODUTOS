package com.fiap.techchallenge4.infrastructure.batch;

import com.fiap.techchallenge4.infrastructure.model.ProdutoEntity;
import com.fiap.techchallenge4.infrastructure.repository.ProdutoRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class ProdutoWriter implements ItemWriter<ProdutoEntity> {

    private final ProdutoRepository repository;

    public ProdutoWriter(final ProdutoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void write(Chunk<? extends ProdutoEntity> chunk) throws Exception {
        chunk.getItems().forEach(produto -> {
            this.repository.findById(produto.getEan())
                    .ifPresentOrElse(produtoNaBase -> {
                        produto.setQuantidade(produto.getQuantidade() + produtoNaBase.getQuantidade());
                        this.repository.save(produto);
                    },
                    () -> this.repository.save(produto)
            );
        });
    }
}

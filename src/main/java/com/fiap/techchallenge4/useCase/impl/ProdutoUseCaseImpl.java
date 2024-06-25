package com.fiap.techchallenge4.useCase.impl;

import com.fiap.techchallenge4.domain.Ean;
import com.fiap.techchallenge4.domain.Produto;
import com.fiap.techchallenge4.infrastructure.controller.dto.AtualizaProdutoDTO;
import com.fiap.techchallenge4.infrastructure.controller.dto.CriaProdutoDTO;
import com.fiap.techchallenge4.infrastructure.controller.dto.ProdutoDTO;
import com.fiap.techchallenge4.infrastructure.model.ProdutoEntity;
import com.fiap.techchallenge4.infrastructure.repository.ProdutoRepository;
import com.fiap.techchallenge4.useCase.ProdutoUseCase;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProdutoUseCaseImpl implements ProdutoUseCase {

    private final ProdutoRepository repository;
    private final JobLauncher jobLauncher;
    private final Job importaProdutosJob;

    public ProdutoUseCaseImpl(final ProdutoRepository repository,
                              final JobLauncher jobLauncher,
                              final Job importaProdutosJob) {
        this.repository = repository;
        this.jobLauncher = jobLauncher;
        this.importaProdutosJob = importaProdutosJob;
    }


    @Override
    public void importa() {
        try {
            final var params = new JobParametersBuilder()
                    .addString("JobID", String.valueOf(System.currentTimeMillis()))
                    .toJobParameters();
            this.jobLauncher.run(this.importaProdutosJob, params);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean cadastra(final CriaProdutoDTO dadosProduto) {
        final var produtoNaBase = this.repository.findById(dadosProduto.ean());
        if(produtoNaBase.isEmpty()) {
            final var produto = new Produto(
                    dadosProduto.ean(),
                    dadosProduto.nome(),
                    dadosProduto.descricao(),
                    dadosProduto.preco(),
                    dadosProduto.quantidade()
            );

            var produtoEntity = new ProdutoEntity(
                    produto.getEan(),
                    produto.getNome(),
                    produto.getDescricao(),
                    produto.getPreco(),
                    produto.getQuantidade(),
                    LocalDateTime.now()
            );

            this.repository.save(produtoEntity);
            return true;
        }
        System.out.println("Produto já cadastrado");
        return false;

    }

    @Override
    public boolean atualiza(final Long ean,
                            final AtualizaProdutoDTO dadosProduto) {
        final var produto = new Produto(
                ean,
                dadosProduto.nome(),
                dadosProduto.descricao(),
                dadosProduto.preco(),
                dadosProduto.quantidade()
        );

        final var produtoNaBase = this.repository.findById(ean);
        if(produtoNaBase.isEmpty()) {
            System.out.println("Produto não está cadastrado");
            return false;
        }

        var produtoEntity = new ProdutoEntity(
                produto.getEan(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getQuantidade() + produtoNaBase.get().getQuantidade(),
                LocalDateTime.now()
        );

        this.repository.save(produtoEntity);
        return true;

    }

    @Override
    public boolean deleta(final Long ean) {
        final var eanObjeto = new Ean(ean);

        final var produtoNaBase = this.repository.findById(eanObjeto.getNumero());
        if(produtoNaBase.isEmpty()) {
            System.out.println("Produto não está cadastrado");
            return false;
        }
        this.repository.deleteById(eanObjeto.getNumero());
        return true;

    }

    @Override
    public ProdutoDTO busca(final Long ean) {
        final var eanObjeto = new Ean(ean);

        final var produtoNaBase = this.repository.findById(eanObjeto.getNumero());
        if(produtoNaBase.isEmpty()) {
            System.out.println("Produto não está cadastrado");
            return null;
        }
        final var produto = produtoNaBase.get();
        return new ProdutoDTO(
                produto.getEan(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getQuantidade(),
                produto.getDataDeCriacao()
        );

    }

}

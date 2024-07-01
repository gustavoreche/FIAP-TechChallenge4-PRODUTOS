package com.fiap.techchallenge4.useCase.impl;

import com.fiap.techchallenge4.domain.Ean;
import com.fiap.techchallenge4.domain.Produto;
import com.fiap.techchallenge4.domain.Quantidade;
import com.fiap.techchallenge4.domain.StatusEstoqueEnum;
import com.fiap.techchallenge4.infrastructure.consumer.response.AtualizaEstoqueDTO;
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
import java.util.Objects;

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
        final var produto = new Produto(
                dadosProduto.ean(),
                dadosProduto.nome(),
                dadosProduto.descricao(),
                dadosProduto.preco(),
                dadosProduto.quantidade()
        );

        final var produtoNaBase = this.repository.findById(dadosProduto.ean());
        if(produtoNaBase.isEmpty()) {
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

        final var produtoNaBase = this.pegaProdutoNaBaseDeDados(eanObjeto.getNumero());
        if(Objects.isNull(produtoNaBase)) {
            System.out.println("Produto não está cadastrado");
            return null;
        }
        return new ProdutoDTO(
                produtoNaBase.getEan(),
                produtoNaBase.getNome(),
                produtoNaBase.getDescricao(),
                produtoNaBase.getPreco(),
                produtoNaBase.getQuantidade(),
                produtoNaBase.getDataDeCriacao()
        );

    }

    @Override
    public Boolean temEstoque(final Long ean,
                              final Long quantidade) {
        final var eanObjeto = new Ean(ean);
        final var quantidadeObjeto = new Quantidade(quantidade);

        final var produto = this.pegaProdutoNaBaseDeDados(eanObjeto.getNumero());
        if(Objects.isNull(produto)) {
            return null;
        }
        return produto.getQuantidade() >= quantidadeObjeto.getNumero();

    }

    private ProdutoEntity pegaProdutoNaBaseDeDados(final Long ean) {
        final var produtoNaBase = this.repository.findById(ean);
        if(produtoNaBase.isEmpty()) {
            System.out.println("Produto não está cadastrado");
            return null;
        }
        return produtoNaBase.get();
    }

    @Override
    public void atualizaEstoque(final AtualizaEstoqueDTO evento) {
        final var eanObjeto = new Ean(evento.ean());
        final var quantidadeObjeto = new Quantidade(evento.quantidade());
        final var status = StatusEstoqueEnum.pegaStatusEnum(evento.statusEstoque());

        final var produto = this.pegaProdutoNaBaseDeDados(eanObjeto.getNumero());
        if(Objects.nonNull(produto)) {
            var novaQuantidade = produto.getQuantidade();

            if(status.equals(StatusEstoqueEnum.RETIRA_DO_ESTOQUE) && produto.getQuantidade() >= quantidadeObjeto.getNumero()) {
                novaQuantidade = produto.getQuantidade() - quantidadeObjeto.getNumero();
                var produtoEntity = new ProdutoEntity(
                        produto.getEan(),
                        produto.getNome(),
                        produto.getDescricao(),
                        produto.getPreco(),
                        novaQuantidade,
                        LocalDateTime.now()
                );

                this.repository.save(produtoEntity);
                return;
            }

            else if (status.equals(StatusEstoqueEnum.VOLTA_PARA_O_ESTOQUE)) {
                novaQuantidade = produto.getQuantidade() + quantidadeObjeto.getNumero();
                var produtoEntity = new ProdutoEntity(
                        produto.getEan(),
                        produto.getNome(),
                        produto.getDescricao(),
                        produto.getPreco(),
                        novaQuantidade,
                        LocalDateTime.now()
                );

                this.repository.save(produtoEntity);
                return;
            }

        }
        System.out.println("Produto não tem estoque suficiente");
        throw new RuntimeException("Produto não tem estoque suficiente");
    }

}

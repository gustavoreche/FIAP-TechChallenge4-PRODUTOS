package com.fiap.techchallenge4.infrastructure.batch;

import com.fiap.techchallenge4.domain.Produto;
import com.fiap.techchallenge4.domain.batch.ProcessadorProduto;
import com.fiap.techchallenge4.infrastructure.model.ProdutoEntity;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfig {

    @Bean
    public Job importaProdutos(JobRepository jobRepository,
                               Step step) {
        return new JobBuilder("importaProdutos", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    public Step step(JobRepository jobRepository,
                     PlatformTransactionManager transactionManager,
                     ItemReader<Produto> reader,
                     ItemProcessor<Produto, ProdutoEntity> processor,
                     ItemWriter<ProdutoEntity> writer) {
        return new StepBuilder("step", jobRepository)
                .<Produto, ProdutoEntity>chunk(15, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public ItemReader<Produto> reader() {
        BeanWrapperFieldSetMapper<Produto> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Produto.class);

        return new FlatFileItemReaderBuilder<Produto>()
                .name("productItemReader")
                .resource(new ClassPathResource("produtos.csv"))
                .delimited()
                .names("ean", "nome", "descricao", "preco", "quantidade")
                .fieldSetMapper(fieldSetMapper)
                .build();
    }

    @Bean
    public ItemProcessor<Produto, ProdutoEntity> processor() {
        return new ProcessadorProduto();
    }

    @Bean
    public ItemWriter<ProdutoEntity> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<ProdutoEntity>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .dataSource(dataSource)
                .sql("INSERT INTO tb_produto " +
                        "(ean, nome, descricao, preco, quantidade, data_de_criacao) " +
                        "VALUES (:ean, :nome, :descricao, :preco, :quantidade, :dataDeCriacao) " +
                        "ON CONFLICT (ean) DO UPDATE SET " +
                        "nome = :nome, " +
                        "descricao = :descricao, " +
                        "preco = :preco, " +
                        "quantidade = tb_produto.quantidade + :quantidade, " +
                        "data_de_criacao = :dataDeCriacao")
                .build();
    }

}

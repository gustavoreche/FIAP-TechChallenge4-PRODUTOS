package com.fiap.techchallenge4.integrados;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge4.infrastructure.controller.dto.CriaProdutoDTO;
import com.fiap.techchallenge4.infrastructure.model.ProdutoEntity;
import com.fiap.techchallenge4.infrastructure.repository.ProdutoRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static com.fiap.techchallenge4.infrastructure.controller.ProdutoController.URL_PRODUTO;
import static com.fiap.techchallenge4.infrastructure.controller.ProdutoController.URL_PRODUTO_IMPORTA;


@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProdutoControllerIT {

    @Autowired
    private MockMvc mockMvc;

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
    public void importa_salvaNaBaseDeDados() throws Exception {

        this.mockMvc
                .perform(MockMvcRequestBuilders.post(URL_PRODUTO_IMPORTA)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk()
                );

        var produtos = this.produtoRepository.findAll();

        Assertions.assertEquals(10, produtos.size());
    }

    @Test
    public void importa_salvaNaBaseDeDados_produtoExistente_adicionaQuantidade() throws Exception {

        this.produtoRepository.save(ProdutoEntity.builder()
                        .ean(2222222222L)
                        .nome("Laranja")
                        .descricao("Fruta")
                        .preco(new BigDecimal("3.00"))
                        .quantidade(10)
                        .dataDeCriacao(LocalDateTime.now())
                .build());

        this.mockMvc
                .perform(MockMvcRequestBuilders.post(URL_PRODUTO_IMPORTA)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk()
                );

        var produtos = this.produtoRepository.findAll();
        var laranja = produtos.stream().filter(produto -> produto.getEan().equals(2222222222L)).findFirst().get();

        Assertions.assertEquals(10, produtos.size());
        Assertions.assertEquals(2222222222L, laranja.getEan());
        Assertions.assertEquals("Laranja", laranja.getNome());
        Assertions.assertEquals("Fruta", laranja.getDescricao());
        Assertions.assertEquals(new BigDecimal("3.00"), laranja.getPreco());
        Assertions.assertEquals(30, laranja.getQuantidade());
        Assertions.assertNotNull(laranja.getDataDeCriacao());
    }

    @Test
    public void importa_salvaNaBaseDeDados_produtoExistente_mudaNome() throws Exception {

        this.produtoRepository.save(ProdutoEntity.builder()
                .ean(2222222222L)
                .nome("Laranja Lima")
                .descricao("Fruta")
                .preco(new BigDecimal("3.00"))
                .quantidade(10)
                .dataDeCriacao(LocalDateTime.now())
                .build());

        this.mockMvc
                .perform(MockMvcRequestBuilders.post(URL_PRODUTO_IMPORTA)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk()
                );

        var produtos = this.produtoRepository.findAll();
        var laranja = produtos.stream().filter(produto -> produto.getEan().equals(2222222222L)).findFirst().get();

        Assertions.assertEquals(10, produtos.size());
        Assertions.assertEquals(2222222222L, laranja.getEan());
        Assertions.assertEquals("Laranja", laranja.getNome());
        Assertions.assertEquals("Fruta", laranja.getDescricao());
        Assertions.assertEquals(new BigDecimal("3.00"), laranja.getPreco());
        Assertions.assertEquals(30, laranja.getQuantidade());
        Assertions.assertNotNull(laranja.getDataDeCriacao());
    }

    @Test
    public void cadastra_deveRetornar201_salvaNaBaseDeDados() throws Exception {

        var request = new CriaProdutoDTO(
                7894900011517L,
                "Produto Teste",
                "Descrição do Produto Teste",
                new BigDecimal("100"),
                100L
        );
        var objectMapper = this.objectMapper
                .writer()
                .withDefaultPrettyPrinter();
        var jsonRequest = objectMapper.writeValueAsString(request);

        this.mockMvc
                .perform(MockMvcRequestBuilders.post(URL_PRODUTO)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isCreated()
                )
                .andReturn();

        var produto = this.produtoRepository.findAll().get(0);

        Assertions.assertEquals(7894900011517L, produto.getEan());
        Assertions.assertEquals("Produto Teste", produto.getNome());
        Assertions.assertEquals("Descrição do Produto Teste", produto.getDescricao());
        Assertions.assertEquals(new BigDecimal("100.00"), produto.getPreco());
        Assertions.assertEquals(100L, produto.getQuantidade());
        Assertions.assertNotNull(produto.getDataDeCriacao());
    }

    @Test
    public void cadastra_deveRetornar409_naoSalvaNaBaseDeDados() throws Exception {

        this.produtoRepository.save(ProdutoEntity.builder()
                .ean(7894900011517L)
                .nome("Produto Teste")
                .descricao("Descrição do Produto Teste")
                .preco(new BigDecimal("100"))
                .quantidade(100L)
                .dataDeCriacao(LocalDateTime.now())
                .build());

        var request = new CriaProdutoDTO(
                7894900011517L,
                "Produto Teste",
                "Descrição do Produto Teste",
                new BigDecimal("100"),
                100L
        );
        var objectMapper = this.objectMapper
                .writer()
                .withDefaultPrettyPrinter();
        var jsonRequest = objectMapper.writeValueAsString(request);

        this.mockMvc
                .perform(MockMvcRequestBuilders.post(URL_PRODUTO)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isConflict()
                )
                .andReturn();

        var produto = this.produtoRepository.findAll().get(0);

        Assertions.assertEquals(1, this.produtoRepository.findAll().size());
        Assertions.assertEquals(7894900011517L, produto.getEan());
        Assertions.assertEquals("Produto Teste", produto.getNome());
        Assertions.assertEquals("Descrição do Produto Teste", produto.getDescricao());
        Assertions.assertEquals(new BigDecimal("100.00"), produto.getPreco());
        Assertions.assertEquals(100L, produto.getQuantidade());
        Assertions.assertNotNull(produto.getDataDeCriacao());
    }

    @ParameterizedTest
    @MethodSource("requestValidandoCampos")
    public void cadastra_camposInvalidos_naoBuscaNaBaseDeDados(Long ean,
                                                               String nome,
                                                               String descricao,
                                                               BigDecimal preco,
                                                               Long quantidade) throws Exception {
        var request = new CriaProdutoDTO(
                ean,
                nome,
                descricao,
                preco,
                quantidade
        );
        var objectMapper = this.objectMapper
                .writer()
                .withDefaultPrettyPrinter();
        var jsonRequest = objectMapper.writeValueAsString(request);

        this.mockMvc
                .perform(MockMvcRequestBuilders.post(URL_PRODUTO)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest()
                );
    }

    private static Stream<Arguments> requestValidandoCampos() {
        return Stream.of(
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
                Arguments.of(123456789L, "Nome de teste", "Descricao teste", BigDecimal.ZERO, 100L),
                Arguments.of(123456789L, "Nome de teste", "Descricao teste", new BigDecimal("-1"), 100L),
                Arguments.of(123456789L, "Nome de teste", "Descricao teste", new BigDecimal("100"), -1L),
                Arguments.of(123456789L, "Nome de teste", "Descricao teste", new BigDecimal("100"), 0L),
                Arguments.of(123456789L, "Nome de teste", "Descricao teste", new BigDecimal("100"), 1001L)
        );
    }

}

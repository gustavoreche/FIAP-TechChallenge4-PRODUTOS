package com.fiap.techchallenge4.integrados;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge4.infrastructure.controller.dto.AtualizaProdutoDTO;
import com.fiap.techchallenge4.infrastructure.controller.dto.CriaProdutoDTO;
import com.fiap.techchallenge4.infrastructure.model.ProdutoEntity;
import com.fiap.techchallenge4.infrastructure.repository.ProdutoRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
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

import static com.fiap.techchallenge4.infrastructure.controller.ProdutoController.*;


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

    @Test
    public void atualiza_deveRetornar200_salvaNaBaseDeDados() throws Exception {

        this.produtoRepository.save(ProdutoEntity.builder()
                .ean(7894900011517L)
                .nome("Produto Teste")
                .descricao("Descrição do Produto Teste")
                .preco(new BigDecimal("100"))
                .quantidade(100L)
                .dataDeCriacao(LocalDateTime.now())
                .build());

        var request = new AtualizaProdutoDTO(
                "Produto Teste",
                "Descrição do Produto Teste",
                new BigDecimal("95"),
                150L
        );
        var objectMapper = this.objectMapper
                .writer()
                .withDefaultPrettyPrinter();
        var jsonRequest = objectMapper.writeValueAsString(request);

        this.mockMvc
                .perform(MockMvcRequestBuilders.put(URL_PRODUTO_COM_EAN.replace("{ean}", "7894900011517"))
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk()
                )
                .andReturn();

        var produto = this.produtoRepository.findAll().get(0);

        Assertions.assertEquals(7894900011517L, produto.getEan());
        Assertions.assertEquals("Produto Teste", produto.getNome());
        Assertions.assertEquals("Descrição do Produto Teste", produto.getDescricao());
        Assertions.assertEquals(new BigDecimal("95.00"), produto.getPreco());
        Assertions.assertEquals(250L, produto.getQuantidade());
        Assertions.assertNotNull(produto.getDataDeCriacao());
    }

    @Test
    public void atualiza_deveRetornar204_naoSalvaNaBaseDeDados() throws Exception {

        var request = new AtualizaProdutoDTO(
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
                .perform(MockMvcRequestBuilders.put(URL_PRODUTO_COM_EAN.replace("{ean}", "7894900011517"))
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNoContent()
                )
                .andReturn();

        Assertions.assertEquals(0, this.produtoRepository.findAll().size());
    }

    @Test
    public void deleta_deveRetornar200_deletaNaBaseDeDados() throws Exception {

        this.produtoRepository.save(ProdutoEntity.builder()
                .ean(7894900011517L)
                .nome("Produto Teste")
                .descricao("Descrição do Produto Teste")
                .preco(new BigDecimal("100"))
                .quantidade(100L)
                .dataDeCriacao(LocalDateTime.now())
                .build());

        this.mockMvc
                .perform(MockMvcRequestBuilders.delete(URL_PRODUTO_COM_EAN.replace("{ean}", "7894900011517"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk()
                )
                .andReturn();

        Assertions.assertEquals(0, this.produtoRepository.findAll().size());
    }

    @Test
    public void deleta_deveRetornar204_naoDeletaNaBaseDeDados() throws Exception {

        this.mockMvc
                .perform(MockMvcRequestBuilders.delete(URL_PRODUTO_COM_EAN.replace("{ean}", "7894900011517"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNoContent()
                )
                .andReturn();

        Assertions.assertEquals(0, this.produtoRepository.findAll().size());
    }

    @Test
    public void busca_deveRetornar200_buscaNaBaseDeDados() throws Exception {

        this.produtoRepository.save(ProdutoEntity.builder()
                .ean(7894900011517L)
                .nome("Produto Teste")
                .descricao("Descricao do Produto Teste")
                .preco(new BigDecimal("100"))
                .quantidade(100L)
                .dataDeCriacao(LocalDateTime.now())
                .build());

        var response = this.mockMvc
                .perform(MockMvcRequestBuilders.get(URL_PRODUTO_COM_EAN.replace("{ean}", "7894900011517"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk()
                )
                .andReturn();
        var responseAppString = response.getResponse().getContentAsString();
        var responseApp = this.objectMapper
                .readValue(responseAppString, new TypeReference<ProdutoEntity>() {});

        var produto = this.produtoRepository.findAll().get(0);

        Assertions.assertEquals(1, this.produtoRepository.findAll().size());
        Assertions.assertEquals(responseApp.getEan(), produto.getEan());
        Assertions.assertEquals(responseApp.getNome(), produto.getNome());
        Assertions.assertEquals(responseApp.getDescricao(), produto.getDescricao());
        Assertions.assertEquals(responseApp.getPreco(), produto.getPreco());
        Assertions.assertEquals(responseApp.getQuantidade(), produto.getQuantidade());
        Assertions.assertEquals(responseApp.getDataDeCriacao(), produto.getDataDeCriacao());
    }

    @Test
    public void busca_deveRetornar204_naoEcontraNaBaseDeDados() throws Exception {

        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URL_PRODUTO_COM_EAN.replace("{ean}", "7894900011517"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNoContent()
                )
                .andReturn();

        Assertions.assertEquals(0, this.produtoRepository.findAll().size());
    }

    @Test
    public void temEstoque_deveRetornar200_quantidadeIgual_buscaNaBaseDeDados() throws Exception {

        this.produtoRepository.save(ProdutoEntity.builder()
                .ean(7894900011517L)
                .nome("Produto Teste")
                .descricao("Descricao do Produto Teste")
                .preco(new BigDecimal("100"))
                .quantidade(100L)
                .dataDeCriacao(LocalDateTime.now())
                .build());

        var response = this.mockMvc
                .perform(MockMvcRequestBuilders.get(URL_PRODUTO_COM_EAN_E_QUANTIDADE
                                .replace("{ean}", "7894900011517")
                                .replace("{quantidade}", "100")
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk()
                )
                .andReturn();
        var responseAppString = response.getResponse().getContentAsString();
        var responseApp = this.objectMapper
                .readValue(responseAppString, new TypeReference<Boolean>() {});

        Assertions.assertEquals(1, this.produtoRepository.findAll().size());
        Assertions.assertTrue(responseApp);
    }

    @Test
    public void temEstoque_deveRetornar200_quantidadeMenor_buscaNaBaseDeDados() throws Exception {

        this.produtoRepository.save(ProdutoEntity.builder()
                .ean(7894900011517L)
                .nome("Produto Teste")
                .descricao("Descricao do Produto Teste")
                .preco(new BigDecimal("100"))
                .quantidade(100L)
                .dataDeCriacao(LocalDateTime.now())
                .build());

        var response = this.mockMvc
                .perform(MockMvcRequestBuilders.get(URL_PRODUTO_COM_EAN_E_QUANTIDADE
                                .replace("{ean}", "7894900011517")
                                .replace("{quantidade}", "99")
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk()
                )
                .andReturn();
        var responseAppString = response.getResponse().getContentAsString();
        var responseApp = this.objectMapper
                .readValue(responseAppString, new TypeReference<Boolean>() {});

        Assertions.assertEquals(1, this.produtoRepository.findAll().size());
        Assertions.assertTrue(responseApp);
    }

    @Test
    public void temEstoque_deveRetornar200_quantidadeMaior_buscaNaBaseDeDados() throws Exception {

        this.produtoRepository.save(ProdutoEntity.builder()
                .ean(7894900011517L)
                .nome("Produto Teste")
                .descricao("Descricao do Produto Teste")
                .preco(new BigDecimal("100"))
                .quantidade(100L)
                .dataDeCriacao(LocalDateTime.now())
                .build());

        var response = this.mockMvc
                .perform(MockMvcRequestBuilders.get(URL_PRODUTO_COM_EAN_E_QUANTIDADE
                                .replace("{ean}", "7894900011517")
                                .replace("{quantidade}", "101")
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk()
                )
                .andReturn();
        var responseAppString = response.getResponse().getContentAsString();
        var responseApp = this.objectMapper
                .readValue(responseAppString, new TypeReference<Boolean>() {});

        Assertions.assertEquals(1, this.produtoRepository.findAll().size());
        Assertions.assertFalse(responseApp);
    }

    @Test
    public void temEstoque_deveRetornar204_naoEncontraNaBaseDeDados() throws Exception {

        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URL_PRODUTO_COM_EAN_E_QUANTIDADE
                                .replace("{ean}", "7894900011517")
                                .replace("{quantidade}", "100")
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNoContent()
                )
                .andReturn();

        Assertions.assertEquals(0, this.produtoRepository.findAll().size());
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

    @ParameterizedTest
    @MethodSource("requestValidandoCampos")
    public void atualiza_camposInvalidos_naoBuscaNaBaseDeDados(Long ean,
                                                               String nome,
                                                               String descricao,
                                                               BigDecimal preco,
                                                               Long quantidade) throws Exception {
        var request = new AtualizaProdutoDTO(
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
                .perform(MockMvcRequestBuilders.put(URL_PRODUTO_COM_EAN.replace("{ean}", ean.toString()))
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest()
                );
    }

    @ParameterizedTest
    @ValueSource(longs = {
            -1L,
            0
    })
    public void deleta_camposInvalidos_naoDeletaNaBaseDeDados(Long ean) throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.delete(URL_PRODUTO_COM_EAN.replace("{ean}", ean.toString()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest()
                );
    }

    @ParameterizedTest
    @ValueSource(longs = {
            -1L,
            0
    })
    public void busca_camposInvalidos_naoBuscaNaBaseDeDados(Long ean) throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URL_PRODUTO_COM_EAN.replace("{ean}", ean.toString()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest()
                );
    }

    @ParameterizedTest
    @MethodSource("requestValidandoDoisCampos")
    public void temEstoque_camposInvalidos_naoBuscaNaBaseDeDados(Long ean,
                                                                 Long quantidade) throws Exception {

        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URL_PRODUTO_COM_EAN_E_QUANTIDADE
                                .replace("{ean}", ean.toString())
                                .replace("{quantidade}", quantidade.toString())
                        )
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

    private static Stream<Arguments> requestValidandoDoisCampos() {
        return Stream.of(
                Arguments.of(-1L, 100L),
                Arguments.of(0L, 100L),
                Arguments.of(123456789L, -1L),
                Arguments.of(123456789L, 0L),
                Arguments.of(123456789L, 1001L)
        );
    }

}

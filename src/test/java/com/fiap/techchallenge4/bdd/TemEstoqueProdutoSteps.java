package com.fiap.techchallenge4.bdd;

import com.fiap.techchallenge4.infrastructure.controller.dto.CriaProdutoDTO;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static com.fiap.techchallenge4.infrastructure.controller.ProdutoController.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class TemEstoqueProdutoSteps {

    private Response response;
    private Long ean;
    private Long quantidade;

    @Dado("que busco um produto que tem estoque sobrando")
    public void queBuscoUmProdutoQueTemEstoqueSobrando() {
        this.ean = System.currentTimeMillis();
        this.quantidade = 1L;
        final var request = new CriaProdutoDTO(
                this.ean,
                "Produto Teste",
                "Descrição do produto teste",
                new BigDecimal("10.00"),
                100L
        );

        RestAssured.baseURI = "http://localhost:8080";
        this.response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post(URL_PRODUTO);
    }

    @Dado("que busco um produto que tem estoque na quantidade exata")
    public void queBuscoUmProdutoQueTemEstoqueNaQuantidadeExata() {
        this.ean = System.currentTimeMillis();
        this.quantidade = 100L;

        final var request = new CriaProdutoDTO(
                this.ean,
                "Produto Teste",
                "Descrição do produto teste",
                new BigDecimal("10.00"),
                100L
        );

        RestAssured.baseURI = "http://localhost:8080";
        this.response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post(URL_PRODUTO);
    }

    @Dado("que busco um produto que não tem estoque")
    public void queBuscoUmProdutoQueNaoTemEstoque() {
        this.ean = System.currentTimeMillis();
        this.quantidade = 101L;

        final var request = new CriaProdutoDTO(
                this.ean,
                "Produto Teste",
                "Descrição do produto teste",
                new BigDecimal("10.00"),
                100L
        );

        RestAssured.baseURI = "http://localhost:8080";
        this.response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post(URL_PRODUTO);
    }

    @Dado("que verifico um produto nao cadastrado")
    public void queVerificoUmProdutoNaoCadastrado() {
        this.ean = System.currentTimeMillis();
        this.quantidade = 1L;
    }

    @Quando("verifico se tem estoque desse produto")
    public void verificoSeTemEstoqueDesseProduto() {
        RestAssured.baseURI = "http://localhost:8080";
        this.response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(URL_PRODUTO_COM_EAN_E_QUANTIDADE
                        .replace("{ean}", this.ean.toString())
                        .replace("{quantidade}", this.quantidade.toString())
                );
    }

    @Entao("recebo uma resposta que o produto tem estoque")
    public void receboUmaRespostaQueOProdutoTemEstoque() {
        this.response
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(".", equalTo(true))
        ;
    }

    @Entao("recebo uma resposta que o produto não tem estoque")
    public void receboUmaRespostaQueOProdutoNaoTemEstoque() {
        this.response
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(".", equalTo(false))
        ;
    }

    @Entao("recebo uma resposta que o produto nao existe")
    public void receboUmaRespostaQueOProdutoNaoExiste() {
        this.response
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
        ;
    }

}

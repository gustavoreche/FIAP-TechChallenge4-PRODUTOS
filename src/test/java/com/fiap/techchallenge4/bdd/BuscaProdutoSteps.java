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

import static com.fiap.techchallenge4.infrastructure.controller.ProdutoController.URL_PRODUTO;
import static com.fiap.techchallenge4.infrastructure.controller.ProdutoController.URL_PRODUTO_COM_EAN;
import static io.restassured.RestAssured.given;


public class BuscaProdutoSteps {

    private Response response;
    private Long ean;

    @Dado("que busco um produto que ja esta cadastrado")
    public void queBuscoUmProdutoQueJaEstaCadastrado() {
        this.ean = System.currentTimeMillis();
        final var request = new CriaProdutoDTO(
                this.ean,
                "Produto Teste",
                "Descrição do produto teste",
                new BigDecimal("10.00"),
                10L
        );

        RestAssured.baseURI = "http://localhost:8080";
        this.response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post(URL_PRODUTO);
    }

    @Dado("que busco um produto nao cadastrado")
    public void queBuscoUmProdutoNaoCadastrado() {
        this.ean = System.currentTimeMillis();
    }


    @Quando("busco esse produto")
    public void buscoEsseProduto() {
        RestAssured.baseURI = "http://localhost:8080";
        this.response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(URL_PRODUTO_COM_EAN.replace("{ean}", this.ean.toString()));
    }

    @Entao("recebo uma resposta que o produto foi encontrado com sucesso")
    public void receboUmaRespostaQueOProdutoFoiEncontradoComSucesso() {
        this.response
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.OK.value())
        ;
    }

    @Entao("recebo uma resposta que o produto nao foi encontrado")
    public void receboUmaRespostaQueOProdutoNaoFoiEncontrado() {
        this.response
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
        ;
    }

}

package com.fiap.techchallenge4.infrastructure.consumer;

import com.fiap.techchallenge4.infrastructure.consumer.response.BaixaNoEstoqueDTO;
import com.fiap.techchallenge4.useCase.ProdutoUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class ConsumerBaixaNoEstoque {

    private final ProdutoUseCase service;

    public ConsumerBaixaNoEstoque(final ProdutoUseCase service) {
        this.service = service;
    }

    @Bean
    public Consumer<BaixaNoEstoqueDTO> input() {
        return evento -> {
            this.service.baixaNoEstoque(evento);
            System.out.println("Evento consumido com sucesso!");
        };
    }


}

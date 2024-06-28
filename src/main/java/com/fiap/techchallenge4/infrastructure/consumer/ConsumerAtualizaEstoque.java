package com.fiap.techchallenge4.infrastructure.consumer;

import com.fiap.techchallenge4.infrastructure.consumer.response.AtualizaEstoqueDTO;
import com.fiap.techchallenge4.useCase.ProdutoUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class ConsumerAtualizaEstoque {

    private final ProdutoUseCase service;

    public ConsumerAtualizaEstoque(final ProdutoUseCase service) {
        this.service = service;
    }

    @Bean
    public Consumer<AtualizaEstoqueDTO> input() {
        return evento -> {
            this.service.atualizaEstoque(evento);
            System.out.println("Evento consumido com sucesso!");
        };
    }


}

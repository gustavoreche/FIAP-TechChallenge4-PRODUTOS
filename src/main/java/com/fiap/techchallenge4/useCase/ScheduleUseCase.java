package com.fiap.techchallenge4.useCase;

import com.fiap.techchallenge4.useCase.impl.ProdutoUseCaseImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduleUseCase {

    private final ProdutoUseCaseImpl service;

    public ScheduleUseCase(final ProdutoUseCaseImpl service) {
        this.service = service;
    }

    //TODO: Executa de 10 em 10 minutos, no minuto 00. Exemplo: 20:00 / 20:10 / 20:20
    @Scheduled(cron = "0 0/10 * * * ?")
    public void importaProdutos() {
        this.service.importa();
    }

}

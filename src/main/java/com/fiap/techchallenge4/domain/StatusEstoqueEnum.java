package com.fiap.techchallenge4.domain;

import java.util.Arrays;

public enum StatusEstoqueEnum {

    RETIRA_DO_ESTOQUE,
    VOLTA_PARA_O_ESTOQUE
    ;

    public static StatusEstoqueEnum pegaStatusEnum(String value) {
        return Arrays.stream(values())
                .filter(valueEnum -> valueEnum.name().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("VALOR DO STATUS INVÁLIDO!!"));
    }

}

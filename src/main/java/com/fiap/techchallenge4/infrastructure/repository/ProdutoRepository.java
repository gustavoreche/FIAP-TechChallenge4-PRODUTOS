package com.fiap.techchallenge4.infrastructure.repository;

import com.fiap.techchallenge4.infrastructure.model.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Long> {
}

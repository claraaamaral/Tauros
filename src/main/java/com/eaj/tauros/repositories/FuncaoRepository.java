package com.eaj.tauros.repositories;

import com.eaj.tauros.models.Funcao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuncaoRepository extends JpaRepository<Funcao, Integer> {
    Funcao findByDescricao(String descricao);
}
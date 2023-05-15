package com.eaj.tauros.repositories;

import com.eaj.tauros.models.Setor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SetorRepository extends JpaRepository<Setor,Long> {
    List<Setor> findAllByDeleteIsNull();


}

package com.eaj.tauros.repositories;

import com.eaj.tauros.models.Problema;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProblemaRepository extends JpaRepository<Problema, Long> {
    List<Problema> findAllByDeleteIsNull();
    @Query(value = "with problemaFrequente as(select distinct " +
            " p.problema_id as idProblema, " +
            " count(c.chamado_id) as qtdChamados\n" +
            " from " +
            " problema p " +
            "join chamado c on c.problema_id = p.problema_id " +
            "group by p.problema_id  " +
            "order by qtdChamados desc " +
            "limit 5) " +
            " select p.* from problema p " +
            " join problemaFrequente on problemaFrequente.idProblema = p.problema_id ", nativeQuery = true)
   List<Problema> findProblemaFrequente();

    @Query(value = "with problemaFrequente as(select distinct " +
            " p.problema_id as idProblema, " +
            " count(c.chamado_id) as qtdChamados\n" +
            " from " +
            " problema p " +
            "join chamado c on c.problema_id = p.problema_id " +
            "group by p.problema_id  " +
            "order by qtdChamados desc " +
            "limit 5) " +
            " select p.* from problema p " +
            " join problemaFrequente on problemaFrequente.idProblema = p.problema_id " +
            " where p.setor_setor_id = ?1", nativeQuery = true)
    List<Problema> findProblemaFrequenteResponsavelSetor(Long idSetor);
}

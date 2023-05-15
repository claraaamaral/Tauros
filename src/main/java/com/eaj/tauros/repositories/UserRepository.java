package com.eaj.tauros.repositories;


import com.eaj.tauros.models.Setor;
import com.eaj.tauros.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByUserName(String userName);
    List<User> findAllByAtivoTrueAndDeleteIsNull();
    List<User> findAllByAtivoTrueAndDeleteIsNullAndSetor(Setor setor);
    List<User> findAllByDeleteIsNotNull();
    List<User> findAllByDeleteIsNotNullAndSetor(Setor setor);
    List<User> findAllByAtivoFalseAndDeleteIsNull();
    Integer countAllByAtivoFalseAndDeleteIsNull();
    @Query(value = " with usuarioEficiente as (" +
            " select distinct " +
            " usuario.id as id_eficiente," +
            " count(chm.chamado_id) as qtdChamados" +
            " from " +
            " users usuario " +
            " join chamado chm on chm.usuario_atendimento_id = usuario.id " +
            " group by usuario.id " +
            " order by qtdChamados desc " +
            " limit 5) " +
            " select usr.* from users usr " +
            " join usuarioEficiente on usuarioEficiente.id_eficiente = usr.id", nativeQuery = true)
    List<User> findOperadorEficiente();
    @Query(value = " with usuarioEficiente as (" +
            " select distinct " +
            " usuario.id as id_eficiente," +
            " count(chm.chamado_id) as qtdChamados" +
            " from " +
            " users usuario " +
            " join chamado chm on chm.usuario_atendimento_id = usuario.id " +
            " group by usuario.id " +
            " order by qtdChamados desc " +
            " limit 5) " +
            " select usr.* from users usr" +
            " join usuarioEficiente on usuarioEficiente.id_eficiente = usr.id" +
            " where usr.id_setor = ?1", nativeQuery = true)
    List<User> findOperadorEficienteResponsavelSetor(Long idSetor);
}

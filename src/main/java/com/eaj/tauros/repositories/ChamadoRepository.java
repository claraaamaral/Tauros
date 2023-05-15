package com.eaj.tauros.repositories;

import com.eaj.tauros.models.Chamado;
import com.eaj.tauros.models.Setor;
import com.eaj.tauros.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ChamadoRepository extends JpaRepository<Chamado,Long> {
    List<Chamado> findAllByDeleteIsNull();
    List<Chamado> findAllByUsuarioAtendimentoIsNull();
    List<Chamado> findAllByUsuarioAtendimentoAndDeleteIsNullAndStatus(User user, String status);
    List<Chamado> getByUsuarioAberturaAndDeleteIsNullAndStatus(User user,String status);
    Integer countAllByStatusLike(String status);
    Integer countAllByStatusLikeAndSetor(String status, Setor setor);
    Integer countAllByStatusLikeAndUsuarioAtendimento(String status, User user);
    Integer countAllByUsuarioAtendimento(User user);
    List<Chamado> findAllByStatus(String status);
    List<Chamado> findAllByStatusAndUsuarioAtendimento(String status, User user);
    List<Chamado> findAllByStatusAndSetor(String status, Setor setor);
    List<Chamado> findAllByStatusAndSetorAndUsuarioAtendimento(String status, Setor setor, User user);
    Chamado findByUsuarioAberturaAndId(User user, Long id);
}

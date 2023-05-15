package com.eaj.tauros.services;

import com.eaj.tauros.models.Chamado;
import com.eaj.tauros.models.Setor;
import com.eaj.tauros.models.User;
import com.eaj.tauros.repositories.ChamadoRepository;
import com.eaj.tauros.util.Constantes;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Service
public class ChamadoService {

    private final ChamadoRepository repository;

    public ChamadoService(ChamadoRepository repository) {
        this.repository = repository;
    }

    public Chamado insert(Chamado chamado){
        SimpleDateFormat data = new SimpleDateFormat(Constantes.FORMATADATA);
        String date = data.format(new Date());
        chamado.setData(date);
        chamado.setStatus(Constantes.STATUS_ABERTO);
        return repository.save(chamado);
    }

    public void atualizarStatus(Chamado chamado){
        repository.save(chamado);
    }

    public Chamado update(Chamado chamado){
        chamado.setStatus(Constantes.STATUS_ANDAMENTO);
        return repository.save(chamado);
    }


    public Chamado finalizar(Chamado chamado){
        chamado.setStatus(Constantes.STATUS_CONCLUIDO);
        return repository.save(chamado);
    }


    public void delete(Long id){
        Date date = new Date();
        Chamado chamado = repository.getById(id);
        chamado.setDelete(date);
        repository.save(chamado);
    }

    public Chamado findById(Long id){
        return repository.getById(id);
    }

    public Chamado findByUsuarioAndId(User user, Long id){
        return repository.findByUsuarioAberturaAndId(user, id);
    }

    public List<Chamado> getAll(){
        List<Chamado> chamados = repository.findAllByDeleteIsNull();
        chamados.stream().filter(e-> e.getStatus().equals(Constantes.STATUS_ANDAMENTO)).forEach(obj -> {
            try {
                SimpleDateFormat data = new SimpleDateFormat(Constantes.FORMATADATA);
                String date = data.format(new Date());
                Date date1 = new SimpleDateFormat(Constantes.FORMATADATA).parse(date);
                Date date2 = new SimpleDateFormat(Constantes.FORMATADATA).parse(obj.getData());
                long diff = date1.getTime() - date2.getTime();
                TimeUnit time = TimeUnit.DAYS;
                long diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
                if ( (diffrence >= 30) && (!obj.getStatus().equals(Constantes.STATUS_ATRASADO))){
                    obj.setStatus(Constantes.STATUS_ATRASADO);
                    atualizarStatus(obj);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        return  chamados;
    }

    public List<Chamado> chamadosEmAberto(){
        return repository.findAllByUsuarioAtendimentoIsNull();
    }

    public List<Chamado> meusChamados(User user,String status){
        return repository.getByUsuarioAberturaAndDeleteIsNullAndStatus(user,status);
    }

    public List<Chamado> meusAtendimentos(User user, String status){
        return repository.findAllByUsuarioAtendimentoAndDeleteIsNullAndStatus(user, status);
    }

    public Integer totalConcluidos(){
        return repository.countAllByStatusLike(Constantes.STATUS_CONCLUIDO);
    }

    public Integer totalChamadosByStatusAndUser(String status, User user){
        return repository.countAllByStatusLikeAndUsuarioAtendimento(status,user);
    }

    public Integer totalAbertos(){
        return repository.countAllByStatusLike(Constantes.STATUS_ABERTO);
    }

    public Integer totalEmAndamento(){
        return repository.countAllByStatusLike(Constantes.STATUS_ANDAMENTO);
    }

    public Integer totalEmAtraso(){
        return repository.countAllByStatusLike(Constantes.STATUS_ATRASADO);
    }

    public Integer totalChamadosPorSetorEStatus(String status, Setor setor){
        return repository.countAllByStatusLikeAndSetor(status,setor);
    }

    public List<Chamado> findByStatus(String status){
        return repository.findAllByStatus(status);
    }

    public List<Chamado> findByStatusAndSetor(String status, Setor setor){
        return repository.findAllByStatusAndSetor(status,setor);
    }

    public List<Chamado> findByStatusAndSetorAndUserClose(String status, Setor setor, User user){
        return repository.findAllByStatusAndSetorAndUsuarioAtendimento(status,setor, user);
    }

    public List<Chamado> findByStatusAndUser(String status, User user){
        return repository.findAllByStatusAndUsuarioAtendimento(status,user);
    }

}

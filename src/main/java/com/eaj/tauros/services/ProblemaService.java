package com.eaj.tauros.services;

import com.eaj.tauros.models.Problema;
import com.eaj.tauros.repositories.ProblemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class ProblemaService {

    ProblemaRepository repository;

    @Autowired
    private void setProblemaRepository(ProblemaRepository repository){ this.repository = repository; }

    public Problema insert(Problema problema){
        problema.setNome(problema.getNome().toUpperCase(Locale.ROOT));
        return repository.save(problema);
    }

    public void delete(Long id){
        SimpleDateFormat data = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        Problema problema = repository.getById(id);
        problema.setDelete(date);
        repository.save(problema);
    }

    public Problema findById(Long id){
        return repository.getById(id);
    }

    public List<Problema> getAll(){
        return repository.findAllByDeleteIsNull();
    }
}

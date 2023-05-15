package com.eaj.tauros.services;

import com.eaj.tauros.models.Setor;
import com.eaj.tauros.repositories.SetorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class SetorService {


    SetorRepository repository;

    @Autowired
    private void setSetorRepository(SetorRepository repository){ this.repository = repository; }

    public Setor insert(Setor setor){
        setor.setNome(setor.getNome().toUpperCase(Locale.ROOT));
        return repository.save(setor);
    }

    public void delete(Long id){
        SimpleDateFormat data = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        Setor setor = repository.getById(id);
        setor.setDelete(date);
        repository.save(setor);
    }

    public Setor findById(Long id){
        return repository.getById(id);
    }

    public List<Setor> getAll(){
        return repository.findAllByDeleteIsNull();
    }

}

package com.eaj.tauros.services;

import com.eaj.tauros.dtos.OperadorDTO;
import com.eaj.tauros.dtos.RelatorioSetorDTO;
import com.eaj.tauros.models.Problema;
import com.eaj.tauros.models.Setor;
import com.eaj.tauros.models.User;
import com.eaj.tauros.repositories.ChamadoRepository;
import com.eaj.tauros.repositories.ProblemaRepository;
import com.eaj.tauros.repositories.SetorRepository;
import com.eaj.tauros.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.eaj.tauros.util.Constantes.*;

@Service
public class RelatoriosService {

    private final ProblemaRepository problemaRepository;
    private final UserRepository userRepository;
    private final SetorRepository setorRepository;
    private final ChamadoRepository chamadoRepository;

    public RelatoriosService(ProblemaRepository problemaRepository,
                             UserRepository userRepository,
                             SetorRepository setorRepository,
                             ChamadoRepository chamadoRepository) {
        this.problemaRepository = problemaRepository;
        this.userRepository = userRepository;
        this.setorRepository = setorRepository;
        this.chamadoRepository = chamadoRepository;
    }

    public RelatorioSetorDTO relatorioSetorResponsavel(Setor setor){
        List<User> operadoresEficientes = userRepository.findOperadorEficienteResponsavelSetor(setor.getId());
        List<Problema> problemasRecorrente = problemaRepository.findProblemaFrequenteResponsavelSetor(setor.getId());
        RelatorioSetorDTO relatorioSetorDTO = new RelatorioSetorDTO();
        List<OperadorDTO> operadores = new ArrayList<>();
        operadoresEficientes.forEach(op -> {
            OperadorDTO operadorDTO = new OperadorDTO();
            operadorDTO.setNome(op.getNome());
            operadorDTO.setQtdChamados(chamadoRepository.countAllByUsuarioAtendimento(op));
            operadores.add(operadorDTO);
        });
        relatorioSetorDTO.setOperadores(operadores);
        relatorioSetorDTO.setProblemas(problemasRecorrente);
        return relatorioSetorDTO;
    }

}

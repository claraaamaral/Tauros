package com.eaj.tauros.controllers;


import com.eaj.tauros.dtos.OperadorDTO;
import com.eaj.tauros.dtos.RelatorioSetorDTO;
import com.eaj.tauros.models.Problema;
import com.eaj.tauros.models.Setor;
import com.eaj.tauros.models.User;
import com.eaj.tauros.repositories.SetorRepository;
import com.eaj.tauros.services.RelatoriosService;
import com.eaj.tauros.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
public class RelatorioController {

    @Autowired
    private final RelatoriosService relatoriosService;

    private final UserService userService;
    private final SetorRepository setorRepository;

    public RelatorioController(RelatoriosService relatoriosService, UserService userService, SetorRepository setorRepository) {
        this.relatoriosService = relatoriosService;
        this.userService = userService;
        this.setorRepository = setorRepository;
    }

    @GetMapping(value = "/relatorio/setor/responsavel")
    public ModelAndView relatorioResponsavel(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        Integer count = userService.countAllPendentes();
        modelAndView.addObject("count", count);
        modelAndView.addObject("usuario2", user);
        RelatorioSetorDTO relatorio = relatoriosService.relatorioSetorResponsavel(user.getSetor());
        List<OperadorDTO> operadores = relatorio.getOperadores();
        List<Problema> problemas = relatorio.getProblemas();
        modelAndView.addObject("operadores", operadores);
        modelAndView.addObject("problemas", problemas);
        modelAndView.setViewName("relatorios/relatorio-setor-responsavel");
        return modelAndView;
    }

    @PostMapping(value = "/relatorio/setor/admin")
    public ModelAndView relatorioAdminPost(Setor setor){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        Integer count = userService.countAllPendentes();
        modelAndView.addObject("count", count);
        modelAndView.addObject("usuario2", user);
        Setor setor1 = new Setor();
        Optional<Setor> setorRequisicao = setorRepository.findById(setor.getId());
        RelatorioSetorDTO relatorio = relatoriosService.relatorioSetorResponsavel(setorRequisicao.get());
        List<OperadorDTO> operadores = relatorio.getOperadores();
        List<Setor> setores = setorRepository.findAllByDeleteIsNull();
        List<Problema> problemas = relatorio.getProblemas();
        modelAndView.addObject("setor", setor1);
        modelAndView.addObject("operadores", operadores);
        modelAndView.addObject("setores", setores);
        modelAndView.addObject("problemas", problemas);
        modelAndView.setViewName("relatorios/relatorio-setor-admin");
        return modelAndView;
    }

    @GetMapping(value = "/relatorio/setor/admin")
    public ModelAndView relatorioAdmin(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        Integer count = userService.countAllPendentes();
        modelAndView.addObject("count", count);
        modelAndView.addObject("usuario2", user);
        Setor setor = new Setor();
        List<Setor> setores = setorRepository.findAllByDeleteIsNull();
        modelAndView.addObject("setor", setor);
        modelAndView.addObject("setores", setores);
        modelAndView.setViewName("relatorios/relatorio-setor-admin");
        return modelAndView;
    }
}

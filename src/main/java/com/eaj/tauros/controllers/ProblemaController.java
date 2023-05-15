package com.eaj.tauros.controllers;


import com.eaj.tauros.models.Problema;
import com.eaj.tauros.models.Setor;
import com.eaj.tauros.models.User;
import com.eaj.tauros.services.ProblemaService;
import com.eaj.tauros.services.SetorService;
import com.eaj.tauros.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ProblemaController {

    @Autowired
    ProblemaService problemaService;

    @Autowired
    SetorService setorService;

    @Autowired
    UserService userService;


    @GetMapping(value = "/problemas")
    public ModelAndView listProblemas(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        Integer count = userService.countAllPendentes();
        modelAndView.addObject("count", count);
        modelAndView.addObject("usuario2", user);
        List<Problema> problemas = problemaService.getAll();
        modelAndView.addObject("problemas", problemas);
        modelAndView.setViewName("problema/problemas");
        return modelAndView;
    }

    @GetMapping(value = "/problema/cadastro")
    public ModelAndView createProblema(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        Integer count = userService.countAllPendentes();
        modelAndView.addObject("count", count);
        modelAndView.addObject("usuario2", user);
        Problema problema = new Problema();
        List<Setor> setores = setorService.getAll();
        modelAndView.addObject("setores", setores);
        modelAndView.addObject("problema", problema);
        modelAndView.setViewName("problema/cadastro-problema");
        return modelAndView;
    }

    @PostMapping(value = "/problema/cadastro")
    public ModelAndView createNewProblema(Problema problema){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer count = userService.countAllPendentes();
        modelAndView.addObject("count", count);
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("usuario2", user);
        if(user.getFuncao().getDescricao().equals("RESPONSAVELSETOR")){
           problema.setSetor(user.getSetor());
        }
        problemaService.insert(problema);
        List<Setor> setores = setorService.getAll();
        modelAndView.addObject("setores", setores);
        modelAndView.addObject("successMessage", "Problema cadastrado com sucesso");
        Problema problema1 = new Problema();
        modelAndView.addObject("problema", problema1);
        modelAndView.setViewName("problema/cadastro-problema");

        return modelAndView;
    }

    @RequestMapping("/problema/deletar/{id}")
    public String doDelete(@PathVariable(name = "id") Long id, RedirectAttributes redirectAttributes){
        problemaService.delete(id);
        redirectAttributes.addAttribute("msg", "Deletado com sucesso");
        return "redirect:/admin/listar/problemas";
    }

    @GetMapping(value = "/problema/editar/{id}")
    public ModelAndView updateProblema(@PathVariable("id") Long id){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        List<Setor> setores = setorService.getAll();
        Integer count = userService.countAllPendentes();
        modelAndView.addObject("count", count);
        modelAndView.addObject("setores", setores);
        modelAndView.addObject("usuario2", user);
        Problema problema = problemaService.findById(id);
        modelAndView.addObject("problema", problema);
        modelAndView.setViewName("problema/atualizar-problema");
        return modelAndView;
    }

    @PostMapping(value = "/problema/editar")
    public String editSave(@ModelAttribute Problema problema, RedirectAttributes redirectAttributes){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        if(user.getFuncao().getDescricao().equals("RESPONSAVELSETOR")){
            problema.setSetor(user.getSetor());
        }
        problemaService.insert(problema);
        redirectAttributes.addAttribute("msg", "Problema atualizado com sucesso");
        return "redirect:/problemas";
    }




}

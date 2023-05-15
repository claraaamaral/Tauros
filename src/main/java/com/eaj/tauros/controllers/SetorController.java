package com.eaj.tauros.controllers;

import com.eaj.tauros.models.Setor;
import com.eaj.tauros.models.User;
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
public class SetorController {

    @Autowired
    SetorService service;

    @Autowired
    UserService userService;

    @GetMapping(value = "/admin/cadastro/setor")
    public ModelAndView createSetor(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("usuario2", user);
        Setor setor = new Setor();
        modelAndView.addObject("setor", setor);
        modelAndView.setViewName("setor/cadastro-setor");
        return modelAndView;
    }

    @PostMapping(value = "/admin/cadastro/setor")
    public ModelAndView createNewSetor(Setor setor){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        modelAndView.addObject("usuario2", user);
        service.insert(setor);
        modelAndView.addObject("successMessage", "Setor cadastrado com sucesso");
        Setor setor1 = new Setor();
        modelAndView.addObject("setor", setor1);
        modelAndView.setViewName("setor/cadastro-setor");

        return modelAndView;
    }

    @RequestMapping("/admin/deletar/setor/{id}")
    public String doDelete(@PathVariable(name = "id") Long id, RedirectAttributes redirectAttributes){
        service.delete(id);
        redirectAttributes.addAttribute("msg", "Deletado com sucesso");
        return "redirect:/admin/listar/setores";
    }

    @GetMapping(value = "/admin/listar/setores")
    public ModelAndView listSetores(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        Integer count = userService.countAllPendentes();
        modelAndView.addObject("count", count);
        modelAndView.addObject("usuario2", user);
        List<Setor> setores = service.getAll();
        modelAndView.addObject("setores", setores);
        modelAndView.setViewName("setor/setores");
        return modelAndView;
    }

    @GetMapping(value = "/admin/editar/setor/{id}")
    public ModelAndView updateSetor(@PathVariable("id") Long id){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        Integer count = userService.countAllPendentes();
        modelAndView.addObject("count", count);
        modelAndView.addObject("usuario2", user);
        Setor setor = service.findById(id);
        modelAndView.addObject("setor", setor);
        modelAndView.setViewName("setor/atualizar-setor");
        return modelAndView;
    }

    @PostMapping(value = "/admin/editar/setor")
    public String editSave(@ModelAttribute Setor setor, RedirectAttributes redirectAttributes){
        service.insert(setor);
        redirectAttributes.addAttribute("msg", "Setor atualizado com sucesso");
        return "redirect:/admin/listar/setores";
    }


}

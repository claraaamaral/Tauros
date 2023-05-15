package com.eaj.tauros.controllers;


import com.eaj.tauros.models.Chamado;
import com.eaj.tauros.models.Setor;
import com.eaj.tauros.models.User;
import com.eaj.tauros.services.ChamadoService;
import com.eaj.tauros.services.SetorService;
import com.eaj.tauros.services.UserService;
import com.eaj.tauros.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ResponsavelPorSetorController {

    @Autowired
    SetorService setorService;

    @Autowired
    UserService userService;

    @Autowired
    ChamadoService chamadoService;

    @GetMapping("/listar/chamados-responsavel-setor")
    public ModelAndView chamadosAdminIndex(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        Setor setor = user.getSetor();
        modelAndView.addObject("usuario2", user);
        Integer totalConluidos = chamadoService.totalChamadosPorSetorEStatus(Constantes.STATUS_CONCLUIDO,setor);
        Integer totalEmAndamento = chamadoService.totalChamadosPorSetorEStatus(Constantes.STATUS_ANDAMENTO,setor);
        Integer totalEmAtraso = chamadoService.totalChamadosPorSetorEStatus(Constantes.STATUS_ATRASADO,setor);
        Integer totalAbertos = chamadoService.totalChamadosPorSetorEStatus(Constantes.STATUS_ABERTO,setor);
        List<Chamado> chamadosEmAndamento = chamadoService.findByStatusAndSetor(Constantes.STATUS_ANDAMENTO,setor);
        List<Chamado> chamadosEmAberto = chamadoService.findByStatusAndSetor(Constantes.STATUS_ABERTO,setor);
        List<Chamado> chamadosConcluidos = chamadoService.findByStatusAndSetor(Constantes.STATUS_CONCLUIDO,setor);
        modelAndView.addObject("chamadosEmAndamento", chamadosEmAndamento);
        modelAndView.addObject("chamadosConcluidos", chamadosConcluidos);
        getModAndViewsForChamados(modelAndView, totalConluidos, totalEmAndamento, totalEmAtraso, totalAbertos, chamadosEmAberto, "chamadosEmAberto");
        modelAndView.setViewName("indexResponsavelSetor");
        return modelAndView;
    }

    private void getModAndViewsForChamados(ModelAndView modelAndView, Integer totalConluidos, Integer totalEmAndamento, Integer totalEmAtraso, Integer totalAbertos, List<Chamado> chamadosEmAberto, String chamadosEmAberto2) {
        modelAndView.addObject(chamadosEmAberto2, chamadosEmAberto);
        modelAndView.addObject("concluidos", totalConluidos);
        modelAndView.addObject("emAndamento", totalEmAndamento);
        modelAndView.addObject("totalAbertos", totalAbertos);
        modelAndView.addObject("totalEmAtraso", totalEmAtraso);
    }

    @GetMapping(value = "/responsavel/listar/usuarios")
    public ModelAndView listUsuarios(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user2 = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("usuario2", user2);
        List<User> userList = userService.getAllBySetor(user2.getSetor());
        Integer count = userService.countAllPendentes();
        modelAndView.addObject("count", count);
        List<User> userListInactives = userService.getAllInactivesBySetor(user2.getSetor());
        modelAndView.addObject("usuarios", userList);
        modelAndView.addObject("usuariosInativos", userListInactives);

        modelAndView.setViewName("usuarios-setor");
        return modelAndView;
    }

    @GetMapping(value = "/responsavel/cadastro/usuario")
    public ModelAndView cadastrarUsuario(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user2 = userService.findUserByUserName(auth.getName());
        Integer count = userService.countAllPendentes();
        modelAndView.addObject("count", count);
        modelAndView.addObject("usuario2", user2);
        User user = new User();
        List<Setor> setores = setorService.getAll();
        modelAndView.addObject("setores", setores);
        modelAndView.addObject("usuario", user);
        modelAndView.setViewName("usuario/cadastro-usuario-responsavel");
        return modelAndView;
    }

    @PostMapping(value = "/responsavel/cadastro/usuario")
    public ModelAndView postForUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user2 = userService.findUserByUserName(auth.getName());
        List<Setor> setores = setorService.getAll();
        modelAndView.addObject("setores", setores);
        modelAndView.addObject("usuario2", user2);
        Integer count = userService.countAllPendentes();
        modelAndView.addObject("count", count);
        Boolean confirm = userService.confirmarSenha(user.getSenha(),user.getConfirmacaoSenha());
        Boolean emailConfirm = userService.findUserByEmail(user.getEmail());
        Boolean userNameConfirm = userService.findUserUsernameBoolean(user.getUserName());
        if(!confirm){
            modelAndView.addObject("senhas","as senhas não coincidem");
            modelAndView.addObject("usuario", user);
            modelAndView.setViewName("usuario/cadastro-usuario");
        } else if(!userNameConfirm){
            modelAndView.addObject("userName","Este nome de usuário já existe");
            modelAndView.addObject("usuario", user);
            modelAndView.setViewName("usuario/cadastro-usuario");
        } else if(!emailConfirm){
            modelAndView.addObject("email","Este email já foi cadastrado");
            modelAndView.addObject("usuario", user);
            modelAndView.setViewName("usuario/cadastro-usuario");
        } else {
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "Usuario cadastrado com sucesso");
            modelAndView.addObject("usuario", new User());
            modelAndView.setViewName("usuario/cadastro-usuario-responsavel");
        }


        return modelAndView;
    }
}

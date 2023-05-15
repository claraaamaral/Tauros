package com.eaj.tauros.controllers;

import com.eaj.tauros.models.Setor;
import com.eaj.tauros.models.User;
import com.eaj.tauros.repositories.FuncaoRepository;
import com.eaj.tauros.services.ChamadoService;
import com.eaj.tauros.services.SetorService;
import com.eaj.tauros.services.UserService;
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
import java.util.Objects;


@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    FuncaoRepository funcaoRepository;

    @Autowired
    SetorService setorService;

    @Autowired
    ChamadoService chamadoService;


    @GetMapping(value={"/", "/login"})
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @GetMapping(value={"/index"})
    public String index(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        if(Boolean.FALSE.equals(user.getAtivo())){
            return "redirect:/logout";
        }
        if(Objects.equals(user.getFuncao().getDescricao(), "ADMIN")){
            return "redirect:/listar/chamados-admin";
        } else if(Objects.equals(user.getFuncao().getDescricao(), "REQUISITANTE")){
          return "redirect:/meus-chamados";
        } else if(Objects.equals(user.getFuncao().getDescricao(), "OPERADOR")){
          return "redirect:/listar/chamados-operador";
        }else if(Objects.equals(user.getFuncao().getDescricao(), "RESPONSAVELSETOR")){
            return "redirect:/listar/chamados-responsavel-setor";
        }else{
          return "redirect:/login";
        }
    }

    @GetMapping(value={"/voltar"})
    public String voltar(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        if(Objects.equals(user.getFuncao().getDescricao(), "ADMIN")){
            return "redirect:/chamados";
        } else if(Objects.equals(user.getFuncao().getDescricao(), "REQUISITANTE")){
            return "redirect:/meus-chamados";
        } else if(Objects.equals(user.getFuncao().getDescricao(), "OPERADOR")){
            return "redirect:/listar/chamados-operador";
        }else if(Objects.equals(user.getFuncao().getDescricao(), "RESPONSAVELSETOR")){
            return "redirect:/listar/chamados-responsavel-setor";
        }else{
            return "redirect:/login";
        }
    }

    @GetMapping(value={"/voltar-usuarios"})
    public String voltarUsuarios(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        if(Objects.equals(user.getFuncao().getDescricao(), "ADMIN")){
            return "redirect:/admin/listar/usuarios";
        } else if(Objects.equals(user.getFuncao().getDescricao(), "RESPONSAVELSETOR")){
            return "redirect:/responsavel/listar/usuarios";
        } else{
            return "redirect:/logout";
        }
    }

    @GetMapping(value="/admin/cadastro/usuario")
    public ModelAndView createUser(){
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
        modelAndView.setViewName("usuario/cadastro-usuario");
        return modelAndView;
    }

    @PostMapping(value = "/admin/cadastro/usuario")
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
                modelAndView.setViewName("usuario/cadastro-usuario");
            }


        return modelAndView;
    }


    @GetMapping(value = "/admin/listar/usuarios")
    public ModelAndView listUsuarios(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user2 = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("usuario2", user2);
        List<User> userList = userService.getAll();
        Integer count = userService.countAllPendentes();
        modelAndView.addObject("count", count);
        List<User> userListInactives = userService.getAllInactives();
        modelAndView.addObject("usuarios", userList);
        modelAndView.addObject("usuariosInativos", userListInactives);

        modelAndView.setViewName("usuario/usuarios");
        return modelAndView;
    }

    @GetMapping(value = "/editar/usuario/{id}")
    public ModelAndView updateUser(@PathVariable("id") Long id){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Setor> setores = setorService.getAll();
        Integer count = userService.countAllPendentes();
        modelAndView.addObject("count", count);
        modelAndView.addObject("setores", setores);
        User user2 = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("usuario2", user2);
        User user = userService.findById(id);
        modelAndView.addObject("usuario", user);
        modelAndView.setViewName("usuario/atualizar-usuario");
        return modelAndView;
    }


    @GetMapping(value = "/editar/usuario")
    public ModelAndView updateUser(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user2 = userService.findUserByUserName(auth.getName());
        List<Setor> setores = setorService.getAll();
        modelAndView.addObject("setores", setores);
        modelAndView.addObject("usuario2", user2);
        modelAndView.addObject("usuario", user2);
        modelAndView.setViewName("usuario/atualizar-usuario");
        return modelAndView;
    }

    @PostMapping(value = "/editar/usuario")
    public ModelAndView editSaver(User user){

        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user2 = userService.findUserByUserName(auth.getName());
        List<Setor> setores = setorService.getAll();
        modelAndView.addObject("setores", setores);
        modelAndView.addObject("usuario2", user2);
        modelAndView.addObject("usuario", user2);
        Boolean confirm = userService.confirmarSenha(user.getSenha(),user.getConfirmacaoSenha());

        if(Boolean.FALSE.equals(confirm)){
            modelAndView.addObject("senhas","as senhas não coincidem");
            modelAndView.addObject("usuario", user);
            modelAndView.setViewName("usuario/atualizar-usuario");
        } else if(user2.getFuncao().getDescricao().equals("ADMIN")) {
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "Usuario atualizado com sucesso");
            modelAndView.addObject("usuario", new User());
            modelAndView.setViewName("usuario/atualizar-usuario");
        }else{
            modelAndView.addObject("successMessage", "Usuario atualizado com sucesso");
            userService.saveUser(user);
            modelAndView.addObject("usuario",user2);
            modelAndView.setViewName("usuario/atualizar-usuario");
        }

        if(user.getSenha()==null){
            modelAndView.addObject("senhas","insira uma senha");
            modelAndView.addObject("usuario", user);
            modelAndView.setViewName("usuario/atualizar-usuario");
        }

        return modelAndView;
    }

    @GetMapping(value = "/alterar/usuario/{id}")
    public ModelAndView alterar(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Setor> setores = setorService.getAll();
        Integer count = userService.countAllPendentes();
        modelAndView.addObject("count", count);
        modelAndView.addObject("setores", setores);
        User user2 = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("usuario2", user2);
        User user = userService.findById(id);
        modelAndView.addObject("usuario", user);
        modelAndView.setViewName("usuario/alterar-usuario");
        return modelAndView;
    }

    @PostMapping(value = "/alterar/usuario")
    public ModelAndView alterarSave(User user){

        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user2 = userService.findUserByUserName(auth.getName());
        List<Setor> setores = setorService.getAll();
        modelAndView.addObject("setores", setores);
        modelAndView.addObject("usuario2", user2);
        modelAndView.addObject("usuario", user);
        modelAndView.addObject("successMessage", "Usuario atualizado com sucesso");
        userService.updateUser(user);
        modelAndView.addObject("usuario",user);
        modelAndView.setViewName("usuario/alterar-usuario");

        return modelAndView;
    }

    // devido um erro de requisição após cadastrar um usuário, foi criada essa rota aqui temporariamente até achar uma solução eficaz


    @GetMapping(value="/cadastro/usuario")
    public ModelAndView createUserPublic(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        List<Setor> setores = setorService.getAll();
        modelAndView.addObject("setores", setores);
        modelAndView.addObject("usuario", user);
        modelAndView.setViewName("cadastro-usuario-public");
        return modelAndView;
    }

    @PostMapping(value = "/cadastro/usuario")
    public String postForUserPublic(@Valid User user, RedirectAttributes redirectAttributes ) {

        Boolean confirm = userService.confirmarSenha(user.getSenha(),user.getConfirmacaoSenha());
        Boolean emailConfirm = userService.findUserByEmail(user.getEmail());
        Boolean userNameConfirm = userService.findUserUsernameBoolean(user.getUserName());

        if(Boolean.FALSE.equals(confirm)){
            redirectAttributes.addAttribute("usuario", user);
            redirectAttributes.addAttribute("senhas", "Senhas não coincidem");
            return "redirect:/cadastro/usuario";
        } else if(Boolean.FALSE.equals(userNameConfirm)){
            redirectAttributes.addAttribute("usuario", user);
            redirectAttributes.addAttribute("userName", "Este nome de usuário já existe.");
            return "redirect:/cadastro/usuario";
        } else if(Boolean.FALSE.equals(emailConfirm)){
            redirectAttributes.addAttribute("usuario", user);
            redirectAttributes.addAttribute("email", "Email já foi cadastrado");
            return "redirect:/cadastro/usuario";
        } else {
            userService.saveUserPublic(user);
            redirectAttributes.addAttribute("msg", "Usuário Cadastrado com sucesso!");
            return "redirect:/cadastro/usuario";
        }

    }


    @GetMapping(value = "/admin/listar/usuarios-pendentes")
    public ModelAndView listUsuariosPendentes(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user2 = userService.findUserByUserName(auth.getName());
        Integer count = userService.countAllPendentes();
        modelAndView.addObject("count", count);
        modelAndView.addObject("usuario2", user2);
        List<User> userList = userService.getAllPendentes();
        modelAndView.addObject("usuarios", userList);

        modelAndView.setViewName("usuario/usuarios-pendentes");
        return modelAndView;
    }

    @GetMapping(value={"/admin/ativar/usuario/{id}"})
    public String ativarUsuario(@PathVariable(name = "id") Long id, RedirectAttributes redirectAttributes){
        userService.ativarUsuario(id);
        redirectAttributes.addAttribute("msg", "Usuário habilitado com sucesso!");
        return "redirect:/admin/listar/usuarios-pendentes";
    }

    @GetMapping(value={"/admin/ativar/usuario-inativo/{id}"})
    public String ativarUsuarioInativo(@PathVariable(name = "id") Long id, RedirectAttributes redirectAttributes){
        userService.ativarUsuarioInativo(id);
        redirectAttributes.addAttribute("msg", "Usuário habilitado com sucesso!");
        return "redirect:/admin/listar/usuarios";
    }

    @GetMapping(value={"/deletar/usuario/{id}"})
    public String desabilitarUsuario(@PathVariable(name = "id") Long id, RedirectAttributes redirectAttributes){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        userService.deletar(id);
        if(Objects.equals(user.getFuncao().getDescricao(), "ADMIN")){
            redirectAttributes.addAttribute("msg", "Usuário desabilitado com sucesso!");
            return "redirect:/admin/listar/usuarios";
        } else if(Objects.equals(user.getFuncao().getDescricao(), "RESPONSAVELSETOR")){
            redirectAttributes.addAttribute("msg", "Usuário desabilitado com sucesso!");
            return "redirect:/responsavel/listar/usuarios";
        }
        return "redirect:/index";
    }

    @GetMapping(value={"/admin/deletar/usuario/{id}"})
    public String dodeleteUser(@PathVariable(name = "id") Long id, RedirectAttributes redirectAttributes){
        userService.deletarPermanente(id);
        redirectAttributes.addAttribute("msg", "Usuário deletado com sucesso!");
        return "redirect:/admin/listar/usuarios";
    }
}

package com.eaj.tauros.controllers;

import com.eaj.tauros.models.Chamado;
import com.eaj.tauros.models.Problema;
import com.eaj.tauros.models.Setor;
import com.eaj.tauros.models.User;
import com.eaj.tauros.services.ChamadoService;
import com.eaj.tauros.services.ProblemaService;
import com.eaj.tauros.services.SetorService;
import com.eaj.tauros.services.UserService;
import com.eaj.tauros.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.Objects;

@Controller
public class ChamadoController {

        @Autowired
        ProblemaService problemaService;

        @Autowired
        SetorService setorService;

        @Autowired
        UserService userService;

        @Autowired
        ChamadoService chamadoService;



    @GetMapping(value = "/listar/chamados")
    public ModelAndView listarChamados(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("usuario2", user);
        List<Chamado> chamados = chamadoService.getAll();
        modelAndView.addObject("chamados", chamados);
        modelAndView.setViewName("chamado/chamados");
        return modelAndView;
    }

    @GetMapping("/meus-chamados")
    public ModelAndView chamadosRequisitante(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        List<Chamado> chamadosAbertos = chamadoService.meusChamados(user, Constantes.STATUS_ABERTO);
        List<Chamado> chamadosEmAndamento = chamadoService.meusChamados(user,Constantes.STATUS_ANDAMENTO);
        List<Chamado> chamadosAtrasados = chamadoService.meusChamados(user,Constantes.STATUS_ATRASADO);
        List<Chamado> chamadosConcluidos = chamadoService.meusChamados(user,Constantes.STATUS_CONCLUIDO);
        modelAndView.addObject("chamadosAbertos", chamadosAbertos);
        modelAndView.addObject("chamadosEmAndamento", chamadosEmAndamento);
        modelAndView.addObject("chamadosConcluidos", chamadosConcluidos);
        modelAndView.addObject("chamadosAtrasados", chamadosAtrasados);
        if(user != null){
            modelAndView.addObject("usuario2", user);
            modelAndView.setViewName("index");

        }else{
            modelAndView.addObject("userName", "Nenhum usuário logado no sistema");
            modelAndView.setViewName("login");
        }

        return modelAndView;
    }

    @GetMapping("/listar/chamados-admin")
    public ModelAndView chamadosAdminIndex(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("usuario2", user);
        Integer totalConluidos = chamadoService.totalConcluidos();
        Integer totalEmAndamento = chamadoService.totalEmAndamento();
        Integer totalEmAtraso = chamadoService.totalEmAtraso();
        Integer totalAbertos = chamadoService.totalAbertos();
        Integer count = userService.countAllPendentes();
        modelAndView.addObject("count", count);
        List<Chamado> chamadosEmAndamento = chamadoService.findByStatus(Constantes.STATUS_ANDAMENTO);
        List<Chamado> chamadosEmAberto = chamadoService.findByStatus(Constantes.STATUS_ABERTO);
        List<Chamado> chamadosConcluidos = chamadoService.findByStatus(Constantes.STATUS_CONCLUIDO);
        modelAndView.addObject("chamadosEmAndamento", chamadosEmAndamento);
        modelAndView.addObject("chamadosConcluidos", chamadosConcluidos);
        getModAndViewsForChamados(modelAndView, totalConluidos, totalEmAndamento, totalEmAtraso, totalAbertos, chamadosEmAberto, "chamadosEmAberto");
        modelAndView.setViewName("indexAdmin");
        return modelAndView;
    }

    private void getModAndViewsForChamados(ModelAndView modelAndView, Integer totalConluidos, Integer totalEmAndamento, Integer totalEmAtraso, Integer totalAbertos, List<Chamado> chamadosEmAberto, String chamadosEmAberto2) {
        modelAndView.addObject(chamadosEmAberto2, chamadosEmAberto);
        modelAndView.addObject("concluidos", totalConluidos);
        modelAndView.addObject("emAndamento", totalEmAndamento);
        modelAndView.addObject("totalAbertos", totalAbertos);
        modelAndView.addObject("totalEmAtraso", totalEmAtraso);
    }

    @GetMapping("/listar/chamados-operador")
    public ModelAndView chamadosOperadorIndex(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("usuario2", user);
        Integer totalConluidos = chamadoService.totalChamadosByStatusAndUser(Constantes.STATUS_CONCLUIDO, user);
        Integer totalEmAndamento = chamadoService.totalChamadosByStatusAndUser(Constantes.STATUS_ANDAMENTO, user);
        Integer totalEmAtraso = chamadoService.totalChamadosByStatusAndUser(Constantes.STATUS_ATRASADO, user);
        Integer totalAbertos = chamadoService.totalChamadosPorSetorEStatus(Constantes.STATUS_ABERTO,user.getSetor());
        List<Chamado> chamadosEmAndamento = chamadoService.findByStatusAndUser(Constantes.STATUS_ANDAMENTO, user);
        List<Chamado> chamadosEmAberto = chamadoService.findByStatusAndSetor(Constantes.STATUS_ABERTO, user.getSetor());
        List<Chamado> chamadosEmAtraso = chamadoService.findByStatusAndSetorAndUserClose(Constantes.STATUS_ATRASADO, user.getSetor(), user);
        List<Chamado> chamadosConcluidos= chamadoService.findByStatusAndSetorAndUserClose(Constantes.STATUS_CONCLUIDO, user.getSetor(), user);

        modelAndView.addObject("totalConluidos", totalConluidos);
        modelAndView.addObject("totalEmAtraso", totalEmAtraso);
        modelAndView.addObject("totalAbertos", totalAbertos);
        modelAndView.addObject("totalEmAndamento", totalEmAndamento);
        modelAndView.addObject("chamadosEmAndamento", chamadosEmAndamento);
        modelAndView.addObject("chamadosConcluidos", chamadosConcluidos);
        modelAndView.addObject("chamadosEmAberto", chamadosEmAberto);
        modelAndView.addObject("chamadosEmAtraso",chamadosEmAtraso);
        modelAndView.setViewName("indexOperador");
        return modelAndView;
    }


    @GetMapping(value = "/chamados")
    public ModelAndView chamadosAdmin(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("usuario2", user);
        Integer count = userService.countAllPendentes();
        modelAndView.addObject("count", count);
        Integer totalConluidos = chamadoService.totalConcluidos();
        Integer totalEmAndamento = chamadoService.totalEmAndamento();
        Integer totalEmAtraso = chamadoService.totalEmAtraso();
        Integer totalAbertos = chamadoService.totalAbertos();
        List<Chamado> chamadosEmAndamento = chamadoService.findByStatus(Constantes.STATUS_ANDAMENTO);
        List<Chamado> chamadosEmAberto = chamadoService.findByStatus(Constantes.STATUS_ABERTO);
        List<Chamado> chamadosEmAtraso = chamadoService.findByStatus(Constantes.STATUS_ATRASADO);
        modelAndView.addObject("chamadosEmAndamento", chamadosEmAndamento);
        modelAndView.addObject("chamadosEmAberto", chamadosEmAberto);
        modelAndView.addObject("chamadosEmAtraso", chamadosEmAberto);
        getModAndViewsForChamados(modelAndView, totalConluidos, totalEmAndamento, totalEmAtraso, totalAbertos, chamadosEmAtraso, "chamadosEmAtraso");
        modelAndView.setViewName("chamado/admin-chamados");
        return modelAndView;
    }


    @GetMapping(value = "/admin/chamados/meus-atendimentos")
    public ModelAndView meusAtendimentos(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        Integer count = userService.countAllPendentes();
        modelAndView.addObject("count", count);
        modelAndView.addObject("usuario2", user);
        List<Chamado> chamadosAndamento = chamadoService.meusAtendimentos(user,Constantes.STATUS_ANDAMENTO);
        List<Chamado> chamadosAtrasados = chamadoService.meusAtendimentos(user,Constantes.STATUS_ATRASADO);
        modelAndView.addObject("chamadosAndamento", chamadosAndamento);
        modelAndView.addObject("chamadosAtrasados", chamadosAtrasados);
        modelAndView.setViewName("chamado/atendimentos");
        return modelAndView;
    }

    @GetMapping(value = "/responsavel/chamados/meus-atendimentos")
    public ModelAndView meusAtendimentosRS(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        Integer count = userService.countAllPendentes();
        modelAndView.addObject("count", count);
        modelAndView.addObject("usuario2", user);
        List<Chamado> chamadosAndamento = chamadoService.meusAtendimentos(user,Constantes.STATUS_ANDAMENTO);
        List<Chamado> chamadosAtrasados = chamadoService.meusAtendimentos(user,Constantes.STATUS_ATRASADO);
        modelAndView.addObject("chamadosAndamento", chamadosAndamento);
        modelAndView.addObject("chamadosAtrasados", chamadosAtrasados);
        modelAndView.setViewName("chamado/atendimentos");
        return modelAndView;
    }



    @GetMapping(value = "/cadastro/chamado")
    public ModelAndView criaChamado(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("usuario2", user);
        Integer count = userService.countAllPendentes();
        modelAndView.addObject("count", count);
        Chamado chamado = new Chamado();
        modelAndView.addObject("chamado",chamado);
        List<Problema> problemas = problemaService.getAll();
        modelAndView.addObject("problemas", problemas);

        modelAndView.setViewName("chamado/cadastro-chamado");

        return modelAndView;
    }

    @GetMapping(value = "/editar/chamado/{id}")
    public ModelAndView updateChamado(@PathVariable("id") Long id){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("usuario2", user);
        List<Setor> setores = setorService.getAll();
        modelAndView.addObject("setores", setores);
        List<Problema> problemas = problemaService.getAll();
        modelAndView.addObject("problemas", problemas);
        if(user.getFuncao().getDescricao().equals("ADMIN")){
            Chamado chamado = chamadoService.findById(id);
            modelAndView.addObject("chamado", chamado);
        }else{
            Chamado chamado = chamadoService.findByUsuarioAndId(user, id);
            if(chamado!=null){
                modelAndView.addObject("chamado", chamado);
            }else{
                Chamado chamado1 = new Chamado();
                modelAndView.addObject("chamado", chamado1);
                modelAndView.addObject("errorMsg", "Você não pode editar esse chamado!");
            }
        }

        modelAndView.setViewName("chamado/editar-chamado");
        return modelAndView;
    }

    @PostMapping(value = "/editar/chamado")
    public ModelAndView createNewSetor(Chamado chamado){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        chamado.setUsuarioAbertura(user);
        modelAndView.addObject("usuario2", user);
        chamadoService.insert(chamado);
        List<Setor> setores = setorService.getAll();
        modelAndView.addObject("setores", setores);

        List<Problema> problemas = problemaService.getAll();
        modelAndView.addObject("problemas", problemas);
        modelAndView.addObject("successMessage", "Chamado atualizado com sucesso");
        Chamado chamado1 = new Chamado();
        modelAndView.addObject("chamado", chamado1);
        modelAndView.setViewName("chamado/editar-chamado");
        return modelAndView;
    }

    @PostMapping(value = "/cadastro/chamado")
    public ModelAndView criarNovoCadastro(Chamado chamado){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("usuario2", user);
        chamado.setUsuarioAbertura(user);
        chamado.setSetor(user.getSetor());
        chamadoService.insert(chamado);
        List<Problema> problemas = problemaService.getAll();
        modelAndView.addObject("problemas", problemas);
        modelAndView.addObject("successMessage", "Chamado cadastrado com sucesso");
        Chamado chamado1 = new Chamado();
        modelAndView.addObject("chamado", chamado1);
        modelAndView.setViewName("chamado/cadastro-chamado");

        return modelAndView;
    }


    @RequestMapping("/atender/chamado/{id}")
    public String atenderChamado(@PathVariable(name = "id") Long id, RedirectAttributes redirectAttributes){
        Chamado chamado = chamadoService.findById(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        chamado.setUsuarioAtendimento(user);
        chamadoService.update(chamado);
        if(Objects.equals(user.getFuncao().getDescricao(), "ADMIN")){
            return "redirect:/chamados";
        }else if(Objects.equals(user.getFuncao().getDescricao(), "RESPONSAVELSETOR")){
            return "redirect:/responsavel/chamados/meus-atendimentos";
        }else{
            return "redirect:/listar/chamados-operador";
        }
    }


    @RequestMapping("/finalizar-atendimento/{id}")
    public String finalizarAtendimento(@PathVariable(name = "id") Long id, @RequestParam(name = "descricao", required = false) String descricaoProblema,
                                       @RequestParam(name = "solucao", required = false) String solucao, RedirectAttributes redirectAttributes){
        Chamado chamado = chamadoService.findById(id);
        chamado.setSolucao(solucao);
        chamado.setDescricaoProblema(descricaoProblema);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        chamadoService.finalizar(chamado);
        if(Objects.equals(user.getFuncao().getDescricao(), "ADMIN")){
            return "redirect:/admin/chamados/meus-atendimentos";
        }else if(Objects.equals(user.getFuncao().getDescricao(), "OPERADOR")){
            return "redirect:/listar/chamados-operador";
        } else{
            return "redirect:/responsavel/chamados/meus-atendimentos";
        }
    }



    @GetMapping(value = "/detalhes/chamado/{id}")
    public ModelAndView detalhesChamado(@PathVariable("id") Long id){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        modelAndView.addObject("usuario2", user);

        Chamado chamado = chamadoService.findById(id);
        modelAndView.addObject("chamado", chamado);

        modelAndView.setViewName("chamado/detalhes-chamado");
        return modelAndView;
    }


    @RequestMapping("/deletar/chamado/{id}")
    public String doDelete(@PathVariable(name = "id") Long id, RedirectAttributes redirectAttributes){
        chamadoService.delete(id);
        redirectAttributes.addAttribute("msg", "Deletado com sucesso");
        return "redirect:/index";
    }


}

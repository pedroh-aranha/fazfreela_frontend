package com.frontfreela.main.controller;

import com.frontfreela.main.model.NotificacaoBean;
import com.frontfreela.main.service.AuthRestClientService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/notificacoes")
public class NotificacaoController {

    private final AuthRestClientService authRestClientService;

    public NotificacaoController(AuthRestClientService authRestClientService) {
        this.authRestClientService = authRestClientService;
    }

    @GetMapping
    public String listarNotificacoes(HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }

        try {
            List<NotificacaoBean> notificacoes = authRestClientService.listarNotificacoes(token);
            model.addAttribute("notificacoes", notificacoes);
            return "notificacoes";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar notificações: " + e.getMessage());
            return "notificacoes";
        }
    }

    @GetMapping("/count")
    @ResponseBody
    public Integer contarNaoLidas(HttpSession session) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return 0;
        }
        try {
            return authRestClientService.contarNotificacoesNaoLidas(token);
        } catch (Exception e) {
            return 0;
        }
    }

    @PostMapping("/{id}/lida")
    public String marcarComoLida(@PathVariable Long id, HttpSession session) {
        String token = (String) session.getAttribute("token");
        if (token != null) {
            try {
                authRestClientService.marcarNotificacaoComoLida(id, token);
            } catch (Exception e) {
                // Ignore errors for now
            }
        }
        return "redirect:/notificacoes";
    }
}

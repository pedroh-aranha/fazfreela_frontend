package com.frontfreela.main.controller;


import com.frontfreela.main.model.UsuarioBean;
import com.frontfreela.main.service.AuthRestClientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.client.HttpClientErrorException;

@Controller
public class PerfilController {

    private final AuthRestClientService authService;

    public PerfilController(AuthRestClientService authService) {
        this.authService = authService;
    }

    @GetMapping("/perfil")
    public String perfil(HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }

        try {
            UsuarioBean usuario = authService.buscarPerfil(token);
            model.addAttribute("usuario", usuario);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatusCode.valueOf(401)) {
                session.invalidate();
                return "redirect:/login";
            }
            model.addAttribute("erro", "Erro ao carregar o perfil.");
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar o perfil.");
        }

        return "perfil";
    }

    @PostMapping("/perfil/atualizar")
    public String atualizarPerfil(@ModelAttribute UsuarioBean usuarioForm, HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }

        try {
            authService.atualizarPerfil(usuarioForm, token);
            return "redirect:/perfil?sucesso=true";
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatusCode.valueOf(401)) {
                session.invalidate();
                return "redirect:/login";
            }
            return "redirect:/perfil?erro=Erro ao atualizar o perfil.";
        } catch (Exception e) {
            return "redirect:/perfil?erro=Erro interno ao atualizar.";
        }
    }
}

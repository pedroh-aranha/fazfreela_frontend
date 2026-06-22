package com.frontfreela.main.controller;

import com.frontfreela.main.model.CandidaturaBean;
import com.frontfreela.main.model.TrabalhoBean;
import com.frontfreela.main.service.AuthRestClientService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.HttpClientErrorException;

@Controller
public class TrabalhoController {

    private final AuthRestClientService authService;

    public TrabalhoController(AuthRestClientService authService) {
        this.authService = authService;
    }

    @GetMapping("/trabalhos")
    public String listarTrabalhos(HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }

        try {
            List<TrabalhoBean> trabalhos = authService.listarTrabalhos(token);
            model.addAttribute("trabalhos", trabalhos);
        } catch (HttpClientErrorException e) {
            // Token expirado ou inválido → logout automático
            if (e.getStatusCode() == HttpStatusCode.valueOf(401)) {
                session.invalidate();
                return "redirect:/login";
            }
            model.addAttribute("erro", "Erro ao carregar os trabalhos. Tente novamente.");
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar os trabalhos. Tente novamente.");
        }

        return "trabalhos";
    }

    @GetMapping("/meus-trabalhos")
    public String meusTrabalhos(HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }

        try {
            List<TrabalhoBean> trabalhosCriados = authService.listarMeusTrabalhos(token);
            List<CandidaturaBean> minhasCandidaturas = authService.listarMinhasCandidaturas(token);
            
            model.addAttribute("trabalhosCriados", trabalhosCriados);
            model.addAttribute("minhasCandidaturas", minhasCandidaturas);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatusCode.valueOf(401)) {
                session.invalidate();
                return "redirect:/login";
            }
            model.addAttribute("erro", "Erro ao carregar seus trabalhos.");
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar seus trabalhos.");
        }

        return "meus-trabalhos";
    }
}

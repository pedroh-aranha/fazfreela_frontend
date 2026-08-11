package com.frontfreela.main.controller;

import com.frontfreela.main.model.AvaliacaoBean;
import com.frontfreela.main.model.CandidaturaBean;
import com.frontfreela.main.model.TrabalhoBean;
import com.frontfreela.main.service.AuthRestClientService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;

@Controller
public class TrabalhoController {

    private final AuthRestClientService authService;

    public TrabalhoController(AuthRestClientService authService) {
        this.authService = authService;
    }

    @GetMapping("/trabalhos")
    public String listarTrabalhos(HttpSession session, Model model,
                                  @RequestParam(value = "erro", required = false) String erro,
                                  @RequestParam(value = "sucesso", required = false) String sucesso) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }

        if (erro != null) {
            model.addAttribute("erroURL", erro);
        }
        if (sucesso != null) {
            model.addAttribute("sucessoURL", sucesso);
        }

        try {
            List<TrabalhoBean> trabalhos = authService.listarTrabalhos(token);
            
            // Oculta trabalhos cuja data já passou
            java.time.LocalDateTime agora = java.time.LocalDateTime.now();
            trabalhos = trabalhos.stream()
                    .filter(t -> t.getDataServico() == null || !t.getDataServico().isBefore(agora))
                    .toList();
            
            try {
                com.frontfreela.main.model.UsuarioBean perfil = authService.buscarPerfil(token);
                if (perfil != null && perfil.getId() != null) {
                    trabalhos = trabalhos.stream()
                            .filter(t -> t.getContratante() == null || !perfil.getId().equals(t.getContratante().getId()))
                            .toList();
                }
            } catch (Exception ex) {
                // ignora o erro e mostra todos
            }
            
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

    @GetMapping("/trabalhos/detalhes/{id}")
    public String detalhesTrabalho(@PathVariable Long id, HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }

        try {
            TrabalhoBean trabalho = authService.buscarTrabalhoPorId(id, token);
            model.addAttribute("trabalho", trabalho);
            return "detalhes-trabalho";
        } catch (Exception e) {
            model.addAttribute("erro", "Não foi possível carregar os detalhes deste trabalho.");
            return "redirect:/trabalhos";
        }
    }

    @GetMapping("/meus-trabalhos")
    public String meusTrabalhos(HttpSession session, Model model,
                                @RequestParam(value = "editado", required = false) String editado,
                                @RequestParam(value = "avaliarTrabalho", required = false) Long avaliarTrabalho,
                                @RequestParam(value = "avaliado", required = false) String avaliado,
                                @RequestParam(value = "erroAvaliacao", required = false) String erroAvaliacao) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }

        if ("true".equals(editado)) {
            model.addAttribute("sucesso", "Anúncio atualizado com sucesso! ✅");
        }
        if ("true".equals(avaliado)) {
            model.addAttribute("sucesso", "Avaliação enviada com sucesso! ⭐");
        }
        if (erroAvaliacao != null) {
            model.addAttribute("erro", erroAvaliacao);
        }
        if (avaliarTrabalho != null) {
            model.addAttribute("avaliarTrabalho", avaliarTrabalho);
            model.addAttribute("sucesso", "Trabalho concluído! Por favor, avalie o profissional.");
        }

        // Flash attributes (sucesso/erro) are automatically merged into model by Spring MVC

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

    @GetMapping("/trabalhos/editar/{id}")
    public String editarTrabalhoForm(@PathVariable Long id, HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }

        try {
            TrabalhoBean trabalho = authService.buscarTrabalhoPorId(id, token);
            model.addAttribute("trabalho", trabalho);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatusCode.valueOf(401)) {
                session.invalidate();
                return "redirect:/login";
            }
            return "redirect:/meus-trabalhos";
        } catch (Exception e) {
            return "redirect:/meus-trabalhos";
        }

        return "editar-trabalho";
    }

    @PostMapping("/trabalhos/editar/{id}")
    public String editarTrabalhoSalvar(@PathVariable Long id,
                                       @ModelAttribute TrabalhoBean trabalho,
                                       HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }

        try {
            authService.atualizarTrabalho(id, trabalho, token);
            return "redirect:/meus-trabalhos?editado=true";
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatusCode.valueOf(401)) {
                session.invalidate();
                return "redirect:/login";
            }
            model.addAttribute("erro", "Erro ao salvar as alterações. Tente novamente.");
            model.addAttribute("trabalho", trabalho);
            return "editar-trabalho";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao salvar as alterações. Tente novamente.");
            model.addAttribute("trabalho", trabalho);
            return "editar-trabalho";
        }
    }

    @PostMapping("/trabalhos/candidatar/{id}")
    public String candidatar(@PathVariable Long id, HttpSession session, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }

        try {
            authService.candidatar(id, token);
            redirectAttributes.addAttribute("sucesso", "Candidatura registrada com sucesso!");
            return "redirect:/trabalhos";
        } catch (org.springframework.web.client.RestClientResponseException e) {
            if (e.getStatusCode() == HttpStatusCode.valueOf(401)) {
                session.invalidate();
                return "redirect:/login";
            }
            String errorMsg = e.getResponseBodyAsString();
            if (errorMsg != null && (errorMsg.contains("Você não pode") || errorMsg.contains("Voce nao pode"))) {
                redirectAttributes.addAttribute("erro", "Você não pode se candidatar ao seu próprio anúncio.");
                return "redirect:/trabalhos";
            } else if (errorMsg != null && errorMsg.contains("recebendo candidaturas")) {
                redirectAttributes.addAttribute("erro", "Este anúncio não está mais recebendo candidaturas.");
                return "redirect:/trabalhos";
            } else if (e.getStatusCode() == HttpStatusCode.valueOf(400)) {
                redirectAttributes.addAttribute("erro", "Não foi possível realizar a candidatura.");
                return "redirect:/trabalhos";
            }
            redirectAttributes.addAttribute("erro", "Erro ao se candidatar.");
            return "redirect:/trabalhos";
        } catch (Exception e) {
            redirectAttributes.addAttribute("erro", "Erro interno ao se candidatar. Tente novamente.");
            return "redirect:/trabalhos";
        }
    }

    @GetMapping("/trabalhos/{id}/candidatos")
    public String verCandidatos(@PathVariable Long id, HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }
        try {
            TrabalhoBean trabalho = authService.buscarTrabalhoPorId(id, token);
            List<CandidaturaBean> candidaturas = authService.listarCandidaturasDoTrabalho(id, token);
            model.addAttribute("trabalho", trabalho);
            model.addAttribute("candidaturas", candidaturas);
            return "candidatos";
        } catch (Exception e) {
            return "redirect:/meus-trabalhos";
        }
    }

    @PostMapping("/trabalhos/{id}/candidatos/{cId}/aprovar")
    public String aprovarCandidato(@PathVariable Long id, @PathVariable Long cId, HttpSession session) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }
        try {
            authService.aprovarCandidatura(cId, id, token);
            return "redirect:/meus-trabalhos";
        } catch (Exception e) {
            return "redirect:/trabalhos/" + id + "/candidatos";
        }
    }

    @PostMapping("/trabalhos/{id}/concluir")
    public String concluirTrabalho(@PathVariable Long id, HttpSession session) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }
        try {
            authService.concluirTrabalho(id, token);
            return "redirect:/meus-trabalhos?avaliarTrabalho=" + id;
        } catch (Exception e) {
            return "redirect:/meus-trabalhos";
        }
    }

    @GetMapping("/trabalhos/novo")
    public String novoTrabalhoForm(HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }
        model.addAttribute("trabalho", new TrabalhoBean());
        return "novo-trabalho";
    }

    @PostMapping("/trabalhos/novo")
    public String novoTrabalhoCriar(@ModelAttribute TrabalhoBean trabalho, HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }
        try {
            // Log para debug caso ocorra algum problema futuro
            System.out.println("Tentando criar trabalho: " + trabalho.getTitulo());

            authService.criarTrabalho(trabalho, token);
            return "redirect:/meus-trabalhos?editado=true";
        } catch (org.springframework.web.client.RestClientResponseException e) {
            // Captura o erro exato retornado pelo back-end para sabermos o motivo real
            String erroDetalhado = e.getResponseBodyAsString();
            System.out.println("Erro do Back-end ao criar trabalho: " + erroDetalhado);

            model.addAttribute("erro", "Erro ao criar o anúncio: " + (erroDetalhado.isEmpty() ? "Verifique os dados." : erroDetalhado));
            model.addAttribute("trabalho", trabalho);
            return "novo-trabalho";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("erro", "Erro interno ao criar o anúncio. Tente novamente.");
            model.addAttribute("trabalho", trabalho);
            return "novo-trabalho";
        }
    }

    @PostMapping("/avaliacoes/criar")
    public String criarAvaliacao(@RequestParam Integer nota,
                                 @RequestParam Long trabalhoId,
                                 HttpSession session) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }
        try {
            AvaliacaoBean avaliacao = new AvaliacaoBean();
            avaliacao.setNota(nota);
            TrabalhoBean trabalho = new TrabalhoBean();
            trabalho.setId(trabalhoId);
            avaliacao.setTrabalho(trabalho);
            authService.criarAvaliacao(avaliacao, token);
            return "redirect:/meus-trabalhos?avaliado=true";
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatusCode.valueOf(401)) {
                session.invalidate();
                return "redirect:/login";
            }
            return "redirect:/meus-trabalhos?erroAvaliacao=Erro ao enviar avaliação.";
        } catch (Exception e) {
            return "redirect:/meus-trabalhos?erroAvaliacao=Erro interno ao avaliar.";
        }
    }

    @PostMapping("/trabalhos/excluir/{id}")
    public String excluirTrabalho(@PathVariable Long id, HttpSession session, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }
        try {
            authService.deletarTrabalho(id, token);
            redirectAttributes.addFlashAttribute("sucesso", "Trabalho excluído com sucesso!");
            return "redirect:/meus-trabalhos";
        } catch (org.springframework.web.client.RestClientResponseException e) {
            if (e.getStatusCode() == HttpStatusCode.valueOf(401)) {
                session.invalidate();
                return "redirect:/login";
            }
            String errorMsg = e.getResponseBodyAsString();
            try {
                if (errorMsg != null && errorMsg.contains("\"message\":\"")) {
                    int start = errorMsg.indexOf("\"message\":\"") + 11;
                    int end = errorMsg.indexOf("\"", start);
                    if (start != -1 && end != -1) {
                        errorMsg = errorMsg.substring(start, end);
                    }
                }
            } catch (Exception ex) {
                // ignorar erro de parsing
            }
            redirectAttributes.addFlashAttribute("erro", "Erro ao excluir: " + errorMsg);
            return "redirect:/meus-trabalhos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro interno ao excluir trabalho.");
            return "redirect:/meus-trabalhos";
        }
    }

    @PostMapping("/candidaturas/cancelar/{id}")
    public String cancelarCandidatura(@PathVariable Long id, HttpSession session, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }
        try {
            authService.deletarCandidatura(id, token);
            redirectAttributes.addFlashAttribute("sucesso", "Candidatura cancelada com sucesso!");
            return "redirect:/meus-trabalhos";
        } catch (org.springframework.web.client.RestClientResponseException e) {
            if (e.getStatusCode() == HttpStatusCode.valueOf(401)) {
                session.invalidate();
                return "redirect:/login";
            }
            String errorMsg = e.getResponseBodyAsString();
            if (errorMsg != null && errorMsg.contains("aprovado")) {
                redirectAttributes.addFlashAttribute("erro", "Você já foi aprovado. Para desistir, use a opção 'Desistir do Trabalho'.");
            } else {
                redirectAttributes.addFlashAttribute("erro", "Erro ao cancelar candidatura: " + errorMsg);
            }
            return "redirect:/meus-trabalhos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro interno ao cancelar candidatura.");
            return "redirect:/meus-trabalhos";
        }
    }

    @PostMapping("/trabalhos/{id}/desistir")
    public String desistirDoTrabalho(@PathVariable Long id, HttpSession session, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }
        try {
            authService.desistirDoTrabalho(id, token);
            redirectAttributes.addFlashAttribute("sucesso", "Desistência registrada. O trabalho voltou para ABERTO e novas candidaturas poderão ser aprovadas.");
            return "redirect:/meus-trabalhos";
        } catch (org.springframework.web.client.RestClientResponseException e) {
            if (e.getStatusCode() == HttpStatusCode.valueOf(401)) {
                session.invalidate();
                return "redirect:/login";
            }
            redirectAttributes.addFlashAttribute("erro", "Erro ao desistir: " + e.getResponseBodyAsString());
            return "redirect:/meus-trabalhos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro interno ao registrar desistência.");
            return "redirect:/meus-trabalhos";
        }
    }
}

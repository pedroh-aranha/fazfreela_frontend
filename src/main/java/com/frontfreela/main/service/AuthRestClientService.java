/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.frontfreela.main.service;

import com.frontfreela.main.model.AvaliacaoBean;
import com.frontfreela.main.model.CandidaturaBean;
import com.frontfreela.main.model.TrabalhoBean;
import com.frontfreela.main.model.NotificacaoBean;
import com.frontfreela.main.model.UserRequestBean;
import com.frontfreela.main.model.UsuarioBean;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AuthRestClientService {

    private final RestClient restClient;

    public AuthRestClientService() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:3333/api")
                .build();
    }

    public String logar(UserRequestBean credenciais) {
        return restClient.post()
                .uri("/auth/logar")
                .body(credenciais)
                .retrieve()
                .body(String.class);
    }

    public String registrar(UsuarioBean user) {
        return restClient.post()
                .uri("/auth/registrar")
                .body(user)
                .retrieve()
                .body(String.class);
    }

    public List<TrabalhoBean> listarTrabalhos(String token) {
        TrabalhoBean[] trabalhos = restClient.get()
                .uri("/trabalhos")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(TrabalhoBean[].class);

        return Arrays.asList(trabalhos);
    }
    
    public void candidatar(Long trabalhoId, String token) {
         restClient.post()
                .uri("/candidaturas/criar/" + trabalhoId)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(String.class);
    }

    public UsuarioBean buscarPerfil(String token) {
        return restClient.get()
                .uri("/usuarios/perfil")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(UsuarioBean.class);
    }

    public UsuarioBean buscarPerfilPublico(Long id, String token) {
        return restClient.get()
                .uri("/usuarios/" + id)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(UsuarioBean.class);
    }

    public List<TrabalhoBean> listarMeusTrabalhos(String token) {
        TrabalhoBean[] trabalhos = restClient.get()
                .uri("/trabalhos/meus")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(TrabalhoBean[].class);
        return trabalhos != null ? Arrays.asList(trabalhos) : List.of();
    }

    public List<CandidaturaBean> listarMinhasCandidaturas(String token) {
        CandidaturaBean[] candidaturas = restClient.get()
                .uri("/candidaturas/minhas")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(CandidaturaBean[].class);
        return candidaturas != null ? Arrays.asList(candidaturas) : List.of();
    }

    public void atualizarPerfil(UsuarioBean user, String token) {
        restClient.put()
                .uri("/usuarios/perfil")
                .header("Authorization", "Bearer " + token)
                .body(user)
                .retrieve()
                .body(String.class);
    }

    public TrabalhoBean buscarTrabalhoPorId(Long id, String token) {
        return restClient.get()
                .uri("/trabalhos/" + id)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(TrabalhoBean.class);
    }

    public void atualizarTrabalho(Long id, TrabalhoBean trabalho, String token) {
        restClient.put()
                .uri("/trabalhos/" + id)
                .header("Authorization", "Bearer " + token)
                .body(trabalho)
                .retrieve()
                .body(String.class);
    }

    public List<CandidaturaBean> listarCandidaturasDoTrabalho(Long trabalhoId, String token) {
        CandidaturaBean[] candidaturas = restClient.get()
                .uri("/candidaturas/trabalho/" + trabalhoId)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(CandidaturaBean[].class);
        return candidaturas != null ? Arrays.asList(candidaturas) : List.of();
    }

    public void aprovarCandidatura(Long candidaturaId, Long trabalhoId, String token) {
        restClient.put()
                .uri("/candidaturas/" + candidaturaId + "/aprovar/" + trabalhoId)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(String.class);
    }

    public void concluirTrabalho(Long trabalhoId, String token) {
        restClient.put()
                .uri("/trabalhos/" + trabalhoId + "/concluir")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(String.class);
    }

    public void criarAvaliacao(AvaliacaoBean avaliacao, String token) {
        restClient.post()
                .uri("/avaliacoes/criar")
                .header("Authorization", "Bearer " + token)
                .body(avaliacao)
                .retrieve()
                .body(String.class);
    }

    public String criarTrabalho(TrabalhoBean trabalho, String token) {
        return restClient.post()
                .uri("/trabalhos/criar")
                .header("Authorization", "Bearer " + token)
                .body(trabalho)
                .retrieve()
                .body(String.class);
    }

    public void deletarTrabalho(Long id, String token) {
        restClient.delete()
                .uri("/trabalhos/" + id)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(String.class);
    }

    public void deletarCandidatura(Long id, String token) {
        restClient.delete()
                .uri("/candidaturas/" + id)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(String.class);
    }

    public void desistirDoTrabalho(Long trabalhoId, String token) {
        restClient.put()
                .uri("/trabalhos/" + trabalhoId + "/desistir")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(String.class);
    }

    public List<NotificacaoBean> listarNotificacoes(String token) {
        NotificacaoBean[] notificacoes = restClient.get()
                .uri("/notificacoes")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(NotificacaoBean[].class);
        return notificacoes != null ? Arrays.asList(notificacoes) : List.of();
    }

    public Integer contarNotificacoesNaoLidas(String token) {
        return restClient.get()
                .uri("/notificacoes/nao-lidas/count")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(Integer.class);
    }

    public void marcarNotificacaoComoLida(Long id, String token) {
        restClient.put()
                .uri("/notificacoes/" + id + "/lida")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(String.class);
    }
}

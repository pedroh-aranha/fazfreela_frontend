/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.frontfreela.main.service;

import com.frontfreela.main.model.CandidaturaBean;
import com.frontfreela.main.model.TrabalhoBean;
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
    
    public void candidatar(CandidaturaBean candidatura, String token) {
         restClient.post()
                .uri("/candidaturas/criar")
                .header("Authorization", "Bearer " + token)
                .body(candidatura)
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
}

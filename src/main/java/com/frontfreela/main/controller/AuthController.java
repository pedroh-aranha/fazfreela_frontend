package com.frontfreela.main.controller;



import com.frontfreela.main.model.UserRequestBean;
import com.frontfreela.main.model.UsuarioBean;
import com.frontfreela.main.service.AuthRestClientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;

@Controller
public class AuthController {

    @Autowired
    private AuthRestClientService restService;

    @GetMapping("/")
    public String home(HttpSession session) {
        if (session.getAttribute("token") != null) {
            return "redirect:/trabalhos";
        }
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        // Não é estritamente necessário se o HTML não usa th:object, mas mantemos 
        // para caso o formulário submeta e retorne com erros.
        model.addAttribute("credenciais", new UserRequestBean());
        return "login";
    }

    @PostMapping("/login")
    public String logar(@ModelAttribute UserRequestBean credenciais, HttpSession session, Model model) {
        try {
            String token = restService.logar(credenciais);
            if (token == null) {
                model.addAttribute("erro", "E-mail ou senha inválidos");
                model.addAttribute("credenciais", credenciais);
                return "login";
            }
            String[] partes = token.split("\\.");
            String payload = new String(java.util.Base64.getUrlDecoder().decode(partes[1]));
            String nome = payload.replaceAll(".*\\\"nome\\\":\\\"([^\\\"]+)\\\".*", "$1");
            String role = payload.replaceAll(".*\\\"role\\\":\\\"([^\\\"]+)\\\".*", "$1");
            session.setAttribute("token", token);
            session.setAttribute("nome", nome);
            session.setAttribute("role", role);
            return "redirect:/trabalhos";

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERRO NO LOGIN: " + e.getMessage());
            model.addAttribute("erro", "E-mail ou senha inválidos");
            model.addAttribute("credenciais", credenciais);
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); 
        return "redirect:/login";
    }

    @GetMapping("/cadastro")
    public String registrar(Model model) {
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String mandarRegistro(@ModelAttribute UsuarioBean user, @RequestParam("confirmarSenha") String confirmarSenha, Model model) {

        // Validação de e-mail
        String emailRegex = "^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$";
        if (user.getEmail() == null || !user.getEmail().matches(emailRegex)) {
            model.addAttribute("erro", "E-mail inválido. Informe um e-mail no formato correto (ex: nome@email.com).");
            preservarCampos(model, user);
            return "cadastro";
        }

        // Validação de senha: mínimo 8 caracteres, pelo menos 1 número e 1 símbolo
        String senhaRegex = "^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':,.<>?/\\\\|`~]).{8,}$";
        if (user.getSenha() == null || !user.getSenha().matches(senhaRegex)) {
            model.addAttribute("erro", "Senha inválida. A senha deve ter no mínimo 8 caracteres, incluindo pelo menos 1 número e 1 símbolo (ex: !@#$).");
            preservarCampos(model, user);
            return "cadastro";
        }

        // Validação de confirmação de senha
        if (!user.getSenha().equals(confirmarSenha)) {
            model.addAttribute("erro", "As senhas não coincidem. Por favor, digite a mesma senha nos dois campos.");
            preservarCampos(model, user);
            return "cadastro";
        }

        try {
            restService.registrar(user);
            return "redirect:/login";
        } catch (HttpClientErrorException e) {
            model.addAttribute("erro", "Não foi possível concluir o cadastro. Verifique os dados ou tente outro e-mail.");
            preservarCampos(model, user);
            return "cadastro";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao cadastrar. Verifique os dados e tente novamente.");
            preservarCampos(model, user);
            return "cadastro";
        }
    }

    /** Repõe os campos do formulário no model para não apagá-los quando houver erro. */
    private void preservarCampos(Model model, UsuarioBean user) {
        model.addAttribute("nome", user.getNome());
        model.addAttribute("email", user.getEmail());
    }
}
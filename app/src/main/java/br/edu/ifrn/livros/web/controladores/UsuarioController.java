package br.edu.ifrn.livros.persistencia.controladores;

import br.edu.ifrn.livros.persistencia.modelo.Usuario;
import br.edu.ifrn.livros.persistencia.repositorio.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/novo")
    public String novoUsuarioForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "formulario-usuario";
    }

    @PostMapping
    public String salvarUsuario(@Valid Usuario usuario, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return "formulario-usuario";
        }
        usuarioRepository.save(usuario);
        attributes.addFlashAttribute("mensagem", "Usuário cadastrado com sucesso!");
        return "redirect:/usuarios";
    }

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "modelo/usuarios";
    }
}

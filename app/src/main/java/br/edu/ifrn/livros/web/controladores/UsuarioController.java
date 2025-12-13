package br.edu.ifrn.livros.web.controladores;

import br.edu.ifrn.livros.persistencia.modelo.Usuario;
import br.edu.ifrn.livros.persistencia.repositorio.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "Usuario/lista-usuario";
    }

    // FORM DE NOVO
    @GetMapping("/novo")
    public String formulario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "Usuario/formulario-usuario";
    }

    // SALVAR NOVO OU EDITADO
    @PostMapping
    public String salvar(@Valid @ModelAttribute("usuario") Usuario usuario,
                         BindingResult result,
                         RedirectAttributes attributes) {

        // valida email duplicado
        Optional<Usuario> existente = usuarioRepository.findByEmail(usuario.getEmail());
        if (existente.isPresent() && !existente.get().getId().equals(usuario.getId())) {
            result.rejectValue("email", "erro.duplicado", "Já existe um usuário com este e-mail");
        }

        if (result.hasErrors()) {
            return "Usuario/formulario-usuario";
        }

        usuarioRepository.save(usuario);
        attributes.addFlashAttribute("mensagem", "Usuário salvo com sucesso!");
        return "redirect:/usuarios";
    }

    // EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes attributes) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);

        if (usuario.isEmpty()) {
            attributes.addFlashAttribute("mensagem", "Usuário não encontrado!");
            return "redirect:/usuarios";
        }

        model.addAttribute("usuario", usuario.get());
        return "Usuario/formulario-usuario";
    }

    // EXCLUIR
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes attributes) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            attributes.addFlashAttribute("mensagem", "Usuário excluído com sucesso!");
        } else {
            attributes.addFlashAttribute("mensagem", "Usuário não encontrado!");
        }

        return "redirect:/usuarios";
    }
}

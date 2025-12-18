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

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public String listar(@RequestParam(value = "busca", required = false) String busca, Model model) {
        List<Usuario> usuarios;

        if (busca != null && !busca.isEmpty()) {
            // Se tiver busca, filtra pelos 3 campos
            usuarios = usuarioRepository.findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCaseOrTelefoneContainingIgnoreCase(busca, busca, busca);
            model.addAttribute("termoBusca", busca); // Devolve o termo para o input não limpar
        } else {
            // Se não, traz tudo
            usuarios = usuarioRepository.findAll();
        }

        model.addAttribute("usuarios", usuarios);
        return "Usuario/lista-usuario";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "Usuario/formulario-usuario";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Usuario usuario, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "Usuario/formulario-usuario";
        }
        
        usuarioRepository.save(usuario);
        attr.addFlashAttribute("mensagem", "Usuário salvo com sucesso!");
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Long id, Model model) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        
        model.addAttribute("usuario", usuario);
        return "Usuario/formulario-usuario";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable("id") Long id, RedirectAttributes attr) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
            
        usuarioRepository.delete(usuario);
        attr.addFlashAttribute("mensagem", "Usuário excluído com sucesso!");
        return "redirect:/usuarios";
    }
}
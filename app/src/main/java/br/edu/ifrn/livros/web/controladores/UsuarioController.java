package br.edu.ifrn.livros.web.controladores;

import br.edu.ifrn.livros.persistencia.modelo.Usuario;
import br.edu.ifrn.livros.persistencia.repositorio.UsuarioRepository;
import br.edu.ifrn.livros.util.FileUploadUtil; // Importe
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils; // Importe
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile; // Importe
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
            usuarios = usuarioRepository.findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCaseOrTelefoneContainingIgnoreCase(busca, busca, busca);
            model.addAttribute("termoBusca", busca);
        } else {
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

    // --- MÉTODO SALVAR ATUALIZADO ---
    @PostMapping("/salvar")
    public String salvar(@Valid Usuario usuario, BindingResult result, 
                         @RequestParam("file") MultipartFile file,
                         RedirectAttributes attr) {
        
        if (result.hasErrors()) {
            return "Usuario/formulario-usuario";
        }
        
        try {
            if (!file.isEmpty()) {
                String nomeArquivo = StringUtils.cleanPath(file.getOriginalFilename());
                usuario.setFoto(nomeArquivo);
                usuarioRepository.save(usuario);
                FileUploadUtil.salvarArquivo("uploads", nomeArquivo, file);
            } else {
                usuarioRepository.save(usuario);
            }
            
            attr.addFlashAttribute("mensagem", "Usuário salvo com sucesso!");
        } catch (Exception e) {
            attr.addFlashAttribute("erro", "Erro ao salvar imagem: " + e.getMessage());
        }
        
        return "redirect:/usuarios";
    }
    // --------------------------------

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
package br.edu.ifrn.livros.web.controladores;

import br.edu.ifrn.livros.persistencia.modelo.GeneroLivro;
import br.edu.ifrn.livros.persistencia.modelo.Livro;
import br.edu.ifrn.livros.persistencia.repositorio.LivroRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/livros")
public class LivroController {

    @Autowired
    private LivroRepository livroRepository;

    @GetMapping
    public String listar(@RequestParam(value = "busca", required = false) String busca,
                         @RequestParam(value = "genero", required = false) GeneroLivro genero,
                         Model model) {
        
        List<Livro> livros;

        // Se tiver busca ou gênero, usa o filtro inteligente
        if ((busca != null && !busca.isEmpty()) || genero != null) {
            livros = livroRepository.buscarComFiltros(busca, genero);
        } else {
            livros = livroRepository.findAll();
        }

        model.addAttribute("livros", livros);
        
        // Dados para manter o filtro preenchido na tela
        model.addAttribute("generos", GeneroLivro.values());
        model.addAttribute("termoBusca", busca);
        model.addAttribute("generoSelecionado", genero);
        
        return "Livro/lista-livro";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("livro", new Livro());
        return "Livro/formulario-livro";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Livro livro, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "Livro/formulario-livro";
        }
        
        livroRepository.save(livro);
        attr.addFlashAttribute("mensagem", "Livro salvo com sucesso!");
        return "redirect:/livros";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Long id, Model model) {
        Livro livro = livroRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        
        model.addAttribute("livro", livro);
        return "Livro/formulario-livro";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable("id") Long id, RedirectAttributes attr) {
        Livro livro = livroRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
            
        livroRepository.delete(livro);
        attr.addFlashAttribute("mensagem", "Livro excluído com sucesso!");
        return "redirect:/livros";
    }
}
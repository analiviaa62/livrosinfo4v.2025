package br.edu.ifrn.livros.web.controladores;

import br.edu.ifrn.livros.persistencia.modelo.Livro;
import br.edu.ifrn.livros.persistencia.modelo.GeneroLivro;
import br.edu.ifrn.livros.persistencia.repositorio.LivroRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Optional;

@Controller
@RequestMapping("/livros")
public class LivroController {

    @Autowired
    private LivroRepository livroRepository;

    // LISTAR TODOS OS LIVROS
    @GetMapping
    public String listar(@RequestParam(required = false) String busca, Model model) {
        if (busca != null && !busca.trim().isEmpty()) {
            model.addAttribute("livros", livroRepository.buscarPorTermo(busca));
            model.addAttribute("termoBusca", busca);
        } else {
            model.addAttribute("livros", livroRepository.findAll());
        }
        model.addAttribute("generos", GeneroLivro.values());
        return "Livro/lista-livro";
    }

    // FORMULÁRIO DE NOVO LIVRO
    @GetMapping("/novo")
    public String formularioNovo(Model model) {
        Livro livro = Livro.builder()
                .quantidadeTotal(1)
                .quantidadeDisponivel(1)
                .quantidadePaginas(100)
                .anoPublicacao(2024)
                .preco(BigDecimal.ZERO)
                .genero(GeneroLivro.OUTRO) // ADICIONE ESTA LINHA
                .build();
        
        model.addAttribute("livro", livro);
        model.addAttribute("generos", GeneroLivro.values());
        return "Livro/formulario-livro";
    }

    // SALVAR NOVO OU EDITAR LIVRO
    @PostMapping
public String salvar(@Valid @ModelAttribute("livro") Livro livro,
                     BindingResult result,
                     RedirectAttributes attributes,
                     Model model) { // REMOVA O @RequestParam String action
    
    // Validação de ISBN único
    if (livro.getIsbn() != null && !livro.getIsbn().trim().isEmpty()) {
        Optional<Livro> livroExistente = livroRepository.findByIsbn(livro.getIsbn());
        if (livroExistente.isPresent() && !livroExistente.get().getId().equals(livro.getId())) {
            result.rejectValue("isbn", "error.livro", "Já existe um livro com este ISBN");
        }
    }

    // Validação de quantidade disponível não maior que total
    if (livro.getQuantidadeDisponivel() > livro.getQuantidadeTotal()) {
        result.rejectValue("quantidadeDisponivel", "error.livro", 
            "Quantidade disponível não pode ser maior que quantidade total");
    }

    if (result.hasErrors()) {
        model.addAttribute("generos", GeneroLivro.values());
        return "Livro/formulario-livro";
    }

    // Se for novo livro, garante que quantidadeDisponivel = quantidadeTotal
    if (livro.getId() == null) {
        livro.setQuantidadeDisponivel(livro.getQuantidadeTotal());
    }

    livroRepository.save(livro);
    
    // REMOVA A REFERÊNCIA AO PARÂMETRO action
    String mensagem = livro.getId() == null ? "Livro cadastrado com sucesso!" : "Livro atualizado com sucesso!";
    attributes.addFlashAttribute("mensagem", mensagem);
    
    return "redirect:/livros";
}

    // EDITAR LIVRO
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes attributes) {
        Optional<Livro> livro = livroRepository.findById(id);
        
        if (livro.isEmpty()) {
            attributes.addFlashAttribute("erro", "Livro não encontrado!");
            return "redirect:/livros";
        }
        
        model.addAttribute("livro", livro.get());
        model.addAttribute("generos", GeneroLivro.values());
        return "Livro/formulario-livro";
    }

    // EXCLUIR LIVRO
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes attributes) {
        if (livroRepository.existsById(id)) {
            livroRepository.deleteById(id);
            attributes.addFlashAttribute("mensagem", "Livro excluído com sucesso!");
        } else {
            attributes.addFlashAttribute("erro", "Livro não encontrado!");
        }
        
        return "redirect:/livros";
    }

    // DETALHES DO LIVRO
    @GetMapping("/detalhes/{id}")
    public String detalhes(@PathVariable Long id, Model model, RedirectAttributes attributes) {
        Optional<Livro> livro = livroRepository.findById(id);
        
        if (livro.isEmpty()) {
            attributes.addFlashAttribute("erro", "Livro não encontrado!");
            return "redirect:/livros";
        }
        
        model.addAttribute("livro", livro.get());
        return "Livro/detalhes-livro";
    }
}
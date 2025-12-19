package br.edu.ifrn.livros.web.controladores;

import br.edu.ifrn.livros.persistencia.modelo.Emprestimo;
import br.edu.ifrn.livros.persistencia.modelo.Livro;
import br.edu.ifrn.livros.persistencia.repositorio.EmprestimoRepository;
import br.edu.ifrn.livros.persistencia.repositorio.LivroRepository;
import br.edu.ifrn.livros.persistencia.repositorio.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/emprestimos")
public class EmprestimoController {

    @Autowired private EmprestimoRepository emprestimoRepository;
    @Autowired private LivroRepository livroRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    @GetMapping
    public String listar(@RequestParam(value = "busca", required = false) String busca,
                         @RequestParam(value = "status", required = false) String status,
                         Model model) {
        
        List<Emprestimo> emprestimos;

        if ((busca != null && !busca.isEmpty()) || (status != null && !status.isEmpty())) {
            emprestimos = emprestimoRepository.buscarComFiltros(busca, status);
        } else {
            emprestimos = emprestimoRepository.findAll();
        }

        model.addAttribute("emprestimos", emprestimos);
        model.addAttribute("hoje", LocalDate.now());
        
        // Mantém os filtros preenchidos na tela
        model.addAttribute("termoBusca", busca);
        model.addAttribute("statusSelecionado", status);
        
        return "Emprestimo/lista-emprestimo";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("emprestimo", new Emprestimo());
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("livros", livroRepository.findAll());
        return "Emprestimo/formulario-emprestimo";
    }

    @PostMapping("/salvar")
    public String salvar(Emprestimo emprestimo, RedirectAttributes attr) {
        Livro livro = livroRepository.findById(emprestimo.getLivro().getId()).orElse(null);
        
        if (livro != null && livro.getQuantidadeDisponivel() > 0) {
            livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() - 1);
            livroRepository.save(livro);

            emprestimoRepository.save(emprestimo);
            attr.addFlashAttribute("mensagem", "Empréstimo realizado com sucesso!");
        } else {
            attr.addFlashAttribute("erro", "Livro não disponível.");
        }
        
        return "redirect:/emprestimos";
    }

    @PostMapping("/{id}/devolver")
    public String devolver(@PathVariable Long id, RedirectAttributes attr) {
        Emprestimo emprestimo = emprestimoRepository.findById(id).orElse(null);

        if (emprestimo != null && emprestimo.getDataDevolucao() == null) {
            emprestimo.setDataDevolucao(LocalDate.now());
            
            Livro livro = emprestimo.getLivro();
            livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() + 1);
            livroRepository.save(livro);
            
            emprestimoRepository.save(emprestimo);
            attr.addFlashAttribute("mensagem", "Livro devolvido com sucesso!");
        }

        return "redirect:/emprestimos";
    }
}
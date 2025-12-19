package br.edu.ifrn.livros.web.controladores;

import br.edu.ifrn.livros.persistencia.repositorio.LivroRepository;
import br.edu.ifrn.livros.persistencia.repositorio.UsuarioRepository;
import br.edu.ifrn.livros.persistencia.repositorio.EmprestimoRepository; // 1. Importar repositório
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmprestimoRepository emprestimoRepository; // 2. Injetar repositório

    @GetMapping("/")
    public String index(Model model) {
        // Busca os totais para mostrar nos cards
        model.addAttribute("totalLivros", livroRepository.count());
        model.addAttribute("totalUsuarios", usuarioRepository.count());
        
        // 3. Adicionar o total de empréstimos ao modelo
        model.addAttribute("totalEmprestimos", emprestimoRepository.count());
        
        return "index";
    }
}
package br.edu.ifrn.livros.web.controladores;

import br.edu.ifrn.livros.persistencia.modelo.GeneroLivro;
import br.edu.ifrn.livros.persistencia.modelo.Livro;
import br.edu.ifrn.livros.persistencia.repositorio.LivroRepository;
import br.edu.ifrn.livros.util.FileUploadUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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

        if ((busca != null && !busca.isEmpty()) || genero != null) {
            livros = livroRepository.buscarComFiltros(busca, genero);
        } else {
            livros = livroRepository.findAll();
        }

        model.addAttribute("livros", livros);
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
    public String salvar(@Valid Livro livro, BindingResult result, 
                         @RequestParam("file") MultipartFile file, 
                         RedirectAttributes attr) {
        
        // Se a validação falhar, volta pro formulário e mostra os erros
        if (result.hasErrors()) {
            return "Livro/formulario-livro";
        }
        
        try {
            // LÓGICA DE CADASTRO NOVO VS EDIÇÃO
            if (livro.getId() != null) {
                // EDIÇÃO: Recupera dados antigos (Data e Imagem se não enviada)
                Livro livroExistente = livroRepository.findById(livro.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Livro inválido"));
                
                livro.setDataCadastro(livroExistente.getDataCadastro());
                
                // Se a imagem nova for vazia, mantém a antiga
                if (file.isEmpty()) {
                    livro.setImagem(livroExistente.getImagem());
                }
            } else {
                // NOVO: Define quantidade disponível igual ao total
                if (livro.getQuantidadeDisponivel() == null) {
                    livro.setQuantidadeDisponivel(livro.getQuantidadeTotal());
                }
            }

            // UPLOAD DA IMAGEM (Se foi enviada)
            if (!file.isEmpty()) {
                String nomeArquivo = StringUtils.cleanPath(file.getOriginalFilename());
                livro.setImagem(nomeArquivo);
                
                String diretorioUpload = "uploads";
                FileUploadUtil.salvarArquivo(diretorioUpload, nomeArquivo, file);
            }
            
            // SALVA NO BANCO
            livroRepository.save(livro);
            attr.addFlashAttribute("mensagem", "Livro salvo com sucesso!");

        } catch (Exception e) {
            attr.addFlashAttribute("erro", "Erro ao salvar: " + e.getMessage());
        }

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
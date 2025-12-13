package br.edu.ifrn.livros.persistencia.repositorio;

import br.edu.ifrn.livros.persistencia.modelo.Livro;
import br.edu.ifrn.livros.persistencia.modelo.GeneroLivro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Long> {
    
    Optional<Livro> findByIsbn(String isbn);
    
    List<Livro> findByTituloContainingIgnoreCase(String titulo);
    
    List<Livro> findByAutorContainingIgnoreCase(String autor);
    
    List<Livro> findByGenero(GeneroLivro genero);
    
    @Query("SELECT l FROM Livro l WHERE l.quantidadeDisponivel > 0")
    List<Livro> findLivrosDisponiveis();
    
    @Query("SELECT l FROM Livro l WHERE LOWER(l.titulo) LIKE LOWER(CONCAT('%', :termo, '%')) " +
           "OR LOWER(l.autor) LIKE LOWER(CONCAT('%', :termo, '%')) " +
           "OR LOWER(l.isbn) LIKE LOWER(CONCAT('%', :termo, '%'))")
    List<Livro> buscarPorTermo(@Param("termo") String termo);
}
package br.edu.ifrn.livros.persistencia.repositorio;

import br.edu.ifrn.livros.persistencia.modelo.GeneroLivro;
import br.edu.ifrn.livros.persistencia.modelo.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

    // Busca inteligente: Filtra por Gênero (se informado) E por Texto (se informado)
    @Query("SELECT l FROM Livro l WHERE " +
           "(:genero IS NULL OR l.genero = :genero) AND " +
           "(:termo IS NULL OR :termo = '' OR " +
           "LOWER(l.titulo) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(l.autor) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "l.isbn LIKE CONCAT('%', :termo, '%'))")
    List<Livro> buscarComFiltros(@Param("termo") String termo, @Param("genero") GeneroLivro genero);
    
}
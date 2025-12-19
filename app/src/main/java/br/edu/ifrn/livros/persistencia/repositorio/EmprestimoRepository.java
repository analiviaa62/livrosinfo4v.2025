package br.edu.ifrn.livros.persistencia.repositorio;

import br.edu.ifrn.livros.persistencia.modelo.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    // Busca Inteligente: Texto (Nome/Livro) + Status (Pendente/Devolvido)
    @Query("SELECT e FROM Emprestimo e WHERE " +
           "(:status IS NULL OR :status = '' OR " +
           " (:status = 'pendente' AND e.dataDevolucao IS NULL) OR " +
           " (:status = 'devolvido' AND e.dataDevolucao IS NOT NULL)) " +
           "AND " +
           "(:termo IS NULL OR :termo = '' OR " +
           " LOWER(e.usuario.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           " LOWER(e.livro.titulo) LIKE LOWER(CONCAT('%', :termo, '%')))")
    List<Emprestimo> buscarComFiltros(@Param("termo") String termo, @Param("status") String status);

}
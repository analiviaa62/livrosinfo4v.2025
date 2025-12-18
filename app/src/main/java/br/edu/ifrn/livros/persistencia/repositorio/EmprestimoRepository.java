package br.edu.ifrn.livros.persistencia.repositorio;

import br.edu.ifrn.livros.persistencia.modelo.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    // Aqui podemos adicionar métodos de busca personalizados no futuro, se precisar
}
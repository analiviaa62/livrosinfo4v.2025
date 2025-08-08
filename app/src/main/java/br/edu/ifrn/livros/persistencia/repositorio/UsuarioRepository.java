package br.edu.ifrn.livros.persistencia.repositorio;

import br.edu.ifrn.livros.persistencia.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}

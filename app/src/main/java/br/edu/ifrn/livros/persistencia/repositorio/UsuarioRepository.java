package br.edu.ifrn.livros.persistencia.repositorio;

import br.edu.ifrn.livros.persistencia.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Método mágico do Spring Data: Busca por Nome OU Email OU Telefone (ignorando maiúsculas/minúsculas)
    List<Usuario> findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCaseOrTelefoneContainingIgnoreCase(String nome, String email, String telefone);

}
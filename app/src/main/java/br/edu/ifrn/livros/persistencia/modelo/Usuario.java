package br.edu.ifrn.livros.persistencia.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String nome;

    @Email(message = "E-mail inválido")
    @NotBlank(message = "O e-mail é obrigatório")
    @Size(max = 100)
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Size(max = 20)
    @Column(length = 20)
    private String telefone;

    // --- NOVO CAMPO PARA FOTO ---
    @Column(nullable = true, length = 64)
    private String foto;

    @Transient
    public String getCaminhoFoto() {
        if (foto == null || id == null) return null;
        return "/uploads/" + foto;
    }
    // ----------------------------
}
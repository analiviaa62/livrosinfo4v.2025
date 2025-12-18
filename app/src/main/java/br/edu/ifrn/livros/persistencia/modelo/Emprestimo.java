package br.edu.ifrn.livros.persistencia.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "emprestimos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O usuário é obrigatório")
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @NotNull(message = "O livro é obrigatório")
    @ManyToOne
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro;

    @NotNull
    @Column(name = "data_emprestimo", nullable = false)
    private LocalDate dataEmprestimo;

    // NOVO CAMPO: Prazo de entrega
    @Column(name = "data_prazo")
    private LocalDate dataPrazo;

    @Column(name = "data_devolucao")
    private LocalDate dataDevolucao;

    @PrePersist
    protected void onCreate() {
        if (dataEmprestimo == null) {
            dataEmprestimo = LocalDate.now();
        }
        // Define o prazo automaticamente para 15 dias após o empréstimo
        if (dataPrazo == null) {
            dataPrazo = dataEmprestimo.plusDays(15);
        }
    }
}
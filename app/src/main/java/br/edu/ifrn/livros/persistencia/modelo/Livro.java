package br.edu.ifrn.livros.persistencia.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "livros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título é obrigatório")
    @Size(max = 200, message = "O título deve ter no máximo 200 caracteres")
    @Column(nullable = false, length = 200)
    private String titulo;

    @NotBlank(message = "O autor é obrigatório")
    @Size(max = 150, message = "O autor deve ter no máximo 150 caracteres")
    @Column(nullable = false, length = 150)
    private String autor;

    @Size(max = 100, message = "O ISBN deve ter no máximo 100 caracteres")
    @Column(unique = true, length = 100)
    private String isbn;

    @NotNull(message = "O ano de publicação é obrigatório")
    @Min(value = 1000, message = "Ano de publicação inválido")
    @Max(value = 2100, message = "Ano de publicação inválido") // Mantido conforme sua preferência
    @Column(name = "ano_publicacao", nullable = false)
    private Integer anoPublicacao;

    @Size(max = 50, message = "A editora deve ter no máximo 50 caracteres")
    @Column(length = 50)
    private String editora;

    @NotNull(message = "A quantidade de páginas é obrigatória")
    @Min(value = 1, message = "A quantidade de páginas deve ser pelo menos 1")
    @Column(name = "quantidade_paginas", nullable = false)
    private Integer quantidadePaginas;

    @Column(columnDefinition = "TEXT")
    private String sinopse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private GeneroLivro genero;

    @NotNull(message = "A quantidade total é obrigatória")
    @Min(value = 0, message = "A quantidade total não pode ser negativa")
    @Column(name = "quantidade_total", nullable = false)
    private Integer quantidadeTotal;

    // --- CORREÇÃO: @NotNull REMOVIDO DAQUI ---
    // Removemos a obrigatoriedade aqui para permitir que o cadastro inicie com esse campo vazio.
    // O valor será preenchido automaticamente (igual ao total) no método onCreate abaixo.
    @Min(value = 0, message = "A quantidade disponível não pode ser negativa")
    @Column(name = "quantidade_disponivel", nullable = false)
    private Integer quantidadeDisponivel;
    // ------------------------------------------

    @DecimalMin(value = "0.0", message = "O preço não pode ser negativo")
    @Column(precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDate dataCadastro;

    @Column(nullable = true, length = 64)
    private String imagem;

    @Transient
    public String getCaminhoImagem() {
        if (imagem == null || id == null) return null;
        return "/uploads/" + imagem;
    }

    @PrePersist
    protected void onCreate() {
        dataCadastro = LocalDate.now();
        // Lógica: Se for cadastro novo (disponível é nulo), ele assume o valor total
        if (quantidadeDisponivel == null) {
            quantidadeDisponivel = quantidadeTotal;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        if (dataCadastro == null) {
            dataCadastro = LocalDate.now();
        }
    }
}
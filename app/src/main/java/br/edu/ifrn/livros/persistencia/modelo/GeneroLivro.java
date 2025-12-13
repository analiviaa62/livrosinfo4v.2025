package br.edu.ifrn.livros.persistencia.modelo;

public enum GeneroLivro {
    FICCAO("Ficção"),
    NAO_FICCAO("Não-Ficção"),
    ROMANCE("Romance"),
    TERROR("Terror"),
    FANTASIA("Fantasia"),
    CIENTIFICO("Científico"),
    BIOGRAFIA("Biografia"),
    HISTORIA("História"),
    POESIA("Poesia"),
    DRAMA("Drama"),
    COMEDIA("Comédia"),
    AVENTURA("Aventura"),
    SUSPENSE("Suspense"),
    INFANTIL("Infantil"),
    DIDATICO("Didático"),
    OUTRO("Outro");

    private final String descricao;

    GeneroLivro(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
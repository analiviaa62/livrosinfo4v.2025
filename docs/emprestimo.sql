CREATE TABLE emprestimos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    livro_id INT NOT NULL,
    usuario_nome VARCHAR(150) NOT NULL,
    data_emprestimo DATE NOT NULL,
    data_devolucao DATE,

    FOREIGN KEY (livro_id) REFERENCES livros(id)
);

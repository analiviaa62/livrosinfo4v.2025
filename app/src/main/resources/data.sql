-- 1. INSERINDO USUÁRIOS
INSERT INTO usuarios (nome, email, telefone) VALUES ('Ana Lívia', 'analivia@email.com', '(84) 99999-1111');
INSERT INTO usuarios (nome, email, telefone) VALUES ('João da Silva', 'joao@email.com', '(84) 98888-2222');
INSERT INTO usuarios (nome, email, telefone) VALUES ('Maria Oliveira', 'maria@email.com', '(84) 97777-3333');

-- 2. INSERINDO LIVROS
-- Livro 1: Ficção (Total 3, Disp 2 -> pois a Ana pegou 1)
INSERT INTO livros (titulo, autor, isbn, ano_publicacao, editora, quantidade_paginas, sinopse, genero, quantidade_total, quantidade_disponivel, preco, data_cadastro) 
VALUES ('Harry Potter e a Pedra Filosofal', 'J.K. Rowling', '978-8532511010', 1997, 'Rocco', 223, 'O menino que sobreviveu.', 'FANTASIA', 3, 2, 49.90, CURRENT_DATE());

-- Livro 2: Romance (Total 5, Disp 5 -> João devolveu, então está cheio)
INSERT INTO livros (titulo, autor, isbn, ano_publicacao, editora, quantidade_paginas, sinopse, genero, quantidade_total, quantidade_disponivel, preco, data_cadastro) 
VALUES ('Dom Casmurro', 'Machado de Assis', '978-8572325089', 1899, 'Penguin', 256, 'Capitu traiu ou não traiu?', 'ROMANCE', 5, 5, 29.90, CURRENT_DATE());

-- Livro 3: Terror (Total 2, Disp 1 -> Maria pegou 1 e atrasou)
INSERT INTO livros (titulo, autor, isbn, ano_publicacao, editora, quantidade_paginas, sinopse, genero, quantidade_total, quantidade_disponivel, preco, data_cadastro) 
VALUES ('It: A Coisa', 'Stephen King', '978-8581050485', 1986, 'Suma', 1104, 'Um palhaço assustador.', 'TERROR', 2, 1, 79.90, CURRENT_DATE());

-- 3. INSERINDO EMPRÉSTIMOS (Para testar as cores)

-- Cenário A: EM ANDAMENTO (No Prazo) - Azul
-- Ana pegou Harry Potter hoje. Prazo daqui a 15 dias.
INSERT INTO emprestimos (usuario_id, livro_id, data_emprestimo, data_prazo, data_devolucao)
VALUES (1, 1, CURRENT_DATE(), DATEADD('DAY', 15, CURRENT_DATE()), NULL);

-- Cenário B: DEVOLVIDO - Verde
-- João pegou Dom Casmurro há 10 dias e devolveu hoje.
INSERT INTO emprestimos (usuario_id, livro_id, data_emprestimo, data_prazo, data_devolucao)
VALUES (2, 2, DATEADD('DAY', -10, CURRENT_DATE()), DATEADD('DAY', 5, CURRENT_DATE()), CURRENT_DATE());

-- Cenário C: ATRASADO - Vermelho
-- Maria pegou IT há 20 dias. O prazo era há 5 dias atrás. Ela não devolveu ainda (data_devolucao NULL).
INSERT INTO emprestimos (usuario_id, livro_id, data_emprestimo, data_prazo, data_devolucao)
VALUES (3, 3, DATEADD('DAY', -20, CURRENT_DATE()), DATEADD('DAY', -5, CURRENT_DATE()), NULL);
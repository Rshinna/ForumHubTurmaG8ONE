INSERT INTO usuario (nome, email, senha) VALUES
('Ana', 'ana@forumhub.com', '$2a$10$Thw3X7aIow71QjJsc2OpUu3LWdHo3Twkm9R1wFVJfSEvqxTFhnI7K'),
('Carlos', 'carlos@forumhub.com', '$2a$10$ZCwXq5LUzoxlbMHdAlR36ujyEVkfuRAOZo09kPgq5i.A4iD9ylOO6');

INSERT INTO usuarios_perfis (usuario_id, perfil_id) VALUES
(2, 1),
(3, 1);

INSERT INTO resposta (mensagem, autor_id, topico_id, data_criacao, solucao) VALUES
('Oi João! Uma forma de contornar isso é usar @Query com JPQL.', 2, 1, CURRENT_TIMESTAMP, false);

INSERT INTO resposta (mensagem, autor_id, topico_id, data_criacao, solucao) VALUES
('Você já testou colocar nativeQuery = true?', 3, 2, CURRENT_TIMESTAMP, false);

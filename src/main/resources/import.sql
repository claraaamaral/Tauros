INSERT INTO funcoes(funcao_id,funcao) VALUES (1,'ADMIN')
INSERT INTO funcoes(funcao_id,funcao) VALUES (2,'REQUISITANTE')
INSERT INTO funcoes(funcao_id,funcao) VALUES (3,'OPERADOR')
INSERT INTO funcoes(funcao_id,funcao) VALUES (4,'RESPONSAVELSETOR')

INSERT INTO setores(setor_id,nome) VALUES (1,'INFORMÁTICA')
INSERT INTO users (id,ativo,email,nome,confirmacao_senha,senha,user_name,funcao_id,id_setor) VALUES (1,true, 'admin@admin.com','admin','$2a$12$vQbLWK4m23PvkJ1wOdtauep3smkIzD6N/Wx5hCi4XgSawRkLnO61i','$2a$12$vQbLWK4m23PvkJ1wOdtauep3smkIzD6N/Wx5hCi4XgSawRkLnO61i','admin',1,1)

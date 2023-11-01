-- Usuarios definition

CREATE TABLE "Usuarios" (
	id INTEGER,
	nome TEXT,
	usuario TEXT,
	senha TEXT,
	status TEXT, 
	perfil TEXT,
	CONSTRAINT Usuarios_PK PRIMARY KEY (id)
);

-- Setores definition

CREATE TABLE "Setores" (
	id INTEGER,
	responsavel INTEGER,
	cadastrado_por INTEGER,
	alterado_por INTEGER,
	cadastrado_em NUMERIC,
	alterado_em NUMERIC, descricao TEXT,
	CONSTRAINT Setores_PK PRIMARY KEY (id),
	CONSTRAINT Setores_FK FOREIGN KEY (responsavel) REFERENCES Usuarios(id),
	CONSTRAINT Setores_FK_1 FOREIGN KEY (cadastrado_por) REFERENCES Usuarios(id),
	CONSTRAINT Setores_FK_2 FOREIGN KEY (alterado_por) REFERENCES Usuarios(id)
);

-- Categorias definition

CREATE TABLE "Categorias" (
	id INTEGER,
	descricao TEXT,
	vida_util INTEGER,
	cadastrado_por INTEGER,
	alterado_por INTEGER,
	cadastrado_em NUMERIC,
	alterado_em NUMERIC,
	CONSTRAINT Categorias_PK PRIMARY KEY (id),
	CONSTRAINT Categorias_FK FOREIGN KEY (cadastrado_por) REFERENCES Usuarios(id),
	CONSTRAINT Categorias_FK_1 FOREIGN KEY (alterado_por) REFERENCES Usuarios(id)
);

-- Ativos definition

CREATE TABLE "Ativos" (
	id INTEGER,
	descricao TEXT,
	categoria INTEGER,
	numero_serie TEXT,
	nota_fiscal TEXT,
	modelo TEXT,
	imagem TEXT,
	valor REAL,
	status TEXT,
	cadastrado_por INTEGER,
	cadastrado_em NUMERIC,
	alterado_por INTEGER,
	alterado_em NUMERIC, observacao TEXT,
	CONSTRAINT Ativos_PK PRIMARY KEY (id),
	CONSTRAINT Ativos_FK FOREIGN KEY (cadastrado_por) REFERENCES Usuarios(id),
	CONSTRAINT Ativos_FK_1 FOREIGN KEY (alterado_por) REFERENCES Usuarios(id),
	CONSTRAINT Ativos_FK_2 FOREIGN KEY (categoria) REFERENCES Categorias(id)
);

-- AtivoSetor definition

CREATE TABLE "AtivoSetor" (
	ativo INTEGER,
	setor INTEGER,
	data_inicio TEXT,
	data_fim TEXT,
	cadastrado_por INTEGER,
	cadastrado_em NUMERIC,
	alterado_por INTEGER,
	alterado_em NUMERIC,
	observacao TEXT,
	CONSTRAINT Ativo_Setor_FK FOREIGN KEY (ativo) REFERENCES Ativos(id),
	CONSTRAINT Ativo_Setor_FK_1 FOREIGN KEY (setor) REFERENCES Setores(id),
	CONSTRAINT Ativo_Setor_FK_2 FOREIGN KEY (cadastrado_por) REFERENCES Usuarios(id),
	CONSTRAINT Ativo_Setor_FK_3 FOREIGN KEY (alterado_por) REFERENCES Usuarios(id)
);

-- ViewBensAtivos source

CREATE VIEW ViewBensAtivos AS 
SELECT COUNT(a.id) as "count_bens"
FROM Ativos a
WHERE a.status = 'ATIVO';

CREATE VIEW ViewValorTotalBensAtivos AS 
SELECT SUM(a.valor) as "sum_valor"
FROM Ativos a
WHERE a.status = 'ATIVO';

CREATE VIEW ViewUltimoBemCadastrado AS 
SELECT a.descricao as "last_ativo"
FROM Ativos a
ORDER BY id DESC
LIMIT 1;

-- Usuário default

INSERT INTO "Usuarios" (
	nome,
	usuario,
	senha,
	status,
	perfil
) VALUES (
	'Administrador',
	'admin',
	'admin',
	'ATIVO',
	'MASTER'
);
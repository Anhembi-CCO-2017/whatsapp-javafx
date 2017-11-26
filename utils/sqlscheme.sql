CREATE TABLE usuario (
	id integer PRIMARY KEY AUTOINCREMENT,
	nome varchar,
	status text,
	telefone text,
	img text,
	lasttime datetime
);

CREATE TABLE conversas (
	id integer PRIMARY KEY AUTOINCREMENT,
	usuario integer PRIMARY KEY AUTOINCREMENT
);

CREATE TABLE mensagem (
	id integer PRIMARY KEY AUTOINCREMENT,
	texto text,
	status integer,
	data datetime,
	conv integer,
	emissor integer
);

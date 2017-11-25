CREATE TABLE Usuario (
	id integer PRIMARY KEY AUTOINCREMENT,
	nome varchar,
	status text,
	telefone text,
	img text,
	lasttime datetime
);

CREATE TABLE Conversas (
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

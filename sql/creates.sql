CREATE TABLE Cidade (
	id				INT  PRIMARY KEY,
	nome			VARCHAR(40),
	valor_frete		NUMERIC(10,2)
);

CREATE TABLE Entrega (
	id				INT PRIMARY KEY,
	nome_cliente	VARCHAR(80),
	peso_carga		NUMERIC(10,2),
	id_cidade		INT REFERENCES CIDADE(id),
	valor_pagar		NUMERIC(10,2),
	data_despacho	DATE
);

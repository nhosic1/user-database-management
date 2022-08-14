BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "korisnik" (
	"id"	INTEGER,
	"ime"	TEXT,
	"prezime"	TEXT,
	"email"	TEXT,
	"username"	TEXT,
	"password"	TEXT,
	"slika"  TEXT,
	PRIMARY KEY("id")
);
INSERT INTO "korisnik" VALUES('1', 'Nedim', 'Hošić', 'nedim.hosic1@gmail.com', 'nhosic1', 'test', '/img/face-smile.png');

COMMIT;

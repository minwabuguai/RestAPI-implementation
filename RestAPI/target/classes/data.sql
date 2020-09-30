DROP TABLE IF EXISTS User_table;

CREATE TABLE User_table (
  id INT NOT NULL PRIMARY KEY,
  version int NOT NULL,
  name VARCHAR(250) NOT NULL
);

INSERT INTO User_table (id, version, name) VALUES
  ('1','1','Alice'),
  ('2','2','Bob'),
  ('3','1','Eve');

CREATE TABLE Unit_table (
  id INT NOT NULL PRIMARY KEY,
  version int NOT NULL,
  name VARCHAR(250) NOT NULL
);

INSERT INTO Unit_table (id, version, name) VALUES
  ('11','2','Kreftregisteret'),
  ('12','1','Akershus universitetssykehus HF'),
  ('13','2','Sørlandet sykehus HF'),
  ('14','2','Vestre Viken HF');

CREATE TABLE Role_table (
  id INT NOT NULL PRIMARY KEY,
  version int NOT NULL,
  name VARCHAR(250) NOT NULL
);

INSERT INTO Role_table (id, version, name) VALUES
  ('101','1','User administration'),
  ('102','2','Endoscopist administration'),
  ('103','1','Report colonoscopy capacity'),
  ('104','2','Send invitations'),
  ('105','1','View statistics');

CREATE TABLE UserRole_table (
  id INT NOT NULL PRIMARY KEY,
  version int NOT NULL,
  UserId int NOT NULL,
  UnitId int NOT NULL,
  RoleId int NOT NULL,
  ValidFrom smalldatetime NULL,
  ValidTo smalldatetime NULL
);

INSERT INTO UserRole_table (id, version, UserId, UnitId, RoleId, ValidFrom, ValidTo) VALUES
  ('1001','1','1','11','101','2019-01-02 00:00:00','2019-12-31 23:59:59'),
  ('1002','2','1','11','104','2019-01-02 00:00:00','2019-12-31 23:59:59'),
  ('1003','1','1','11','105','2019-06-11 00:00:00','2019-12-31 23:59:59'),
  ('1004','2','2','12','101','2020-01-28 00:00:00',null),
  ('1005','1','2','12','105','2020-01-28 00:00:00',null),
  ('1006','1','2','14','101','2020-01-28 00:00:00',null),
  ('1007','1','2','14','102','2020-01-28 00:00:00',null),
  ('1008','1','1','11','101','2020-01-28 00:00:00',null),
  ('1009','1','1','11','104','2020-01-28 00:00:00',null);
INSERT INTO peoples (age_people,happy_day,first_last_name,employeename,second_last_name,email_people,full_name,identity_people,ingreso,estadocivil,cargo,genero,tdocumento)VALUES ('29', '1995-07-10','Tamani', 'Raul', 'Arbildo', 'rtamanar@gmail.com', 'Raul Tamani Arbildo', '75887601','2023-01-23','Soltero','Soporte de Procesos','Hombre','D.N.I');

INSERT INTO addresses (people_id, distrito, pais, region, direccion, referencia)VALUES ('1', 'Ate', 'Perú', 'Lima', 'Calle las Ñejas MZ Z1 Lote 1 SN', 'Por los Nogales de Puente amarillo');

INSERT INTO USERS(USERNAME,USERPASSWORD,USERENABLED,people_id)VALUES('rtamanar','$2a$10$ww0edmDL7JG3s3vXN8QlJu5zn2dtaVamx6YsU52eA/nB/mFAfmx5G',1,1);

INSERT INTO ROLS(USER_ID,AUTHORITY,DESCRIP_ROL_USER)VALUES(1,'ROLE_USER','Acceso a algunos items del sistema');
INSERT INTO ROLS(USER_ID,AUTHORITY,DESCRIP_ROL_USER) VALUES(1,'ROLE_ADMIN','Acceso total al sistema');
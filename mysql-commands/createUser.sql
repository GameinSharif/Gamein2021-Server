CREATE USER 'readGame'@'%' IDENTIFIED BY 'd454rq23@#$c';
GRANT SELECT, SHOW VIEW ON gamein_test.* TO 'readGame'@'%';
FLUSH PRIVILEGES;

ALTER USER 'root'@'localhost' IDENTIFIED BY 'eihdea32o3q@%2';
ALTER USER 'root'@'%' IDENTIFIED BY 'eihdea32o3q@%2';
FLUSH PRIVILEGES;

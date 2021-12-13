CREATE USER 'readGame'@'%' IDENTIFIED BY 'd454rq23@#$c';
GRANT SELECT, SHOW VIEW ON gamein_test.* TO 'readGame'@'%';
FLUSH PRIVILEGES;
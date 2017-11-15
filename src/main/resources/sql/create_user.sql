CREATE USER 'payment_user'@'localhost' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON * . * TO 'payment_user'@'localhost';
FLUSH PRIVILEGES;

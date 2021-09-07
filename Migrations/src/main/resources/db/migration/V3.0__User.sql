CREATE TABLE User
(
    id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50),
    password VARCHAR(50),
    team_id   INT DEFAULT 0,
    PRIMARY KEY (id),
    FOREIGN KEY (teamId) REFERENCES Team (id) ON UPDATE CASCADE ON DELETE CASCADE
);

INSERT INTO User (username, password, team_id)
VALUES ('ali', '12345', 1);

INSERT INTO User (username, password, team_id)
VALUES ('mahdi', '54321', 1);
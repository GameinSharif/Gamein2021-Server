CREATE TABLE Team
(
    id       INT NOT NULL AUTO_INCREMENT,
    teamName VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO Team (teamName)
values ('team1');
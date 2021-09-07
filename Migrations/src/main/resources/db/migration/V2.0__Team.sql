CREATE TABLE Team
(
    id       INT NOT NULL AUTO_INCREMENT,
    team_name VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO Team (team_name)
values ('team1');
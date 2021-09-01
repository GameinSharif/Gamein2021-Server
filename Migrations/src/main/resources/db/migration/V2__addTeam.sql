CREATE TABLE Team
(
    id       number,
    teamName varchar(50),
    PRIMARY KEY (id)
);

INSERT INTO Team (id, teamName)
values (1, 'team1');

CREATE TABLE User
(
    id       number,
    username varchar(50),
    password varchar(50),
    teamId   number,
    PRIMARY KEY (id),
    FOREIGN KEY (teamId) REFERENCES Team (id)
);

INSERT INTO User (id, username, password, teamId)
VALUES (1, 'ali', '12345', 1)
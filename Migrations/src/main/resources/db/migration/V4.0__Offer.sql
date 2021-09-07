CREATE TABLE Offer
(
    id INT NOT NULL AUTO_INCREMENT,
    team_id INT DEFAULT 0,
    type VARCHAR(50) NOT NULL,
    volume INT NOT NULL,
    cost_per_unit INT NOT NULL,
    earliest_expected_arrival DATETIME NOT NULL,
    latest_expected_arrival DATETIME NOT NULL,
    offer_deadline DATETIME NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (team_id) REFERENCES Team (id) ON UPDATE CASCADE ON DELETE CASCADE
);

INSERT INTO Offer (team_id, type, volume, cost_per_unit, earliest_expected_arrival, latest_expected_arrival, offer_deadline)
VALUES (1, 'T7', 100, 10, "2021-9-5 12:00:00", "2021-9-18 12:00:00", "2021-9-5 12:00:00");
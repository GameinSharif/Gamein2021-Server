package ir.sharif.gamein2021.core.importer;

import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.domain.entity.User;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;

@Component
public class DatabaseSeeder {
    private final DataImporter<Team> teamDataImporter;
    private final DataImporter<User> userDataImporter;

    public DatabaseSeeder(TeamDataImporter teamDataImporter,
                          DataImporter<User> userDataImporter) {
        this.teamDataImporter = teamDataImporter;
        this.userDataImporter = userDataImporter;
    }

    public void seed() {
        try {
            System.out.println("seed start " + LocalDateTime.now());
            teamDataImporter.importData("CSVFiles/team.csv");
            teamDataImporter.importData("CSVFiles/users.csv");
            System.out.println("seed finished " + LocalDateTime.now());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

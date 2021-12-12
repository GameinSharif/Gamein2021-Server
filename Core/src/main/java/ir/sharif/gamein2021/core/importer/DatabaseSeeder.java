package ir.sharif.gamein2021.core.importer;

import ir.sharif.gamein2021.core.domain.entity.Team;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;

@Component
public class DatabaseSeeder {
    private final DataImporter<Team> teamDataImporter;

    public DatabaseSeeder(TeamDataImporter teamDataImporter) {
        this.teamDataImporter = teamDataImporter;
    }

    public void seed() {
        try {
            System.out.println("seed start " + LocalDateTime.now());
            teamDataImporter.importData("CSVFiles/team.csv");
            System.out.println("seed finished " + LocalDateTime.now());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

package ir.sharif.gamein2021.core.importer;

import ir.sharif.gamein2021.core.domain.entity.Team;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;

@Component
public class DatabaseSeeder {
    private final DataImporter<Team> teamDataImporter;

    public DatabaseSeeder(TeamDataImporter teamDataImporter) {
        this.teamDataImporter = teamDataImporter;
    }

    public void seed(){
        try {
            teamDataImporter.importData("CSVFiles/team.csv");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

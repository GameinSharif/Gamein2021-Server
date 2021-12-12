package ir.sharif.gamein2021.core.importer;

import ir.sharif.gamein2021.core.domain.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class TeamDataImporter extends DataImporter<Team> {
    public TeamDataImporter(CSVHelper<Team> csvHelper, JpaRepository<Team, Integer> repository) {
        super(csvHelper, repository);
    }
}

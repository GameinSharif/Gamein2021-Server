package ir.sharif.gamein2021.core.importer;

import ir.sharif.gamein2021.core.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class UserDataImporter extends DataImporter<User> {
    public UserDataImporter(CSVHelper<User> csvHelper, JpaRepository<User, Integer> repository) {
        super(csvHelper, repository);
    }
}

package ir.sharif.gamein2021.core.importer.recordParser;

import ir.sharif.gamein2021.core.dao.TeamRepository;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.domain.entity.User;
import ir.sharif.gamein2021.core.util.Enums;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

@Component
public class UserRecordParser implements CSVRecordParser<User>{
    private final TeamRepository teamRepository;

    public UserRecordParser(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public User parse(CSVRecord record) {
        User user = new User();

        user.setId(Integer.parseInt(record.get("id")));
        user.setPassword(record.get("password"));
        user.setUsername(record.get("username"));

        Team team = teamRepository.getById(Integer.parseInt(record.get("team_id")));
        user.setTeam(team);

        return user;
    }
}

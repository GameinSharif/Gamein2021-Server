package ir.sharif.gamein2021.core.importer.recordParser;

import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.util.Enums;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class TeamRecordParser implements CSVRecordParser<Team> {
    @Override
    public Team parse(CSVRecord record) {
        Team team = new Team();

        team.setId(Integer.parseInt(record.get("id")));
        team.setTeamName(record.get("team_name"));
        team.setCountry(Enum.valueOf(Enums.Country.class, record.get("country")));
        team.setCredit(Float.parseFloat(record.get("credit")));
        team.setWealth(Float.parseFloat(record.get("wealth")));
        team.setBrand(Float.parseFloat(record.get("brand")));
        team.setInFlow(Float.parseFloat(record.get("in_flow")));
        team.setOutFlow(Float.parseFloat(record.get("out_flow")));
        team.setProductionCost(Float.parseFloat(record.get("production_cost")));
        team.setTransportationCost(Float.parseFloat(record.get("transportation_cost")));
        team.setUsedWater(Long.parseLong(record.get("used_water")));
        team.setDonatedAmount(Float.parseFloat(record.get("donated_money")));
//        team.setBanEnd(LocalDateTime.(record.get("ban_end")).toLocalDate());

        return team;
    }
}

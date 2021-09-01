package ir.sharif.gamein2021.core.model;

public class TeamModel {
    private int id;
    private String teamName;

    public TeamModel() {
    }

    public TeamModel(int id) {
        this.id = id;
    }

    public TeamModel(int id, String teamName) {
        this.id = id;
        this.teamName = teamName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}

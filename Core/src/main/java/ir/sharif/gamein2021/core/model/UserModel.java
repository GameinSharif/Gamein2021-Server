package ir.sharif.gamein2021.core.model;

import ir.sharif.gamein2021.core.entity.Team;

public class UserModel extends BaseModel{
    private int id;
    private String username;
    private Team teamModel;


    public UserModel() {
    }

    public UserModel(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public UserModel(int id, String username, Team teamModel) {
        this(id, username);
        this.teamModel = teamModel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Team getTeamModel() {
        return teamModel;
    }

    public void setTeamModel(Team teamModel) {
        this.teamModel = teamModel;
    }
}

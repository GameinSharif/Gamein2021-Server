package ir.sharif.gamein2021.core.model;

public class TeamModel {
    private int id;
    private String username;

    public TeamModel() {
    }

    public TeamModel(int id) {
        this.id = id;
    }

    public TeamModel(int id, String username) {
        this.id = id;
        this.username = username;
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
}

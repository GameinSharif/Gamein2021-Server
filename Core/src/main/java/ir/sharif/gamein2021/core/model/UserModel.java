package ir.sharif.gamein2021.core.model;

public class UserModel extends BaseModel{
    private int id;
    private String username;


    public UserModel() {
    }

    public UserModel(int id, String username) {
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

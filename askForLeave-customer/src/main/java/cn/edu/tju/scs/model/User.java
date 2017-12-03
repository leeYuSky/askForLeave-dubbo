package cn.edu.tju.scs.model;

import java.io.Serializable;

public class User implements Serializable{

    private String id;  // username

    private String password;

    protected User() {}

    public User(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

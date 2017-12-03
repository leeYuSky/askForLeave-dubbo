package cn.edu.tju.scs.dto;


import java.io.Serializable;

public class ResponseNameData extends ResponseData implements Serializable{
    private String username;

    public String getUsername() {
        return username;
    }

    public ResponseNameData(String username) {
        this.username = username;
    }

    public void setUsername(String username) {
        this.username = username;
    }




}

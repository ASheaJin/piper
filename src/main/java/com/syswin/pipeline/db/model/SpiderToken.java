package com.syswin.pipeline.db.model;

public class SpiderToken {
    private Integer id;

    private String ptemail;

    private String token;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPtemail() {
        return ptemail;
    }

    public void setPtemail(String ptemail) {
        this.ptemail = ptemail;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
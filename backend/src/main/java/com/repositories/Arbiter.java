package com.repositories;

import java.io.Serializable;

public class Arbiter implements Serializable, HasId<Integer> {
    private Integer id;

    private String username;

    private String password;

    private Integer probaId;

    public Arbiter() {
    }

    public Integer getProbaId() {
        return probaId;
    }

    public void setProbaId(Integer probaId) {
        this.probaId = probaId;
    }

    public Arbiter(Integer id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.probaId = -1;
    }

    public Arbiter(Integer id, String username, String password, Integer probaId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.probaId = probaId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}

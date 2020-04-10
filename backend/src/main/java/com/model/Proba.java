package com.model;

import java.io.Serializable;

public class Proba implements HasId<Integer>, Serializable {

    private Integer id;
    private Integer arbiterId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Proba(Integer id, Integer arbiterId) {
        this.id = id;
        this.arbiterId = arbiterId;
    }

    public Integer getArbiterId() {
        return arbiterId;
    }

    public void setArbiterId(Integer arbiterId) {
        this.arbiterId = arbiterId;
    }
}

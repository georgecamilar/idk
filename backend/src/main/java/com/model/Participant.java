package com.model;

import java.io.Serializable;

public class Participant implements Serializable, HasId<Integer> {
    private Integer id;
    private String name;
    private Double total;
    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Participant(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.total = 0.0;
    }

    public Participant(Integer id, String name, Double total) {
        this.id = id;
        this.name = name;
        this.total = total;
    }
    
    public Participant(String name, Double total){
        this.name = name;
        this.total = total;
    }
}

package model;

import java.io.Serializable;

public class Participant implements Serializable, HasId<Integer> {
    private Integer id;
    private String name;

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

    public Participant(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}

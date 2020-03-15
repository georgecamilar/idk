package model;

import javax.persistence.*;
import java.io.Serializable;

//@Entity
//@Table(name = "arbiter")
public class Arbiter implements Serializable, HasId<Integer> {
    //    @Id
//    @GeneratedValue
//    @Column(name = "arbiterId")
    private Integer id;

    //    @Column(name = "username")
    private String username;

    //    @Column(name = "password")
    private String password;


    public Arbiter() {
    }

    public Arbiter(Integer id,String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
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

package org.rest.webapp.Entity;

import javax.persistence.*;

/**
 * Created by Ivan on 10/8/2015.
 */
@Entity
@Table(name = "USER")
public class User {

    @Id @GeneratedValue
    private int id;
    @Column
    private String nickName;
    @Column
    private String password;
    @Column
    private String firstName;
    @Column
    private String email;



    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

}

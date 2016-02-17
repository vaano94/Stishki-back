package org.rest.webapp.Entity;

import com.sun.istack.NotNull;

import javax.persistence.*;
import javax.ws.rs.DefaultValue;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Ivan on 10/8/2015.
 */
@Entity
@Table
public class User {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long user_id;
    @Column
    private String nickName;
    @Column
    private String password;
    @Column
    private String firstName;
    @Column
    private String email;
    @Column
    private String token;
    @Column(nullable=false, columnDefinition = "long default 0")
    @DefaultValue("0")
    private long tokenExpirationTime;
    @Column
    private String type;

    @OneToMany(cascade =CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Poem> poems = new HashSet<Poem>();

    public Set<Poem> getPoems() {
        return this.poems;
    }

    public long getId() {
        return user_id;
    }

    public void setId(long id) {
        this.user_id = id;
    }

    public void setPoems(Set<Poem> poems) {
        this.poems = poems;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Long getTokenExpirationTime() {
        return tokenExpirationTime;
    }

    public void setTokenExpirationTime(Long tokenExpirationTime) {
        this.tokenExpirationTime = tokenExpirationTime;
    }
}

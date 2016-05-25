package org.rest.webapp.Entity;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.ws.rs.DefaultValue;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Ivan on 10/8/2015.
 */
@Entity
@Table
@DynamicUpdate(value = true)
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

    @ElementCollection (fetch = FetchType.EAGER)
    @CollectionTable(joinColumns = @JoinColumn(name="user_id"))
    @Fetch(FetchMode.SELECT)
    @Cascade(org.hibernate.annotations.CascadeType.ALL )
    private List<String> genres = new ArrayList<String>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.ALL )
    private Set<Poem> poems = new HashSet<Poem>();

    // cascade = CascadeType.ALL
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "user", fetch = FetchType.EAGER, orphanRemoval=true)
    //@Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Draft> drafts = new ArrayList<Draft>();

    public List<Draft> getDrafts() {
        return drafts;
    }

    public void setDrafts(List<Draft> drafts) {
        this.drafts = drafts;
    }

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

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
}

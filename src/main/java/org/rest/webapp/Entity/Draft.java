package org.rest.webapp.Entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ivan on 2/16/2016.
 */
@Entity
@Table
public class Draft {

    @Id
    @Column
    @GeneratedValue
    private long id;

    @Column(length = 800)
    private String content;

    @Column
    private Date date;

    @Column
    private String genre;

    @ElementCollection (fetch = FetchType.EAGER)
    @CollectionTable(joinColumns = @JoinColumn(name="id"))
    @Fetch(value = FetchMode.SUBSELECT)
    private List<String> hashtags = new ArrayList<String>();


    @ManyToOne
    @JoinColumn
    private User user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public List<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<String> hashtags) {
        this.hashtags = hashtags;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

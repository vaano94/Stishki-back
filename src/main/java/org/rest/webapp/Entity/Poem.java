package org.rest.webapp.Entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Created by Ivan on 10/8/2015.
 */
@Entity
@Table
@DynamicUpdate(value=true)
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="poem")
public class Poem {

    @Id
    @Column
    @GeneratedValue
    private long id;

    @ElementCollection (fetch = FetchType.EAGER)
    @CollectionTable(joinColumns = @JoinColumn(name="id"))
    @Fetch(FetchMode.SELECT)
    private List<Long> likes = new ArrayList<Long>();

    @Column(nullable = false, columnDefinition = "long default 0L")
    private Long likesCount = 0L;

    @ElementCollection (fetch = FetchType.EAGER)
    @CollectionTable(joinColumns = @JoinColumn(name="id"))
    @Fetch(FetchMode.SELECT)
    private List<Long> dislikes = new ArrayList<Long>();

    @Column
    private String content;

    @Column
    private Date date;

    @Column
    private String genre;

    @ElementCollection (fetch = FetchType.EAGER)
    @CollectionTable(joinColumns = @JoinColumn(name="id"))
    @Fetch(value = FetchMode.SUBSELECT)
    private List<String> hashtags = new ArrayList<String>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private User user;

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addLike(long id) {
        getLikes().add(id);
    }

    public Long getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Long likesCount) {
        this.likesCount = likesCount;
    }

    public void addDislike(long id) {
        this.getDislikes().add(id);
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Long> getLikes() {
        return likes;
    }

    public List<Long> getDislikes() {
        return dislikes;
    }

    public List<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<String> hashtags) {
        this.hashtags = hashtags;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}

package org.rest.webapp.Entity;

import javax.persistence.*;

/**
 * Created by Ivan on 10/8/2015.
 */
@Entity
@Table
public class Poem {

    @Id
    @GeneratedValue
    private long id;

    @Column
    private long likes;

    @Column
    private long dislikes;

    @Column
    private long poemsCount;

    public long getPoemsCount() {
        return poemsCount;
    }

    public void setPoemsCount(long poemsCount) {
        this.poemsCount = poemsCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public long getDislikes() {
        return dislikes;
    }

    public void setDislikes(long dislikes) {
        this.dislikes = dislikes;
    }


}

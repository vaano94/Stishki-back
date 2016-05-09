package org.rest.webapp.Entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Ivan on 5/7/2016.
 */
@Entity
@Table
public class PoemInfo {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String type;

    @Column
    private String shortDescription;

    @Column
    private String syllableRules;

    @Column
    private String howToWrite;

    @Column
    private String imagePath;

    @Column(columnDefinition="TEXT")
    @ElementCollection (fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<String> examples;


    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getSyllableRules() {
        return syllableRules;
    }

    public void setSyllableRules(String syllableRules) {
        this.syllableRules = syllableRules;
    }

    public String getHowToWrite() {
        return howToWrite;
    }

    public void setHowToWrite(String howToWrite) {
        this.howToWrite = howToWrite;
    }

    public List<String> getExamples() {
        return examples;
    }

    public void setExamples(List<String> examples) {
        this.examples = examples;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

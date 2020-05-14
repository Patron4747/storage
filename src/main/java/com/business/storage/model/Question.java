package com.business.storage.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author: mmustafin@context-it.ru
 * @created: 13.05.2020
 */
@Entity
@Table(name = "question")
public class Question implements Serializable {

    private static final long serialVersionUID = -2343245243252432345L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String title;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}

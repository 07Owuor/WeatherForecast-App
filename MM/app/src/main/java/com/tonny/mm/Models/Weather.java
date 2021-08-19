package com.tonny.mm.Models;

import java.io.Serializable;

/**
 * Created by Nitrozeus on 19/08/2021.
 */

public class Weather implements Serializable {

    String date, title, description;

    public Weather() {
    }

    public Weather(String date, String title, String description) {
        this.date = date;
        this.title = title;
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

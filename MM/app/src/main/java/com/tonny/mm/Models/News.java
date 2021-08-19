package com.tonny.mm.Models;

import java.io.Serializable;

/**
 * Created by Nitrozeus on 02/07/2021.
 */

public class News implements Serializable {

    String image, title, text;

    public News() {
    }

    public News(String image, String title, String text) {
        this.image = image;
        this.title = title;
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

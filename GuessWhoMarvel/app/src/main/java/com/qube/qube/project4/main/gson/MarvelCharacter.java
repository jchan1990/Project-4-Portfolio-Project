package com.qube.qube.project4.main.gson;

/**
 * Created by Qube on 9/9/16.
 */
public class MarvelCharacter {
    private String name;
    private MarvelThumbnail thumbnail;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MarvelThumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(MarvelThumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }
}

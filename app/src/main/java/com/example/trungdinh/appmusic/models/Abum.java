package com.example.trungdinh.appmusic.models;

public class Abum {
    private String nameAbum;
    private String nameSinger;
    private String images;

    public Abum() {}

    public Abum(String nameAbum, String nameSinger, String images) {
        this.nameAbum = nameAbum;
        this.nameSinger = nameSinger;
        this.images = images;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getNameAbum() {
        return nameAbum;
    }

    public void setNameAbum(String nameAbum) {
        this.nameAbum = nameAbum;
    }

    public String getNameSinger() {
        return nameSinger;
    }

    public void setNameSinger(String nameSinger) {
        this.nameSinger = nameSinger;
    }
}

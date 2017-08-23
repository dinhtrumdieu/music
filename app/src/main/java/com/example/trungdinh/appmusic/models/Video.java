package com.example.trungdinh.appmusic.models;

/**
 * Created by TrungDinh on 7/25/2017.
 */

public class Video {

    private String videoUrl;
    private String title;
    private String producer;

    public void Video(){}

    public Video(String videoUrl, String title, String producer) {
        this.videoUrl = videoUrl;
        this.title = title;
        this.producer = producer;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }
}

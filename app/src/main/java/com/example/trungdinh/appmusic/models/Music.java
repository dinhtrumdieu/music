package com.example.trungdinh.appmusic.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by TrungDinh on 6/22/2017.
 */

public class Music implements Parcelable {
    private long id;
    private String nameSong;
    private String nameSinger;
    private String data;
    private String time;
    private String abum;

    public Music() {
    }

    public Music(long id, String nameSong, String nameSinger, String data, String time, String abum) {
        this.id = id;
        this.nameSong = nameSong;
        this.nameSinger = nameSinger;
        this.data = data;
        this.time = time;
        this.abum = abum;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNameSong() {
        return nameSong;
    }

    public void setNameSong(String nameSong) {
        this.nameSong = nameSong;
    }

    public String getNameSinger() {
        return nameSinger;
    }

    public void setNameSinger(String nameSinger) {
        this.nameSinger = nameSinger;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAbum() {
        return abum;
    }

    public void setAbum(String abum) {
        this.abum = abum;
    }

    protected Music(Parcel in) {
        id = in.readLong();
        nameSong = in.readString();
        nameSinger = in.readString();
        data = in.readString();
        time = in.readString();
        abum = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(nameSong);
        dest.writeString(nameSinger);
        dest.writeString(data);
        dest.writeString(time);
        dest.writeString(abum);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Music> CREATOR = new Creator<Music>() {
        @Override
        public Music createFromParcel(Parcel in) {
            return new Music(in);
        }

        @Override
        public Music[] newArray(int size) {
            return new Music[size];
        }
    };
}

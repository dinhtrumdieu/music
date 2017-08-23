package com.example.trungdinh.appmusic.utils;

import android.os.Handler;

import com.example.trungdinh.appmusic.models.Music;

import java.util.ArrayList;

/**
 * Created by TrungDinh on 7/3/2017.
 */

public class PlayerConstants {
    //List of Songs
    public static ArrayList<Music> SONGS_LIST = new ArrayList<Music>();
    //song number which is playing right now from SONGS_LIST
    public static int SONG_NUMBER = 0;
    //song is playing or paused
    public static boolean SONG_PAUSED = true;
    //handler for song changed(next, previous) defined in service(SongService)
    public static Handler SONG_CHANGE_HANDLER;
    //handler for song play/pause defined in service(SongService)
    public static Handler PLAY_PAUSE_HANDLER;
    //handler for showing song progress defined in Activities(MainActivity, AudioPlayerActivity)
    public static Handler PROGRESSBAR_HANDLER;
}

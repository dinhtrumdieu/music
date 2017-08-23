package com.example.trungdinh.appmusic;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.trungdinh.appmusic.models.Music;

import java.util.ArrayList;

public class MusicRetriever {

    public static ArrayList<Music> getPlaylist(Context context) {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mCR = context.getContentResolver();
        Cursor cur = mCR.query(uri, null, MediaStore.Audio.Media.IS_MUSIC
                + "=1", null, null);

        ArrayList<Music> mSongs = new ArrayList<>();
        cur.moveToFirst();
        int idColumn = cur.getColumnIndex(MediaStore.Audio.Media._ID);
        int artistColumn = cur.getColumnIndex(MediaStore.Audio.Media.ARTIST);
        int titleColumn = cur.getColumnIndex(MediaStore.Audio.Media.TITLE);
        int dataColumn = cur.getColumnIndex(MediaStore.Audio.Media.DATA);
        int timeColumn = cur.getColumnIndex(MediaStore.Audio.Media.DURATION);
        int abum = cur.getColumnIndex(MediaStore.Audio.Media.ALBUM);
        do {
            mSongs.add(new Music(
                    cur.getLong(idColumn),
                    cur.getString(titleColumn),
                    cur.getString(artistColumn),
                    cur.getString(dataColumn),
                    cur.getString(timeColumn),
                    cur.getString(abum)
            ));
            Log.d("vodayid: ", "" + cur.getLong(idColumn) + "title: " + cur.getString(titleColumn) + "artist: " + cur.getString(artistColumn) + "data:"
                    + cur.getString(dataColumn) + "time:" + cur.getString(timeColumn) + "abum: " + cur.getString(abum));
        } while (cur.moveToNext());
        Log.d("xxS",mSongs.size()+"");
        return mSongs;
    }

}

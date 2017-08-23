package com.example.trungdinh.appmusic.adapters;

import android.support.v7.widget.RecyclerView;

import com.example.trungdinh.appmusic.models.Music;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by TrungDinh on 5/8/2017.
 */

public abstract class MusicRecycleAdapter extends RecyclerView.Adapter {
    private List items = new ArrayList<>();

    MusicRecycleAdapter() {
        setHasStableIds(true);
    }

    public void add(Music object) {
        items.add(object);
        notifyDataSetChanged();
    }

    public void add(int index, Music object) {
        items.add(index, object);
        notifyDataSetChanged();
    }

    public void addAll(Collection collection) {
        if (collection != null) {
            items.addAll(collection);
            notifyDataSetChanged();
        }
    }

    public void addAll(Music... items) {
        addAll(Arrays.asList(items));
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void remove(Music object) {
        items.remove(object);
        notifyDataSetChanged();
    }
}


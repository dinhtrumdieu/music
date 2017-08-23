package com.example.trungdinh.appmusic.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.trungdinh.appmusic.R;
import com.example.trungdinh.appmusic.listeners.IItemRecycle;
import com.example.trungdinh.appmusic.models.Music;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.List;

public class MusicRecycleHeaderAdapter extends MusicRecycleAdapter implements StickyRecyclerHeadersAdapter {

    private List<Music> mList;
    private Context mContext;
    private IItemRecycle mListener;

    public MusicRecycleHeaderAdapter(Context context, List<Music> list, IItemRecycle listener) {
        this.mList = list;
        this.mContext = context;
        this.mListener = listener;
    }

    @Override
    public long getHeaderId(int position) {
        if (position == 0) {
            return -1;
        } else {
            return getItemId(position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_music_recycle, parent, false);
        return new ItemHeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemHeaderViewHolder) {
            if (getItem(position).getNameSong() != null) {
                String header = String.valueOf(getItem(position).getNameSong().charAt(0));
                Log.d("giatriHeader", header);
                ((ItemHeaderViewHolder) holder).header.setText(header);
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music_offline_recycle, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final Music itemModel = mList.get(position);
        ((ItemViewHolder) holder).nameSong.setText(itemModel.getNameSong());
        ((ItemViewHolder) holder).nameSinger.setText(itemModel.getNameSinger());
        final Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_item_recycle);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("trang",position+"");
                mListener.onItemClick(itemModel,position);
            }
        });
/*
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        holder.itemView.startAnimation(animation);
                        return true;

                    case MotionEvent.ACTION_UP:
                        holder.itemView.clearAnimation();
                        return false;
                }
                return false;
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public Music getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView nameSong , nameSinger;

        public ItemViewHolder(View itemView) {
            super(itemView);
            nameSong = (TextView) itemView.findViewById(R.id.tvNameMusic);
            nameSinger = (TextView)itemView.findViewById(R.id.tvNameSinger);
        }
    }

    public static class ItemHeaderViewHolder extends RecyclerView.ViewHolder {

        TextView header;

        public ItemHeaderViewHolder(View itemView) {
            super(itemView);
            header = (TextView) itemView.findViewById(R.id.textHeader);
        }
    }

    public void broadcastIntent(Music song, int position) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("path", song);
        bundle.putInt("position",position);
        intent.putExtras(bundle);
        intent.setAction(mContext.getString(R.string.broadcast_music_intent));
        mContext.sendBroadcast(intent);
    }
}


package com.example.trungdinh.appmusic.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trungdinh.appmusic.R;
import com.example.trungdinh.appmusic.listeners.IItemRecycle;
import com.example.trungdinh.appmusic.models.Music;

import java.util.List;


public class MusicOfflineRecycleAdapter extends RecyclerView.Adapter<MusicOfflineRecycleAdapter.MusicOfflineViewHolder> {

    private Context mContext;
    private List<Music> mList;
    private IItemRecycle mListener;

    public MusicOfflineRecycleAdapter(Context context, List<Music> list, IItemRecycle listener) {
        this.mContext = context;
        this.mList = list;
        this.mListener = listener;
    }


    @Override
    public MusicOfflineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_music_online_recycle, parent, false);
            return new MusicOfflineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MusicOfflineViewHolder holder, final int position) {
        final Music music = mList.get(position);

        holder.nameSong.setText(music.getNameSong());
        holder.nameSinger.setText(music.getNameSinger());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(music, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static public class MusicOfflineViewHolder extends RecyclerView.ViewHolder {
        TextView nameSong, nameSinger;

        public MusicOfflineViewHolder(View itemView) {
            super(itemView);
            nameSong = (TextView) itemView.findViewById(R.id.tvNameMusic);
            nameSinger = (TextView) itemView.findViewById(R.id.tvNameSinger);
        }
    }

}

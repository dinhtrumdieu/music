package com.example.trungdinh.appmusic.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.trungdinh.appmusic.R;
import com.example.trungdinh.appmusic.listeners.IItemRecycle;
import com.example.trungdinh.appmusic.listeners.OnLoadMoreListener;
import com.example.trungdinh.appmusic.models.Music;

import java.util.List;


public class MusicOnlineRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Music> mList;
    private IItemRecycle mListener;

    // load more
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private OnLoadMoreListener mOnLoadMoreListener;


    public MusicOnlineRecycleAdapter(Context context, List<Music> list, IItemRecycle listener,RecyclerView mRecyclerView) {
        this.mContext = context;
        this.mList = list;
        this.mListener = listener;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM){
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_music_online_recycle, parent, false);
            return new MusicOnlineViewHolder(view);
        }else if(viewType == VIEW_TYPE_LOADING){
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof MusicOnlineViewHolder){
            final Music music = mList.get(position);

            MusicOnlineViewHolder musicOnlineViewHolder = (MusicOnlineViewHolder) holder;
            musicOnlineViewHolder.nameSong.setText(music.getNameSong());
            musicOnlineViewHolder.nameSinger.setText(music.getNameSinger());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(music, position);
                }
            });
        }else if(holder instanceof LoadingViewHolder){
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    public void setLoaded() {
        isLoading = false;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static public class MusicOnlineViewHolder extends RecyclerView.ViewHolder {
        TextView nameSong, nameSinger;

        public MusicOnlineViewHolder(View itemView) {
            super(itemView);
            nameSong = (TextView) itemView.findViewById(R.id.tvNameMusic);
            nameSinger = (TextView) itemView.findViewById(R.id.tvNameSinger);
        }
    }

    static private class LoadingViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);

            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }
}

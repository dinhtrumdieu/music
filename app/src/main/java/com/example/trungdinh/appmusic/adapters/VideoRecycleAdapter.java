package com.example.trungdinh.appmusic.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.trungdinh.appmusic.R;
import com.example.trungdinh.appmusic.activitys.VideoPlayActivity;
import com.example.trungdinh.appmusic.models.Video;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class VideoRecycleAdapter extends RecyclerView.Adapter<VideoRecycleAdapter.AbumViewholder>{

    private Context mContext;
    private List<Video> mListVideo;

    public VideoRecycleAdapter(Context context, List<Video> list) {
        this.mContext = context;
        this.mListVideo = list;
    }

    @Override
    public AbumViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_video_playlist_recycle,parent,false);
        return new AbumViewholder(view);
    }

    @Override
    public void onBindViewHolder(final AbumViewholder holder, int position) {
        Video video = mListVideo.get(position);

        holder.tvTitle.setText(video.getTitle());
        holder.tvNameProducer.setText(video.getProducer());

        Picasso.with(mContext).load(Integer.parseInt(video.getVideoUrl())).error(R.drawable.img_video).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.youTubePlayerView.setBackground(new BitmapDrawable(mContext.getResources(),bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext , VideoPlayActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListVideo.size();
    }

    static public class AbumViewholder extends RecyclerView.ViewHolder{

        TextView tvTitle;
        TextView tvNameProducer;
        RelativeLayout youTubePlayerView;

        public AbumViewholder(final View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvNameProducer = (TextView) itemView.findViewById(R.id.tvNameProducer);
            youTubePlayerView = (RelativeLayout) itemView.findViewById(R.id.rllVideo);

        }
    }
}

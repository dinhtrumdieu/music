package com.example.trungdinh.appmusic.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.trungdinh.appmusic.R;
import com.example.trungdinh.appmusic.models.Abum;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class AbumRecycleAdapter extends RecyclerView.Adapter<AbumRecycleAdapter.AbumViewholder>{

    private Context mContext;
    private List<Abum> mListAbum;

    public AbumRecycleAdapter(Context context, List<Abum> list) {
        this.mContext = context;
        this.mListAbum = list;
    }

    @Override
    public AbumViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_abum_playlist_recycle,parent,false);
        return new AbumViewholder(view);
    }

    @Override
    public void onBindViewHolder(final AbumViewholder holder, int position) {
        Abum abum = mListAbum.get(position);

        holder.tvNameAbum.setText(abum.getNameAbum());
        holder.tvNameSinger.setText(abum.getNameSinger());

        Picasso.with(mContext).load(Integer.parseInt(abum.getImages())).error(R.drawable.img_abum).into(new Target(){

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.rllAbum.setBackground(new BitmapDrawable(mContext.getResources(), bitmap));
            }

            @Override
            public void onBitmapFailed(final Drawable errorDrawable) {
                Log.d("TAG", "FAILED");
            }

            @Override
            public void onPrepareLoad(final Drawable placeHolderDrawable) {
                Log.d("TAG", "Prepare Load");
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mListAbum == null) ? 0: mListAbum.size();
    }

    static public class AbumViewholder extends RecyclerView.ViewHolder{

        TextView tvNameAbum;
        TextView tvNameSinger;
        RelativeLayout rllAbum;

        public AbumViewholder(View itemView) {
            super(itemView);

            tvNameAbum = (TextView) itemView.findViewById(R.id.tvNameAbum);
            tvNameSinger = (TextView) itemView.findViewById(R.id.tvNameSinger);
            rllAbum = (RelativeLayout) itemView.findViewById(R.id.rllAbum);
        }
    }
}

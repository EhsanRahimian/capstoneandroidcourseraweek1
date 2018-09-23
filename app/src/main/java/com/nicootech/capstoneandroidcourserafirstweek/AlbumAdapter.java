package com.nicootech.capstoneandroidcourserafirstweek;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.CustomViewHolder> {
    private ArrayList<Album> albumList;
    private Context mContext;

    public AlbumAdapter(Context mContext, ArrayList<Album> albumList) {
        this.albumList = albumList;
        this.mContext = mContext;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_row, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Album album = albumList.get(position);

        if (!TextUtils.isEmpty(album.getAlbum_name())) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.imageView.getLayoutParams();
            params.height = Resources.getSystem().getDisplayMetrics().widthPixels;
            params.width = Resources.getSystem().getDisplayMetrics().widthPixels;
            holder.imageView.requestLayout();
            Picasso.with(mContext)
                    .load(album.getImage_url())
                    .fit()
                    .into(holder.imageView);
        }

        holder.textView.setText(album.getAlbum_name());
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

class CustomViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView textView;

    CustomViewHolder(View view) {
        super(view);
        this.imageView = (ImageView) view.findViewById(R.id.row_image);
        this.textView = (TextView) view.findViewById(R.id.row_name);
    }
}
}

package com.example.afinal;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;
public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {

    private final Context context;
    private final int[] bannerImages;

    public BannerAdapter(Context context, int[] bannerImages) {
        this.context = context;
        this.bannerImages = bannerImages;
    }

    @Override
    public BannerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new BannerViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(BannerViewHolder holder, int position) {
        holder.imageView.setImageResource(bannerImages[position]);
    }

    @Override
    public int getItemCount() {
        return bannerImages.length;
    }

    public static class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public BannerViewHolder(ImageView imageView) {
            super(imageView);
            this.imageView = imageView;
        }
    }
}



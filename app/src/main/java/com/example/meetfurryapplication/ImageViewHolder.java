package com.example.meetfurryapplication;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class ImageViewHolder extends RecyclerView.ViewHolder {

    View view;

    public ImageViewHolder(View itemView){
        super(itemView);

        view = itemView;

    }

    public void setDetails(Context context, String imgUrl) {
        ImageView img = view.findViewById(R.id.imgAdd);
        Picasso.get().load(R.drawable.image).into(img);

        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        itemView.startAnimation(animation);
    }
}

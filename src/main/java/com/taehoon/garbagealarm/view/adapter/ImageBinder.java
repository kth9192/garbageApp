package com.taehoon.garbagealarm.view.adapter;


import androidx.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by kth919 on 2017-06-06.
 */

public class ImageBinder {

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView imageView, String resID){
        Glide.with(imageView.getContext()).load(resID).into(imageView);
//        Log.d("확인" , resID);
    }
    @BindingAdapter({"imageInt"})
    public static void loadImage(ImageView imageView, int resID){
        Glide.with(imageView.getContext()).load(resID).apply(new RequestOptions().centerCrop()).into(imageView);
    }
}

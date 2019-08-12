package com.app.eduapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.eduapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    Context mContext;
    List<String> imageurls;
    private LayoutInflater layoutInflater;
    int pos=0;

    public ViewPagerAdapter(Context mContext, List<String> imageurls,int position) {
        this.mContext = mContext;
        this.imageurls = imageurls;
        this.pos=position;
        this.layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return imageurls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = this.layoutInflater.inflate(R.layout.custom_imageview, container, false);
        ImageView imageView = view.findViewById(R.id.img_image);
        final ProgressBar progressbar = view.findViewById(R.id.progressbar);
        Log.d("ImagesUrl", imageurls.get(position));
        position=pos;
        Picasso.get()
                .load(imageurls.get(position))
                .into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        progressbar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
       // pos=0;


        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(((View) object));
    }
}

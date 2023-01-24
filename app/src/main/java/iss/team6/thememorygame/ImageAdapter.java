package iss.team6.thememorygame;


import static iss.team6.thememorygame.ImageLoader.mainActivity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;
import java.util.List;


 class ImageAdapter extends BaseAdapter{
     private ArrayList<String> imageUrls;
     private Context context;

     public ImageAdapter(ArrayList<String> imageUrls,Context context) {
         this.imageUrls = imageUrls;
         this.context = context;
     }
     @Override
     public int getCount() {
         return imageUrls.size();
     }

     @Override
     public Object getItem(int position) {
         return imageUrls.get(position);
     }

     @Override
     public long getItemId(int position) {
         return position;
     }

     @Override
     public View getView(int position, View convertView, ViewGroup viewGroup) {
         ImageView imageView;
         if (convertView == null) {
             imageView = new ImageView(context);
             imageView.setLayoutParams(new GridView.LayoutParams(230, 240));
             imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

         } else {
             imageView = (ImageView) convertView;
         }

         String url = (String) getItem(position);
         new ImageLoader(mainActivity).execute(imageView, url);
         return imageView;
     }
 }


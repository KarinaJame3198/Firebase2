package com.example.laujame.firebase2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.LayoutInflaterCompat;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import com.squareup.picasso.Target;

import java.util.List;

import modelodedatos.Upload;

public class MyImageAdapater extends RecyclerView.Adapter<MyImageAdapater.ImageViewHolder> {

    private Context mContext;
    private List<Upload> mUploads;

    public MyImageAdapater(Context mContext, List<Upload> mUploads){
        this.mContext= mContext;
        this.mUploads= mUploads;

    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View v= LayoutInflater.from(mContext).inflate(R.layout.image_item, viewGroup, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int position) {
    Upload uploadCurrent=mUploads.get(position);
    imageViewHolder.lblViewName.setText(uploadCurrent.getName());
    //valor de la imagen
        Picasso.with(mContext).load(uploadCurrent.getImageUrl()).placeholder(R.mipmap.ic_launcher).fit()
                .centerCrop().into((Target) imageViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }



    public class ImageViewHolder extends RecyclerView.ViewHolder {
  public TextView lblViewName;
  public TextView imageView;

        public ImageViewHolder(View v) {
            super(v);
            lblViewName=itemView.findViewById(R.id.lblTitleCardView);
            imageView= itemView.findViewById(R.id.imgCardView);
        }
    }
}

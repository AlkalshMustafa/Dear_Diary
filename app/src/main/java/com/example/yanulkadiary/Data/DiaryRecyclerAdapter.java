package com.example.yanulkadiary.Data;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.WithHint;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yanulkadiary.Model.Diary;
import com.example.yanulkadiary.R;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;


public class DiaryRecyclerAdapter extends RecyclerView.Adapter<DiaryRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Diary> DiaryList;

    public DiaryRecyclerAdapter(Context context, List<Diary> diaryList) {
        this.context = context;
        DiaryList = diaryList;
    }

    @NonNull
    @Override
    public DiaryRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.postrow, parent, false);

        return new ViewHolder(view, context);    }

    @Override
    public void onBindViewHolder(@NonNull DiaryRecyclerAdapter.ViewHolder holder, int position) {

        Diary diary = DiaryList.get(position);
        String imageUrl = null;
        String profImageUrl = null;

        // setUp the widgets
        holder.title.setText(diary.getTitle());
        holder.desc.setText(diary.getDesc());
        holder.userName.setText(diary.getFullUserName());


       java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
       String formattedDate = dateFormat.format(new Date(new Date(Long.valueOf(diary.getTimestamp())).getTime()));
       holder.timestamp.setText(formattedDate);

       imageUrl = diary.getImage();
       //Use Picasso library to load image
        Picasso.with(context)
                .load(imageUrl)
                .into(holder.image);

        profImageUrl = diary.getProfileImg();
        Picasso.with(context)
                .load(profImageUrl)
                .into(holder.profileImg);


    }

    @Override
    public int getItemCount() {
        return DiaryList.size();
    }


    // Setting app the card Items
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public TextView desc;
        public TextView timestamp;
        public ImageView image;
        public ImageView profileImg;
        public TextView userName;
        String userId;

        public ViewHolder(View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            profileImg = (ImageView) itemView.findViewById(R.id.profileImgID);
            userName = (TextView) itemView.findViewById(R.id.fullNameID);
            timestamp = (TextView) itemView.findViewById(R.id.timestampList);
            image   = (ImageView) itemView.findViewById(R.id.crdImageViewID);
            title   = (TextView) itemView.findViewById(R.id.titleEdt);
            desc    = (TextView)  itemView.findViewById(R.id.postDescID);

            userId = null;

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//        });
        }
    }
}

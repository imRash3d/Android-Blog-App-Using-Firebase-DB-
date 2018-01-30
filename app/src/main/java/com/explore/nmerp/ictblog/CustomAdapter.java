package com.explore.nmerp.ictblog;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.security.PublicKey;
import java.util.ArrayList;

/**
 * Created by NMERP on 13-Nov-17.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.IteamViewHolder> {
    private Context context;
    private  ArrayList<BlogPost> postList;

    public CustomAdapter(Context context, ArrayList<BlogPost> postList) {
        this.context=context;
        this.postList=postList;
    }

    @Override
    public IteamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
        View  view = layoutInflater.inflate(R.layout.custom_post,parent,false);
        IteamViewHolder iteamViewHolder= new IteamViewHolder(view,context,postList);

        return iteamViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomAdapter.IteamViewHolder holder, int position) {
    holder.titleTV.setText(postList.get(position).getTitle());
        Picasso.with(context)
                .load(postList.get(position).getPost_img())
                .placeholder(R.drawable.img13827)
                .into(holder.postimgIV);

        holder.showusername.setText(postList.get(position).getUser_name());
        holder.showtitme.setText(postList.get(position).getTime());


    }

    @Override
    public int getItemCount() {
        if(postList!=null) {
            return postList.size();
        }
        return  0;

    }

    public class IteamViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener  {


        private CardView cview;
        private ImageView postimgIV;
        private TextView showusername;
        private TextView showtitme;
        private TextView titleTV;

        ArrayList<BlogPost> postiteam= new ArrayList<BlogPost>();
        Context context;
        public IteamViewHolder(View itemView , Context context,ArrayList<BlogPost> postiteam) {
            super(itemView);
            this.postiteam=postiteam;
            this.context=context;

            itemView.setOnClickListener(this);
            cview= (CardView) itemView.findViewById(R.id.cview);
            postimgIV= (ImageView) itemView.findViewById(R.id.postimgV);
            titleTV= (TextView) itemView.findViewById(R.id.titleTv);

            showusername= (TextView) itemView.findViewById(R.id.showusername);
            showtitme= (TextView) itemView.findViewById(R.id.showtitme);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            String key= postList.get(position).getPost_id();

            Intent intent= new Intent(this.context,SinglePost.class);
            intent.putExtra("key",key);
            this.context.startActivity(intent);

        }
    }

    public void searchfilter (ArrayList<BlogPost> list) {
        postList = new ArrayList<>();
        postList.addAll(list);
        notifyDataSetChanged();


    }
}

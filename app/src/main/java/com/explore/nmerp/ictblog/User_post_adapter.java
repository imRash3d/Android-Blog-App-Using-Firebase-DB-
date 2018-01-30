package com.explore.nmerp.ictblog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by NMERP on 19-Nov-17.
 */

public class User_post_adapter extends RecyclerView.Adapter<User_post_adapter.postIteamViewHolder> {
    private Context context;
    private ArrayList<BlogPost> postList;

public  User_post_adapter(Context context, ArrayList<BlogPost> postList) {

    this.context=context;
    this.postList=postList;
}
    @Override
    public postIteamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
        View  view = layoutInflater.inflate(R.layout.user_post_custom_row,parent,false);
        User_post_adapter.postIteamViewHolder iteamViewHolder= new User_post_adapter.postIteamViewHolder(view,context);

        return iteamViewHolder;
    }

    @Override
    public void onBindViewHolder(postIteamViewHolder holder, int position) {
        holder.titleTV.setText(postList.get(position).getTitle());
        Picasso.with(context)
                .load(postList.get(position).getPost_img())
                .placeholder(R.drawable.img13827)
                .into(holder.postimgIV);

        holder.postTV.setText(postList.get(position).getPost());
        holder.showtitme.setText(postList.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        if(postList!=null) {
            return postList.size();
        }
        return  0;

    }

    public class postIteamViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener ,View.OnLongClickListener {

        private CardView cview;
        private ImageView postimgIV;
        private TextView showtitme;
        private TextView titleTV;
        private TextView postTV;
        private Context context;
        public postIteamViewHolder(View itemView,Context context) {
            super(itemView);
            this.context=context;

            cview= (CardView) itemView.findViewById(R.id.cview);
            postimgIV= (ImageView) itemView.findViewById(R.id.postimgV);
            titleTV= (TextView) itemView.findViewById(R.id.titleTv);
            postTV= (TextView) itemView.findViewById(R.id.post);

            showtitme= (TextView) itemView.findViewById(R.id.showtitme);
           itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            String key= postList.get(position).getPost_id();

            Intent intent= new Intent(this.context,SinglePost.class);
            intent.putExtra("key",key);
            this.context.startActivity(intent);


        }

        @Override
        public boolean onLongClick(View v) {


            AlertDialog.Builder dialog = new AlertDialog.Builder(this.context);
            dialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DatabaseReference postref= FirebaseDatabase.getInstance().getReference("Blog");
                    postref.child(postList.get(getAdapterPosition()).getPost_id()).removeValue();
                    Toast.makeText(context, "Post Deleted", Toast.LENGTH_SHORT).show();
                }
            });

            dialog.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent intent= new Intent(context,Edit_Post.class);
                    intent.putExtra("key",postList.get(getAdapterPosition()).getPost_id());
                    context.startActivity(intent);

                }
            });
            dialog.show();
            dialog.create();

                //Filter RecyclerView by placing a SearchView on Toolbar
            return true;
        }
    }

}

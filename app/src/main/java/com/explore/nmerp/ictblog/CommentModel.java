package com.explore.nmerp.ictblog;

import android.content.Context;
import android.content.DialogInterface;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.zip.CheckedOutputStream;

/**
 * Created by NMERP on 18-Nov-17.
 */

public class CommentModel extends RecyclerView.Adapter<CommentModel.iteamViewHolder> {

    private Context context;
    private  ArrayList<Comment> commentlist;
    public CommentModel(Context context, ArrayList<Comment> commentList) {
        this.commentlist=commentList;
        this.context=context;
    }

    @Override


    public iteamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
        View view= layoutInflater.inflate(R.layout.comment_layout,parent,false);
        iteamViewHolder commentView = new iteamViewHolder(view,commentlist,context);


        return commentView;
    }

    @Override
    public void onBindViewHolder(iteamViewHolder holder, int position) {

        holder.commentTV.setText(commentlist.get(position).getComment());
        holder.commenttimeTV.setText(commentlist.get(position).getTime());
        holder.commentusernameTV.setText(commentlist.get(position).getUsername());
        Picasso.with(context).load(commentlist.get(position).getImg()).error(R.drawable.profilepic).into(holder.commentUserPic);


    }



    @Override
    public int getItemCount() {
        if(commentlist!=null) {
            return commentlist.size();
        }
        return  0;
    }

    public class iteamViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {


        DatabaseReference comentref;
        FirebaseAuth userAuth;

        private TextView commentTV,commenttimeTV,commentusernameTV;
        private ImageView commentUserPic;
        private  ArrayList<Comment> list;
        private Context context;
        public iteamViewHolder(View itemView,ArrayList<Comment>list,Context context) {
            super(itemView);
            this.list=list;
            this.context=context;
            commentTV = (TextView) itemView.findViewById(R.id.comment);
            commenttimeTV = (TextView) itemView.findViewById(R.id.commenttime);
            commentusernameTV = (TextView) itemView.findViewById(R.id.commentusername);
            commentUserPic = (ImageView) itemView.findViewById(R.id.commentUserPic);

     itemView.setOnLongClickListener(this);


        }




        @Override
        public boolean onLongClick(View v) {
            AlertDialog.Builder dialog=new AlertDialog.Builder(context);
            dialog.setMessage("Are You Sure You want to delete this ?");



            dialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String id= list.get(getAdapterPosition()).gePostKey();

                    comentref= FirebaseDatabase.getInstance().getReference("Comments").child(id);
                    String comment_key= list.get(getAdapterPosition()).getCommnent_key();

                    comentref.child(comment_key).removeValue();
                    Toast.makeText(context,"Comment Removed ", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.setPositiveButton("No",null);
            userAuth = FirebaseAuth.getInstance();
            if(userAuth.getCurrentUser().getUid().equals(list.get(getAdapterPosition()).getId())) {
                dialog.show();
                dialog.create();
            }




            return false;
        }
    }

}

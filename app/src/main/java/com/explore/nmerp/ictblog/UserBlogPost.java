package com.explore.nmerp.ictblog;

import android.app.ProgressDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserBlogPost extends AppCompatActivity {


    ArrayList<BlogPost> list=new ArrayList<>();
    private RecyclerView listView;

    private ProgressDialog dialog;
    private DatabaseReference blogPost;
    private Query userPost;
    private FirebaseAuth mauth;
    private MenuItem menuIteam ;
    private AlertDialog.Builder alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_blog_post);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dialog = new ProgressDialog(this);
        alertDialog = new AlertDialog.Builder(this);
        listView= (RecyclerView) findViewById(R.id.Postlist);

        listView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(linearLayoutManager);
        mauth= FirebaseAuth.getInstance();
        blogPost= FirebaseDatabase.getInstance().getReference().child("Blog");
        blogPost.keepSynced(true);
    }

    protected void onStart() {
        super.onStart();

        dialog.setMessage("Loding.......");
        dialog.show();
        String current_user= mauth.getCurrentUser().getUid();
        userPost= blogPost.orderByChild("user_Id").equalTo(current_user);
        userPost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot postObj : dataSnapshot.getChildren() ) {

                           BlogPost blogPost = postObj.getValue(BlogPost.class);


                        list.add(blogPost);



                }

                User_post_adapter customAdapter= new User_post_adapter(UserBlogPost.this,list);
                listView.setAdapter(customAdapter);

                dialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home) {
            finish();

        }

        return super.onOptionsItemSelected(item);
    }
}

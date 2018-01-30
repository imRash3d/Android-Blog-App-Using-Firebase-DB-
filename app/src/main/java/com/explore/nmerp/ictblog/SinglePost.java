package com.explore.nmerp.ictblog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SinglePost extends AppCompatActivity {
    private ArrayList<Comment> commentArrayList=new ArrayList<>();
    private RelativeLayout commentbox;
    private EditText commentEt;
    private ImageView postimgIV;
    private TextView showusername;
    private TextView showtitme;
    private TextView titleTV;
    private TextView showlike;
    private TextView postTv;
    private String key;
    private DatabaseReference   userRef;
    private DatabaseReference singlePostref;
    private DatabaseReference likeref;
    private boolean likeBtnClcked =false;
    private boolean isLogin =false;
    private FirebaseAuth mauth;
    private ImageButton likeBtn;
    private RecyclerView commentList;
    private  String u_id="Default";
private DatabaseReference  mdblike;
    private  DatabaseReference commentRef;

    private    long count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        titleTV = (TextView) findViewById(R.id.titleTv);
        postTv = (TextView) findViewById(R.id.postTv);
        showlike = (TextView) findViewById(R.id.showlike);
        showusername = (TextView) findViewById(R.id.showusername);
        showtitme = (TextView) findViewById(R.id.showtitme);
        postimgIV = (ImageView) findViewById(R.id.postimgV);
        likeBtn = (ImageButton) findViewById(R.id.likeBtn);
        Intent intent= getIntent();
         key= intent.getStringExtra("key");
        commentList = (RecyclerView) findViewById(R.id.commentList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        commentList.setHasFixedSize(true);
        commentList.setLayoutManager(linearLayoutManager);

        commentEt = (EditText) findViewById(R.id.usercomment);

        commentbox= (RelativeLayout) findViewById(R.id.commentbox);

        mauth= FirebaseAuth.getInstance();
        likeref = FirebaseDatabase.getInstance().getReference("Likes").child(key);
         mdblike = FirebaseDatabase.getInstance().getReference("Likes").child(key);
        singlePostref = FirebaseDatabase.getInstance().getReference("Blog").child(key);


        commentRef = FirebaseDatabase.getInstance().getReference("Comments").child(key);
        singlePostref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               String title= (String) dataSnapshot.child("title").getValue();
               String dec= (String) dataSnapshot.child("post").getValue();
               String img_link= (String) dataSnapshot.child("post_img").getValue();
               String time= (String) dataSnapshot.child("time").getValue();
               String username= (String) dataSnapshot.child("user_name").getValue();
                titleTV.setText(title);
                postTv.setText(dec);
                showusername.setText(username);
                showtitme.setText(time);
                showtitme.setText(time);
                mdblike.keepSynced(true);
                Picasso.with(getApplicationContext()).load(img_link).error(R.drawable.img13827).into(postimgIV);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


    @Override
    protected void onStart() {
        super.onStart();
        checkLike();
        count_like();
        setlike_btn();
        showComment();
        if(mauth.getCurrentUser()!=null ) {
            userRef =   FirebaseDatabase.getInstance().getReference().child("Users").child(mauth.getCurrentUser().getUid());
            commentbox.setVisibility(View.VISIBLE);
        }

    }

    public void likeBtn(View view) {

        if(isLogin) {

            if(likeBtnClcked) {
                likeref.child(mauth.getCurrentUser().getUid()).removeValue();
                likeBtnClcked=false;
                likeBtn.setImageResource(R.mipmap.ic_thumb_up_black_24dp);
            }

            else {

                likeref.child(mauth.getCurrentUser().getUid()).setValue("liked");
                likeBtnClcked=true;
                likeBtn.setImageResource(R.mipmap.ic_thumb_up_orange_24dp);


            }



        }
        else {

            Toast.makeText(this, "Please Login for like This Post", Toast.LENGTH_SHORT).show();

        }

/*
        if(mauth.getCurrentUser()!=null) {
           final DatabaseReference checkuserLiked= likeref.child(key);
            checkuserLiked.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild(mauth.getCurrentUser().getUid())) {

                        likeref.child(mauth.getCurrentUser().getUid()).removeValue();

                        //Toast.makeText(SinglePost.this, "You Already Liked this post ", Toast.LENGTH_SHORT).show();
                    }

                    else {

                        likeref.child(mauth.getCurrentUser().getUid()).setValue("liked");

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });




        }
        else {

            Toast.makeText(this, "Please Login for like This Post", Toast.LENGTH_SHORT).show();
        }
*/


    }

    public void checkLike() {



        if(mauth.getCurrentUser()!=null) {
            DatabaseReference checkuserLiked= likeref.child(key);
            checkuserLiked.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild(mauth.getCurrentUser().getUid())) {


                        likeBtnClcked=true;


                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



            isLogin=true;


        }
        else {
            likeBtn.setImageResource(R.mipmap.ic_thumb_up_black_24dp);

            isLogin=false;
        }



    }


    public void count_like () {
        likeref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                  count = dataSnapshot.getChildrenCount();
                showlike.setText(count+"");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public  void setlike_btn () {
        if(mauth.getCurrentUser()!=null) {



        mdblike.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(mauth.getCurrentUser().getUid())) {

                    likeBtn.setImageResource(R.mipmap.ic_thumb_up_orange_24dp);
                }
                else  {

                    likeBtn.setImageResource(R.mipmap.ic_thumb_up_black_24dp);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        }
        else {

            likeBtn.setImageResource(R.mipmap.ic_thumb_up_black_24dp);
        }

    }

    public  void showComment () {



        commentRef.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                commentArrayList.clear();

                for (DataSnapshot commentobj: dataSnapshot.getChildren() ) {
                   Comment comment = commentobj.getValue(Comment.class);
                    commentArrayList.add(comment);
                    comment.setPostKey(key);

                    String c_key= commentobj.getKey();
                    comment.setCommnent_key(c_key);
                    String user_id= (String) commentobj.child("id").getValue();
                    comment.setId(user_id);

                }


               // Comment comment = new Comment("jdjd","eee","sdndskd","eweenh","heudjdu");
                //commentArrayList.add(comment);
                CommentModel commentModel = new CommentModel(SinglePost.this,commentArrayList);
                commentList.setAdapter(commentModel);





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void postComment(View view) {

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String current_time = DateFormat.getDateInstance().format(new Date());
                String comment= commentEt.getText().toString();
                String user_id=mauth.getCurrentUser().getUid();
                String user_img= (String) dataSnapshot.child("image").getValue();
                String user_name= (String) dataSnapshot.child("name").getValue();
                if(!TextUtils.isEmpty(comment)) {
                    Comment singleComment = new Comment(user_id,current_time,user_name,user_img,comment);

                    commentRef.push().setValue(singleComment);



                    commentEt.setText("");
                }
                else  {

                    Toast.makeText(SinglePost.this, "Write Something !!!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

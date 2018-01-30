package com.explore.nmerp.ictblog;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MyProfile extends AppCompatActivity {
    private FirebaseAuth profileAuth;
    private DatabaseReference profileRef;
    private ImageView profileImg;
    private TextView showusernameTV,showEmailTv;
    AlertDialog.Builder dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        profileAuth = FirebaseAuth.getInstance();
        profileRef = FirebaseDatabase.getInstance().getReference().child("Users");
        profileImg = (ImageView) findViewById(R.id.profileImg);
        showusernameTV = (TextView) findViewById(R.id.showusername);
        showEmailTv = (TextView) findViewById(R.id.showEmail);
        dialog = new AlertDialog.Builder(this);
    }


    @Override
    protected void onStart() {
        super.onStart();

        Check_user_isExist();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home) {
            finish();
        }

        if(item.getItemId()==R.id.logout) {

            dialog.setMessage("Are You Sure You want to Logout ? ");

            dialog.create();
            dialog.show();
            dialog.setNegativeButton("No",null);
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    profileAuth.signOut();
                    startActivity(new Intent(MyProfile.this,HomePage.class));
                }
            });


        }




        return super.onOptionsItemSelected(item);
    }

    public void Check_user_isExist () {


        if(profileAuth.getCurrentUser()==null) {

            Intent loginIntent = new Intent(MyProfile.this,LoginPage.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);


        }
        else {

//            Intent loginIntent = new Intent(MyProfile.this,LoginPage.class);
//            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(loginIntent);


            ShowProfile();

        }

    }

    public  void ShowProfile () {

        profileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String p_id=profileAuth.getCurrentUser().getUid();

                if(dataSnapshot.hasChild(p_id)) {
                    String img_url= dataSnapshot.child(p_id).child("image").getValue().toString();
                    showusernameTV.setText(profileAuth.getCurrentUser().getEmail());
                    showEmailTv.setText(dataSnapshot.child(p_id).child("name").getValue().toString());
                    Picasso.with(MyProfile.this).load(img_url).error(R.drawable.profilepic).into(profileImg);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MyProfile.this, "Server Is Busy Please Come Back Later", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void MyPost(View view) {

        startActivity(new Intent(MyProfile.this,UserBlogPost.class));
    }
}

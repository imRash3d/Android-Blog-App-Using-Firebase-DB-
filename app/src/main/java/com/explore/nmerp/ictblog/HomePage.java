package com.explore.nmerp.ictblog;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class HomePage extends AppCompatActivity  implements SearchView.OnQueryTextListener{
ArrayList<BlogPost> list=new ArrayList<>();
    private RecyclerView listView;
private FloatingActionButton addBtn;
private ProgressDialog dialog;
    private DatabaseReference blogPost;
    private FirebaseAuth mauth;
    private MenuItem menuIteam ;
    private CustomAdapter customAdapter;
    private AlertDialog.Builder alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        dialog = new ProgressDialog(this);
        alertDialog = new AlertDialog.Builder(this);
        addBtn = (FloatingActionButton) findViewById(R.id.add_Blog_post);
        listView= (RecyclerView) findViewById(R.id.Postlist);

        listView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(linearLayoutManager);
        mauth= FirebaseAuth.getInstance();
        blogPost= FirebaseDatabase.getInstance().getReference().child("Blog");
        blogPost.keepSynced(true);

    }


    @Override
    protected void onStart() {
        super.onStart();
        check_user();
            dialog.setMessage("Loding.......");
        dialog.show();
        blogPost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    list.clear();
                for(DataSnapshot postObj : dataSnapshot.getChildren() ) {
                    BlogPost blogPost= postObj.getValue(BlogPost.class);

                    list.add(blogPost);

                }
                 customAdapter= new CustomAdapter(HomePage.this,list);
                listView.setAdapter(customAdapter);

                dialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        if(mauth.getCurrentUser()==null) {
            getMenuInflater().inflate(R.menu.guest_menu,menu);
        }

        else {
            getMenuInflater().inflate(R.menu.main_menu,menu);
        }

MenuItem menuItem= menu.findItem(R.id.search);

        SearchView searchView= (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {




        if(item.getItemId()==R.id.logout) {
            alertDialog.setMessage("Are You Sure You want to Logout ? ");


            alertDialog.setNegativeButton("No",null);
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mauth.signOut();
                    startActivity(new Intent(HomePage.this,HomePage.class));
                }
            });

            alertDialog.show();
            alertDialog.create();


        }
        if(item.getItemId()==R.id.profile || item.getItemId()==R.id.showprofile) {
         startActivity(new Intent(HomePage.this,MyProfile.class));


        }


        if(item.getItemId()==R.id.login) {
            startActivity(new Intent(HomePage.this,LoginPage.class));


        }

        if(item.getItemId()==R.id.signUp) {
            startActivity(new Intent(HomePage.this,RegisterActivity.class));


        }





        return super.onOptionsItemSelected(item);
    }

    public  void check_user () {

        if (mauth.getCurrentUser()!=null) {



            addBtn.setVisibility(View.VISIBLE);
        }
        else  {

        }



    }



    public void addBlogPost(View view) {


        startActivity(new Intent(HomePage.this,Add_new_post.class));

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        newText = newText.toLowerCase();
        ArrayList<BlogPost> arrayList = new ArrayList<>();
        for(BlogPost post : list) {

            String title = post.getTitle().toLowerCase();
            if(title.contains(newText)) {

                arrayList.add(post);
                customAdapter.searchfilter(arrayList);
            }

        }
        return true;
    }
}

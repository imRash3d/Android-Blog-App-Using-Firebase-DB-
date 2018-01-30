package com.explore.nmerp.ictblog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;

public class Edit_Post extends AppCompatActivity {
    private ImageButton postImg;
    private EditText titleET,postET;
    private static final int GALLERY_REQUSET=1;
    private ProgressDialog progressDialog;
    private FirebaseAuth mauth;
    private Uri imageUri;
    private String key;
    private StorageReference imgStorage;
    private String img_downldlink;
    private DatabaseReference postRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent= getIntent();
        key= intent.getStringExtra("key");
        setContentView(R.layout.activity_edit__post);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        titleET= (EditText) findViewById(R.id.EditTitle);
        postET= (EditText) findViewById(R.id.Editpost);
        postImg= (ImageButton) findViewById(R.id.EditPostImg);
        progressDialog = new ProgressDialog(this);

        postRef = FirebaseDatabase.getInstance().getReference("Blog").child(key);
        imgStorage = FirebaseStorage.getInstance().getReference("Photos");
        mauth= FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();

        showpost();
    }

    @Override




    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home) {

            finish();
        }

        return super.onOptionsItemSelected(item);

    }

    public void AddImage(View view) {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUSET);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode==GALLERY_REQUSET) {
            imageUri = data.getData();
            progressDialog.setMessage("Uploading Image");
            progressDialog.show();
            postImg.setImageURI(imageUri);
            StorageReference filepath= imgStorage.child("Blog_post_img").child(imageUri.getLastPathSegment());
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    img_downldlink= taskSnapshot.getDownloadUrl().toString();
                    postImg.setImageURI(imageUri);

                    progressDialog.dismiss();

                }
            });





        }else {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_SHORT).show();

        }
    }


    public void EditPost(View view) {
        progressDialog.setMessage("Updating Post");
        progressDialog.show();
        final String title= titleET.getText().toString().trim();
        final String post= postET.getText().toString().trim();
        final String user_id= mauth.getCurrentUser().getUid();

        DatabaseReference usernameRef= FirebaseDatabase.getInstance().getReference("Users").child(user_id);
        usernameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final  String current_time = DateFormat.getDateInstance().format(new Date());
                String name= (String) dataSnapshot.child("name").getValue();
                BlogPost blogPost= new BlogPost(title,post,img_downldlink,user_id,name,current_time,key);
                postRef.setValue(blogPost);

                Toast.makeText(Edit_Post.this, "Post Updated Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Edit_Post.this,UserBlogPost.class));
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void showpost () {


        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String title= (String) dataSnapshot.child("title").getValue();
                String imglink= (String) dataSnapshot.child("post_img").getValue();
                String  post = (String) dataSnapshot.child("post").getValue();
img_downldlink=imglink;
                titleET.setText(title);
                postET.setText(post);
                Picasso.with(Edit_Post.this).load(imglink).into(postImg);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}

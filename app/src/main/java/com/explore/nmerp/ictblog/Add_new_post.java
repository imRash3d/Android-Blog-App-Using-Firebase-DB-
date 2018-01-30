package com.explore.nmerp.ictblog;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.text.DateFormat;
import java.util.Date;

public class Add_new_post extends AppCompatActivity {
private ImageButton postImg;
StorageReference imageStorage;
    private EditText titleET,postET;
    private static final int GALLERY_REQUSET=1;
    private ProgressDialog progressDialog;
    private Uri imageUri;
private DatabaseReference databaseref;
    private FirebaseAuth mauth;
    private FirebaseAuth.AuthStateListener mauthlistener;

    private DatabaseReference loginref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_post);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        titleET= (EditText) findViewById(R.id.title);
        postET= (EditText) findViewById(R.id.post);
        postImg= (ImageButton) findViewById(R.id.PostImg);
        progressDialog= new ProgressDialog(this);
        imageStorage = FirebaseStorage.getInstance().getReference("Photos");
    databaseref= FirebaseDatabase.getInstance().getReference("Blog");
        loginref = FirebaseDatabase.getInstance().getReference().child("Users");
        mauth= FirebaseAuth.getInstance();
        mauthlistener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {


                if(firebaseAuth.getCurrentUser()!=null) {

                    checkUser_Exist();
                }
                else {

                    Intent loginIntent = new Intent(Add_new_post.this,LoginPage.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };
    }


    public void checkUser_Exist() {

        loginref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String user_id= mauth.getCurrentUser().getUid();
                if(!dataSnapshot.hasChild(user_id)) {

                    Intent loginIntent= new Intent(Add_new_post.this,SetUpAccount.class);

                    startActivity(loginIntent);
                }


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
        if(item.getItemId()==R.id.logout) {
             mauth.signOut();


        }
        return super.onOptionsItemSelected(item);
    }








    @Override
    protected void onStart() {
        super.onStart();
      //  checkUser_Exist();
        mauth.addAuthStateListener(mauthlistener);

    }

    public void AddImage(View view) {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUSET);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode==GALLERY_REQUSET) {
           imageUri = data.getData();

            postImg.setImageURI(imageUri);





        }else {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_SHORT).show();

        }
    }

    public void addPost(View view) {
        progressDialog.setMessage("Posting...");
        progressDialog.show();
        final String title= titleET.getText().toString().trim();
        final String post= postET.getText().toString().trim();
        final String user_id= mauth.getCurrentUser().getUid();
        if(imageUri != null && !imageUri.equals(Uri.EMPTY) && !TextUtils.isEmpty(title) && !TextUtils.isEmpty(post) ){


            StorageReference filepath= imageStorage.child("Blog_post_img").child(imageUri.getLastPathSegment());
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    final Uri downloadPath=taskSnapshot.getDownloadUrl();

                    final String key=  databaseref.push().getKey();
                   final  String current_time = DateFormat.getDateInstance().format(new Date());


                    DatabaseReference usernameRef= FirebaseDatabase.getInstance().getReference("Users").child(user_id);
                    usernameRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String name= (String) dataSnapshot.child("name").getValue();
                            BlogPost blogPost= new BlogPost(title,post,downloadPath.toString(),user_id,name,current_time,key);
                            //databaseref.push().child(key);
                            databaseref.child(key).setValue(blogPost);

                            Toast.makeText(Add_new_post.this, "Post Upload Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Add_new_post.this,HomePage.class));
                            progressDialog.dismiss();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });













                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Add_new_post.this, "Upload Fail ! Try Again ", Toast.LENGTH_SHORT).show();
                }
            });

        }


        else {
            progressDialog.dismiss();
            Toast.makeText(this, "You Have Missed Something !! Try Again ", Toast.LENGTH_SHORT).show();


        }

    }
}

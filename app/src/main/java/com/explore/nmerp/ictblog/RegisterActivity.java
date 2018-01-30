package com.explore.nmerp.ictblog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameET,passwordET,emailET;
    private ImageButton profileImg;
    private FirebaseAuth mauth;
    private DatabaseReference databaseReference;
    private ProgressDialog dialog;
private static  final int IMG_REQUEST_CODE=1;
    private StorageReference userSignU;
    private Uri imgUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        profileImg = (ImageButton) findViewById(R.id.profileImg);
        nameET = (EditText) findViewById(R.id.userName);
        passwordET = (EditText) findViewById(R.id.userPassword);
        emailET = (EditText) findViewById(R.id.userEmail);
        dialog = new ProgressDialog(this);
        mauth= FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");
        userSignU = FirebaseStorage.getInstance().getReference("Photos");

    }


    public void addImg(View view) {
        Intent uploadImg= new Intent(Intent.ACTION_PICK);
        uploadImg.setType("image/*");
        startActivityForResult(uploadImg,IMG_REQUEST_CODE);

    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMG_REQUEST_CODE && resultCode==RESULT_OK) {

            imgUrl= data.getData();
            profileImg.setImageURI(imgUrl);
        }
        else {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_SHORT).show();

        }
    }


    public void SignUp(View view) {

        final String name= nameET.getText().toString().trim();
        final String email= emailET.getText().toString().trim();
        final String password= passwordET.getText().toString().trim();

        if(imgUrl != null && !imgUrl.equals(Uri.EMPTY) && !TextUtils.isEmpty(name)&&!TextUtils.isEmpty(email)&& !TextUtils.isEmpty(password)) {
            dialog.setMessage("Sign Up...");
            dialog.show();
            mauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()) {


                        StorageReference filepath = userSignU.child("User_photos").child(imgUrl.getLastPathSegment());

                        filepath.putFile(imgUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                               String  imageurl = taskSnapshot.getDownloadUrl().toString();
                                String user_id= mauth.getCurrentUser().getUid();
                                UserModel user= new UserModel(name,imageurl);
                                databaseReference.child(user_id).setValue(user);
                                dialog.dismiss();
                                startActivity(new Intent(RegisterActivity.this,HomePage.class));

                            }


                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "Upload Fail ! Try Again ", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                }
            });







        }

        else {
            Toast.makeText(this, "Something is Missing ", Toast.LENGTH_SHORT).show();

        }

    }
}

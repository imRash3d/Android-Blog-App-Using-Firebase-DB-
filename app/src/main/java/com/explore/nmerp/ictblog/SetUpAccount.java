package com.explore.nmerp.ictblog;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

public class SetUpAccount extends AppCompatActivity {
    private FirebaseAuth mAuth;
   // private FirebaseAuth.AuthStateListener mlistener;
    private ImageButton profileImg;
    private Uri imgUrl;

    private DatabaseReference setUpaccountref;
    private TextView nameET;
private final  static  int IMG_REQUEST_CODE=1;
   private  StorageReference imageStorage;

    private  ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_account);
        mAuth= FirebaseAuth.getInstance();
        profileImg = (ImageButton) findViewById(R.id.profileImg);
        dialog = new ProgressDialog(this);
        nameET= (TextView) findViewById(R.id.nameET);
        imageStorage = FirebaseStorage.getInstance().getReference("Photos");
        setUpaccountref = FirebaseDatabase.getInstance().getReference().child("Users");
    }




    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.logout) {

            mAuth.signOut();
        }


        if(item.getItemId()==R.id.signUp) {
            startActivity(new Intent(SetUpAccount.this,RegisterActivity.class));


        }
        return super.onOptionsItemSelected(item);
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

    public void setupAccount(View view) {


        dialog.setMessage("SetUp Account..");
        dialog.show();

        if(imgUrl != null && !imgUrl.equals(Uri.EMPTY) && !TextUtils.isEmpty(nameET.getText().toString())) {




            StorageReference filepath= imageStorage.child("User_photos").child(imgUrl.getLastPathSegment());

            filepath.putFile(imgUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                     Uri downlodlink = taskSnapshot.getDownloadUrl();
                    String id= mAuth.getCurrentUser().getUid();
                    String name= nameET.getText().toString().trim();
                    if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(id) && !TextUtils.isEmpty(downlodlink.toString())) {

                        UserModel user = new UserModel(name,downlodlink.toString());
                        setUpaccountref.child(id).setValue(user);
                        Toast.makeText(SetUpAccount.this, "Account Setup Successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    else {

                        Toast.makeText(SetUpAccount.this, "You Missed Something ", Toast.LENGTH_SHORT).show();
                    }

                }


            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(SetUpAccount.this, "Upload Fail ! Try Again ", Toast.LENGTH_SHORT).show();
                }
            });
        }
   else {
            dialog.dismiss();

            Toast.makeText(SetUpAccount.this, "Something Wrong ", Toast.LENGTH_SHORT).show();
        }









    }
}

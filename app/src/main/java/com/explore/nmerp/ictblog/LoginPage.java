package com.explore.nmerp.ictblog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginPage extends AppCompatActivity {
    private FirebaseAuth loginAuth;
private EditText useremailET,userPasswordEt;
private ProgressDialog dialog;
    private FirebaseAuth.AuthStateListener mauthlistener;
    DatabaseReference loginref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        useremailET= (EditText) findViewById(R.id.loginemail);
        userPasswordEt= (EditText) findViewById(R.id.loginPassword);
        dialog= new ProgressDialog(this);
        loginAuth= FirebaseAuth.getInstance();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        loginref = FirebaseDatabase.getInstance().getReference().child("Users");
        mauthlistener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {


                if(firebaseAuth.getCurrentUser()!=null) {
                    Intent loginIntent = new Intent(LoginPage.this,HomePage.class);

                    startActivity(loginIntent);

                }
            }
        };









    }












    @Override
    protected void onStart() {
        super.onStart();
        loginAuth.addAuthStateListener(mauthlistener);

    }

    public void LoginUser(View view) {
        String email= useremailET.getText().toString().trim();
        String password= userPasswordEt.getText().toString().trim();

        if(!TextUtils.isEmpty(email)&& !TextUtils.isEmpty(password)) {
            dialog.setMessage("Login...");
            dialog.show();
            loginAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {


                    if(task.isSuccessful()) {

                        checkDatabaseUser_Exist();


                    }
                    else  {
                        Toast.makeText(LoginPage.this, "Login Failed ! Invalid Username & Password", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            });
        }
        else {

            Toast.makeText(this, "Something  Missing ", Toast.LENGTH_SHORT).show();
        }

    }


    public void SignUpUser(View view) {

        startActivity(new Intent(LoginPage.this,RegisterActivity.class));
    }

    public  void checkDatabaseUser_Exist () {

        dialog.dismiss();
        loginref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String user_id= loginAuth.getCurrentUser().getUid();
                if(dataSnapshot.hasChild(user_id)) {

                   Intent loginIntent= new Intent(LoginPage.this,HomePage.class);
                    startActivity(loginIntent);
                }
                else {

                    Intent loginIntent= new Intent(LoginPage.this,SetUpAccount.class);
                    startActivity(loginIntent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }





    public void googleBtn(View view) {

        // Configure Google Sign In //



    }

    public void homeBtn(View view) {

        startActivity(new Intent(LoginPage.this,HomePage.class));
    }
}

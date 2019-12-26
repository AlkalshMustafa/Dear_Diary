package com.example.yanulkadiary.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yanulkadiary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;


    private EditText emailEdt;
    private EditText passwordEdt;
    private Button signInBtn;
    private Button   signUpBtn;
    private ProgressDialog mProgressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);


        emailEdt    = (EditText)findViewById(R.id.signInEmailEdt);
        passwordEdt = (EditText)findViewById(R.id.sgnInPswEdt);
        signInBtn = (Button) findViewById(R.id.signInBtn);
        signUpBtn = (Button) findViewById(R.id.signInSinUpBtn);



        signUpBtn.setOnClickListener(this);
        signInBtn.setOnClickListener(this);

        //=============== AuthStateListener ==================

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                mUser = firebaseAuth.getCurrentUser();  // to  check the state of auth. user is connecting or not.
                if (mUser != null) {
                    Toast.makeText(SignInActivity.this, "Signed In", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignInActivity.this, PostsActivity.class));
                    finish();
                }else {
                    Toast.makeText(SignInActivity.this, "Not Signed In", Toast.LENGTH_SHORT).show();
                }
            }
        };




    }
    /**
     *  ============ OnClick method ==============
     */

    @Override
    public void onClick(View v){

        switch (v.getId()) {
            //  ============== going to SignUp activity ==============
            case R.id.signInBtn:
                if (!TextUtils.isEmpty(emailEdt.getText().toString())
                        && !TextUtils.isEmpty(passwordEdt.getText().toString())) {

                    mProgressDialog.setMessage("Signing In...");
                    mProgressDialog.show();
                    String email = emailEdt.getText().toString();
                    String pwd = passwordEdt.getText().toString();

                    signIn(email, pwd);

                }else {

                    Toast.makeText(SignInActivity.this, "Insert Valid Information",
                            Toast.LENGTH_SHORT).show();

                }
                break;
            //  ============== signIn an go to main activity ==============
            case R.id.signInSinUpBtn:
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
        }
    }

    /**
     *  ============ SignIn method ==============
     */
    private void signIn(String email, String pwd) {

        mAuth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(SignInActivity.this, "Signed in", Toast.LENGTH_SHORT)
                                    .show();

                            Intent intent = new Intent(SignInActivity.this, PostsActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(SignInActivity.this, "SignIn Unsuccessful",
                                    Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                        }

                    }
                });
    }

    /**
     *  ============ onStart method ==============
     */
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);  // to  check the state of auth. user is connecting or not.


    }
}

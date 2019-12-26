package com.example.yanulkadiary.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.yanulkadiary.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;

    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;
    private StorageReference mFireBaseStorage;


    private EditText userName;
    private EditText emailEdt;
    private EditText passwordEdt;
    private Button   signUpBtn;
    private Button signInBtn;
    private ImageButton profilePic;

    private Uri resultUri;
    private static final int GALLERY_CODE = 1;
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

       mDatabase = FirebaseDatabase.getInstance();
       mDatabaseReference = mDatabase.getReference().child("MUsers"); // create new database
        mAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);
        mFireBaseStorage = FirebaseStorage.getInstance().getReference().child("MBlog_Profile_Pics");



        userName    = (EditText) findViewById(R.id.signUpNameEdt);
        emailEdt    = (EditText) findViewById(R.id.signUpEmailEdt);
        passwordEdt = (EditText) findViewById(R.id.signUpPswEdt);
        signUpBtn  = (Button) findViewById(R.id.signUpBtn);
        signInBtn = (Button) findViewById(R.id.signUpSignInBtn);
        profilePic = (ImageButton)findViewById(R.id.signUpprofileImg);

        signUpBtn.setOnClickListener(this);
        signInBtn.setOnClickListener(this);

        profilePic.setOnClickListener(this);

    }

    /**
     *  ============ OnClick method ==============
     */

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.signUpBtn:
                createNewAccount();
                break;

            case R.id.signUpSignInBtn:
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.signUpprofileImg:
                resultUri = null;
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {

            CropImage.activity(resultUri)
                    .setAspectRatio(1,1)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                profilePic.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    /**
     *  ============ Create account method ==============
     */

    private void createNewAccount() {
        name = userName.getText().toString().trim();
        String em = emailEdt.getText().toString().trim();
        String pwd = passwordEdt.getText().toString().trim();

        if(resultUri != null){

            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(em) && !TextUtils.isEmpty(pwd) && pwd.length()>= 6) {
                mProgressDialog.setMessage("Creating Account...");
                mProgressDialog.show();
                // creation of the account
                mAuth.createUserWithEmailAndPassword(em, pwd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if (authResult != null) {

                            StorageReference imagePath = mFireBaseStorage.child("MBlog_Profile_Pics")
                                    .child(resultUri.getLastPathSegment());
                            imagePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String link = uri.toString();

                                            // creating new user
                                            String userId = mAuth.getCurrentUser().getUid();
                                            DatabaseReference currenUserDb = mDatabaseReference.child(userId);

                                            // Info fields inside the new user
                                            currenUserDb.child("fullName").setValue(name);
                                            currenUserDb.child("profilPiceUrl").setValue(link);

                                            mProgressDialog.dismiss();
                                            //send users to postList
                                            Intent intent = new Intent(SignUpActivity.this, PostsActivity.class );
                                            intent.putExtra("DisplayName",name);
                                            intent.putExtra("PhotoUrl",link);
                                            startActivity(intent);
                                            finish();

                                        }
                                    });

                                }
                            });

                        }

                    }
                });
            }else{
                mProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Fill all fields Data..", Toast.LENGTH_SHORT).show();
                passwordEdt.setHint("Password (6 to 10) characters...!");
            }
        }else {
            mProgressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Add Profile Picture..!!", Toast.LENGTH_SHORT).show();
        }

    }

}
package com.example.yanulkadiary.Activities;

import androidx.annotation.NonNull;
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

import com.example.yanulkadiary.Model.UserInfor;
import com.example.yanulkadiary.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {

    private ImageButton mPostImage;
    private EditText mPostTitle;
    private EditText mPostDesc;
    private Button mSubmitButton;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mPostDatabase;



    private ProgressDialog mProgress;
    private StorageReference mStorage;

    private Uri mImageUri;
    private static final int GALLERY_CODE = 1;


    private DatabaseReference mUserInfo;
    private DatabaseReference mUserReference;

    private String userFullName;
    private String profileUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        mProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance().getReference();
        mPostDatabase = FirebaseDatabase.getInstance().getReference().child("MPosts");

        mUserInfo = FirebaseDatabase.getInstance().getReference();
        mUserReference = mUserInfo.child("MUsers").child(mUser.getUid());




        // Bundle bundle = getIntent().getExtras();



//        if(bundle != null){
//            fullUserName = bundle.getString("DisplayName");
//            profilePicUrl = bundle.getString("PhotoUrl");
//        }

//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            // Name, email address, and profile photo Url
//            String name = user.getDisplayName();
//            String email = user.getEmail();
//            Uri photoUrl = user.getPhotoUrl();
//
//            // Check if user's email is verified
//            boolean emailVerified = user.isEmailVerified();
//
//            // The user's ID, unique to the Firebase project. Do NOT use this value to
//            // authenticate with your backend server, if you have one. Use
//            // FirebaseUser.getIdToken() instead.
//            String uid = user.getUid();
//        }


        mPostImage = (ImageButton) findViewById(R.id.addPostImgID);
        mPostTitle = (EditText) findViewById(R.id.addPostTitleID);
        mPostDesc = (EditText)findViewById(R.id.addPostDescID);
        mSubmitButton = (Button) findViewById(R.id.addPostBtnID);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Posting to our database
                startPosting();


            }
        });

        mPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            mPostImage.setImageURI(mImageUri);


        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        mUserReference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // mapped to Diary class
                UserInfor userInfor = dataSnapshot.getValue(UserInfor.class);
                String x = userInfor.getFullName();
                String y = userInfor.getProfilPiceUrl();

                userFullName = userInfor.getFullName();
                profileUrl   = userInfor.getProfilPiceUrl();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void startPosting() {

        mProgress.setMessage("Posting to blog...");
        mProgress.show();

        final String titleVal = mPostTitle.getText().toString().trim();
        final String descVal = mPostDesc.getText().toString().trim();

        if (!TextUtils.isEmpty(titleVal) && !TextUtils.isEmpty(descVal) && (mImageUri != null)) {

            StorageReference filepath = mStorage.child("MPosts_images").
                   child(mImageUri.getLastPathSegment());

            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String link = uri.toString();

                            DatabaseReference newPost = mPostDatabase.push();
                            final Map<String, String> dataToSave = new HashMap<>();

                            dataToSave.put("fullUserName", userFullName);
                            dataToSave.put("profileImg", profileUrl);
                            dataToSave.put("image", link);
                            dataToSave.put("title", titleVal);
                            dataToSave.put("desc", descVal);

                            dataToSave.put("timestamp", String.valueOf(System.currentTimeMillis()));
                            dataToSave.put("userId", mUser.getUid());

                            newPost.setValue(dataToSave);
                        }
                    });




                }
            });
            mProgress.dismiss();
            startActivity(new Intent(AddPostActivity.this, PostsActivity.class));
            finish();
        }else{
            mProgress.dismiss();
            Toast.makeText(getApplicationContext(),"Insert Data..", Toast.LENGTH_SHORT).show();
        }
    }
}
//Old way
//            newPost.child("title").setValue(titleVal);
//                    newPost.child("desc").setValue(descrVal);
//                    newPost.child("image").setValue(downloadUrl.toString());
//                    newPost.child("timestamp").setValue(java.lang.System.currentTimeMillis());
//


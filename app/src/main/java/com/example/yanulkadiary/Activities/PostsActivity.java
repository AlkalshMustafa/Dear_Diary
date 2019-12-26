package com.example.yanulkadiary.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.example.yanulkadiary.Data.DiaryRecyclerAdapter;
import com.example.yanulkadiary.Model.Diary;
import com.example.yanulkadiary.Model.UserInfor;
import com.example.yanulkadiary.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PostsActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mUserDatabase;

    private ProgressDialog mProgressDialog;
    private RecyclerView recyclerView;
    private DiaryRecyclerAdapter blogRecyclerAdapter;
    private List<Diary> postList;

    private String displayedName;
    private String profilePicUrl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get user reference
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        // get DataBase reference
        // get child reference
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("MPosts");


        mDatabaseReference.keepSynced(true);
        mProgressDialog = new ProgressDialog(this);
        postList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            displayedName = bundle.getString("DisplayName");
            profilePicUrl = bundle.getString("PhotoUrl");
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToAddPost(displayedName, profilePicUrl);
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_signOut) {

            mAuth.signOut();
            mProgressDialog.setMessage("Signing Out...");
            mProgressDialog.show();
            Intent intent = new Intent(PostsActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();

        }
        if (id == R.id.addPost) {
            moveToAddPost(displayedName, profilePicUrl);
        }

        return super.onOptionsItemSelected(item);
    }

    private void moveToAddPost(String displayName, String photoUrl){
        if((mUser != null) && (mAuth != null)) {
            Intent intent = new Intent(PostsActivity.this, AddPostActivity.class);
//            intent.putExtra("DisplayName", displayName);
//            intent.putExtra("PhotoUrl", photoUrl);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                // mapped to Diary class
                Diary diary = dataSnapshot.getValue(Diary.class);
                // adding to adapter
                postList.add(diary);
                Collections.reverse(postList);

                blogRecyclerAdapter = new DiaryRecyclerAdapter(PostsActivity.this, postList);
                recyclerView.setAdapter(blogRecyclerAdapter);
                blogRecyclerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}

package com.example.meetfurryapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetfurryapplication.Adapter.PostAdapter;
import com.example.meetfurryapplication.Model.Post;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class userForum extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;



    ///////////////////////////////////////////////forum
    private FirebaseFirestore firestore;
    private RecyclerView mRecycleView;
    private FloatingActionButton fab;
    private Button mHomeBtn;
    private Button mForumBtn;



    private PostAdapter adapter;
    private List<Post> list;
    private Query query;
    private ListenerRegistration listenerRegistration;
    ///////////////////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_forum);


        firebaseAuth = FirebaseAuth.getInstance();


        /////////////////////////////////////////////////////////////////////////////////
        firestore = FirebaseFirestore.getInstance();
        mRecycleView = findViewById(R.id.recyclerView2);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(userForum.this));

        list = new ArrayList<>();
        adapter = new PostAdapter(userForum.this, list);

        mRecycleView.setAdapter(adapter);


        fab = findViewById(R.id.floatingActionButton);
        mHomeBtn = findViewById(R.id.homeBtn);
        mForumBtn = findViewById(R.id.forumBtn);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(userForum.this, AddPost.class));

            }
        });

        mHomeBtn.setOnClickListener(this);
        mForumBtn.setOnClickListener(this);


/////////------------------------------------------------------------------------------
        if(firebaseAuth.getCurrentUser() != null){

            mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    Boolean isBottom = !mRecycleView.canScrollVertically(1);
                    if(isBottom){
                        Toast.makeText(userForum.this, "Reached the end", Toast.LENGTH_SHORT).show();}
                }
            });

            query = firestore.collection("ForumPostss").orderBy("time" , com.google.firebase.firestore.Query.Direction.DESCENDING);
            listenerRegistration = query.addSnapshotListener(userForum.this, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {

                    for(DocumentChange doc : Objects.requireNonNull(value).getDocumentChanges()){

                        if(doc.getType() == DocumentChange.Type.ADDED){


                            String postId = doc.getDocument().getId();


                            Post post = doc.getDocument().toObject(Post.class).withId(postId);
                            list.add(post);
                            adapter.notifyDataSetChanged();

                        }
                        else{
                            adapter.notifyDataSetChanged();
                        }
                    }
                    listenerRegistration.remove();
                }
            });
        }

/////////////////////-------------------------------------------------------////////////////////////////////////////
    }




    @Override
    protected void onStart(){

        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser == null){

            startActivity(new Intent(userForum.this, LoginActivity.class));
            //finish();

        }



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.homeBtn:

                Intent i=new Intent(userForum.this,userHomepage.class);
                startActivity(i);
                finish();
                break;
            case R.id.forumBtn:

                Toast.makeText(userForum.this, "You already at the forum page now!", Toast.LENGTH_LONG).show();
                Intent startUp = new Intent(userForum.this, userForum.class);
                startActivity(startUp);
                finish();
                break;
        }

    }}
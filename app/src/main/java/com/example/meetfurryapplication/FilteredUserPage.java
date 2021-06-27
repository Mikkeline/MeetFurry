package com.example.meetfurryapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetfurryapplication.Adapter.petAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FilteredUserPage extends AppCompatActivity implements View.OnClickListener {




    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Pet_Registration");
    private FirebaseAuth firebaseAuth;



    private RecyclerView mRecyclerView;
    private GridLayoutManager layoutManager;
    private petAdapter adapter;
    private List<Image> list;



    private Button mHomeBtn, mLogout;
    private Button mForumBtn;
    private ImageView filterPic, chatPic, profileSetting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_user_page);


        Intent intentReceived = getIntent();
        Bundle data = intentReceived.getExtras();
        String filter = data.getString("Filter");


        firebaseAuth = FirebaseAuth.getInstance();




        mRecyclerView = findViewById(R.id.PetRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);

        list = new ArrayList<>();
        adapter = new petAdapter(FilteredUserPage.this, list);
        mRecyclerView.setAdapter(adapter);



        if(firebaseAuth.getCurrentUser() != null){

            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    Boolean isBottom = !mRecyclerView.canScrollVertically(1);
                    if(isBottom){
                        Toast.makeText(FilteredUserPage.this, "Reached the end", Toast.LENGTH_SHORT).show();}
                }
            });}


        root.orderByChild("filter").equalTo(filter).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Image image = dataSnapshot.getValue(Image.class);
                    list.add(image);

                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }


        });


        mHomeBtn = findViewById(R.id.homeBtn);
        mHomeBtn.setOnClickListener(this);

        mForumBtn = findViewById(R.id.forumBtn);
        mForumBtn.setOnClickListener(this);

        filterPic = findViewById(R.id.filter);
        filterPic.setOnClickListener(this);

        chatPic = findViewById(R.id.chat);
        chatPic.setOnClickListener(this);

        profileSetting= findViewById(R.id.profileSetting);
        profileSetting.setOnClickListener(this);

        mLogout = findViewById(R.id.logoutBtn);
        mLogout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.logoutBtn:

                Intent logout=new Intent(FilteredUserPage.this,StartingPage.class);
                startActivity(logout);
                finish();
                break;

            case R.id.forumBtn:

                Intent i=new Intent(FilteredUserPage.this,userForum.class);
                startActivity(i);
                finish();
                break;
            case R.id.homeBtn:

                Toast.makeText(FilteredUserPage.this, "You already at the homepage now!", Toast.LENGTH_LONG).show();
                Intent startUp = new Intent(FilteredUserPage.this, userHomepage.class);
                startActivity(startUp);
                finish();
                break;


            case R.id.chat:

                Toast.makeText(FilteredUserPage.this, "Starting chat...", Toast.LENGTH_LONG).show();
                Intent chat = new Intent(FilteredUserPage.this, ChatMainActivity.class);
                startActivity(chat);
                finish();
                break;

            case R.id.profileSetting:
                Toast.makeText(FilteredUserPage.this, "Viewing Profile...", Toast.LENGTH_LONG).show();
                Intent profile = new Intent(FilteredUserPage.this, UserProfile.class);
                startActivity(profile);
                finish();
                break;

            case R.id.filter:
                Toast.makeText(FilteredUserPage.this, "Filter function...", Toast.LENGTH_LONG).show();
                Intent filter = new Intent(FilteredUserPage.this, UserFilterFunction.class);
                startActivity(filter);
                finish();
                break;

        }






    }

}

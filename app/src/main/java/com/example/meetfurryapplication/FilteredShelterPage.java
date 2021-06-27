package com.example.meetfurryapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetfurryapplication.Adapter.shelterPetAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FilteredShelterPage extends AppCompatActivity implements View.OnClickListener{


    private String userID;
    private FirebaseUser user;
    private SearchView searchView;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("shelter_pet");

    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;


    private RecyclerView mRecyclerView;
    private GridLayoutManager layoutManager;
    private shelterPetAdapter adapter;
    private List<Image> list;



    private Button mHomeBtn, mLogout;
    private Button mForumBtn;
    private ImageView filterPic, chatPic, profileSetting;
    private FloatingActionButton fab;


    private TextView mShelterName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_shelter_page);


        Intent intentReceived = getIntent();
        Bundle data = intentReceived.getExtras();
        String filter = data.getString("Filter");

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        /////////////////----------------------------------------------------------

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                UsersClass userProfile = snapshot.getValue(UsersClass.class);
                if (userProfile != null) {
                    String Username = userProfile.username;
                    mShelterName.setText(Username);

                    if(root != null) {
                        root.child(userID).orderByChild("filter").equalTo(filter).addValueEventListener(new ValueEventListener() {
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
                    }

                    if(searchView != null){

                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String str) {
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String str) {
                                search(str);
                                return true;
                            }
                        });
                    }
                }
            }

            private void search(String str) {
                ArrayList<Image> myList= new ArrayList<>();
                for(Image object : list ){

                    if(object.getPetType().toLowerCase().contains(str.toLowerCase()) || object.getPetColor().toLowerCase().contains(str.toLowerCase()) || object.getPetBreed().toLowerCase().contains(str.toLowerCase()) || object.getPetGender().toLowerCase().contains(str.toLowerCase())){

                        myList.add(object);
                    }
                }
                adapter = new shelterPetAdapter(FilteredShelterPage.this, myList);
                mRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(FilteredShelterPage.this, "Something Wrong happened!", Toast.LENGTH_LONG).show();
            }
        });


        mShelterName = findViewById(R.id.shelterName);


        firebaseAuth = FirebaseAuth.getInstance();
        mRecyclerView = findViewById(R.id.PetRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);

        list = new ArrayList<>();
        adapter = new shelterPetAdapter(FilteredShelterPage.this, list);
        mRecyclerView.setAdapter(adapter);

        if(firebaseAuth.getCurrentUser() != null){

            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    Boolean isBottom = !mRecyclerView.canScrollVertically(1);
                    if(isBottom){
                        Toast.makeText(FilteredShelterPage.this, "Reached the end", Toast.LENGTH_SHORT).show();}
                }
            });}










        mHomeBtn = findViewById(R.id.homeBtn);
        mHomeBtn.setOnClickListener(this);

        mForumBtn = findViewById(R.id.forumBtn);
        mForumBtn.setOnClickListener(this);

        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(this);


        filterPic = findViewById(R.id.filter); //set onclick listener to filter page
        filterPic.setOnClickListener(this);

        chatPic = findViewById(R.id.chat);
        chatPic.setOnClickListener(this);

        profileSetting = findViewById(R.id.profileSetting);
        profileSetting.setOnClickListener(this);

        mLogout = findViewById(R.id.logoutBtn);
        mLogout.setOnClickListener(this);
    }

    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.logoutBtn:

                Intent logout=new Intent(FilteredShelterPage.this,StartingPage.class);
                startActivity(logout);
                finish();
                break;


            case R.id.forumBtn:

                Intent i=new Intent(FilteredShelterPage.this,Forum.class);
                startActivity(i);
                finish();
                break;
            case R.id.homeBtn:

                Toast.makeText(FilteredShelterPage.this, "You already at the homepage now!", Toast.LENGTH_LONG).show();
                Intent startUp = new Intent(FilteredShelterPage.this, shelterHomepage.class);
                startActivity(startUp);
                finish();
                break;

            case R.id.floatingActionButton:

                Toast.makeText(FilteredShelterPage.this, "Register your pet here...", Toast.LENGTH_LONG).show();
                Intent start = new Intent(FilteredShelterPage.this, PetRegister.class);
                startActivity(start);
                finish();
                break;


            case R.id.chat:

                Toast.makeText(FilteredShelterPage.this, "Starting chat...", Toast.LENGTH_LONG).show();
                Intent chat = new Intent(FilteredShelterPage.this, ChatMainActivity.class);
                startActivity(chat);
                finish();
                break;


            case R.id.filter:
                Toast.makeText(FilteredShelterPage.this, "Filter function...", Toast.LENGTH_LONG).show();
                Intent filter = new Intent(FilteredShelterPage.this, FilterFunction.class);
                startActivity(filter);
                finish();
                break;

            case R.id.profileSetting:
                Toast.makeText(FilteredShelterPage.this, "Viewing Profile...", Toast.LENGTH_LONG).show();
                Intent profile = new Intent(FilteredShelterPage.this, ShelterProfile.class);
                startActivity(profile);
                finish();
                break;



        }
    }
}
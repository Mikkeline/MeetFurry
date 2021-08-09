package com.example.meetfurryapplication;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class userPetDetails extends AppCompatActivity implements View.OnClickListener {

    private TextView mPetName, mBreed, mAge, mGender, mColor,mIntakeDate, mDesc, mshelterUID;
    private ImageView petPic;
    private Button mChat, mBack;
    private FirebaseAuth mauth;
    private String currentUserId;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Pet_Registration");
    private DatabaseReference refer = db.getReference().child("shelter_pet");
    private DatabaseReference userRef = db.getReference().child("Users");
    private ImageSlider mainslider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pet_details);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //getPetName from petAdapter
        Intent intentReceived = getIntent();
        Bundle data = intentReceived.getExtras();
        String petName = data.getString("PetName");

        mPetName = findViewById(R.id.petName_tv);
        mPetName.setText(petName);


        mBreed = findViewById(R.id.Breed_tv);
        mAge = findViewById(R.id.age_tv);
        mGender = findViewById(R.id.gender_tv);
        mColor = findViewById(R.id.color_tv);
        mIntakeDate = findViewById(R.id.intakeDate_tv);
        mDesc = findViewById(R.id.description_tv);

        mainslider = (ImageSlider)findViewById(R.id.image_slider);
        final List<SlideModel> remoteImages = new ArrayList<>();

        root.child(petName).child("imageUrl2")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for(DataSnapshot data:snapshot.getChildren())

                            remoteImages.add(new SlideModel(data.child("ImgLink").getValue().toString(), ScaleTypes.FIT));

                        mainslider.setImageList(remoteImages,ScaleTypes.FIT);


                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });


        petPic = findViewById(R.id.PetImage);
        petPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(userPetDetails.this);
                dialog.setContentView(R.layout.custom_image_layout);

                ImageView enlargePetPic = dialog.findViewById(R.id.PetImage);


                Intent intentReceived = getIntent();
                Bundle data = intentReceived.getExtras();
                String petName = data.getString("PetName");
                root.child(petName).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        String url = Objects.requireNonNull(snapshot.child("imageUrl").getValue()).toString();
                        Glide.with(getApplicationContext()).load(url).into(enlargePetPic);
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
                dialog.show();


            }
        });

        mshelterUID = findViewById(R.id.petsUID_tv);

        mChat = findViewById(R.id.chatBtn);
        mChat.setOnClickListener(this);

        mBack = findViewById(R.id.BackBtn);
        mBack.setOnClickListener(this);


        root.child(petName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String Breed = Objects.requireNonNull(snapshot.child("petBreed").getValue()).toString();
                mBreed.setText(Breed);

                String Age = Objects.requireNonNull(snapshot.child("petAge").getValue()).toString();
                mAge.setText(Age);

                String Gender = Objects.requireNonNull(snapshot.child("petGender").getValue()).toString();
                mGender.setText(Gender);

                String Color = Objects.requireNonNull(snapshot.child("petColor").getValue()).toString();
                mColor.setText(Color);

                String IntakeDate = Objects.requireNonNull(snapshot.child("petIntakeDate").getValue()).toString();
                mIntakeDate.setText(IntakeDate);

                String Desc = Objects.requireNonNull(snapshot.child("petDesc").getValue()).toString();
                mDesc.setText(Desc);

                String shelterUID = Objects.requireNonNull(snapshot.child("uid").getValue()).toString();
                mshelterUID.setText(shelterUID);
                mshelterUID.setVisibility(View.INVISIBLE);

                String url = Objects.requireNonNull(snapshot.child("imageUrl").getValue()).toString();
                Glide.with(getApplicationContext()).load(url).into(petPic);


            }





            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(userPetDetails.this,"Error", Toast.LENGTH_SHORT).show();
            }
        });




    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.BackBtn:
                Toast.makeText(userPetDetails.this, "Back to Homepage...", Toast.LENGTH_LONG).show();
                Intent back = new Intent(userPetDetails.this, userHomepage.class);
                startActivity(back);
                finish();
                break;

            case R.id.chatBtn:
                saveInContact();
                break;


        }

    }

    private void saveInContact() {

        DatabaseReference contactref = FirebaseDatabase.getInstance().getReference().child("ChatLists");
        mauth=FirebaseAuth.getInstance();
        currentUserId=mauth.getCurrentUser().getUid();
        String sUID = mshelterUID.getText().toString();

        userRef.child(sUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String shelterName = Objects.requireNonNull(snapshot.child("username").getValue()).toString();
                String imgurl = Objects.requireNonNull(snapshot.child("imageUrl").getValue()).toString();

                contactref.child(currentUserId).child(sUID)
                        .child("Contacts").setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {

                        if(task.isSuccessful())
                        {
                            contactref.child(sUID).child(currentUserId)
                                    .child("Contacts")
                                    .setValue("Saved")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            Toast.makeText(userPetDetails.this, "Starting chat...", Toast.LENGTH_LONG).show();
                                            Intent chat = new Intent(userPetDetails.this, MessagesActivity.class);
                                            chat.putExtra("visit_user_id",sUID);
                                            chat.putExtra("visit_user_name",shelterName);
                                            chat.putExtra("visit_image",imgurl );
                                            startActivity(chat);
                                            //Toast.makeText(userPetDetails.this,"Contact in chatlists",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });



    }




}
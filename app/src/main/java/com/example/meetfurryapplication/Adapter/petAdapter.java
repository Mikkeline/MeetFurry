package com.example.meetfurryapplication.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.meetfurryapplication.Image;
import com.example.meetfurryapplication.R;
import com.example.meetfurryapplication.userPetDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class petAdapter extends RecyclerView.Adapter<petAdapter.PetViewHolder> {

    private List<Image> petList;
    private Activity context;
    private FirebaseFirestore firestore;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String currentUserId;


    public petAdapter(Activity context, List<Image> petList){
        this.petList = petList;
        this.context = context;

    }

    @NonNull
    @NotNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(context).inflate(R.layout.each_pet, parent, false);


        firestore = FirebaseFirestore.getInstance();


        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        currentUserId = user.getUid();


       return new PetViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PetViewHolder holder, int position) {

        Image image = petList.get(position);
        holder.setImage(image.getImageUrl());
        holder.setName(image.getPetName());
        holder.setBreed(image.getPetBreed());
        holder.setGender(image.getPetGender());
        holder.setAddress(image.getAddress());


    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public class PetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView petPic;
        TextView petname, petbreed, petgender, Address;
        View mView;


        public PetViewHolder(@NonNull @NotNull View itemView) {

            super(itemView);
            mView = itemView;

            //set onClick on the cardView
            itemView.setOnClickListener(this);

        }

        public void setImage(String imageUrl) {
            petPic = mView.findViewById(R.id.petPic);
            Glide.with(context).load(imageUrl).into(petPic);
        }

        public void setName(String petName) {
            petname = mView.findViewById(R.id.petName_tv);
            petname.setText(petName);

        }

        public void setBreed(String petBreed) {
            petbreed = mView.findViewById(R.id.breed_tv);
            petbreed.setText(petBreed);
        }

        public void setGender(String petGender) {
            petgender = mView.findViewById(R.id.petGender_tv);
            petgender.setText(petGender);
        }

        public void setAddress(String address) {
            Address = mView.findViewById(R.id.address_tv);
            Address.setText(address);
        }

        @Override
        public void onClick(View v) {

            Toast.makeText(context, "Viewing Pet Details...", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context, userPetDetails.class);

            //Pass pet name to userPetDetails page
            Bundle data1 = new Bundle();
            data1.putString("PetName", petname.getText().toString());
            intent.putExtras(data1);

            context.startActivity(intent);

        }
    }

}

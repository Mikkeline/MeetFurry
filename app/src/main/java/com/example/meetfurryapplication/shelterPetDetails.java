package com.example.meetfurryapplication;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class shelterPetDetails extends AppCompatActivity implements View.OnClickListener  {
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private static final int GalleryPick = 1;
    public Uri imageUri = null;
    private String downloadUrl;

    private TextView mPetName;
    private ImageView petPic, editPic;
    private EditText mBreed, mAge, mGender, mColor,mIntakeDate, mDesc;
    private Button mEdit, mDelete, mBack;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Pet_Registration");
    private DatabaseReference refer = db.getReference().child("shelter_pet");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_pet_details);


        //getPetName from shelterPetAdapter
        Intent intentReceived = getIntent();
        Bundle data = intentReceived.getExtras();
        String petName = data.getString("petName");

        mPetName = findViewById(R.id.petName_tv);
        mPetName.setText(petName);


        mBreed = findViewById(R.id.Breed_et);
        mAge = findViewById(R.id.age_et);
        mGender = findViewById(R.id.gender_et);
        mColor = findViewById(R.id.color_et);
        mIntakeDate = findViewById(R.id.intakeDate_et);
        mDesc = findViewById(R.id.description_et);
        petPic = findViewById(R.id.PetImage);
        petPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(shelterPetDetails.this);
                dialog.setContentView(R.layout.custom_image_layout);

                ImageView enlargePetPic = dialog.findViewById(R.id.PetImage);


                Intent intentReceived = getIntent();
                Bundle data = intentReceived.getExtras();
                String petName = data.getString("petName");
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


        mEdit = findViewById(R.id.updateBtn);
        mEdit.setOnClickListener(this);

        mDelete = findViewById(R.id.deleteBtn);
        mDelete.setOnClickListener(this);

        mBack = findViewById(R.id.BackBtn);
        mBack.setOnClickListener(this);


        root.child(petName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String Breed = snapshot.child("petBreed").getValue().toString();
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

                String url = Objects.requireNonNull(snapshot.child("imageUrl").getValue()).toString();
                Glide.with(getApplicationContext()).load(url).into(petPic);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(shelterPetDetails.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });


        editPic = findViewById(R.id.editPetImage);
        editPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GalleryPick);

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            imageUri = data.getData();
            petPic.setImageURI(imageUri);


        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.BackBtn:
                Toast.makeText(shelterPetDetails.this, "Back to Homepage...", Toast.LENGTH_LONG).show();
                Intent back = new Intent(shelterPetDetails.this, shelterHomepage.class);
                startActivity(back);
                finish();
                break;

            case R.id.updateBtn:
                update();
                break;

            case R.id.deleteBtn:
                delete();
                break;

        }


    }



    private void update() {

        Intent intentReceived = getIntent();
        Bundle data = intentReceived.getExtras();
        String petName = data.getString("petName");

        //Edit firebase Pet_Registration
        String petBreed = mBreed.getText().toString().trim();
        root.child(petName).child("petBreed").setValue(petBreed);

        String petAge = mAge.getText().toString().trim();
        root.child(petName).child("petAge").setValue(petAge);

        String petGender = mGender.getText().toString().trim();
        root.child(petName).child("petGender").setValue(petGender);

        String petColor = mColor.getText().toString().trim();
        root.child(petName).child("petColor").setValue(petColor);

        String petIntakeDate = mIntakeDate.getText().toString().trim();
        root.child(petName).child("petIntakeDate").setValue(petIntakeDate);

        String petDesc = mDesc.getText().toString().trim();
        root.child(petName).child("petDesc").setValue(petDesc);


        if (imageUri != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference("Pet_Registration");


            StorageReference imgRef = storageReference
                    .child("Pet_Image/")
                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child(petName);

            imgRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Uri> task) {
                            downloadUrl = Objects.requireNonNull(task.getResult()).toString();
                            //     databaseReference.child(name).child("imageUrl/").setValue(downloadUrl);
                            root.child(petName).child("imageUrl/").setValue(downloadUrl);
                            //   databaseRefer.child(shelterName).child(name).child("imageUrl/").setValue(downloadUrl);


                            FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                            String UID = User.getUid();

                            reference.child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                    UsersClass userProfile = snapshot.getValue(UsersClass.class);
                                    if (userProfile != null) {
                                        String username = userProfile.username;

                                        refer.child(UID).child(petName).child("imageUrl/").setValue(downloadUrl);

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                    Toast.makeText(shelterPetDetails.this, "Something Wrong happened!", Toast.LENGTH_LONG).show();
                                }
                            });


                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {

                    Toast.makeText(getApplicationContext(), "Failed to Edit Image!", Toast.LENGTH_LONG).show();
                }
            });


            //Edit firebase shelter_pet
            FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            String UID = User.getUid();

            reference.child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    UsersClass userProfile = snapshot.getValue(UsersClass.class);
                    if (userProfile != null) {
                        String username = userProfile.username;

                        refer.child(UID).child(petName).child("petBreed").setValue(petBreed);
                        refer.child(UID).child(petName).child("petAge").setValue(petAge);
                        refer.child(UID).child(petName).child("petGender").setValue(petGender);
                        refer.child(UID).child(petName).child("petColor").setValue(petColor);
                        refer.child(UID).child(petName).child("petIntakeDate").setValue(petIntakeDate);
                        refer.child(UID).child(petName).child("petDesc").setValue(petDesc);

                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    Toast.makeText(shelterPetDetails.this, "Something Wrong happened!", Toast.LENGTH_LONG).show();
                }
            });


            Toast.makeText(shelterPetDetails.this, "Pet details updated successfully...\n You can notify the pet lovers in the Forum section", Toast.LENGTH_LONG).show();
           // Intent edit = new Intent(shelterPetDetails.this, Forum.class);
        //    startActivity(edit);


        }


        else{
            //Edit firebase shelter_pet
            FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            String UID = User.getUid();

            reference.child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    UsersClass userProfile = snapshot.getValue(UsersClass.class);
                    if (userProfile != null) {
                        String username = userProfile.username;

                        refer.child(UID).child(petName).child("petBreed").setValue(petBreed);
                        refer.child(UID).child(petName).child("petAge").setValue(petAge);
                        refer.child(UID).child(petName).child("petGender").setValue(petGender);
                        refer.child(UID).child(petName).child("petColor").setValue(petColor);
                        refer.child(UID).child(petName).child("petIntakeDate").setValue(petIntakeDate);
                        refer.child(UID).child(petName).child("petDesc").setValue(petDesc);

                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    Toast.makeText(shelterPetDetails.this, "Something Wrong happened!", Toast.LENGTH_LONG).show();
                }
            });


            Toast.makeText(shelterPetDetails.this, "Pet details updated successfully...\n You can notify the pet lovers in the Forum section", Toast.LENGTH_LONG).show();
            //Intent edit = new Intent(shelterPetDetails.this, Forum.class);
          //  startActivity(edit);


        }

    }


    private void delete() {

        AlertDialog.Builder builder = new AlertDialog.Builder(shelterPetDetails.this);
        builder.setTitle("Are you sure to mark this pet as ADOPTED?");
        builder.setMessage("If yes, all the information and data related to this pet will be deleted.");
        builder.setIcon(R.drawable.meetfurrylogo);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intentReceived = getIntent();
                Bundle data = intentReceived.getExtras();
                String petName = data.getString("petName");



                //Delete firebase shelter_pet
                FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                String UID = Objects.requireNonNull(User).getUid();

                reference.child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        UsersClass userProfile = snapshot.getValue(UsersClass.class);
                        if (userProfile != null) {
                            String username = userProfile.username;

                            refer.child(UID).child(petName).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(shelterPetDetails.this, "Deleted...", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(shelterPetDetails.this, "Failed...", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }



                        //Delete firebase Pet_Registration
                        Intent intentReceived = getIntent();
                        Bundle data = intentReceived.getExtras();
                        String petName = data.getString("petName");

                        root.child(petName).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(shelterPetDetails.this, "Deleted...", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(shelterPetDetails.this, "Failed...", Toast.LENGTH_SHORT).show();
                            }
                        });



                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        Toast.makeText(shelterPetDetails.this, "Something Wrong happened!", Toast.LENGTH_LONG).show();
                    }
                });

                Toast.makeText(shelterPetDetails.this, "The pet is marked as adopted and removed successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(shelterPetDetails.this, shelterHomepage.class);
                startActivity(intent);

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(shelterPetDetails.this, "Cancel...", Toast.LENGTH_LONG).show();
            }
        });
        builder.create().show();

    }

}
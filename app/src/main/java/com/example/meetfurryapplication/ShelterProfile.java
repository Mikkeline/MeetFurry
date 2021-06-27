package com.example.meetfurryapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
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
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShelterProfile extends AppCompatActivity {

    private EditText mUsername, mName, mEmail, mAddress, mContact;
    private DatabaseReference reference;
    private String userID;
    private Uri imageUri;
    private CircleImageView sPic;
    private StorageReference storageRef;

////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_profile);

///////////////////////////////
        /////
        storageRef = FirebaseStorage.getInstance().getReference("User_ProfilePic");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        assert user != null;
        userID = user.getUid();

        mUsername = findViewById(R.id.sUsername_et);
        mName = findViewById(R.id.sName_et);
        mContact = findViewById(R.id.sContactNum_et);
        mAddress = findViewById(R.id.sAddress_et);
        mEmail = findViewById(R.id.sEmail_et);
        sPic = findViewById(R.id.shelterImg);
        ImageButton mBack = findViewById(R.id.btnBackTosHomePage);
        Button mUpdate = findViewById(R.id.btnsUpdate);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @com.google.firebase.database.annotations.NotNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChild("name")
                        && snapshot.hasChild("email")
                        && snapshot.hasChild("contact")
                        && snapshot.hasChild("address")
                        && snapshot.hasChild("username")
                        && snapshot.hasChild("imageUrl"))
                {
                    String name = snapshot.child("name").getValue().toString();
                    String email = snapshot.child("email").getValue().toString();
                    String contact = snapshot.child("contact").getValue().toString();
                    String address = snapshot.child("address").getValue().toString();
                    String userName = snapshot.child("username").getValue().toString();
                    String imgUrl = snapshot.child("imageUrl").getValue().toString();

                    mName.setText(name);
                    mEmail.setText(email);
                    mContact.setText(contact);
                    mAddress.setText(address);
                    mUsername.setText(userName);
                    Picasso.get().load(imgUrl).into(sPic);
                    closeKeyboard();
                }else if (snapshot.exists() && snapshot.hasChild("name")
                        && snapshot.hasChild("email")
                        && snapshot.hasChild("contact")
                        && snapshot.hasChild("address")
                        && snapshot.hasChild("username"))
                {
                    String name = snapshot.child("name").getValue().toString();
                    String email = snapshot.child("email").getValue().toString();
                    String contact = snapshot.child("contact").getValue().toString();
                    String address = snapshot.child("address").getValue().toString();
                    String userName = snapshot.child("username").getValue().toString();

                    mName.setText(name);
                    mEmail.setText(email);
                    mContact.setText(contact);
                    mAddress.setText(address);
                    mUsername.setText(userName);
                    closeKeyboard();
                }
                else
                {
                    Toast.makeText(ShelterProfile.this,"Please set and update your profile information...",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(ShelterProfile.this, "Something Wrong happened!", Toast.LENGTH_LONG).show();
            }
        });

        sPic.setOnClickListener(v -> {
            closeKeyboard();
            chooseImage();
        });
/////////////////////////////////////


        mBack.setOnClickListener(v -> {
            closeKeyboard();
            Toast.makeText(ShelterProfile.this, "Back to Homepage...", Toast.LENGTH_SHORT).show();
            finish();
        });


        mUpdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                closeKeyboard();
                String Name = mName.getText().toString().trim();
                String Contact = mContact.getText().toString().trim();
                String Address = mAddress.getText().toString().trim();
                String Username = mUsername.getText().toString().trim();

                if (TextUtils.isEmpty(Name)) {
                    mName.setError("Full Name is required!");
                    mName.requestFocus();
                    return;
                }

                else if (TextUtils.isEmpty(Contact)) {
                    mName.setError("Contact is required!");
                    mName.requestFocus();
                    return;
                }

                else if (TextUtils.isEmpty(Address)) {
                    mName.setError("Address is required!");
                    mName.requestFocus();
                    return;
                }

                else if (TextUtils.isEmpty(Username)) {
                    mName.setError("Username is required!");
                    mName.requestFocus();
                    return;
                }

                else {
                    reference.child(userID).child("name").setValue(Name);
                    reference.child(userID).child("contact").setValue(Contact);
                    reference.child(userID).child("address").setValue(Address);
                    reference.child(userID).child("username").setValue(Username);

                    if (imageUri != null){
                        StorageReference filepath = storageRef.child("Profile_Pic/").child(userID).child(Username);
                        filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                                firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        final String downloadUrl = uri.toString();
                                        reference.child(userID).child("imageUrl")
                                                .setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    Toast.makeText(ShelterProfile.this, "Image saved in database successfully!", Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                {
                                                    String message = task.getException().toString();
                                                    Toast.makeText(ShelterProfile.this, "Error: " + message,Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        });}

                    Toast.makeText(ShelterProfile.this, "Profile Updated Successfully!", Toast.LENGTH_LONG).show();
                    sendUserToHome();
                }
            }
        });
    }

    private void sendUserToHome() {
        Toast.makeText(ShelterProfile.this, "Back to Homepage...", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void closeKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            sPic.setImageURI(imageUri);
        }
    }


}
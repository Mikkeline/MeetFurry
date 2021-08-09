package com.example.meetfurryapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.meetfurryapplication.R;
import com.example.meetfurryapplication.shelterHomepage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AddMoreActivity extends AppCompatActivity {


    private Uri imageUri;
    private String downloadUrl;
    FirebaseStorage storage;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseRefer;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final int PICK_IMAGE = 1;
    ArrayList<Uri> ImageList = new ArrayList<Uri>();
    private Uri ImageUri;
    private int uploadCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_more);

        Intent intentReceived = getIntent();
        Bundle data = intentReceived.getExtras();
        String name = data.getString("petName");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("Pet_Registration");
        databaseReference = database.getReference("Pet_Registration");
        databaseRefer = database.getReference("shelter_pet");


        Button add = findViewById(R.id.addBtn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        Button done = findViewById(R.id.doneBtn);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private void submit() {
        Intent intentReceived = getIntent();
        Bundle data = intentReceived.getExtras();
        String name = data.getString("petName");

        StorageReference imgRef = storageReference
                .child("Pet_Image/")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child(name);

        for(uploadCount = 0; uploadCount < ImageList.size(); uploadCount++){
            Uri IndividualImage = ImageList.get(uploadCount);
            StorageReference ImgName = imgRef.child("Image" + IndividualImage.getLastPathSegment());

            ImgName.putFile(IndividualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ImgName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String url = String.valueOf(uri);

                            FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
                            String UID = User.getUid();




                            DatabaseReference data = databaseReference.child(name).child("imageUrl2/").child("Image" + IndividualImage.getLastPathSegment());

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("ImgLink",url);
                            data.setValue(hashMap);

                            DatabaseReference shelterData =  databaseRefer.child(UID).child(name).child("imageUrl2/").child("Image" + IndividualImage.getLastPathSegment());
                            HashMap<String, String> hashMap2 = new HashMap<>();
                            hashMap2.put("ImgLink",url);
                            data.setValue(hashMap2);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {

                    Toast.makeText(getApplicationContext(), "Failed to Upload Image!", Toast.LENGTH_LONG).show();
                }
            });
        }


        Intent intent = new Intent(AddMoreActivity.this, shelterHomepage.class);
        startActivity(intent);
    }

    private void chooseImage() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent,PICK_IMAGE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getClipData() != null) {


            int countClipData = data.getClipData().getItemCount();


            int currentImageSelect = 0;
            while(currentImageSelect < countClipData){
                ImageUri = data.getClipData().getItemAt(currentImageSelect).getUri();
                ImageList.add(ImageUri);
                currentImageSelect = currentImageSelect + 1;

            }



        }
        else{
            Toast.makeText(AddMoreActivity.this, "Please choose multiple images", Toast.LENGTH_LONG).show();
        }
        /*if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            pic.setImageURI(imageUri);
        }*/
    }




}
package com.example.meetfurryapplication.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.meetfurryapplication.Adapter.ImageAdapter;
import com.example.meetfurryapplication.Image;
import com.example.meetfurryapplication.R;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AddImgActivity extends AppCompatActivity implements View.OnClickListener {

    private Button backBtn,pickBtn, uploadImgBtn;
    private static final int IMAGE_CODE = 1;
    private RecyclerView recyclerView;
    private List<Image> ImageList;
    private ImageAdapter imageAdapter;
    private String uid, petName;
    private FirebaseUser user;
    private int totalnum;

    private DatabaseReference reference;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;
    private DatabaseReference databaseRefer;

    private DatabaseReference root = database.getReference().child("Pet_Registration");
    private DatabaseReference refer = database.getReference().child("shelter_pet");


    private static final int PICK_IMAGE = 1;
    private Uri ImageUri;
    ArrayList ImageListS = new ArrayList();
    private int upload_count = 0;
    ArrayList urlStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_img);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        uid = user.getUid();

        databaseReference = database.getReference("Pet_Registration");
        databaseRefer = database.getReference("shelter_pet");

        petName=getIntent().getExtras().get("petName").toString();

        backBtn = findViewById(R.id.backtoPetDetailsBtn);
        backBtn.setOnClickListener(this);

        pickBtn = findViewById(R.id.pickBtn);
        pickBtn.setOnClickListener(this);

        uploadImgBtn = findViewById(R.id.imgUploadBtn);
        uploadImgBtn.setOnClickListener(this);

        recyclerView = findViewById(R.id.recyclerViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageList = new ArrayList<>();

        root.child(petName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.hasChild("i")){
                     totalnum = Integer.parseInt(String.valueOf(snapshot.child("i").getValue()));
                }else{
                    totalnum = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        refer.child(petName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.hasChild("i")){
                    totalnum = Integer.parseInt(String.valueOf(snapshot.child("i").getValue()));
                }else{
                    totalnum = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backtoPetDetailsBtn:
                finish();
                break;
            case R.id.pickBtn:
                pickImg();
                break;
            case R.id.imgUploadBtn:
                uploadImg();
                break;

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void pickImg() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, PICK_IMAGE);
    }

    private void uploadImg() {
        urlStrings = new ArrayList<>();
        StorageReference mRef = storageReference.child("Pet_Image/")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child(petName);

        for (upload_count = 0; upload_count < ImageList.size(); upload_count++) {
            int size = ImageList.size();
            int newSize = totalnum + size;
            Uri IndividualImage = (Uri) ImageListS.get(upload_count);
            String imgName = petName + IndividualImage.getLastPathSegment();
            final StorageReference ImageName = mRef.child(imgName);

            ImageName.putFile(IndividualImage).addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ImageName.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Uri> task) {
                                    String downloadUrl = Objects.requireNonNull(task.getResult()).toString();
                                    urlStrings.add(String.valueOf(task));
                                    databaseReference.child(petName).child("ImageFolder").child(imgName).setValue(downloadUrl);
                                    databaseReference.child(petName).child("i").setValue(newSize);
                                    databaseRefer.child(uid).child(petName).child("ImageFolder").child(imgName).setValue(downloadUrl);
                                    databaseRefer.child(uid).child(petName).child("i").setValue(newSize);
                                    Toast.makeText(AddImgActivity.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();


                                    if (urlStrings.size() == ImageList.size()){

                                    }
                                    //pickBtn.setVisibility(View.GONE);


                                }
                            });
                          /*  ImageName.getDownloadUrl().addOnSuccessListener(
                                    new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            urlStrings.add(String.valueOf(uri));
                                            databaseReference.child(petName).child("ImageFolder").child(imgName).setValue(downloadUrl);

                                            databaseRefer.child(uid).child(petName).child("ImageFolder").child(imgName).setValue(downloadUrl);

                                            if (urlStrings.size() == ImageListS.size()){
                                                storeLink(urlStrings);
                                            }

                                        }
                                    }
                            );
                        */}
                    }
            );


        }ImageList.clear();
        ImageListS.clear();


    }



    private void storeLink(ArrayList urlStrings) {
        HashMap<String, String> hashMap = new HashMap<>();

        for (int i = 0; i <urlStrings.size() ; i++) {
            hashMap.put("ImgLink"+i, String.valueOf(urlStrings.get(i)));

        }
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(petName).child("ImageFolder");

        databaseReference.push().setValue(hashMap)
                .addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AddImgActivity.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddImgActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        pickBtn.setVisibility(View.GONE);

        ImageList.clear();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        uid = user.getUid();

        if (requestCode == IMAGE_CODE && resultCode == RESULT_OK) {

            if (data.getClipData() != null) {

                int countClipData = data.getClipData().getItemCount();
                int currentImageSlect = 0;
                Uri ImageUri = null;
                while (currentImageSlect < countClipData) {

                    ImageUri = data.getClipData().getItemAt(currentImageSlect).getUri();
                    ImageListS.add(ImageUri);
                    currentImageSlect = currentImageSlect + 1;

                    String imageUrl = ImageUri.toString();
                    String imageName;
                    imageName = getFileName(ImageUri);
                    //imageName = petName;

                    Image imageClass = new Image(imageName, imageUrl);
                    ImageList.add(imageClass);

                    imageAdapter = new ImageAdapter(AddImgActivity.this, ImageList);
                    recyclerView.setAdapter(imageAdapter);

                }


                /*int totalitem = data.getClipData().getItemCount();

                for (int i = 0; i < totalitem; i++) {


                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    //String imagename = getFileName(imageUri);
                    String imageUrl = imageUri.toString();
                    String imageName;
                    //imageName = getFileName(imageUri);
                    imageName = petName + i;

                    Image imageClass = new Image(imageName,imageUrl);
                    ImageList.add(imageClass);

                    imageAdapter = new ImageAdapter(AddImgActivity.this, ImageList);
                    recyclerView.setAdapter(imageAdapter);

                    StorageReference mRef = storageReference.child("Pet_Image/")
                            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child(petName).child(imageName);

                    int finalI = i+1;
                    mRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Uri> task) {

                                    String downloadUrl = Objects.requireNonNull(task.getResult()).toString();
                                    databaseReference.child(petName).child("ImageFolder").child(imageName).setValue(downloadUrl);
                                    databaseReference.child(petName).child("i/").setValue(finalI);
                                    databaseRefer.child(uid).child(petName).child("ImageFolder").child(imageName).setValue(downloadUrl);
                                    databaseRefer.child(uid).child(petName).child("i/").setValue(finalI);
                                    *//*root.child(petName).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                            if(snapshot.hasChild("ImageTotal")){
                                                String totalnum = snapshot.child("ImageTotal").getValue().toString();
                                                String newTotal = totalnum + totalitem;
                                                newTotal.trim();

                                                databaseReference.child(petName).child("ImageTotal").setValue(newTotal);
                                                databaseRefer.child(uid).child(petName).child("ImageTotal").setValue(newTotal);

                                            }else{
                                                databaseReference.child(petName).child("ImageTotal").setValue(totalitem);
                                                databaseRefer.child(uid).child(petName).child("ImageTotal").setValue(totalitem);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                        }
                                    });*//*
                                }
                            });
                            Toast.makeText(AddImgActivity.this, "Image Done Uploaded! " , Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddImgActivity.this, "Fail to Upload!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }*/

            } else if (data.getData() != null) {
                Toast.makeText(this, "single", Toast.LENGTH_SHORT).show();
            }

        }

    }


    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


}

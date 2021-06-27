package com.example.meetfurryapplication;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;


public class AddPost extends AppCompatActivity implements View.OnClickListener {

    ///////////////////////////////////////////////////////////////
    private FirebaseFirestore firestore;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage storage;
    private String userID;
    private String Username;
    private FirebaseUser user;
    private DatabaseReference reference;


    public Uri imageUri = null;

    private TextView mUsername;
    private ImageView addPhoto;
    private Button mUploadPhotoBtn, mAddPostBtn, mBackBtn;
    private EditText mCaptionText;
    // private ProgressBar mProgressBar;
    private static final int GalleryPick = 1;
    ////////////////////////////////////////////////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);


        //////////////////////////////////////////////////////////////

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
                    mUsername.setText(Username);

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(AddPost.this, "Something Wrong happened!", Toast.LENGTH_LONG).show();
            }
        });
///////////////////////////////-----------------------------------------

        mUploadPhotoBtn = findViewById(R.id.uploadBtn);
        mAddPostBtn = findViewById(R.id.postBtn);
        mCaptionText = findViewById(R.id.caption_text);
        mUsername = findViewById(R.id.username_tv);






        addPhoto = findViewById(R.id.addPhoto);

        storage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


        mBackBtn = findViewById(R.id.BackBtn);
        mBackBtn.setOnClickListener(this);


        mUploadPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GalleryPick);


            }
        });

        mAddPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String caption = mCaptionText.getText().toString();
                String username = mUsername.getText().toString();
                if (!caption.isEmpty() && imageUri != null) {

                    StorageReference postRef = storageReference.child("Forum_Image").child(FieldValue.serverTimestamp().toString() + ".jpg");
                    postRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                            postRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    HashMap<String, Object> postMap = new HashMap<>();
                                    postMap.put("forumImage", uri.toString());
                                    postMap.put("user", userID);
                                    postMap.put("Username", username);
                                    postMap.put("caption", caption);
                                    postMap.put("time", FieldValue.serverTimestamp());

                                    firestore.collection("ForumPostss").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {


                                            user = FirebaseAuth.getInstance().getCurrentUser();
                                            reference = FirebaseDatabase.getInstance().getReference("Users");
                                            userID = user.getUid();

                                            reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                    String userType = snapshot.child("userType").getValue().toString();

                                                    if (!userType.isEmpty()){

                                                        switch (userType){
                                                            case "PetLover":
                                                                Intent intent=new Intent(AddPost.this,userForum.class);
                                                                startActivity(intent);
                                                                finish();
                                                                break;
                                                            case "AnimalShelter":
                                                                Intent i=new Intent(AddPost.this,Forum.class);
                                                                startActivity(i);
                                                                finish();
                                                                break;
                                                            default:
                                                                Toast.makeText(AddPost.this, "Something went wrong, Please try again!", Toast.LENGTH_SHORT).show();
                                                                break;
                                                        }
                                                    }else{
                                                        Toast.makeText(AddPost.this, "Something went wrong, Please try again!", Toast.LENGTH_SHORT).show();
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                                }
                                            });



                                            Toast.makeText(AddPost.this, "Post added successfully!", Toast.LENGTH_SHORT).show();
                                         //   startActivity(new Intent(AddPost.this, Forum.class));

                                        }
                                    });
                                }
                            });
                        }
                    });
                }

                if (caption.isEmpty()){
                    Toast.makeText(AddPost.this, "Please enter your Caption!", Toast.LENGTH_LONG).show();
                }

                if (imageUri == null){
                    Toast.makeText(AddPost.this, "Please upload your Image!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            imageUri = data.getData();
            addPhoto.setImageURI(imageUri);


        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.BackBtn:
                Cancel();
                break;
        }
    }

    private void Cancel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddPost.this);
        builder.setTitle("Are you sure you want to exit to Forum page? ");
        builder.setMessage("If yes, your post will be discarded.");
        builder.setIcon(R.drawable.meetfurrylogo);

        builder.setPositiveButton("Yes", (dialog, which) -> {
            Toast.makeText(AddPost.this, "Back to forum...", Toast.LENGTH_LONG).show();
          /*  Intent intent = new Intent(AddPost.this, Forum.class);
            startActivity(intent);*/
            finish();
        });
        builder.setNegativeButton("No", (dialog, which) -> Toast.makeText(AddPost.this, "You can continue to edit your post", Toast.LENGTH_LONG).show());
        builder.create().show();
    }
}
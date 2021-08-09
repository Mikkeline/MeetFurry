package com.example.meetfurryapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int GalleryPick = 1;
    public Uri imageUri = null;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    private FirebaseAuth mAuth;
    private TextInputLayout editTextName, editTextEmail, editTextContact, editTextAddress, editTextusername, editTextpassword, editTextconfirmpassword;
    private ProgressBar progressBar;
    private String userType;
    private CircleImageView profilePic;
    private String downloadUrl;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Users");
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_activity);
        mAuth = FirebaseAuth.getInstance();

        TextView signUpAction;
        signUpAction = findViewById(R.id.btnSubmit);
        signUpAction.setOnClickListener(this);

        TextView cancelAction;
        cancelAction = findViewById(R.id.btnCancel);
        cancelAction.setOnClickListener(this);

        ImageButton backAction;
        backAction = findViewById(R.id.btnBackToStartingPage);
        backAction.setOnClickListener(this);

        editTextName = findViewById(R.id.Name);
        editTextEmail = findViewById(R.id.Email);
        editTextContact = findViewById(R.id.ContactNumber);
        editTextAddress = findViewById(R.id.Address);
        editTextusername = findViewById(R.id.Username);
        editTextpassword = findViewById(R.id.Password);
        editTextconfirmpassword = findViewById(R.id.ConfirmPassword);

        progressBar = findViewById(R.id.progressBar1);

        userType = getIntent().getExtras().get("userType").toString();
        profilePic = findViewById(R.id.signup_userImage);
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                //get photo from user's device
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
            profilePic.setImageURI(imageUri);

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                closeKeyboard();
                progressBar.setVisibility(View.VISIBLE);
                Sign_Up();
                break;
            case R.id.btnCancel:
            case R.id.btnBackToStartingPage:
                closeKeyboard();
                progressBar.setVisibility(View.VISIBLE);
                Cancel();
                break;
        }
    }


    private void Cancel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
        builder.setTitle("Are you sure to cancel your sign up process?");
        builder.setMessage("If yes, you will jump back to starting page.");
        builder.setIcon(R.drawable.meetfurrylogo);

        builder.setPositiveButton("Yes", (dialog, which) -> {
            Toast.makeText(SignUpActivity.this, "Opps, See you Next Time!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(SignUpActivity.this, StartingPage.class);
            startActivity(intent);
            finish();
        });
        builder.setNegativeButton("No", (dialog, which) -> Toast.makeText(SignUpActivity.this, "PHEW, Almost lost your information!", Toast.LENGTH_LONG).show());
        builder.create().show();
    }

    public final boolean isValidPassword(String pw) {
        final boolean matches = Pattern.compile("^(?=.*\\d)(?=.*[a-zA-Z])[a-zA-Z0-9]{8,}$").matcher(pw).matches();
        return matches;
    }

    private void Sign_Up() {
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Users");
        //validate field
        String Name = Objects.requireNonNull(editTextName.getEditText()).getText().toString().trim();
        String Email = Objects.requireNonNull(editTextEmail.getEditText()).getText().toString().trim();
        String ContactNumber = Objects.requireNonNull(editTextContact.getEditText()).getText().toString().trim();
        String Address = Objects.requireNonNull(editTextAddress.getEditText()).getText().toString().trim();
        String Username = Objects.requireNonNull(editTextusername.getEditText()).getText().toString().toLowerCase().trim();
        String Password = Objects.requireNonNull(editTextpassword.getEditText()).getText().toString().trim();
        String ConfirmPassword = Objects.requireNonNull(editTextconfirmpassword.getEditText()).getText().toString().trim();

        if (Name.isEmpty()) {
            progressBar.setVisibility(View.INVISIBLE);
            editTextName.setErrorEnabled(true);
            Toast.makeText(this, "Full Name is required!", Toast.LENGTH_LONG).show();
            editTextName.setError("Full Name is required!");
            editTextName.requestFocus();
            return;
        }

        if (Email.isEmpty()) {
            progressBar.setVisibility(View.INVISIBLE);
            editTextEmail.setErrorEnabled(true);
            Toast.makeText(this, "Email is required!", Toast.LENGTH_LONG).show();
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            progressBar.setVisibility(View.INVISIBLE);
            editTextEmail.setErrorEnabled(true);
            Toast.makeText(this, "Please provide a valid email address!", Toast.LENGTH_LONG).show();
            editTextEmail.setError("Please provide a valid email address!");
            editTextEmail.requestFocus();
            return;
        }

        if (ContactNumber.isEmpty()) {
            progressBar.setVisibility(View.INVISIBLE);
            editTextContact.setErrorEnabled(true);
            Toast.makeText(this, "ContactNumber is required!", Toast.LENGTH_LONG).show();
            editTextContact.setError("ContactNumber is required!");
            editTextContact.requestFocus();
            return;
        }

        if (Address.isEmpty()) {
            progressBar.setVisibility(View.INVISIBLE);
            editTextAddress.setErrorEnabled(true);
            Toast.makeText(this, "Address is required!", Toast.LENGTH_LONG).show();
            editTextAddress.setError("Address is required!");
            editTextAddress.requestFocus();
            return;
        }

        if (Username.isEmpty()) {
            progressBar.setVisibility(View.INVISIBLE);
            editTextusername.setErrorEnabled(true);
            Toast.makeText(this, "Username is required!", Toast.LENGTH_LONG).show();
            editTextusername.setError("Username is required!");
            editTextusername.requestFocus();
            return;
        }

        if (Password.isEmpty()) {
            progressBar.setVisibility(View.INVISIBLE);
            editTextpassword.setErrorEnabled(true);
            Toast.makeText(this, "Password is required!", Toast.LENGTH_LONG).show();
            editTextpassword.setError("Password is required!");
            editTextpassword.requestFocus();
            return;
        }

        if (!isValidPassword(Password)){
            progressBar.setVisibility(View.INVISIBLE);
            editTextpassword.setErrorEnabled(true);
            Toast.makeText(this, "Password require at least 1 Upper Case Letter, 1 Lower Case Letter and 1 numeric character!", Toast.LENGTH_LONG).show();
            editTextpassword.setError("Password require at least 1 Upper Case Letter, 1 Lower Case Letter and 1 numeric character!");
            editTextpassword.requestFocus();
            return;
        }

        if (Password.length() < 8) {
            progressBar.setVisibility(View.INVISIBLE);
            editTextpassword.setErrorEnabled(true);
            Toast.makeText(this, "Min Password Length should be 8 characters!", Toast.LENGTH_LONG).show();
            editTextpassword.setError("Min Password Length should be 8 characters!");
            editTextpassword.requestFocus();
            return;
        }

        if (ConfirmPassword.isEmpty()) {
            progressBar.setVisibility(View.INVISIBLE);
            editTextconfirmpassword.setErrorEnabled(true);
            Toast.makeText(this, "Confirm Password is required!", Toast.LENGTH_LONG).show();
            editTextconfirmpassword.setError("Confirm Password is required!");
            editTextconfirmpassword.requestFocus();
            return;
        }

        if (!ConfirmPassword.matches(Password)) {
            progressBar.setVisibility(View.INVISIBLE);
            editTextconfirmpassword.setErrorEnabled(true);
            editTextconfirmpassword.requestFocus();
            Toast.makeText(this, "Confirm Password is not the same with Password!", Toast.LENGTH_LONG).show();
            editTextconfirmpassword.setError("Confirm Password is not the same with Password!");
            return;
        }
        closeKeyboard();
        progressBar.setVisibility(View.VISIBLE);
        //create an user account
        mAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {

                            FirebaseUser currentUsers = FirebaseAuth.getInstance().getCurrentUser();
                            assert currentUsers != null;
                            final String uid = currentUsers.getUid();

                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            storageReference = storage.getReference("User_ProfilePic");

                            StorageReference imgRef = storageReference
                                    .child("Profile_Pic/")
                                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child(Username);
                            //store profile image in firebase storage
                            imgRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    //getDownloadUrl from the firebase storage to store in realtime db
                                    imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Uri> task) {
                                            downloadUrl = Objects.requireNonNull(task.getResult()).toString();
                                            UsersClass usersClass = new UsersClass();

                                            reference.child(Username).setValue(usersClass);
                                            UsersClass user = new UsersClass();
                                            user.setName(Name);
                                            user.setEmail(Email);
                                            user.setContact(ContactNumber);
                                            user.setAddress(Address);
                                            user.setUsername(Username);
                                            user.setPassword(Password);
                                            user.setUserType(userType);
                                            user.setUid(uid);
                                            user.setImageUrl(downloadUrl);
                                            //store user's information into realtime db
                                            rootNode.getReference("Users")
                                                    .child(uid)
                                                    .setValue(user).addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    Toast.makeText(SignUpActivity.this, "User Account has been Sign Up successfully! Now Please Login with your account!", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(SignUpActivity.this, StartingPage.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Toast.makeText(SignUpActivity.this, "Almost there, sign Up process failed! ;( Please Try again!", Toast.LENGTH_LONG).show();
                                                    progressBar.setVisibility(View.GONE);
                                                }
                                            });

                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Failed to Add Image!", Toast.LENGTH_LONG).show();
                                }
                            });

                    }else {
                        Toast.makeText(SignUpActivity.this, "Sign Up process failed! Please Try again!", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void closeKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

    }
}
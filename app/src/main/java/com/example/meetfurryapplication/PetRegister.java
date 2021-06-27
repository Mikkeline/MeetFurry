package com.example.meetfurryapplication;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.Calendar;
import java.util.Objects;

public class PetRegister extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextpetName , editTextpetBreed, editTextpetColor, editTextpetDesc;
    private String Type, Gender;
    private DatePickerDialog dpd;
    private Calendar c;
    private TextView petIntakeDate, mAddress, mName, mAge;
    private int count = 0;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    private ImageView pic;
    private Uri imageUri;
    private String downloadUrl;
    FirebaseStorage storage;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseRefer;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_register);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                UsersClass userProfile = snapshot.getValue(UsersClass.class);
                if (userProfile != null) {
                    String address = userProfile.Address;
                    mAddress.setText(address);

                    String shelterName = userProfile.username;
                    mName.setText(shelterName);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(PetRegister.this, "Something Wrong happened!", Toast.LENGTH_LONG).show();
            }
        });


        mAddress = findViewById(R.id.address_tv);
        mName = findViewById(R.id.shelterName_tv);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("Pet_Registration");
        databaseReference = database.getReference("Pet_Registration");
        databaseRefer = database.getReference("shelter_pet");

        editTextpetName = findViewById(R.id.PetName);
        editTextpetBreed = findViewById(R.id.PetBreed);
        editTextpetColor = findViewById(R.id.PetColor);
        editTextpetDesc = findViewById(R.id.PetDes);

        Spinner st = findViewById(R.id.spinner_petType);
        String[] pet_type = getResources().getStringArray(R.array.a_PetType);
        ArrayAdapter<String> at = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, pet_type);
        at.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        st.setAdapter(at);
        st.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getId() == R.id.spinner_petType){
                    Type = parent.getItemAtPosition(position).toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(PetRegister.this, "Please choose a pet type.", Toast.LENGTH_LONG).show();
            }
        });

        Spinner sg = findViewById(R.id.spinner_petGender);
        String[] pet_gender = getResources().getStringArray(R.array.a_PetGender);
        ArrayAdapter<String> ag = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, pet_gender);
        ag.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sg.setAdapter(ag);
        sg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getId() == R.id.spinner_petGender){
                    Gender = parent.getItemAtPosition(position).toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(PetRegister.this, "Please choose a date.", Toast.LENGTH_LONG).show();
            }
        });

        mAge = findViewById(R.id.etAge);
        Button BtnPlus = findViewById(R.id.btnPlus);
        BtnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if (count > 30 ){
                    count = 30;
                    Toast.makeText(PetRegister.this, "Oops, 30 years old seem like the limits!", Toast.LENGTH_LONG).show();
                }
                mAge.setText(String.valueOf(count));
            }
        });

        Button BtnMinus = findViewById(R.id.btnMinus);
        BtnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count--;
                if (count < 0 ){
                    count = 0;
                    Toast.makeText(PetRegister.this, "Oops, there is no age below than 0.", Toast.LENGTH_LONG).show();
                }
                mAge.setText(String.valueOf(count));
            }
        });


        petIntakeDate = findViewById(R.id.PetIntakeDate);
        petIntakeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                dpd = new DatePickerDialog(PetRegister.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDay) {
                        petIntakeDate.setText (mDay + "/" + (mMonth+1) + "/" + mYear);
                    }
                },day, month, year);
                dpd.show();
            }
        });

        pic = findViewById(R.id.petPictureupload);
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                chooseImage();
            }
        });

        Button btnsubmitReg = findViewById(R.id.btnSubmitReg);
        btnsubmitReg.setOnClickListener(this);
        Button btncancelReg = findViewById(R.id.btnCancelReg);
        btncancelReg.setOnClickListener(this);
        ImageButton btnBack = findViewById(R.id.btnBackToStartingPage);
        btnBack.setOnClickListener(this);

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
            pic.setImageURI(imageUri);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSubmitReg:
                RegisterPet();
                break;
            case R.id.btnCancelReg:
            case R.id.btnBackToStartingPage:
                Cancel();
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void submitRegistration() {

        closeKeyboard();
        AlertDialog.Builder progress = new AlertDialog.Builder(this);
        progress.setCancelable(false);
        progress.setView(R.layout.progress_dialog);
        AlertDialog dialog = progress.create();
        dialog.show();
        //RegisterPet();
        String name = editTextpetName.getText().toString().trim();
        String shelterName = mName.getText().toString().trim();
        if (imageUri != null) {
            StorageReference imgRef = storageReference
                    .child("Pet_Image/")
                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child(name);

            imgRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Uri> task) {

                            FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
                            String UID = User.getUid();

                            downloadUrl = Objects.requireNonNull(task.getResult()).toString();
                            databaseReference.child(name).child("imageUrl/").setValue(downloadUrl);
                            databaseRefer.child(UID).child(name).child("imageUrl/").setValue(downloadUrl);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Failed to Upload Image!", Toast.LENGTH_LONG).show();
                }
            });
        }else {
            Toast.makeText(PetRegister.this,"Image not found",Toast.LENGTH_LONG).show();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void RegisterPet() {
        AlertDialog.Builder progress = new AlertDialog.Builder(this);
        progress.setCancelable(false);
        progress.setView(R.layout.progress_dialog);
        AlertDialog dialog = progress.create();

        String Pet_Name = editTextpetName.getText().toString().trim();
        String Pet_Type = Type.trim();
        String Pet_Breed = editTextpetBreed.getText().toString().trim();
        String Pet_Gender = Gender.trim();
        String Pet_Color = editTextpetColor.getText().toString().trim();
        String Pet_Age = mAge.getText().toString().trim();
        String Pet_IntakeDate = petIntakeDate.getText().toString().trim();
        String Pet_Description = editTextpetDesc.getText().toString().trim();
        String address = mAddress.getText().toString().trim();
        String shelterName = mName.getText().toString().trim();
        String filter = Pet_Type + Pet_Gender + Pet_Color + Pet_Breed;


        if (Pet_Name.isEmpty()) {
            Toast.makeText(PetRegister.this, "Pet Name is required!", Toast.LENGTH_LONG).show();
            editTextpetName.setError("Pet Name is required!");
            editTextpetName.requestFocus();
            dialog.dismiss();
            return;
        }

        if (Pet_Breed.isEmpty()) {
            Toast.makeText(PetRegister.this, "Pet Breed is required!", Toast.LENGTH_LONG).show();
            editTextpetBreed.setError("Pet Breed is required!");
            editTextpetBreed.requestFocus();
            dialog.dismiss();
            return;
        }

        if (Pet_Color.isEmpty()) {
            Toast.makeText(PetRegister.this, "Pet Color is required!", Toast.LENGTH_LONG).show();
            editTextpetColor.setError("Pet Color is required!");
            editTextpetColor.requestFocus();
            dialog.dismiss();
            return;
        }

        if (Pet_Description.isEmpty()) {
            Toast.makeText(PetRegister.this, "Pet Description is required!", Toast.LENGTH_LONG).show();
            editTextpetDesc.setError("Pet Description is required!");
            editTextpetDesc.requestFocus();
            dialog.dismiss();
            return;
        }

        PetsClass petsClass = new PetsClass();
        FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
        String UID = User.getUid();

        databaseReference.child(Pet_Name).setValue(petsClass);
        PetsClass pets = new PetsClass(Pet_Name, Pet_Type,Pet_Breed,Pet_Gender,Pet_Color,Pet_Age,Pet_IntakeDate,Pet_Description, UID, address, shelterName, filter);



        database.getReference("Pet_Registration")
                .child(Pet_Name)
                .setValue(pets).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {

                submitRegistration();
                Toast.makeText(PetRegister.this, "Pets has been successfully register in MeetFurry! Please promote in Forum section!", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(PetRegister.this, Forum.class);
                startActivity(intent);
                finish();
            } else {
                dialog.dismiss();
                Toast.makeText(PetRegister.this, "Almost there, Pet Registration process failed! ;( Please Try again!", Toast.LENGTH_LONG).show();
            }
        });


        databaseRefer.child(UID).child(Pet_Name).setValue(petsClass);
        PetsClass pet = new PetsClass(Pet_Name, Pet_Type,Pet_Breed,Pet_Gender,Pet_Color,Pet_Age,Pet_IntakeDate,Pet_Description, UID, address, shelterName, filter);

        database.getReference("shelter_pet")
                .child(UID)
                .child(Pet_Name)
                .setValue(pet).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                submitRegistration();
                Toast.makeText(PetRegister.this, "Pets has been successfully register in MeetFurry! Please promote in Forum section!", Toast.LENGTH_LONG).show();
            } else {
                dialog.dismiss();
                Toast.makeText(PetRegister.this, "Almost there, Pet Registration process failed! ;( Please Try again!", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void Cancel() {
        closeKeyboard();
        AlertDialog.Builder builder = new AlertDialog.Builder(PetRegister.this);
        builder.setTitle("Are you sure to cancel the register process?");
        builder.setMessage("If yes, you will jump back to home page.");
        builder.setIcon(R.drawable.meetfurrylogo);

        builder.setPositiveButton("Yes", (dialog, which) -> {
            finish();
        });
        builder.setNegativeButton("No", (dialog, which) -> Toast.makeText(PetRegister.this, "PHEW, Almost lost the pet information!", Toast.LENGTH_LONG).show());
        builder.create().show();
    }

    private void closeKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

    }
}
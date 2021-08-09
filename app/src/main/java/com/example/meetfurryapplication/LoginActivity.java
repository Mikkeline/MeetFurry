package com.example.meetfurryapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText userEmail, userPassword;
    private ProgressBar progressBar1;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private String userType;

    public LoginActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);

        userEmail = (EditText) findViewById(R.id.UserEmail);
        userPassword = (EditText) findViewById(R.id.UserPassword);

        TextView buttonLogin;
        buttonLogin = findViewById(R.id.btnLogin);
        buttonLogin.setOnClickListener(this);

        TextView buttonForgotPassword;
        buttonForgotPassword = findViewById(R.id.btnForgotPassword);
        buttonForgotPassword.setOnClickListener(this);

        TextView buttonSignUp;
        buttonSignUp = findViewById(R.id.btnSignUp);
        buttonSignUp.setOnClickListener(this);

        ImageButton backAction;
        backAction = findViewById(R.id.btnBackToStartingPage2);
        backAction.setOnClickListener(this);

        progressBar1 = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                closeKeyboard();
                userLogin();
                break;
            case R.id.btnForgotPassword:
                closeKeyboard();
                Intent intent=new Intent(LoginActivity.this,ForgotPassword.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnSignUp:
                closeKeyboard();
                //select userType to signup
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Type Of User?");
                builder.setMessage("Please select type of the user. ");
                builder.setIcon(R.drawable.meetfurrylogo);

                builder.setPositiveButton("   Pet Lover", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userType = "PetLover";
                        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                        intent.putExtra("userType",userType);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Animal Shelter  ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userType = "AnimalShelter";
                        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                        intent.putExtra("userType",userType);
                        startActivity(intent);
                    }
                });
                builder.create().show();
                break;
            case R.id.btnBackToStartingPage2:
                closeKeyboard();
                progressBar1.setVisibility(View.VISIBLE);
                Toast.makeText(LoginActivity.this, "Opps, See you Next Time!", Toast.LENGTH_LONG).show();
                Intent startUp = new Intent(LoginActivity.this, StartingPage.class);
                startActivity(startUp);
                finish();
                break;
        }
    }

    private void userLogin(){
        String email = userEmail.getText().toString().trim();
        String pw = userPassword.getText().toString().trim();

        //validation checking
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pw)) {
            progressBar1.setVisibility(View.INVISIBLE);
            Toast.makeText(LoginActivity.this, "Email and Password is not complete, Please fill in your email and password!", Toast.LENGTH_SHORT).show();
            userEmail.requestFocus();
            userPassword.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            progressBar1.setVisibility(View.INVISIBLE);
            Toast.makeText(LoginActivity.this, "Email is not valid, Please fill in a valid email!", Toast.LENGTH_SHORT).show();
            userEmail.requestFocus();
            return;
        }
        if (pw.length() < 8){
            progressBar1.setVisibility(View.INVISIBLE);
            Toast.makeText(LoginActivity.this, "Password is not valid, Please fill in a valid password!", Toast.LENGTH_SHORT).show();
            userPassword.requestFocus();
            return;
        }

        progressBar1.setVisibility(View.VISIBLE);

        //login checking
        mAuth.signInWithEmailAndPassword(email,pw).addOnCompleteListener(task1 -> {
            if(task1.isSuccessful()){
                user = FirebaseAuth.getInstance().getCurrentUser();
                reference = FirebaseDatabase.getInstance().getReference("Users");
                userID = user.getUid();
                //check for userType
                reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        String userType = snapshot.child("userType").getValue().toString();

                        if (!userType.isEmpty()){

                            switch (userType){
                                case "PetLover":
                                    Intent intent=new Intent(LoginActivity.this,userHomepage.class);
                                    startActivity(intent);
                                    finish();
                                    break;
                                case "AnimalShelter":
                                    Intent i=new Intent(LoginActivity.this,shelterHomepage.class);
                                    startActivity(i);
                                    finish();
                                    break;
                                default:
                                    Toast.makeText(LoginActivity.this, "Something went wrong, Please try again!", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }else{
                            Toast.makeText(LoginActivity.this, "Something went wrong, Please try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    }
                });

            }else{
                Toast.makeText(LoginActivity.this, "Failed to login! Please check your credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void closeKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

    }
}







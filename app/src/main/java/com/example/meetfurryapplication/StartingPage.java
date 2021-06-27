package com.example.meetfurryapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class StartingPage extends AppCompatActivity implements View.OnClickListener {

    private TextView btnLogin, btnSignUp;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_page);

        btnLogin = findViewById(R.id.btnLoginNavigate);
        btnLogin.setOnClickListener(this);

        btnSignUp = findViewById(R.id.btnSignUpNavigate);
        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLoginNavigate:
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.btnSignUpNavigate:

                AlertDialog.Builder builder = new AlertDialog.Builder(StartingPage.this);
                builder.setTitle("Type Of User?");
                builder.setMessage("Please select type of the user. ");
                builder.setIcon(R.drawable.meetfurrylogo);

                builder.setPositiveButton("   Pet Lover", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userType = "PetLover";
                        Intent intent = new Intent(StartingPage.this, SignUpActivity.class);
                        intent.putExtra("userType",userType);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Animal Shelter  ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userType = "AnimalShelter";
                        Intent intent = new Intent(StartingPage.this, SignUpActivity.class);
                        intent.putExtra("userType",userType);
                        startActivity(intent);
                    }
                });
                builder.create().show();
                break;
        }
    }
}
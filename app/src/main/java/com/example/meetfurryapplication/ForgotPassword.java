package com.example.meetfurryapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    Button btnresetpw;
    EditText edittextEmail;
    ProgressBar progressBar;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        btnresetpw = findViewById(R.id.btnResetPassword);
        btnresetpw.setOnClickListener(v -> resetpassword());

        edittextEmail =findViewById(R.id.rp_email);

        ImageButton backAction;
        backAction = findViewById(R.id.btnBackToStartingPage3);
        backAction.setOnClickListener(this);

        progressBar= findViewById(R.id.progressBar_ForgotPassword);

        auth = FirebaseAuth.getInstance();
    }

    private void resetpassword() {
        String email = edittextEmail.getText().toString().trim();

        if (email.isEmpty()){
            edittextEmail.setError("Email is required!");
            edittextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edittextEmail.setError("Please provide a valid email address!");
            edittextEmail.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {

            if(task.isSuccessful()){
                Toast.makeText(ForgotPassword.this,"Check your email to reset your password.",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(ForgotPassword.this,StartingPage.class);
                startActivity(intent);
            }else {
                Toast.makeText(ForgotPassword.this,"Please try again! Something went wrong!.",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                Intent intent=new Intent(ForgotPassword.this,StartingPage.class);
                startActivity(intent);
            }
            finish();
        });
    }

    @Override
    public void onClick(View v) {
        progressBar.setVisibility(View.VISIBLE);
        Toast.makeText(ForgotPassword.this, "Opps, See you Next Time!", Toast.LENGTH_LONG).show();
        Intent startUp = new Intent(ForgotPassword.this, StartingPage.class);
        startActivity(startUp);
        finish();
    }
}
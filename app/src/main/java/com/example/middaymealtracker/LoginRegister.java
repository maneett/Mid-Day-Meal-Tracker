package com.example.middaymealtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginRegister extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private EditText editTextEmail, editTextPass;
    private Button loginButton;

    private FirebaseAuth mAuth;


    private ProgressBar progressBar;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.textView_register);
        register.setOnClickListener(this);

        loginButton = findViewById(R.id.button_Login);
        loginButton.setOnClickListener(this);

        editTextEmail = findViewById(R.id.editTextTextEmailAddress_Login);
        editTextPass = findViewById(R.id.editTextTextPassword_Login);

        progressBar = findViewById(R.id.progressBar_Login);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case(R.id.textView_register):
                startActivity(new Intent(this, com.example.middaymealtracker.register.class));
                break;
            case R.id.button_Login:
               userLogin();
        }
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String pass = editTextPass.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please Enter a Valid Email Address!");
            editTextEmail.requestFocus();
            return;
        }
        if(pass.isEmpty()){
            editTextPass.setError("Password id required!");
            editTextPass.requestFocus();
            return;
        }
        if(pass.length() < 0){
            editTextPass.setError("min Password length should be 6 characters!");
            editTextPass.requestFocus();
            return;

        }
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful() && user != null){
                    startActivity(new Intent(LoginRegister.this,userProfile.class));
                    //redirect to user profile
//                    if(user.isEmailVerified()){
//                        startActivity(new Intent(LoginRegister.this,userProfile.class));
//                    }else {
//                        user.sendEmailVerification();
//                        Toast.makeText(LoginRegister.this, "Check your Email!", Toast.LENGTH_SHORT).show();
//                    }
                }else {

                    Toast.makeText(LoginRegister.this, "Failed to login try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
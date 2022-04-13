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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


    private TextView  banner;
    private EditText  editTextFullname, editTextEmail, editTextPassword, editTextAge ;
    private Button editTextRegisteruser;

    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        banner = findViewById(R.id.banner);
        banner.setOnClickListener(this);

        editTextFullname = findViewById(R.id.editTextFullname);
        editTextEmail = findViewById(R.id.editTextEmailAddress_Register);
        editTextPassword = findViewById(R.id.editTextPassword_Register);
        editTextAge = findViewById(R.id.editTextAge);

        editTextRegisteruser = findViewById(R.id.button_Register);
        editTextRegisteruser.setOnClickListener(this);

       progressBar = findViewById(R.id.progressBar_Register);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.banner:
                startActivity(new Intent(this, LoginRegister.class));
                break;
            case R.id.button_Register:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String age = editTextAge.getText().toString().trim();
        String pass = editTextPassword.getText().toString().trim();
        String fullname = editTextFullname.getText().toString().trim();

        if(fullname.isEmpty()){
            editTextFullname.setError("FullName is required!");
            editTextFullname.requestFocus();
            return;
        }
        if(age.isEmpty()){
            editTextAge.setError("Age is required!");
            editTextAge.requestFocus();
            return;
        }
        if(email.isEmpty()){
            editTextEmail.setError("FullName is required!");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please Enter a Valid Email Address!");
            editTextEmail.requestFocus();
            return;
        }
        if(pass.isEmpty()){
            editTextPassword.setError("Password id required!");
            editTextPassword.requestFocus();
            return;
        }
        if(pass.length() < 0){
            editTextPassword.setError("min Password length should be 6 characters!");
            editTextPassword.requestFocus();
            return;

        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isComplete() && currentUser != null){
                            user user = new user(fullname,email,age,pass);
                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(currentUser.getUid()).setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isComplete()){
                                        Toast.makeText(register.this,"user has been registered!",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                        startActivity(new Intent(register.this, LoginRegister.class));
                                    }
                                    else{
                                        Toast.makeText(register.this,"Failed!",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(register.this,"Failed! to registered",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}
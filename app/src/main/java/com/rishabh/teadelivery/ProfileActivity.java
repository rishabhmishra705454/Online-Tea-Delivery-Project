package com.rishabh.teadelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    TextInputEditText inputFullName  , imputPhoneNo, inputEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (firebaseAuth.getCurrentUser() == null){
            finish();

            Intent intent = new Intent(ProfileActivity.this , LoginActivity.class);
            startActivity(intent);
            finish();
        }

        inputFullName = findViewById(R.id.inputFullName);
        imputPhoneNo = findViewById(R.id.inputPhoneNumber);
        inputEmail= findViewById(R.id.inputEmail);

        Button backBtn = findViewById(R.id.backbutton);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProfileActivity.super.onBackPressed();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("ProfileDetail" , MODE_PRIVATE);
        String fullName = sharedPreferences.getString("fullName","");
        String email = sharedPreferences.getString("email","");

        inputFullName.setText(fullName);
        imputPhoneNo.setText(user.getPhoneNumber());
        inputEmail.setText(email);

    }


}
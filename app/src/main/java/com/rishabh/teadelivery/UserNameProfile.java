package com.rishabh.teadelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rishabh.teadelivery.Helperclass.UserProfileHelperClass;

public class UserNameProfile extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    TextInputEditText mUserName, mUserEmail;
    Button mContinueBtn;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name_profile);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null){
            finish();

            Intent intent = new Intent(UserNameProfile.this , LoginActivity.class);
            startActivity(intent);
        }

        mUserName = findViewById(R.id.inputUserName);
        mContinueBtn = findViewById(R.id.userProfileContinueBtn);

        mUserEmail = findViewById(R.id.inputUserEmail);

        FirebaseUser user = firebaseAuth.getCurrentUser();


        mContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                

               if (!validateFullName() || !validateEmail()) {
                    return;
                }

                String fullName = mUserName.getText().toString();
                String uid = user.getUid();
                String phone = user.getPhoneNumber();
                String email = mUserEmail.getText().toString();



                 reference = FirebaseDatabase.getInstance().getReference("Users");

                UserProfileHelperClass addData = new UserProfileHelperClass( phone, fullName, uid, email);

                reference.child(uid).setValue(addData);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String fullNameFromDB = snapshot.child(user.getUid()).child("fullName").getValue(String.class);
                        String mobileNoFromDB = snapshot.child(user.getUid()).child("phoneNo").getValue(String.class);
                        String emailFromDB = snapshot.child(user.getUid()).child("email").getValue(String.class);

                        SharedPreferences sharedPreferences = getSharedPreferences("ProfileDetail" , MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                        myEdit.putString("fullName" , fullNameFromDB);
                        myEdit.putString("phoneNo" , mobileNoFromDB);
                        myEdit.putString("email" , emailFromDB);
                        myEdit.commit();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Intent intent = new Intent(UserNameProfile.this , LocationPermission.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();


            }
        });

    }

    private boolean validateEmail() {

        String val = mUserEmail.getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

        if (val.isEmpty()) {
            mUserEmail.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkEmail)) {
            mUserEmail.setError("Invalid Email!");
            return false;
        } else {
            mUserEmail.setError(null);
            return true;
        }
    }

    private boolean validateFullName() {


        String val = mUserName.getText().toString().trim();
        if (val.isEmpty()) {
            mUserName.setError("Please enter full name");
            return false;
        } else {
            mUserName.setError(null);
            return true;
        }
    }


}
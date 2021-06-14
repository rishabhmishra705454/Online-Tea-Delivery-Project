package com.rishabh.teadelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText mInputPhoneNumber;
    Button mSendOtpBtn;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mInputPhoneNumber = findViewById(R.id.inputPhoneNumber);
        mSendOtpBtn = findViewById(R.id.buttonGetOTP);
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null){
            finish();

            Intent intent = new Intent(LoginActivity.this , LocationPermission.class);
            startActivity(intent);


        }

        mSendOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validatePhoneNumber()){
                    return;
                }

               String mPhoneNumber = mInputPhoneNumber.getText().toString().trim() ;

                Intent intent = new Intent(LoginActivity.this , OtpActivity.class);
                intent.putExtra("phoneNumber", mPhoneNumber);
                startActivity(intent);
            }
        });


    }



    private boolean validatePhoneNumber() {
        String val = mInputPhoneNumber.getText().toString().trim();
        String checkspaces = "Aw{1,20}z";
        if (val.isEmpty()){
            mInputPhoneNumber.setError("Enter mobile number");
            return false;
        }else if (val.length()<10){
            mInputPhoneNumber.setError("Enter correct number");
            return false;
        }else {
            mInputPhoneNumber.setError(null);
            return true;
        }

    }
}
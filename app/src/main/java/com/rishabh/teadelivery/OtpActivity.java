package com.rishabh.teadelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import static android.media.MediaExtractor.MetricsConstants.FORMAT;

public class OtpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    TextView mSetPhoneNumber, resendOtpBtn, countDownTimer;
    PinView pinView;
    Button verifyOtpBtn;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    ProgressDialog progress;

    String mPhoneNumber;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        progress = new ProgressDialog(this);
        progress.setMessage("Please wait...");
        mAuth = FirebaseAuth.getInstance();

        resendOtpBtn = findViewById(R.id.textResendOtp);
        //hooks
        mSetPhoneNumber = findViewById(R.id.textPhoneNumber);
        pinView = findViewById(R.id.inputCode);
        verifyOtpBtn = findViewById(R.id.verifyBtn);

        countDownTimer = findViewById(R.id.countDownTimer);

        mPhoneNumber = getIntent().getStringExtra("phoneNumber");

        mSetPhoneNumber.setText("Code is send to +91 " + mPhoneNumber);

        phone = "+91" + mPhoneNumber;

        if (mAuth.getCurrentUser() != null) {
            finish();

            Intent intent = new Intent(OtpActivity.this, HomeActivity.class);
            startActivity(intent);


        }
        sendVerificationCode(phone);

        resendOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinView.setText("");
                sendVerificationCode(phone);
                Toast.makeText(OtpActivity.this, "OTP Resend", Toast.LENGTH_SHORT).show();
            }
        });

        verifyOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCode(pinView.getText().toString());
            }
        });

        LinearLayout resendLayout = findViewById(R.id.resendLayout);
        LinearLayout timerLayout = findViewById(R.id.timerLayout);

        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                countDownTimer.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                timerLayout.setVisibility(View.INVISIBLE);
                resendLayout.setVisibility(View.VISIBLE);
            }
        }.start();


    }


    private void sendVerificationCode(String phone) {

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            mVerificationId = s;
            mResendToken = forceResendingToken;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            final String code = phoneAuthCredential.getSmsCode();

            if (code != null) {
                pinView.setText(code);
                verifyCode(code);
                progress.show();
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {


            Toast.makeText(OtpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();


        }
    };

    private void verifyCode(String code) {


        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(phoneAuthCredential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {


        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            isUser();

                        } else {
                            Toast.makeText(OtpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    private void isUser() {

        FirebaseUser user = mAuth.getCurrentUser();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Query checkUser = reference.orderByChild("userId").equalTo(user.getUid());

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    String fullNameFromDB = snapshot.child(user.getUid()).child("fullName").getValue(String.class);
                    String mobileNoFromDB = snapshot.child(user.getUid()).child("phoneNo").getValue(String.class);
                    String emailFromDB = snapshot.child(user.getUid()).child("email").getValue(String.class);
                    SharedPreferences sharedPreferences = getSharedPreferences("ProfileDetail" , MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("fullName" , fullNameFromDB);
                    myEdit.putString("phoneNo" , mobileNoFromDB);
                    myEdit.putString("email" , emailFromDB);
                    myEdit.commit();

                    Intent intent = new Intent(OtpActivity.this, LocationPermission.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                     startActivity(intent);
                }
                else {
                    Intent intent = new Intent(OtpActivity.this, UserNameProfile.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
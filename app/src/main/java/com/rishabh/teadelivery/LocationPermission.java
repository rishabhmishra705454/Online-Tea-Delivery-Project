package com.rishabh.teadelivery;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class LocationPermission extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private Button agreeButton;
    SharedPreferences setLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locaton_permission);

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (firebaseAuth.getCurrentUser() == null) {
            finish();

            Intent intent = new Intent(LocationPermission.this, LoginActivity.class);
            startActivity(intent);
        }


        if (ContextCompat.checkSelfPermission(LocationPermission.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            setLocation = getSharedPreferences("LOCATION" , MODE_PRIVATE);


            if (!setLocation.contains("location")){
                startActivity(new Intent(LocationPermission.this, CurrentLocationActivity.class));
                finish();
            }else {
                startActivity(new Intent(LocationPermission.this, HomeActivity.class));
                finish();
            }
            return;
        }

        agreeButton = findViewById(R.id.permission_btn);
        agreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withContext(LocationPermission.this)
                        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                                setLocation = getSharedPreferences("LOCATION" , MODE_PRIVATE);


                                if (!setLocation.contains("location")){
                                    startActivity(new Intent(LocationPermission.this, CurrentLocationActivity.class));
                                    finish();
                                }else {
                                    startActivity(new Intent(LocationPermission.this, HomeActivity.class));
                                    finish();
                                }

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                                if (permissionDeniedResponse.isPermanentlyDenied()){
                                    AlertDialog.Builder builder= new AlertDialog.Builder(LocationPermission.this);
                                    builder.setTitle("Permission Denied")
                                            .setMessage("Permission to access device location is permanently denied .you need to go setting to allow the permission")
                                            .setNegativeButton("Cancel",null)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent = new Intent();
                                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                    intent.setData(Uri.fromParts("package", getPackageName(),null));
                                                }
                                            })
                                            .show();
                                } else {
                                    Toast.makeText(LocationPermission.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                                permissionToken.continuePermissionRequest();
                            }
                        })
                        .check();
            }
        });
    }
}
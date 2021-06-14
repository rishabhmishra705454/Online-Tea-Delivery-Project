package com.rishabh.teadelivery;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private FirebaseAuth firebaseAuth;
    private GoogleMap mMap;
    private View mapView;
    private HashMap<String, Marker> mMarkers = new HashMap<>();

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private final float DEFAULT_ZOOM = 17;
    private Location mLastKnownLocation;
    private LocationCallback locationCallback;

    double latitude, longitude;

    private GoogleMap.OnCameraIdleListener onCameraIdleListener;

    TextView showLocality, showCurrentLocation, showAddress;

    Button confirmLocationBtn;

    String address , pinCode , locality;

    LottieAnimationView viewAnimation;

    double mlatitude ;
    double mlongitude ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


       // showLocation = findViewById(R.id.showLocation);
        //showCurrentLocation = findViewById(R.id.showCurrentLocation);
        showAddress = findViewById(R.id.showAddress);
        showLocality = findViewById(R.id.setlocality);
        confirmLocationBtn = findViewById(R.id.confirmLocationBtn);
        viewAnimation = findViewById(R.id.loadingView);




        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (firebaseAuth.getCurrentUser() == null) {
            finish();

            Intent intent = new Intent(MapsActivity.this, LoginActivity.class);
            startActivity(intent);
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);

        checkLocatonEnabled();

    }

    private void checkLocatonEnabled() {

        LocationManager locationManager  = null;
        boolean gps_enabled  =false;
        boolean network_enabled = false;
        if (locationManager == null){
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex){}
        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex){}


        if ( !gps_enabled && !network_enabled ){
            new MaterialAlertDialogBuilder(MapsActivity.this)
                    .setTitle("Hello")
                    .setMessage("locaton is not enabled")
                    .setNeutralButton("cancil", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
        .show();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);

        mMap.getUiSettings().setMyLocationButtonEnabled(true);






        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 20, 50);
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(MapsActivity.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(MapsActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();
            }
        });


        task.addOnFailureListener(MapsActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(MapsActivity.this, 51);
                        getDeviceLocation();
                    } catch (IntentSender.SendIntentException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                confirmLocationBtn.setEnabled(false);

                LatLng target = mMap.getCameraPosition().target;
                mlatitude = target.latitude;
                mlongitude = target.longitude;
                // showLocation.setText(Double.toString(latitude) + " " + Double.toString(longitude));

                try {
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    List<Address> addressList = geocoder.getFromLocation(mlatitude, mlongitude, 1);

                    if (addressList != null && addressList.size() > 0) {
                        address  = addressList.get(0).getAddressLine(0);
                        pinCode = addressList.get(0).getPostalCode();
                        locality = addressList.get(0).getLocality();

                        showLocality.setText(locality);
                        showAddress.setText(address);
                        viewAnimation.setVisibility(View.GONE);
                        confirmLocationBtn.setEnabled(true);
                    }

                    //  textLatLong.setText(Double.toString(latitude) + " " + Double.toString(longitude));


                } catch (Exception e) {
                    e.fillInStackTrace();
                }
            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                LatLng target = mMap.getCameraPosition().target;
                mlatitude = target.latitude;
               mlongitude = target.longitude;
                //  showLocation.setText(Double.toString(latitude) + " " + Double.toString(longitude));

                try {
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    List<Address> addressList = geocoder.getFromLocation(mlatitude, mlongitude, 1);


                    if (addressList != null && addressList.size() > 0) {
                         address  = addressList.get(0).getAddressLine(0);
                        pinCode = addressList.get(0).getPostalCode();
                        locality = addressList.get(0).getLocality();

                        showLocality.setText(locality);
                        showAddress.setText(address);
                        viewAnimation.setVisibility(View.GONE);
                        confirmLocationBtn.setEnabled(true);
                    }




                    //  textLatLong.setText(Double.toString(latitude) + " " + Double.toString(longitude));


                } catch (Exception e) {
                    e.fillInStackTrace();
                }
            }
        });

        mMap.setOnCameraMoveCanceledListener(new GoogleMap.OnCameraMoveCanceledListener() {
            @Override
            public void onCameraMoveCanceled() {
                LatLng target = mMap.getCameraPosition().target;
                mlatitude = target.latitude;
                mlongitude = target.longitude;
                //  showLocation.setText(Double.toString(latitude) + " " + Double.toString(longitude));

                try {
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    List<Address> addressList = geocoder.getFromLocation(mlatitude, mlongitude, 1);


                    if (addressList != null && addressList.size() > 0) {
                        address  = addressList.get(0).getAddressLine(0);
                        pinCode = addressList.get(0).getPostalCode();
                        locality = addressList.get(0).getLocality();

                        showLocality.setText(locality);
                        showAddress.setText(address);
                        viewAnimation.setVisibility(View.GONE);
                        confirmLocationBtn.setEnabled(true);
                    }




                    //  textLatLong.setText(Double.toString(latitude) + " " + Double.toString(longitude));


                } catch (Exception e) {
                    e.fillInStackTrace();
                }
            }
        });

        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {

                confirmLocationBtn.setEnabled(false);
            }
        });





    }


    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {

                        if (task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            } else {
                                final LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(20000);
                                locationRequest.setFastestInterval(10000);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if (locationResult == null) {
                                            return;
                                        }
                                        mLastKnownLocation = locationResult.getLastLocation();
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));


                                        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);

                                    }
                                };




                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

                                // latitude = String.valueOf(lat);
                                //longitude = String.valueOf(longi);


                            }
                        } else {
                            Toast.makeText(MapsActivity.this, "Unable to get location", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void getlocation(View view) {


        SharedPreferences setLocation = getSharedPreferences("LOCATION", MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = setLocation.edit();
        editor.putString("address" , address);
        editor.putString("pincode",pinCode);
        editor.putString("locality" ,locality);
        editor.putString("latitude" , Double.toString(mlatitude));
        editor.putString("longitude" , Double.toString(mlongitude));
        editor.apply();

        Intent intent = new Intent(MapsActivity.this , HomeActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();



    }




}
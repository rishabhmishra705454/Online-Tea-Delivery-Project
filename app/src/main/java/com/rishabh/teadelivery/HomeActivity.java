package com.rishabh.teadelivery;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.rishabh.teadelivery.Adapters.CartItemAdapter;
import com.rishabh.teadelivery.Adapters.HomeChaiAdapter;
import com.rishabh.teadelivery.Adapters.StaticRV1Adapter;
import com.rishabh.teadelivery.Helperclass.CartHelperclass;
import com.rishabh.teadelivery.Helperclass.HomeChaiHelperclass;
import com.rishabh.teadelivery.Helperclass.OrderHelperclass;
import com.rishabh.teadelivery.Helperclass.StaticRV1Helperclass;
import com.rishabh.teadelivery.Interface.UpdateRVInterface;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, PaymentResultListener, UpdateRVInterface {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private FirebaseAuth firebaseAuth;

    ImageView chaiAdd, chaiMinus, coffeeAdd, coffeeMinus, toolBar;

    TextView chaiQuantity, coffeeQuantity, chaiQuantityInfo, coffeeQuantityInfo, cartItem, chaiPrice, coffeePrice, totalQuantity, totalPrice, textLocation;

    Button chaiCart, coffeeCart;
    MaterialButton checkoutBtn;

    DrawerLayout drawerLayout;

    NavigationView navigationView;

    //payment

    String fullName;


    Integer totalItems;
    Integer totalPrice1;

    String phoneNo, uid, address, pincode, locality, latitude, longitude, orderPrice, mchaiQuantity, mcoffeeQuantity, houseFlatBlockNo, landmark, mPhoneNumber;

    int quantity3;
    int quantity4;

    FirebaseDatabase database;
    DatabaseReference myRef;


    List<OrderHelperclass> orderHelperclassList;

    List<CartHelperclass> cartHelperclasses;

    private BottomSheetDialog checkoutBottomSheetDialog;

    RelativeLayout bottomSheetRL;
    String email;

    ArrayList<HomeChaiHelperclass> chaiHelperclasses;

    private RecyclerView recyclerView1, recyclerView2, cartLV;
    private StaticRV1Adapter staticRV1Adapter;
    private HomeChaiAdapter homeChaiAdapter;

    LinearLayout layoutBottomSheet;


    BottomSheetBehavior sheetBehavior;


    RelativeLayout bottomSheetRLA;

    TextView textItems, textSubtotal, textTotal;

    LottieAnimationView upDownAnimation;
    TextView textOpenCart, textAddressShow;

    TextInputLayout inputOrderPhoneNo;
    TextInputLayout inputHouse ;
    TextInputLayout inputLandmark;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        textOpenCart = findViewById(R.id.textOpenCart);
        upDownAnimation = findViewById(R.id.upDownAnimation);
        layoutBottomSheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);


        textAddressShow = findViewById(R.id.showAddress1);

        textItems = findViewById(R.id.textItems);
        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:

                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        textOpenCart.setText("MY CART");
                        upDownAnimation.setRotation(0);
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {

                        textOpenCart.setText("OPEN CART");
                        upDownAnimation.setRotation(180);

                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:

                        break;
                    case BottomSheetBehavior.STATE_SETTLING:

                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        sheetBehavior.setHideable(false);

        ArrayList<StaticRV1Helperclass> items = new ArrayList<>();
        items.add(new StaticRV1Helperclass(R.drawable.tea_icon, "Chai"));
        items.add(new StaticRV1Helperclass(R.drawable.coffee_icon, "Coffee"));
        items.add(new StaticRV1Helperclass(R.drawable.milk_icon, "Milk"));


        recyclerView1 = findViewById(R.id.recycler_view_1);
        staticRV1Adapter = new StaticRV1Adapter(items, this, this);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView1.setAdapter(staticRV1Adapter);


        // recycler view

        chaiHelperclasses = new ArrayList<>();
        recyclerView2 = findViewById(R.id.recycler_view_2);
        homeChaiAdapter = new HomeChaiAdapter(chaiHelperclasses, this);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView2.setAdapter(homeChaiAdapter);


        orderHelperclassList = new ArrayList<>();
        cartHelperclasses = new ArrayList<>();

        cartLV = findViewById(R.id.cartLV);
        checkoutBtn = findViewById(R.id.checkoutBtn2);

        textSubtotal = findViewById(R.id.textSubtotalPrice);
        textTotal = findViewById(R.id.textTotalPrice);
        //payment

        SharedPreferences sharedPreferences = getSharedPreferences("ProfileDetail", MODE_PRIVATE);
        fullName = sharedPreferences.getString("fullName", "");
        email = sharedPreferences.getString("email", "");

        Checkout.preload(getApplicationContext());

        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startPayment();


                displayBottomSheet();


            }
        });


        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (firebaseAuth.getCurrentUser() == null) {
            finish();

            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }


        chaiPrice = findViewById(R.id.chaiPrice);

        drawerLayout = findViewById(R.id.drawer_layout);
        toolBar = findViewById(R.id.imageView);
        navigationView = findViewById(R.id.nav_view);
        textLocation = findViewById(R.id.textView6);

        database = FirebaseDatabase.getInstance();
        phoneNo = user.getPhoneNumber();
        uid = user.getUid();
        myRef = database.getReference("orders").child(uid);


        SharedPreferences setLocation = getSharedPreferences("LOCATION", MODE_PRIVATE);
        address = setLocation.getString("address", "");
        locality = setLocation.getString("locality", "");
        pincode = setLocation.getString("pincode", "");
        latitude = setLocation.getString("latitude", "");
        longitude = setLocation.getString("longitude", "");
        textLocation.setText(locality);
        textAddressShow.setText(address);


        toolBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        cartListView();

        //navigation drawer menu

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);


    }

    private void displayBottomSheet() {

        final BottomSheetDialog bottomSheetTeachersDialog = new BottomSheetDialog(HomeActivity.this, R.style.BottomSheetDialogTheme);

        // passing a layout file for our bottom sheet dialog.
        View layout = LayoutInflater.from(HomeActivity.this).inflate(R.layout.checkout_layout, bottomSheetRL);

        // passing our layout file to our bottom sheet dialog.
        bottomSheetTeachersDialog.setContentView(layout);
        bottomSheetTeachersDialog.setCancelable(false);


        TextView textLocality = layout.findViewById(R.id.setlocality);
        TextView textAddress = layout.findViewById(R.id.showAddress);
         inputOrderPhoneNo =  layout.findViewById(R.id.textPhoneNo2);
         inputHouse = layout.findViewById(R.id.inputHouseNo2);
         inputLandmark = layout.findViewById(R.id.inputLandMark2);

        inputOrderPhoneNo.getEditText().setText(phoneNo);
        textLocality.setText(locality);
        textAddress.setText(address);




        MaterialButton orderNowBtn = layout.findViewById(R.id.orderNowBtn);

        CheckBox checkBoxOnline = layout.findViewById(R.id.checkboxOnline);
        CheckBox checkBoxCod = layout.findViewById(R.id.checkboxCod);

        checkBoxOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxOnline.setChecked(true);
                checkBoxCod.setChecked(false);
                orderNowBtn.setEnabled(true);
            }
        });

        checkBoxCod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkBoxOnline.setChecked(false);
                checkBoxCod.setChecked(true);
                orderNowBtn.setEnabled(true);
            }
        });




        orderNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxOnline.isChecked()) {
                    startPayment();
                    bottomSheetTeachersDialog.dismiss();
                }
                if (checkBoxCod.isChecked()) {
                    Toast.makeText(HomeActivity.this, "Ordered Cash", Toast.LENGTH_SHORT).show();
                    sendOrderDetailToServer();
                    bottomSheetTeachersDialog.dismiss();
                    successDialog();
                }
            }
        });


        // below line is to set our bottom sheet cancelable.
        bottomSheetTeachersDialog.setCanceledOnTouchOutside(true);

        FrameLayout frameLayout = bottomSheetTeachersDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
        bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);


        // below line is to display our bottom sheet dialog.
        bottomSheetTeachersDialog.show();

    }


    private void cartListView() {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("cart").child(uid);

        dR.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                CartItemAdapter cartItemAdapter = new CartItemAdapter(HomeActivity.this, cartHelperclasses);

                cartLV.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, false));
                cartLV.setAdapter(cartItemAdapter);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        dR.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartHelperclasses.clear();
                totalItems = 0;
                totalPrice1 = 0;

                if (snapshot.exists()) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {


                        cartLV.setVisibility(View.VISIBLE);
                        // textItems.setText(snapshot.getChildrenCount() + "");
                        CartHelperclass postCartHelperclass = postSnapshot.getValue(CartHelperclass.class);

                        Integer items = Integer.valueOf(postCartHelperclass.getChaiQuanty());
                        Integer cost = Integer.valueOf(postCartHelperclass.getTotalPrice());

                        totalPrice1 = totalPrice1 + cost;
                        totalItems = totalItems + items;
                        cartHelperclasses.add(postCartHelperclass);

                        textItems.setText(Integer.toString(totalItems));
                        textSubtotal.setText(Integer.toString(totalPrice1) + " Rs");
                        textTotal.setText(Integer.toString(totalPrice1) + " Rs");


                    }

                    CartItemAdapter cartItemAdapter = new CartItemAdapter(HomeActivity.this, cartHelperclasses);
                    cartLV.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, false));
                    cartLV.setAdapter(cartItemAdapter);

                } else {
                    textItems.setText("0");
                    textSubtotal.setText("0 Rs");
                    textTotal.setText("0 Rs");
                    cartLV.setVisibility(View.GONE);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    /*
   private void initializeListView() {

       FirebaseDatabase mdatabase = FirebaseDatabase.getInstance();
       DatabaseReference mReference = mdatabase.getReference("orders").child(uid);

       Query query = mReference.orderByChild("orderStatus").equalTo("Ordered");
       query.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {

               orderHelperclassList.clear();
               for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                   OrderHelperclass postSnapshotValue = postSnapshot.getValue(OrderHelperclass.class);
                   orderHelperclassList.add(postSnapshotValue);
               }
               OrderListAdapter orderListAdapter = new OrderListAdapter(HomeActivity.this, orderHelperclassList);
               orderLV.setAdapter(orderListAdapter);

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });


   }


     */
    public void callProfileActivity(View view) {

        Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            setExitAlert();
        }


    }

    private void setExitAlert() {

        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                HomeActivity.this);

        alertDialog2.setMessage("Are you want to exit app");

        alertDialog2.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        alertDialog2.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog2.show();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.nav_bookings:
                Intent intent = new Intent(getApplicationContext(), OrdersActivity.class);
                startActivity(intent);
                break;


        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void callLocationSetupActivity(View view) {

        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(intent);
    }


    public void startPayment() {

        final Activity activity = this;

        final Checkout co = new Checkout();
        co.setKeyID("rzp_test_XgTty3NlOCh4pl");
        try {


            // amount to be in paisa only


            // converting 12p rupees into paisa

            JSONObject options = new JSONObject();
            options.put("name", fullName);
            options.put("description", "");
            //You can omit the image option to fetch the image from dashboard
            //options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", String.valueOf(totalPrice1 * 100));

            //      options.put("order_id", order_DBJOWzybf0sJbb);//from response of step 3.

            JSONObject preFill = new JSONObject();
            preFill.put("email", email);
            preFill.put("contact", mPhoneNumber);

            JSONObject notes = new JSONObject();
            notes.put("Product_details", "Here are the notes");

            options.put("prefill", preFill);
            options.put("notes", notes);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();

        }
    }


    @Override
    public void onPaymentSuccess(String s) {

        sendOrderDetailToServer();

        Toast.makeText(this, "Payment Success with id : " + s, Toast.LENGTH_SHORT).show();

        successDialog();

    }

    private void sendOrderDetailToServer() {

        FirebaseDatabase mdatabase = FirebaseDatabase.getInstance();

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());

        houseFlatBlockNo = inputHouse.getEditText().getText().toString();
        landmark = inputLandmark.getEditText().getText().toString();
        mPhoneNumber = inputOrderPhoneNo.getEditText().getText().toString();

        DatabaseReference refFrom = mdatabase.getReference("cart").child(uid);
        DatabaseReference refTo = mdatabase.getReference("orderItems").child(uid);
        String id = refTo.push().getKey();
        refFrom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                refTo.child(id).setValue(snapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        DatabaseReference ref = mdatabase.getReference("orderDetails").child(uid);
        OrderHelperclass addData = new OrderHelperclass(id, mPhoneNumber, uid, address, locality, pincode, latitude, longitude, Double.toString(totalPrice1), Double.toString(totalItems), "Ordered", currentDate, currentTime, houseFlatBlockNo, landmark);

        ref.child(id).setValue(addData);
    }

    private void successDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);


        ViewGroup viewGroup = findViewById(android.R.id.content);
        View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.order_success_layout, viewGroup, false);

        ImageView closeImg = view.findViewById(R.id.successDialogCLoseImg);

        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        unsuccessDialog();
    }

    private void unsuccessDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);


        ViewGroup viewGroup = findViewById(android.R.id.content);
        View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.order_unsucessful_layout, viewGroup, false);

        ImageView closeImg = view.findViewById(R.id.successDialogCLoseImg);

        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    @Override
    public void callback(int position, ArrayList<HomeChaiHelperclass> items) {

        homeChaiAdapter = new HomeChaiAdapter(items, this);
        homeChaiAdapter.notifyDataSetChanged();
        recyclerView2.setAdapter(homeChaiAdapter);
    }
}
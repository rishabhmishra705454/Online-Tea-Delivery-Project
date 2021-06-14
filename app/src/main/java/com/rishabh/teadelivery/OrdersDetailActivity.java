package com.rishabh.teadelivery;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rishabh.teadelivery.Adapters.OrderDetaiAdapter.OrderItemsAdapter;
import com.rishabh.teadelivery.Helperclass.OrderDetail.OrderItemsModel;

import java.util.ArrayList;

public class OrdersDetailActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    ArrayList<OrderItemsModel> orderItemsModels;

    OrderItemsAdapter orderItemsAdapter;
    RecyclerView itemRV;

    String uid;
    String key;

    TextView textAddress, textOrderDate, textOrderTime, textItemNo, textOrderCost, textOrderStatus , textHouseNo , textLandmark , textPhoneNo , orderStatus1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_detail);

        key = getIntent().getStringExtra("key");

        orderItemsModels = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        uid = user.getUid();

        itemRV = findViewById(R.id.itemRV);
        itemRV.setHasFixedSize(true);

        textAddress = findViewById(R.id.textOrderLocation);
        textOrderDate = findViewById(R.id.textOrderDate);
        textOrderTime = findViewById(R.id.textOrderTime);
        textItemNo = findViewById(R.id.orderChai);
        textOrderCost = findViewById(R.id.orderCost);
        textOrderStatus = findViewById(R.id.textOrderStatus);
        textHouseNo = findViewById(R.id.textHouseNo);
        textLandmark = findViewById(R.id.textLandmark);
        textPhoneNo=  findViewById(R.id.textPhoneNo);
        orderStatus1 = findViewById(R.id.orderStatus1);

        loadDetailData();

        loadRecyclerViewData();
    }

    private void loadDetailData() {

        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("orderDetails").child(uid).child(key);
        dR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String id = snapshot.child("id").getValue().toString();
                String phoneNo = snapshot.child("phoneNo").getValue().toString();
                String uid = snapshot.child("uid").getValue().toString();
                String address = snapshot.child("address").getValue().toString();
                String locality = snapshot.child("locality").getValue().toString();
                String pincode = snapshot.child("pincode").getValue().toString();
                String latitude = snapshot.child("latitude").getValue().toString();
                String longitude = snapshot.child("longitude").getValue().toString();
                String orderPrice = snapshot.child("orderPrice").getValue().toString();
                String orderQuantity = snapshot.child("orderQuantity").getValue().toString();
                String orderStatus = snapshot.child("orderStatus").getValue().toString();
                String orderDate = snapshot.child("orderDate").getValue().toString();
                String orderTime = snapshot.child("orderTime").getValue().toString();
                String houseFlatBlockNo = snapshot.child("houseFlatBlockNo").getValue().toString();
                String landmark = snapshot.child("landmark").getValue().toString();


                textAddress.setText(address);
                textOrderDate.setText(orderDate);
                textOrderTime.setText(orderTime);
                textItemNo.setText(orderQuantity);
                textOrderCost.setText(orderPrice + " Rs");
                textOrderStatus.setText(orderStatus);
                textHouseNo.setText(houseFlatBlockNo);
                textLandmark.setText(landmark);
                textPhoneNo.setText(phoneNo);
                orderStatus1.setText(orderStatus);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadRecyclerViewData() {

        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("orderItems").child(uid).child(key);
        dR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {


                    // textItems.setText(snapshot.getChildrenCount() + "");
                    OrderItemsModel orderItemsModel = postSnapshot.getValue(OrderItemsModel.class);
                    orderItemsModels.add(orderItemsModel);


                }

                itemRV.setLayoutManager(new LinearLayoutManager(OrdersDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));

                orderItemsAdapter = new OrderItemsAdapter(orderItemsModels, OrdersDetailActivity.this);

                itemRV.setAdapter(orderItemsAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
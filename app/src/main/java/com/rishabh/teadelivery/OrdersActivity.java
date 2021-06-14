package com.rishabh.teadelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rishabh.teadelivery.Adapters.OrderListAdapter;
import com.rishabh.teadelivery.Helperclass.OrderHelperclass;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {


    List<OrderHelperclass> orderHelperclassList;

    private FirebaseAuth firebaseAuth;

    ListView orderLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        orderHelperclassList = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        String uid = user.getUid();

        orderLV =  findViewById(R.id.orderList);

        FirebaseDatabase mdatabase = FirebaseDatabase.getInstance();
        DatabaseReference mReference = mdatabase.getReference("orderDetails").child(uid);

        Query query = mReference.orderByChild("orderStatus").equalTo("Ordered");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                orderHelperclassList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    OrderHelperclass postSnapshotValue = postSnapshot.getValue(OrderHelperclass.class);
                    orderHelperclassList.add(postSnapshotValue);
                }
                OrderListAdapter orderListAdapter = new OrderListAdapter(OrdersActivity.this, orderHelperclassList);
                orderLV.setAdapter(orderListAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
package com.rishabh.teadelivery.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.rishabh.teadelivery.Helperclass.OrderHelperclass;
import com.rishabh.teadelivery.HomeActivity;
import com.rishabh.teadelivery.OrdersDetailActivity;
import com.rishabh.teadelivery.R;

import java.util.List;

public class OrderListAdapter extends  ArrayAdapter<OrderHelperclass> {


    private Activity context ;
    List<OrderHelperclass> orderHelperclasses ;

    public OrderListAdapter(Activity context, List<OrderHelperclass> orderHelperclasses) {
        super(context, R.layout.order_list_layout, orderHelperclasses);
        this.context = context;
        this.orderHelperclasses = orderHelperclasses;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.order_list_layout , parent , false);

        }

        OrderHelperclass orderHelperclass = getItem(position);
        TextView textAddress = listItemView.findViewById(R.id.textLocality);
        TextView textOrderStatus = listItemView.findViewById(R.id.orderStatus);
        TextView textOrderChai = listItemView.findViewById(R.id.orderChai);
        TextView textOrderCost= listItemView.findViewById(R.id.orderCost);
        TextView textOrderDate= listItemView.findViewById(R.id.textOrderDate);
        TextView textOrderTime= listItemView.findViewById(R.id.textOrderTime);

        MaterialButton cancilBtn = listItemView.findViewById(R.id.orderCancil);
        MaterialButton trackBtn = listItemView.findViewById(R.id.orderTrack);
        MaterialButton callBtn = listItemView.findViewById(R.id.orderCall);


        textAddress.setText(orderHelperclass.getAddress());
        textOrderStatus.setText(orderHelperclass.getOrderStatus());
        textOrderChai.setText(orderHelperclass.getOrderQuantity());
       // textOrderCoffee.setText(orderHelperclass.getCoffeeQuantity() + " Coffee");
        textOrderCost.setText(orderHelperclass.getOrderPrice() + " Rs");

        textOrderDate.setText(orderHelperclass.getOrderDate());
        textOrderTime.setText(orderHelperclass.getOrderTime());

        if (orderHelperclass.getOrderStatus().equals("Canceled")){
            trackBtn.setEnabled(false);
            callBtn.setEnabled(false);
            cancilBtn.setEnabled(false);
        }

        cancilBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (orderHelperclass.getOrderStatus().equals("Canceled")) {



                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                            context);


                    alertDialog2.setMessage("Your order is already canceled");
                    alertDialog2.setTitle("Cancel Order");

                    alertDialog2.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });


                    alertDialog2.show();




                } else {

                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                            context);


                    alertDialog2.setMessage("Do you want to cancel your order");
                    alertDialog2.setTitle("Cancel Order");

                    alertDialog2.setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    DatabaseReference dR = FirebaseDatabase.getInstance().getReference("orderDetails").child(orderHelperclass.getUid()).child(orderHelperclass.getId());
                                    OrderHelperclass addData = new OrderHelperclass(orderHelperclass.getId(),orderHelperclass.getPhoneNo() , orderHelperclass.getUid() , orderHelperclass.getAddress() , orderHelperclass.getLocality() , orderHelperclass.getPincode() , orderHelperclass.getLatitude() , orderHelperclass.getLongitude(), orderHelperclass.getOrderPrice() ,orderHelperclass.getOrderQuantity() , "Canceled" ,orderHelperclass.getOrderDate() , orderHelperclass.getOrderTime() ,orderHelperclass.getHouseFlatBlockNo(),orderHelperclass.getLandmark());

                                    dR.setValue(addData);
                                    Toast.makeText(context, "Your order is successfully canceled", Toast.LENGTH_SHORT).show();
                                    trackBtn.setEnabled(false);
                                    callBtn.setEnabled(false);
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
            }
        });

        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on the item click on our list view.

                Intent intent = new Intent(context, OrdersDetailActivity.class );
                intent.putExtra("key", orderHelperclass.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
        return listItemView;
    }
}

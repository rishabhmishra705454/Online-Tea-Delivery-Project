package com.rishabh.teadelivery.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rishabh.teadelivery.Helperclass.CartHelperclass;
import com.rishabh.teadelivery.Helperclass.HomeChaiHelperclass;
import com.rishabh.teadelivery.Interface.UpdateRVInterface;
import com.rishabh.teadelivery.R;

import java.util.ArrayList;

public class HomeChaiAdapter extends RecyclerView.Adapter<HomeChaiAdapter.ViewHolder> {

    private ArrayList<HomeChaiHelperclass> chaiHelperclasses;
    Context context;
    private FirebaseAuth firebaseAuth;


    int totalQuantity;
    int totalPrices = 0;

    public HomeChaiAdapter(ArrayList<HomeChaiHelperclass> chaiHelperclasses, Context context) {
        this.chaiHelperclasses = chaiHelperclasses;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_recyclerview2_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        HomeChaiHelperclass homeChaiHelperclass = chaiHelperclasses.get(position);
        holder.chaiImage.setImageResource(homeChaiHelperclass.getChaiimage());
        holder.chaiTitle.setText(homeChaiHelperclass.getChaiTitle());
        holder.chaiPrice.setText(homeChaiHelperclass.getChaiPrice());


        holder.addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int quantity = Integer.parseInt(holder.noOfItem.getText().toString());

                if (quantity < 10) {

                    totalQuantity = quantity + 1 ;
                    holder.noOfItem.setText( Integer.toString(totalQuantity) );
                } else {
                    Toast.makeText(context, "You can order maximum 10", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.minusImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int quantity = Integer.parseInt(holder.noOfItem.getText().toString());
                if (quantity > 1) {

                    totalQuantity = quantity - 1;
                    //  totalPrice = totalPrice - (totalQuantity * Integer.parseInt(homeChaiHelperclass.getChaiPrice()));
                    // Toast.makeText(context, Integer.toString(totalPrice), Toast.LENGTH_SHORT).show();
                    holder.noOfItem.setText(Integer.toString(totalQuantity));
                } else {
                    Toast.makeText(context, "You can order maximum 10", Toast.LENGTH_SHORT).show();
                }
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        String uid = user.getUid();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("cart").child(uid).child(homeChaiHelperclass.getChaiTitle());


        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {

                holder.addButton.setText("ADDED");
                CartHelperclass cartHelperclass = new CartHelperclass(homeChaiHelperclass.getChaiTitle(), homeChaiHelperclass.getChaiPrice(), homeChaiHelperclass.getChaiQuantity() , Integer.toString(Integer.parseInt(homeChaiHelperclass.getChaiPrice())* Integer.parseInt(homeChaiHelperclass.getChaiQuantity())));

                myRef.setValue(cartHelperclass);

                Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show();


                //holder.minusImg.setVisibility(View.VISIBLE);
                // holder.noOfItem.setVisibility(View.VISIBLE);
                // holder.addImg.setVisibility(View.VISIBLE);

            }
        });

    }

    @Override
    public int getItemCount() {
        return chaiHelperclasses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView chaiImage, addImg, minusImg;
        TextView chaiTitle, chaiPrice, noOfItem;
        MaterialButton addButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            chaiImage = itemView.findViewById(R.id.chaiImage);
            chaiTitle = itemView.findViewById(R.id.chaiTitle);
            chaiPrice = itemView.findViewById(R.id.chaiPrice);
            addButton = itemView.findViewById(R.id.addButton);
            addImg = itemView.findViewById(R.id.addImg);
            minusImg = itemView.findViewById(R.id.minusImg);
            noOfItem = itemView.findViewById(R.id.noofforder);
        }
    }
}

package com.rishabh.teadelivery.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rishabh.teadelivery.Helperclass.CartHelperclass;
import com.rishabh.teadelivery.Helperclass.HomeChaiHelperclass;
import com.rishabh.teadelivery.HomeActivity;
import com.rishabh.teadelivery.R;

import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder> {

    private Activity context;
    List<CartHelperclass> cartHelperclasses;

    TextView noOfOrder;


    private FirebaseAuth firebaseAuth;


    int totalQuantity;

    public CartItemAdapter(@NonNull Activity context, List<CartHelperclass> cartHelperclasses) {
        this.context = context;
        this.cartHelperclasses = cartHelperclasses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item_layout , parent , false);
        CartItemAdapter.ViewHolder viewHolder = new CartItemAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CartHelperclass cartHelperclass = cartHelperclasses.get(position);
        holder.textTitle.setText(cartHelperclass.getChaiTitle());
        holder.textPrice.setText(cartHelperclass.getChaiPrice());
        holder.noOfOrder.setText(cartHelperclass.getChaiQuanty());

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        String uid = user.getUid();

        holder.addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int quantity = Integer.parseInt(holder.noOfOrder.getText().toString());


                if (quantity < 10) {

                    totalQuantity = quantity + 1;
                    holder.noOfOrder.setText(Integer.toString(totalQuantity));

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("cart").child(uid).child(cartHelperclass.getChaiTitle());

                    CartHelperclass cartHelperclass1 = new CartHelperclass(cartHelperclass.getChaiTitle() , cartHelperclass.getChaiPrice() , holder.noOfOrder.getText().toString() , Integer.toString(Integer.parseInt(cartHelperclass.getChaiPrice())* Integer.parseInt(holder.noOfOrder.getText().toString()))) ;

                    myRef.setValue(cartHelperclass1);

                } else {

                }
            }
        });

        holder.minusImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int quantity = Integer.parseInt(holder.noOfOrder.getText().toString());
                if (quantity > 1) {

                    totalQuantity = quantity - 1;
                    holder.noOfOrder.setText(Integer.toString(totalQuantity));

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("cart").child(uid).child(cartHelperclass.getChaiTitle());

                    CartHelperclass cartHelperclass1 = new CartHelperclass(cartHelperclass.getChaiTitle() , cartHelperclass.getChaiPrice() , holder.noOfOrder.getText().toString() , Integer.toString(Integer.parseInt(cartHelperclass.getChaiPrice())* Integer.parseInt(holder.noOfOrder.getText().toString()))) ;

                    myRef.setValue(cartHelperclass1);
                    

                } else {
                    Toast.makeText(context, "You can order maximum 10", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartItemAdapter cartItemAdapter = new CartItemAdapter(context, cartHelperclasses);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("cart").child(uid).child(cartHelperclass.getChaiTitle());

                myRef.removeValue();

                cartHelperclasses.remove(cartHelperclass.getTotalPrice());
                cartItemAdapter.notifyDataSetChanged();


            }
        });
    }

    @Override
    public int getItemCount() {
        return cartHelperclasses.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textTitle;
        TextView textPrice;
        ImageView deleteBtn;
        ImageView minusImg;
        ImageView addImg;
        TextView noOfOrder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.itemTitle);
            textPrice = itemView.findViewById(R.id.itemPrice);

            minusImg = itemView.findViewById(R.id.minusImg);
            addImg = itemView.findViewById(R.id.addImg);
            noOfOrder = itemView.findViewById(R.id.noofforder);
            deleteBtn = itemView.findViewById(R.id.btnDelete);
        }
    }
}

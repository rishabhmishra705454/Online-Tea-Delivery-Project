package com.rishabh.teadelivery.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rishabh.teadelivery.Helperclass.HomeChaiHelperclass;
import com.rishabh.teadelivery.Helperclass.StaticRV1Helperclass;
import com.rishabh.teadelivery.Interface.UpdateRVInterface;
import com.rishabh.teadelivery.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class StaticRV1Adapter  extends RecyclerView.Adapter<StaticRV1Adapter.StaticRV1ViewHolder>{

    private ArrayList<StaticRV1Helperclass> items ;

    UpdateRVInterface updateRVInterface ;

    Activity activity ;
    boolean check = true;
    boolean select = true;

    int row_index = -1;

    public StaticRV1Adapter(ArrayList<StaticRV1Helperclass> items  , Activity activity , UpdateRVInterface updateRVInterface) {
        this.items = items;
        this.activity = activity;
        this.updateRVInterface = updateRVInterface;
    }

    @NonNull
    @Override
    public StaticRV1ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_layout , parent , false);
        StaticRV1ViewHolder staticRV1ViewHolder = new StaticRV1ViewHolder(view);
        return  staticRV1ViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StaticRV1ViewHolder holder, int position) {

        StaticRV1Helperclass currentItem = items.get(position);
        holder.imageView.setImageResource(currentItem.getImage());
        holder.textView.setText(currentItem.getText());

        if (check){

            ArrayList<HomeChaiHelperclass> items  = new ArrayList<HomeChaiHelperclass>();
            items.add(new HomeChaiHelperclass(R.drawable.two_mugs_masala_tea, "Masala Chai", "15" ,"1"));
            items.add(new HomeChaiHelperclass(R.drawable.two_mugs_masala_tea, "Cutting Chai", "20", "1"));
            items.add(new HomeChaiHelperclass(R.drawable.two_mugs_masala_tea, "Irani Chai", "25" , "1"));

            updateRVInterface.callback(position , items);

            check = false;
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index = position;
                notifyDataSetChanged();

                if (position ==0){

                    ArrayList<HomeChaiHelperclass> items  = new ArrayList<HomeChaiHelperclass>();
                    items.add(new HomeChaiHelperclass(R.drawable.two_mugs_masala_tea, "Masala Chai", "15" ,"1"));
                    items.add(new HomeChaiHelperclass(R.drawable.two_mugs_masala_tea, "Cutting Chai", "20", "1"));
                    items.add(new HomeChaiHelperclass(R.drawable.two_mugs_masala_tea, "Irani Chai", "25" , "1"));

                    updateRVInterface.callback(position , items);


                }else if (position==1){

                    ArrayList<HomeChaiHelperclass> items  = new ArrayList<HomeChaiHelperclass>();
                    items.add(new HomeChaiHelperclass(R.drawable.two_mugs_masala_tea, "Masala Coffee", "15" ,"1"));
                    items.add(new HomeChaiHelperclass(R.drawable.two_mugs_masala_tea, "Cutting Coffee", "20", "1"));
                    items.add(new HomeChaiHelperclass(R.drawable.two_mugs_masala_tea, "Irani Coffee", "25" , "1"));

                    updateRVInterface.callback(position , items);

                }else if (position==2){

                    ArrayList<HomeChaiHelperclass> items  = new ArrayList<HomeChaiHelperclass>();
                    items.add(new HomeChaiHelperclass(R.drawable.two_mugs_masala_tea, "Masala Milk", "15" ,"1"));
                    items.add(new HomeChaiHelperclass(R.drawable.two_mugs_masala_tea, "Cutting Milk", "20", "1"));
                    items.add(new HomeChaiHelperclass(R.drawable.two_mugs_masala_tea, "Irani Milk", "25" , "1"));

                    updateRVInterface.callback(position , items);

                }
            }
        });

        if (select){
            if (position==0)
                holder.cardView.setCardElevation(30);
                holder.linearLayout.setBackgroundResource(R.drawable.active_border);
            select = false;
        }

        else {
            if (row_index == position){
                holder.cardView.setCardElevation(30);
                holder.linearLayout.setBackgroundResource(R.drawable.active_border);
            }else{
                holder.cardView.setCardElevation(0);
                holder.linearLayout.setBackgroundResource(R.drawable.card_background_border);
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class StaticRV1ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;
        CardView cardView;
        LinearLayout linearLayout;

        public StaticRV1ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageViewIcon);
            textView  = itemView.findViewById(R.id.textViewIcon);
            cardView = itemView.findViewById(R.id.itemCardLayout);

            linearLayout = itemView.findViewById(R.id.cardLinearLayout);

        }
    }
}

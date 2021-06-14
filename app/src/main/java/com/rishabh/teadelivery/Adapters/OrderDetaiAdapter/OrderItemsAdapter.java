package com.rishabh.teadelivery.Adapters.OrderDetaiAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rishabh.teadelivery.Adapters.HomeChaiAdapter;
import com.rishabh.teadelivery.Helperclass.OrderDetail.OrderItemsModel;
import com.rishabh.teadelivery.R;

import java.util.ArrayList;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.ViewHolder> {

    private ArrayList<OrderItemsModel> orderItemsModels ;
    Context context;

    public OrderItemsAdapter(ArrayList<OrderItemsModel> orderItemsModels, Context context) {
        this.orderItemsModels = orderItemsModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.order_item_detail_layout, parent, false);
        OrderItemsAdapter.ViewHolder viewHolder = new OrderItemsAdapter.ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        OrderItemsModel orderItemsModel = orderItemsModels.get(position);
        holder.textTitle.setText(orderItemsModel.getChaiTitle());
        holder.textQuantity.setText(orderItemsModel.getChaiQuanty() + " Cup");
        holder.textPrice.setText(orderItemsModel.getTotalPrice() + " Rs");

    }

    @Override
    public int getItemCount() {
        return orderItemsModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textTitle , textQuantity , textPrice ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.textTitle);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            textPrice = itemView.findViewById(R.id.textTotalPrice);
        }
    }
}

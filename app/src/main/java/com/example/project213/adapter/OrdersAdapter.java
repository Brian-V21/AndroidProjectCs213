package com.example.project213.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import model.Order;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {
    private List<Order> orders = new ArrayList<>();
    private final OnOrderClickListener listener;

    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    public OrdersAdapter(OnOrderClickListener listener) {
        this.listener = listener;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView text1;

        OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(android.R.id.text1);

            itemView.setOnClickListener(v -> {
                int pos = getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onOrderClick(orders.get(pos));
                }
            });
        }



        void bind(Order order) {
            text1.setText("Order #" + order.getOrderNumber()
                    + " - Total: " + String.format("$%.2f", order.getTotal()));
        }
    }
}

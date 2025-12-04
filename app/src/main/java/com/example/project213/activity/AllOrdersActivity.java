package com.example.project213.activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project213.adapter.ItemsAdapter;
import com.example.project213.adapter.OrdersAdapter;

import java.util.Collections;

import model.Order;
import model.OrdersManager;

/**
 * Show placed orders, allows cancellation and export.
 */
public class AllOrdersActivity extends AppCompatActivity implements OrdersActivity{

    private RecyclerView ordersRecyclerView;
    private RecyclerView itemsRecyclerView;
    private TextView totalLabel;

    private OrdersManager ordersManager;

    private OrdersAdapter ordersAdapter;
    private ItemsAdapter itemsAdapter;

    private Order selectedOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_orders); // Create this layout XML

        // Get singleton manager
        ordersManager = OrdersManager.getInstance();

        // Initialize views
        ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
        itemsRecyclerView = findViewById(R.id.itemsRecyclerView);
        totalLabel = findViewById(R.id.totalLabel);

        // Setup adapters
        ordersAdapter = new OrdersAdapter(order -> {
            selectedOrder = order;
            showOrderDetails(order);
        });
        itemsAdapter = new ItemsAdapter();

        // Setup RecyclerViews
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ordersRecyclerView.setAdapter(ordersAdapter);
        itemsRecyclerView.setAdapter(itemsAdapter);

        refreshOrders();
    }

    private void refreshOrders() {
        if (ordersManager != null) {
            ordersAdapter.setOrders(ordersManager.getPlacedOrders());
        }
        itemsAdapter.setItems(Collections.emptyList());
        totalLabel.setText("$0.00");
    }

    private void showOrderDetails(Order order) {
        if (order == null) {
            itemsAdapter.setItems(Collections.emptyList());
            totalLabel.setText("$0.00");
            return;
        }
        itemsAdapter.setItems(order.getItems());
        totalLabel.setText(String.format("$%.2f", order.getTotal()));
    }

    // Cancel the currently selected order
    private void cancelSelectedOrder() {
        if (selectedOrder == null || ordersManager == null) return;

        ordersManager.cancelPlacedOrder(selectedOrder);
        selectedOrder = null;
        refreshOrders();
    }

    // Show alert message
    private void show(String msg) {
        new AlertDialog.Builder(this)
                .setMessage(msg)
                .setPositiveButton("OK", null)
                .show();
    }
}

package com.example.project213.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import model.MenuItem;
import model.Order;
import model.OrdersManager;

public class CurrentOrderActivity extends AppCompatActivity implements OrdersActivity {

    private ListView itemsListView;
    private TextView subtotalLabel, taxLabel, totalLabel, orderNumberLabel;
    private Button removeItemBtn, clearBtn, placeOrderBtn;

    private OrdersManager ordersManager;
    private ArrayAdapter<MenuItem> itemsAdapter;

    private MenuItem selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order);

        // Initialize views
        itemsListView = findViewById(R.id.itemsListView);
        subtotalLabel = findViewById(R.id.subtotalLabel);
        taxLabel = findViewById(R.id.taxLabel);
        totalLabel = findViewById(R.id.totalLabel);
        orderNumberLabel = findViewById(R.id.orderNumberLabel);
        removeItemBtn = findViewById(R.id.removeItemButton);
        clearBtn = findViewById(R.id.clearButton);
        placeOrderBtn = findViewById(R.id.placeOrderButton);

        // Adapter for ListView
        itemsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, new ArrayList<>());
        itemsListView.setAdapter(itemsAdapter);

        // Select item
        itemsListView.setOnItemClickListener((parent, view, position, id) ->
                selectedItem = itemsAdapter.getItem(position));

        // Buttons
        removeItemBtn.setOnClickListener(v -> removeSelectedItem());
        clearBtn.setOnClickListener(v -> clearCurrentOrder());
        placeOrderBtn.setOnClickListener(v -> placeOrder());

        refresh();
    }

    @Override
    public void setOrdersManager(OrdersManager manager) {
        this.ordersManager = manager;
        refresh();
    }

    private void removeSelectedItem() {
        if (ordersManager == null || selectedItem == null) return;
        Order order = ordersManager.getCurrentOrder();
        if (order == null) return;

        order.removeItem(selectedItem);
        selectedItem = null;
        refresh();
    }

    private void clearCurrentOrder() {
        if (ordersManager == null) return;
        Order order = ordersManager.getCurrentOrder();
        if (order != null) order.clear();
        selectedItem = null;
        refresh();
    }

    private void placeOrder() {
        if (ordersManager == null) return;
        Order order = ordersManager.getCurrentOrder();
        if (order == null || order.getItems().isEmpty()) {
            show("No items in current order.");
            return;
        }

        ordersManager.placeCurrentOrder();
        show("Order placed!");
        refresh();
    }

    private void refresh() {
        if (ordersManager == null) return;
        Order order = ordersManager.getCurrentOrder();
        itemsAdapter.clear();
        if (order != null) itemsAdapter.addAll(order.getItems());

        subtotalLabel.setText(order != null ? String.format("$%.2f", order.getSubTotal()) : "$0.00");
        taxLabel.setText(order != null ? String.format("$%.2f", order.getTax()) : "$0.00");
        totalLabel.setText(order != null ? String.format("$%.2f", order.getTotal()) : "$0.00");
        orderNumberLabel.setText(order != null ? "Order #" + order.getOrderNumber() : "No current order");
    }

    private void show(String msg) {
        new AlertDialog.Builder(this)
                .setMessage(msg)
                .setPositiveButton("OK", null)
                .show();
    }
}

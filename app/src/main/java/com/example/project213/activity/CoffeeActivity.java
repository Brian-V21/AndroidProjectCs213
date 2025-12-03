package com.example.project213.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project213.adapter.CoffeeAddInsAdapter;
import com.example.project213.adapter.SizeSpinnerAdapter;

import java.util.EnumSet;

import enums.CoffeeAddIn;
import enums.CupSize;
import model.Coffee;
import model.MenuItem;
import model.Order;
import model.OrdersManager;

public class CoffeeActivity extends AppCompatActivity {

    private Spinner sizeSpinner; // dropdown for CupSize
    private RecyclerView addInsRecyclerView; // multi-select add-ins
    private TextView quantityText;
    private Button addButton;
    private TextView priceLabel;
    private ImageView coffeeImage;

    private OrdersManager ordersManager;

    private CoffeeAddInsAdapter addInsAdapter;

    private int quantity = 1; // default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee);

        // Initialize views
        sizeSpinner = findViewById(R.id.sizeSpinner);
        addInsRecyclerView = findViewById(R.id.addInsRecyclerView);
        quantityText = findViewById(R.id.quantityText);
        addButton = findViewById(R.id.addCoffeeButton);
        priceLabel = findViewById(R.id.priceLabel);
        coffeeImage = findViewById(R.id.coffeeImage);

        // Setup CupSize spinner
        CupSize[] sizes = CupSize.values();
        SizeSpinnerAdapter sizeAdapter = new SizeSpinnerAdapter(this, sizes);
        sizeSpinner.setAdapter(sizeAdapter);

        // Setup AddIns RecyclerView
        addInsAdapter = new CoffeeAddInsAdapter(CoffeeAddIn.values(), selected -> updatePrice());
        addInsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        addInsRecyclerView.setAdapter(addInsAdapter);

        // Setup quantity buttons
        findViewById(R.id.incrementQtyButton).setOnClickListener(v -> {
            quantity++;
            quantityText.setText(String.valueOf(quantity));
            updatePrice();
        });
        findViewById(R.id.decrementQtyButton).setOnClickListener(v -> {
            if (quantity > 1) quantity--;
            quantityText.setText(String.valueOf(quantity));
            updatePrice();
        });

        addButton.setOnClickListener(v -> addCoffeeToOrder());

        coffeeImage.setImageResource(R.drawable.coffee_large); // put coffee_large.jpg in res/drawable

        updatePrice();
    }

    public void setOrdersManager(OrdersManager manager) {
        this.ordersManager = manager;
        if (ordersManager.getCurrentOrder() == null) {
            ordersManager.startNewOrder();
        }
    }

    private void updatePrice() {
        CupSize size = (CupSize) sizeSpinner.getSelectedItem();
        if (size == null) return;

        EnumSet<CoffeeAddIn> addIns = EnumSet.noneOf(CoffeeAddIn.class);
        addIns.addAll(addInsAdapter.getSelectedAddIns());

        MenuItem item = new Coffee(size, addIns, quantity);
        priceLabel.setText(String.format("$%.2f", item.price()));
    }

    private void addCoffeeToOrder() {
        CupSize size = (CupSize) sizeSpinner.getSelectedItem();
        if (size == null || quantity <= 0) {
            show("Please choose a size and quantity.");
            return;
        }

        EnumSet<CoffeeAddIn> addIns = EnumSet.noneOf(CoffeeAddIn.class);
        addIns.addAll(addInsAdapter.getSelectedAddIns());

        Order order = ensureCurrentOrder();
        order.addItem(new Coffee(size, addIns, quantity));
        show("Coffee added to order #" + order.getOrderNumber());
    }

    private Order ensureCurrentOrder() {
        Order cur = ordersManager.getCurrentOrder();
        if (cur == null) cur = ordersManager.startNewOrder();
        return cur;
    }

    private void show(String msg) {
        new AlertDialog.Builder(this)
                .setMessage(msg)
                .setPositiveButton("OK", null)
                .show();
    }
}

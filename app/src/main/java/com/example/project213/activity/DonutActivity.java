package com.example.project213.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.EnumSet;

import enums.DonutFlavor;
import enums.DonutType;
import model.Donut;
import model.Order;
import model.OrdersManager;
import com.example.project213.adapter.DonutFlavorsAdapter;
import com.example.project213.R;

public class DonutActivity extends AppCompatActivity implements OrdersActivity {

    private Spinner typeSpinner;
    private RecyclerView flavorsRecyclerView;
    private TextView quantityText;
    private Button incrementButton;
    private Button decrementButton;
    private TextView priceLabel;
    private ImageView donutImage;
    private Button addButton;

    private DonutFlavorsAdapter flavorsAdapter;
    private int quantity = 1;

    private OrdersManager ordersManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donut);

        ordersManager = OrdersManager.getInstance(); // singleton

        // Initialize views
        typeSpinner = findViewById(R.id.typeSpinner);
        flavorsRecyclerView = findViewById(R.id.flavorsRecyclerView);
        quantityText = findViewById(R.id.quantityText);
        incrementButton = findViewById(R.id.incrementQtyButton);
        decrementButton = findViewById(R.id.decrementQtyButton);
        priceLabel = findViewById(R.id.priceLabel);
        donutImage = findViewById(R.id.donutImage);
        addButton = findViewById(R.id.addDonutButton);

        // Setup type spinner
        ArrayAdapter<DonutType> typeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, DonutType.values());
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        // Setup flavors RecyclerView
        flavorsAdapter = new DonutFlavorsAdapter(DonutFlavor.values(), selected -> updatePrice());
        flavorsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        flavorsRecyclerView.setAdapter(flavorsAdapter);

        // Quantity buttons
        quantityText.setText(String.valueOf(quantity));
        incrementButton.setOnClickListener(v -> {
            quantity++;
            quantityText.setText(String.valueOf(quantity));
            updatePrice();
        });
        decrementButton.setOnClickListener(v -> {
            if (quantity > 1) quantity--;
            quantityText.setText(String.valueOf(quantity));
            updatePrice();
        });

        // Type spinner listener to update image
        typeSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                DonutType type = (DonutType) typeSpinner.getSelectedItem();
                switch (type) {
                    case YEAST:
                        donutImage.setImageResource(R.drawable.yeast);
                        break;
                    case CAKE:
                        donutImage.setImageResource(R.drawable.cakedonut);
                        break;
                    case HOLE:
                        donutImage.setImageResource(R.drawable.holedonut);
                        break;
                    case SEASONAL:
                        donutImage.setImageResource(R.drawable.seasonal);
                        break;
                }
                updatePrice();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        // Add button
        addButton.setOnClickListener(v -> addDonutToOrder());

        updatePrice(); // initial price
    }

    @Override
    public void setOrdersManager(OrdersManager manager) {
        this.ordersManager = OrdersManager.getInstance(); // ensure singleton
        if (ordersManager.getCurrentOrder() == null) {
            ordersManager.startNewOrder();
        }
    }

    private void updatePrice() {
        DonutType type = (DonutType) typeSpinner.getSelectedItem();
        EnumSet<DonutFlavor> selectedFlavors = EnumSet.noneOf(DonutFlavor.class);
        selectedFlavors.addAll(flavorsAdapter.getSelectedFlavors());

        if (type == null || selectedFlavors.isEmpty()) {
            priceLabel.setText("$0.00");
            return;
        }

        Donut d = new Donut(type, selectedFlavors, quantity);
        priceLabel.setText(String.format("$%.2f", d.price()));
    }

    private void addDonutToOrder() {
        DonutType type = (DonutType) typeSpinner.getSelectedItem();
        EnumSet<DonutFlavor> selectedFlavors = EnumSet.noneOf(DonutFlavor.class);
        selectedFlavors.addAll(flavorsAdapter.getSelectedFlavors());

        if (type == null || selectedFlavors.isEmpty() || quantity <= 0) {
            showAlert("Please select donut type, flavors, and quantity.");
            return;
        }

        Order order = ensureCurrentOrder();
        order.addItem(new Donut(type, selectedFlavors, quantity));
        showAlert("Donut added to order #" + order.getOrderNumber());
    }

    private Order ensureCurrentOrder() {
        Order cur = ordersManager.getCurrentOrder();
        if (cur == null) cur = ordersManager.startNewOrder();
        return cur;
    }

    private void showAlert(String msg) {
        new AlertDialog.Builder(this)
                .setMessage(msg)
                .setPositiveButton("OK", null)
                .show();
    }
}

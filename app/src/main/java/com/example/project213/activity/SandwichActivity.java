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

import enums.Bread;
import enums.Protein;
import enums.SandwichAddOn;
import model.Order;
import model.OrdersManager;
import model.Sandwich;
import com.example.project213.adapter.SandwichAddOnsAdapter;

public class SandwichActivity extends AppCompatActivity {

    private Spinner breadSpinner, proteinSpinner;
    private RecyclerView addOnsRecyclerView;
    private TextView quantityText, priceLabel;
    private Button incrementButton, decrementButton, addButton;
    private ImageView sandwichImage;

    // Setup AddOns RecyclerView
    private SandwichAddOnsAdapter addOnsAdapter = new SandwichAddOnsAdapter(SandwichAddOn.values(), selected -> updatePrice());
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sandwich);

        // Initialize views
        breadSpinner = findViewById(R.id.breadSpinner);
        proteinSpinner = findViewById(R.id.proteinSpinner);
        addOnsRecyclerView = findViewById(R.id.addOnsRecyclerView);
        quantityText = findViewById(R.id.quantityText);
        incrementButton = findViewById(R.id.incrementQtyButton);
        decrementButton = findViewById(R.id.decrementQtyButton);
        priceLabel = findViewById(R.id.priceLabel);
        sandwichImage = findViewById(R.id.sandwichImage);
        addButton = findViewById(R.id.addSandwichButton);

        // Setup Bread spinner
        ArrayAdapter<Bread> breadAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Bread.values());
        breadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        breadSpinner.setAdapter(breadAdapter);
        breadSpinner.setSelection(0);

        // Setup Protein spinner
        ArrayAdapter<Protein> proteinAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Protein.values());
        proteinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        proteinSpinner.setAdapter(proteinAdapter);
        proteinSpinner.setSelection(0);

        addOnsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        addOnsRecyclerView.setAdapter(addOnsAdapter);

        // Setup increment/decrement buttons
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

        // Add sandwich button
        addButton.setOnClickListener(v -> addSandwichToOrder());

        // Default sandwich image
        sandwichImage.setImageResource(R.drawable.sandwich);

        // Initial price calculation
        updatePrice();
    }

    private void updatePrice() {
        Bread bread = (Bread) breadSpinner.getSelectedItem();
        Protein protein = (Protein) proteinSpinner.getSelectedItem();
        EnumSet<SandwichAddOn> addOns = EnumSet.noneOf(SandwichAddOn.class);
        addOns.addAll(addOnsAdapter.getSelectedAddOns());

        if (bread == null || protein == null) {
            priceLabel.setText("$0.00");
            return;
        }

        Sandwich s = new Sandwich(bread, protein, addOns, quantity);
        priceLabel.setText(String.format("$%.2f", s.price()));
    }

    private void addSandwichToOrder() {
        Bread bread = (Bread) breadSpinner.getSelectedItem();
        Protein protein = (Protein) proteinSpinner.getSelectedItem();
        EnumSet<SandwichAddOn> addOns = EnumSet.noneOf(SandwichAddOn.class);
        addOns.addAll(addOnsAdapter.getSelectedAddOns());

        if (bread == null || protein == null || quantity <= 0) {
            showAlert("Please select bread, protein, add-ons, and quantity.");
            return;
        }

        OrdersManager manager = OrdersManager.getInstance();
        Order order = manager.getCurrentOrder();
        if (order == null) order = manager.startNewOrder();

        order.addItem(new Sandwich(bread, protein, addOns, quantity));
        showAlert("Sandwich added to order #" + order.getOrderNumber());
    }

    private void showAlert(String msg) {
        new AlertDialog.Builder(this)
                .setMessage(msg)
                .setPositiveButton("OK", null)
                .show();
    }
}

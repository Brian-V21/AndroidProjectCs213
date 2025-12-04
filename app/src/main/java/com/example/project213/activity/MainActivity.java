package com.example.project213.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import model.OrdersManager;
import com.example.project213.R;

public class MainActivity extends AppCompatActivity {

    private Button donutsBtn, coffeeBtn, sandwichBtn, currentOrderBtn, allOrdersBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // create this layout XML

        // Initialize buttons
        donutsBtn = findViewById(R.id.donutsBtn);
        coffeeBtn = findViewById(R.id.coffeeBtn);
        sandwichBtn = findViewById(R.id.sandwichBtn);
        currentOrderBtn = findViewById(R.id.currentOrderBtn);
        allOrdersBtn = findViewById(R.id.allOrdersBtn);

        // Set click listeners
        donutsBtn.setOnClickListener(v -> openActivity(DonutActivity.class));
        coffeeBtn.setOnClickListener(v -> openActivity(CoffeeActivity.class));
        sandwichBtn.setOnClickListener(v -> openActivity(SandwichActivity.class));
        currentOrderBtn.setOnClickListener(v -> openActivity(CurrentOrderActivity.class));
        allOrdersBtn.setOnClickListener(v -> openActivity(AllOrdersActivity.class));
    }

    private void openActivity(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }
    public OrdersManager getOrdersManager() {
        return OrdersManager.getInstance();
    }
}

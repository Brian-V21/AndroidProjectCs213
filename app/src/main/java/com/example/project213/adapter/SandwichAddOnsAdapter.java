package com.example.project213.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import enums.SandwichAddOn;

public class SandwichAddOnsAdapter extends RecyclerView.Adapter<SandwichAddOnsAdapter.ViewHolder> {

    private final List<SandwichAddOn> addOns;
    private final List<SandwichAddOn> selectedAddOns = new ArrayList<>();
    private final OnSelectionChangedListener listener;

    public SandwichAddOnsAdapter(SandwichAddOn[] addOns, OnSelectionChangedListener listener) {
        this.addOns = Arrays.asList(addOns);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.item_addin, parent, false); // your checkbox layout
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SandwichAddOn addOn = addOns.get(position);
        holder.checkBox.setText(addOn.name());
        holder.checkBox.setChecked(selectedAddOns.contains(addOn));

        holder.checkBox.setOnClickListener(v -> {
            if (holder.checkBox.isChecked()) {
                if (!selectedAddOns.contains(addOn)) selectedAddOns.add(addOn);
            } else {
                selectedAddOns.remove(addOn);
            }
            if (listener != null) listener.onSelectionChanged();
        });
    }

    @Override
    public int getItemCount() {
        return addOns.size();
    }

    public List<SandwichAddOn> getSelectedAddOns() {
        return selectedAddOns;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.item_addon_checkbox);
        }
    }

    public interface OnSelectionChangedListener {
        void onSelectionChanged();
    }
}

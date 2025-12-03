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

import enums.CoffeeAddIn;

/**
 * RecyclerView Adapter for showing Coffee Add-Ins with multi-selection.
 */
public class CoffeeAddInsAdapter extends RecyclerView.Adapter<CoffeeAddInsAdapter.ViewHolder> {

    private final List<CoffeeAddIn> addIns;
    private final List<CoffeeAddIn> selectedAddIns = new ArrayList<>();
    private final OnAddInSelectedListener listener;

    public CoffeeAddInsAdapter(CoffeeAddIn[] addIns, OnAddInSelectedListener listener) {
        this.addIns = Arrays.asList(addIns);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_multiple_choice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CoffeeAddIn addIn = addIns.get(position);
        holder.checkBox.setText(addIn.name());
        holder.checkBox.setChecked(selectedAddIns.contains(addIn));

        holder.checkBox.setOnClickListener(v -> {
            if (holder.checkBox.isChecked()) {
                if (!selectedAddIns.contains(addIn)) selectedAddIns.add(addIn);
            } else {
                selectedAddIns.remove(addIn);
            }
            if (listener != null) listener.onSelectionChanged(addIn);
        });
    }

    @Override
    public int getItemCount() {
        return addIns.size();
    }

    public List<CoffeeAddIn> getSelectedAddIns() {
        return selectedAddIns;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(android.R.id.text1);
        }
    }

    /**
     * Listener interface for selection changes
     */
    public interface OnAddInSelectedListener {
        void onSelectionChanged(CoffeeAddIn addIn);
    }
}

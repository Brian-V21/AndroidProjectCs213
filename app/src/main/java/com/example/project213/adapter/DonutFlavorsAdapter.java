package com.example.project213.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import enums.DonutFlavor;
import com.example.project213.R;

/**
 * RecyclerView Adapter for selecting multiple Donut Flavors
 */
public class DonutFlavorsAdapter extends RecyclerView.Adapter<DonutFlavorsAdapter.ViewHolder> {

    private final List<DonutFlavor> flavors;
    private final List<DonutFlavor> selectedFlavors = new ArrayList<>();
    private final OnFlavorSelectedListener listener;

    public DonutFlavorsAdapter(DonutFlavor[] flavors, OnFlavorSelectedListener listener) {
        this.flavors = Arrays.asList(flavors);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Use a simple checkbox layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_donut_flavor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DonutFlavor flavor = flavors.get(position);
        holder.checkBox.setText(flavor.name());
        holder.checkBox.setChecked(selectedFlavors.contains(flavor));

        int resId;
        switch (flavor) {
            case CHOCOLATE:
                resId = R.drawable.flavor_chocolate;
                break;
            case GLAZED:
                resId = R.drawable.flavor_glazed;
                break;
            default: resId = R.drawable.donuts;
        }
        holder.flavorImage.setImageResource(resId);

        holder.checkBox.setOnClickListener(v -> {
            if (holder.checkBox.isChecked()) {
                if (!selectedFlavors.contains(flavor)) selectedFlavors.add(flavor);
            } else {
                selectedFlavors.remove(flavor);
            }
            if (listener != null) listener.onSelectionChanged(flavor);
        });
    }

    @Override
    public int getItemCount() {
        return flavors.size();
    }

    public List<DonutFlavor> getSelectedFlavors() {
        return selectedFlavors;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView flavorImage;
        CheckBox checkBox;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            flavorImage = itemView.findViewById(R.id.flavor_image);
            checkBox = itemView.findViewById(R.id.item_flavor_checkbox);
        }
    }

    /**
     * Listener interface for selection changes
     */
    public interface OnFlavorSelectedListener {
        void onSelectionChanged(DonutFlavor flavor);
    }
}

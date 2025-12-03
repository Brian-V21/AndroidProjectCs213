package com.example.project213.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import enums.CupSize;

public class SizeSpinnerAdapter extends ArrayAdapter<CupSize> {

    public SizeSpinnerAdapter(@NonNull Context context, CupSize[] sizes) {
        super(context, android.R.layout.simple_spinner_item, sizes);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public @NonNull String toString() {
        return super.toString();
    }
}

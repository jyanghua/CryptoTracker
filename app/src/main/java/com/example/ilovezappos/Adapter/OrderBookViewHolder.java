package com.example.ilovezappos.Adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ilovezappos.R;

public class OrderBookViewHolder extends RecyclerView.ViewHolder {

    TextView txtValue, txtAmount, txtPrice;

    public OrderBookViewHolder(@NonNull View itemView) {
        super(itemView);

        txtAmount = itemView.findViewById(R.id.amount);
        txtValue = itemView.findViewById(R.id.value);
        txtPrice = itemView.findViewById(R.id.price);
    }
}

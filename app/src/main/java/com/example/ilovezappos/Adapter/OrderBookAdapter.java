package com.example.ilovezappos.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ilovezappos.R;

import java.util.List;

public class OrderBookAdapter extends RecyclerView.Adapter<OrderBookViewHolder>{

    private Context context;
    private List<List<String>> orderBookList;
    private String type;

    /**
     * Default constructor
     * @param context access to context of the app
     * @param orderBookList a list containing lists of pairing [price, amount]
     * @param type the type of Order Book can be "Bids" or "Asks"
     */
    public OrderBookAdapter(Context context, List<List<String>> orderBookList, String type) {
        this.context = context;
        this.orderBookList = orderBookList;
        this.type = type;
    }

    @NonNull
    @Override
    public OrderBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderBookViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.book_order_layout, parent, false));
    }

    /**
     * Function that sets up all the correct values of the order book to the view holders of
     * the recycler view.
     * @param holder the view holder
     * @param position the position of the elements of the view holder
     */
    @Override
    public void onBindViewHolder(@NonNull OrderBookViewHolder holder, int position) {
        String price = String.valueOf(orderBookList.get(position).get(0));
        String amount = String.valueOf(orderBookList.get(position).get(1));
        String value = String.format("%.2f", Double.parseDouble(price) * Double.parseDouble(amount));
        holder.txtPrice.setText(price);
        holder.txtAmount.setText(amount);
        holder.txtValue.setText(value);

        // Changes the color of the view holder depending if it's a bid or an ask
        if(type.equals("bids")){
            holder.txtPrice.setTextColor(Color.rgb(0, 125, 0));
        } else if(type.equals("asks")){
            holder.txtPrice.setTextColor(Color.rgb(238, 0, 0));
        }
    }

    /**
     * Function to get the number of items in the list
     * @return number of items in the order book list
     */
    @Override
    public int getItemCount() {
        return orderBookList.size();
    }
}

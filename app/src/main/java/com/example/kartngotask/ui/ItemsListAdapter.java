package com.example.kartngotask.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kartngotask.R;
import com.example.kartngotask.model.Item;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ItemsListAdapter extends RecyclerView.Adapter<ItemsListAdapter.ItemViewHolder> {

    final List<Item> items = new ArrayList<>();

    // Listener interface for item interactions
    public interface OnItemInteractionListener {
        void onItemCardClicked(Item item);
        void onItemQuantityChanged(Item item, int newQuantity);
    }

    private OnItemInteractionListener listener;

    public void setOnItemInteractionListener(OnItemInteractionListener listener) {
        this.listener = listener;
    }

    public void setItems(List<Item> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final CircleImageView imageItem;
        private final CircleImageView imageStore;
        private final TextView textTitle;
        private final TextView textPrice;
        private final TextView textQuantity;
        private final ImageButton buttonMinus;
        private final ImageButton buttonPlus;

        private int quantity = 0;
        private Item currentItem;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageItem = itemView.findViewById(R.id.imageView_item);
            imageStore = itemView.findViewById(R.id.imageView_store);
            textTitle = itemView.findViewById(R.id.text_title);
            textPrice = itemView.findViewById(R.id.text_price);
            textQuantity = itemView.findViewById(R.id.text_quantity);
            buttonMinus = itemView.findViewById(R.id.button_minus);
            buttonPlus = itemView.findViewById(R.id.button_plus);

            // Card click listener
            itemView.setOnClickListener(v -> {
                if (listener != null && currentItem != null) {
                    listener.onItemCardClicked(currentItem);
                }
            });

            buttonMinus.setOnClickListener(v -> {
                if (quantity > 0) {
                    quantity--;
                    textQuantity.setText(String.valueOf(quantity));
                    if (listener != null && currentItem != null) {
                        listener.onItemQuantityChanged(currentItem, quantity);
                    }
                }
            });

            buttonPlus.setOnClickListener(v -> {
                quantity++;
                textQuantity.setText(String.valueOf(quantity));
                if (listener != null && currentItem != null) {
                    listener.onItemQuantityChanged(currentItem, quantity);
                }
            });
        }

        public void bind(Item item) {
            currentItem = item;
            textTitle.setText(item.getTitle());
            textPrice.setText(String.format("%.2f SAR", item.getPrice()));

            Glide.with(imageItem.getContext())
                    .load(item.getImageUri())
                    .placeholder(R.drawable.burger_king)
                    .into(imageItem);

            imageStore.setImageResource(R.drawable.burger_king);

            quantity = 0;
            textQuantity.setText(String.valueOf(quantity));
        }
    }
}
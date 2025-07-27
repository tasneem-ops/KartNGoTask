package com.example.kartngotask.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kartngotask.MainActivity;
import com.example.kartngotask.R;
import com.example.kartngotask.model.Item;
import com.example.kartngotask.viewmodel.ItemsListViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ItemsListFragment extends Fragment {

    private ItemsListAdapter adapter;
    private TextView textTotalPrice;

    // Map to keep track of quantities per item
    private final Map<String, Integer> itemQuantities = new HashMap<>();

    public ItemsListFragment() {
        super(R.layout.fragment_items_list);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_items_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_items);
        textTotalPrice = view.findViewById(R.id.tv_total_price); // Add this TextView in your layout for total price display

        adapter = new ItemsListAdapter();
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemInteractionListener(new ItemsListAdapter.OnItemInteractionListener() {
            @Override
            public void onItemCardClicked(Item item) {
                Toast.makeText(requireContext(), "Clicked: " + item.getTitle(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemQuantityChanged(Item item, int newQuantity) {
                itemQuantities.put(item.getTitle(), newQuantity);
                updateTotalPrice();
            }
        });

        ItemsListViewModel viewModel = new ViewModelProvider(this).get(ItemsListViewModel.class);
        viewModel.getItemsLiveData().observe(getViewLifecycleOwner(), items -> {
            adapter.setItems(items);
            // Reset quantities when new data arrives
            itemQuantities.clear();
            updateTotalPrice();
        });
        viewModel.syncItemsFromFirestore();
        ChipGroup chipGroupFilters = view.findViewById(R.id.chip_group);

        chipGroupFilters.setOnCheckedChangeListener((group, checkedId) -> {
            // Iterate through all chips to update text color based on selection
            for (int i = 0; i < group.getChildCount(); i++) {
                Chip chip = (Chip) group.getChildAt(i);
                if (chip.getId() == checkedId) {
                    chip.setTextColor(getResources().getColor(R.color.primary_blue));
                    chip.setChipBackgroundColorResource(R.color.chip_selected_background);
                } else {
                    chip.setTextColor(getResources().getColor(R.color.chip_unselected_text));
                    chip.setChipBackgroundColorResource(R.color.chip_unselected_background);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).showAppBar(true);
    }

    private void updateTotalPrice() {
        double total = 0.0;
        for (Map.Entry<String, Integer> entry : itemQuantities.entrySet()) {
            String title = entry.getKey();
            int quantity = entry.getValue();
            // Find item price from adapter's list
            Item item = findItemByTitle(title);
            if (item != null) {
                total += item.getPrice() * quantity;
            }
        }
        if (textTotalPrice != null) {
            textTotalPrice.setText(String.format("%.2f SAR", total));
        }
    }

    private Item findItemByTitle(String title) {
        List<Item> items = adapter != null ? adapter.items : null;
        if (items != null) {
            for (Item item : items) {
                if (item.getTitle().equals(title)) {
                    return item;
                }
            }
        }
        return null;
    }
}
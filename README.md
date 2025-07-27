# [KartNGo] - Android Demo

This is a basic demonstration Android application showcasing e-commerce functionalities. It's built using Java, XML for UI layouts, and utilizes Firebase Firestore as its backend database for product listings, user data, and potentially order management.

## Table of Contents

* [Features](#features)
* [Tech Stack](#tech-stack)
* [Prerequisites](#prerequisites)
* [Screenshots](#screenshots)
* [AI prompts](#ai-prompts)

## Features

*   Browse a list of products.
*   View product details (name, price, image).
*   Add products to a shopping cart


## Tech Stack

*   **Programming Language:** Java
*   **UI Layouts:** XML
*   **Database:** Firebase Firestore
*   **Image Loading :** Glide
*   **Android SDK Version:** Min SDK 24, Target SDK 36

## Prerequisites

*   Android Studio (latest stable version recommended)
*   An Android device or emulator running Android [Your Min SDK Version] or higher.
*   A Firebase project.

## Screenshots
![eats](https://github.com/user-attachments/assets/0bd10c95-6a62-4a9b-a236-2b885a062024)
![Untitled(1)](https://github.com/user-attachments/assets/f521b692-3882-4da9-84a5-f0c62419e17d)

## AI prompts
* Prompt: createviewmodel for this fragment, it should have a function to sync items from firestore and livedata to observe on data when loaded
* Response:

public class ItemsListViewModel extends ViewModel {

    private final MutableLiveData<List<Item>> itemsLiveData = new MutableLiveData<>();
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public LiveData<List<Item>> getItemsLiveData() {
        return itemsLiveData;
    }

    /**
     * Starts syncing items from Firestore collection "items".
     * Updates LiveData whenever data changes.
     */
    public void syncItemsFromFirestore() {
        firestore.collection("items")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            // Handle error, optionally post empty list or null
                            itemsLiveData.postValue(new ArrayList<>());
                            return;
                        }
                        if (value != null && !value.isEmpty()) {
                            List<Item> itemList = new ArrayList<>();
                            value.getDocuments().forEach(documentSnapshot -> {
                                Item item = documentSnapshot.toObject(Item.class);
                                if (item != null) {
                                    itemList.add(item);
                                }
                            });
                            itemsLiveData.postValue(itemList);
                        } else {
                            itemsLiveData.postValue(new ArrayList<>());
                        }
                    }
                });
    }
}

* Prompt: create list adapter for recycler view which has item of this layout item.xml and initialize it in itemslistfragment
* Response:

public class ItemsListAdapter extends RecyclerView.Adapter<ItemsListAdapter.ItemViewHolder> {

    private final List<Item> items = new ArrayList<>();

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

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private final CircleImageView imageItem;
        private final CircleImageView imageStore;
        private final TextView textTitle;
        private final TextView textPrice;
        private final TextView textQuantity;
        private final ImageButton buttonMinus;
        private final ImageButton buttonPlus;

        private int quantity = 0;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageItem = itemView.findViewById(R.id.imageView_item);
            imageStore = itemView.findViewById(R.id.imageView_store);
            textTitle = itemView.findViewById(R.id.text_title);
            textPrice = itemView.findViewById(R.id.text_price);
            textQuantity = itemView.findViewById(R.id.text_quantity);
            buttonMinus = itemView.findViewById(R.id.button_minus);
            buttonPlus = itemView.findViewById(R.id.button_plus);
        }

        public void bind(Item item) {
            textTitle.setText(item.getTitle());
            textPrice.setText(String.format("%.2f SAR", item.getPrice()));

            // Load item image using Glide or any image loading library
            Glide.with(imageItem.getContext())
                    .load(item.getImageUri())
                    .placeholder(R.drawable.burger_king) // fallback placeholder
                    .into(imageItem);

            // For store image, using Burger King logo as in your layout
            imageStore.setImageResource(R.drawable.burger_king);

            // Reset quantity to 0 on bind
            quantity = 0;
            textQuantity.setText(String.valueOf(quantity));

            buttonMinus.setOnClickListener(v -> {
                if (quantity > 0) {
                    quantity--;
                    textQuantity.setText(String.valueOf(quantity));
                }
            });

            buttonPlus.setOnClickListener(v -> {
                quantity++;
                textQuantity.setText(String.valueOf(quantity));
            });
        }
    }
}




    

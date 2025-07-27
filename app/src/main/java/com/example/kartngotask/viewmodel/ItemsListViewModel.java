package com.example.kartngotask.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.kartngotask.model.Item;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

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
                    public void onEvent(QuerySnapshot value,  FirebaseFirestoreException error) {
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
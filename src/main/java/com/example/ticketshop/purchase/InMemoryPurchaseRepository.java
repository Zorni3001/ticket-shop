package com.example.ticketshop.purchase;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryPurchaseRepository implements PurchaseRepository {

    private final Map<String, Purchase> purchases = new ConcurrentHashMap<>();

    @Override
    public void save(Purchase purchase) {
        purchases.put(purchase.purchaseId(), purchase);
    }

    @Override
    public Optional<Purchase> findById(String purchaseId) {
        return Optional.ofNullable(purchases.get(purchaseId));
    }
}

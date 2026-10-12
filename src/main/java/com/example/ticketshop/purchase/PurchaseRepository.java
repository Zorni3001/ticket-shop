package com.example.ticketshop.purchase;

import java.util.Optional;

public interface PurchaseRepository {

    void save(Purchase purchase);

    Optional<Purchase> findById(String purchaseId);
}

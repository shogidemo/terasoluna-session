package com.example.session.app.cart;

import java.util.Set;

import javax.validation.constraints.NotEmpty;

public class CartForm {

    @NotEmpty
    private Set<String> removedItemsIds;

    public Set<String> getRemovedItemsIds() {
        return removedItemsIds;
    }

    public void setRemovedItemsIds(Set<String> removedItemsIds) {
        this.removedItemsIds = removedItemsIds;
    }
}
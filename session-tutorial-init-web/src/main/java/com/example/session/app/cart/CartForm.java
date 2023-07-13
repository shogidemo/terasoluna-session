package com.example.session.app.cart;

import java.util.Set;

import javax.validation.constraints.NotEmpty;

public class CartForm {

    @NotEmpty
    private Set<String> removedItemsIds;

    public Set<String> getRemovedItemsIds() {
        return removedItemsIds;
    }

    @SuppressWarnings("unused") // 未使用警告が表示されるが、フレームワーク経由で呼ばれるので実際は未使用ではない。警告回避のためにアノテーションをつける。
    public void setRemovedItemsIds(Set<String> removedItemsIds) {
        this.removedItemsIds = removedItemsIds;
    }
}
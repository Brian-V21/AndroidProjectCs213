package com.example.project213.activity;

import model.OrdersManager;

/**
 * Controllers that need access to the shared OrdersManager implement this.
 */
public interface OrdersActivity {
    /**
     * @param manager orders manager
     */
    void setOrdersManager(OrdersManager manager);
}

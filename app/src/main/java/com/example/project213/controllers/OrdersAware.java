package org.example.project4cs213.controller;

import model.OrdersManager;

/**
 * Controllers that need access to the shared OrdersManager implement this.
 */
public interface OrdersAware {
    /**
     * @param manager orders manager
     */
    void setOrdersManager(OrdersManager manager);
}

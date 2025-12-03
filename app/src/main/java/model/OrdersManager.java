package model;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class OrdersManager {
    /**
     * Singleton instance
     */
    private static OrdersManager instance;
    
    private int nextOrder = 10001;
    private final List<Order> placed = new ArrayList<>();
    private Order current = new Order(nextOrder++);


    /**
     * Private Constructor
     */
    private OrdersManager() {}

    public static synchronized OrdersManager getInstance() {
        if (instance == null) {
            instance = new OrdersManager();
        }
        return instance;
    }

    /**
     * To get the current order being used
     * @return current order
     */
    public Order getCurrentOrder() {
        return current;
    }

    /**
     * To get the list of ordered items
     * @return placed orders
     */
    public List<Order> getPlacedOrders() {
        return placed;
    }

    /**
     * To start a new order from the beginning
     * @return current as it is a new order
     */
    public Order startNewOrder() {
        current = new Order(nextOrder++);
        return current;
    }

    /**
     *Insert current order into placed list
     */
    public void placeCurrentOrder() {
        if (current.getItems().isEmpty())
            return;
        placed.add(current);
        startNewOrder();
    }

    /**
     * To cancel the desired placed order
     * @param o order object being removed
     */
    public void cancelPlacedOrder(Order o) {
        placed.remove(o);
    }
}

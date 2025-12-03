package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.PrintWriter;

public class OrdersManager {
    private int nextOrder = 10001;
    private final ObservableList<Order> placed = FXCollections.observableArrayList();
    private Order current = new Order(nextOrder++);

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
    public ObservableList<Order> getPlacedOrders() {
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

    /**
     * Export orders to a text file
     * @param file the file that will hold the orders
     * @throws Exception throw exception to user if not correct file
     */
    public void exportOrders(File file) throws Exception {
        try (PrintWriter pw = new PrintWriter(file, "UTF-8")) {
            for (Order o : placed) {
                pw.println(o.recieptText());
                pw.println("----");
            }
        }
    }
}

package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Order {
    private final int orderNumber;
    private final List<MenuItem> items = new ArrayList<>();

    /**
     * Constructor for Order object
     * @param orderNumber the number of order objects
     */
    public Order(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * Get the number of ordered objects
     * @return orderNumber of Order constructor
     */
    public int getOrderNumber() {
        return orderNumber;
    }

    /**
     * get items in the List of menu items
     * @return items in List<MenuItem>
     */
    public List<MenuItem> getItems() {
        return items;
    }

    /**
     * Add item into item list
     * @param mi Menu Item being added into item list
     */
    public void addItem(MenuItem mi) {
        items.add(mi);
    }

    /**
     * Remove an item inside item list
     * @param mi MenuItem being removed from the list
     */
    public void removeItem(MenuItem mi) {
        items.remove(mi);
    }

    /**
     * Clear the list in items
     */
    public void clear() {
        items.clear();
    }

    /**
     * Calculate the subtotal of items inside the list
     * @return cost of the items inside the list before tax
     */
    public double subtotal() {
        return items.stream().mapToDouble(MenuItem::price).sum();
    }

    /**
     * Calculate tax of the items inside the list
     * @return cost of the tax based on the items subtotal
     */
    public double tax() {
        return subtotal() * Util.NJ_TAX;
    }

    /**
     * Calculate the total cost with tax and subtotal included
     * @return the total cost of the items
     */
    public double total() {
        return subtotal() + tax();
    }

    /**
     * To get the cost of tax based on the items in the list
     * @return cost of tax of items
     */
    public double getTax() {
        return tax();
    }

    /**
     * To get the total cost based on the items in the list
     * @return total cost of items
     */
    public double getTotal() {
        return total();
    }

    /**
     * To get subtotal cost of the items in the list
     * @return subtotal cost of the items
     */
    public double getSubTotal(){
        return subtotal();
    }

    /**
     * Output the receipt of the items with the subtotal, total, and tax
     * @return formatted String for the receipt of the items
     */
    public String recieptText() {
        String lines = items.stream()
            .map(MenuItem::displayLine)
            .collect(Collectors.joining("\n"));
        return "Order #" + orderNumber + "\n" + lines + "\n" +
                "Subtotal: " + Util.fmt(subtotal()) + "\n" +
                "Tax: " + Util.fmt(tax()) + "\n" +
                "Total: " + Util.fmt(total()) + "\n";
    }
}

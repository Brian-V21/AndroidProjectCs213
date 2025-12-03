package model;

public abstract class MenuItem {
    protected int quantity;

    /**
     * Constructor for MenuItem object
     * @param quantity the number of items for a menu item
     */
    public MenuItem(int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be > 0");
        this.quantity = quantity;
    }

    /**
     * To get the number of items
     * @return the quantity of a menu item
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Set the quantity/amount of a menu item
     * @param q
     */
    public void setQuantity (int q) {
        if (q <= 0) throw new IllegalArgumentException("quantity must be > 0");
        this.quantity = q;
    }

    /**
     * Subclasses must implement this method and is the adjusted price of item based on specific costs
     * @return price that shows the amount a menu item costs
     */
    public abstract double price();

    /**
     * Subclasses must implement this method and is a formatted string for item
     * @return formatted String that describes menu item
     */
    public abstract String displayLine();
}

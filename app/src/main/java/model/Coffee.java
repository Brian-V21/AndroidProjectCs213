package model;

import enums.CoffeeAddIn;
import enums.CupSize;

import java.util.EnumSet;

public class Coffee extends MenuItem {
    private CupSize size;
    private final EnumSet<CoffeeAddIn> addIns;

    /**
     * constructor for coffee object
     * @param size the cup size of the coffee
     * @param addIns the add-ins for coffee
     * @param qty the number of coffees being ordered
     */
    public Coffee(CupSize size, EnumSet<CoffeeAddIn> addIns, int qty) {
        super(qty);
        this.size = size;
        this.addIns = (addIns == null) ? EnumSet.noneOf(CoffeeAddIn.class) : EnumSet.copyOf(addIns);
    }

    /**
     * Calculate price of coffee based on size, qty, and add-ins
     * @return the price of coffee
     */
    @Override
    public double price() {
        double baseShort = 2.39;
        double sizeUp = 0.60;
        double unit = baseShort;
        switch (size) {
            case TALL: unit = baseShort + sizeUp;
                break;
            case GRANDE: unit = baseShort + 2 * sizeUp;
                break;
            case VENTI: unit = baseShort + 3 * sizeUp;
                break;
            default:
                break;
        }
        unit += addIns.size() * 0.25;
        return unit * quantity;
    }

    /**
     * formatted string to display coffee
     * @return String for coffee
     */
    @Override
    public String displayLine() {
        String addInsString = addIns.isEmpty() ? "no add-ins" : addIns.toString();
        return String.format("Coffee (%s, %s) x%d - %s", size, addInsString, quantity, Util.fmt(price()));
    }

    /**
     * To get the size of the coffee being used
     * @return size of coffee
     */
    public CupSize getSize() {
        return size;
    }

    /**
     * to get the add-ins for the coffee
     * @return add-ins for coffee
     */
    public EnumSet<CoffeeAddIn> getAddIns() {
        return EnumSet.copyOf(addIns);
    }

}

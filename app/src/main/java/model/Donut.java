package model;

import java.util.EnumSet;

import enums.DonutFlavor;
import enums.DonutType;


public class Donut extends MenuItem {
    private final DonutType type;
    private final DonutFlavor flavor;

    /**
     * Constructor for donut object
     * @param type the type of donut will be
     * @param flavor the flavor of the donut
     * @param qty the number of donuts
     */
    public Donut(DonutType type, EnumSet<DonutFlavor> flavor, int qty) {
        super(qty);
        this.type = type;
        this.flavor = flavor;
    }

    /**
     * calculated price of donut based on type and quantity
     * @return the price of the donut
     */
    @Override
    public double price() {
        double unit;
        switch(type) {
            case YEAST: unit = 1.99;
                break;
            case CAKE: unit = 2.19;
                break;
            case HOLE: unit = 0.39;
                break;
            case SEASONAL: unit = 2.49;
                break;
            default: unit = 0;
        }
        return unit * quantity;
    }

    /**
     * formatted String for donuts to outprint
     * @return the String for donuts
     */
    @Override
    public String displayLine() {
        return String.format("Donut (%s, %s) x%d - %s", type, flavor, quantity, Util.fmt(price()));
    }

    /**
     * To get the type of donut
     * @return donut type
     */
    public DonutType getType() {
        return type;
    }

    /**
     * To get the flavor of the donut
     * @return donut flavor
     */
    public DonutFlavor getFlavor() {
        return flavor;
    }
}

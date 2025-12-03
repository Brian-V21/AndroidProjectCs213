package model;

import enums.Bread;
import enums.SandwichAddOn;
import enums.Protein;

import java.util.EnumSet;

public class Sandwich extends MenuItem {
    private Bread bread;
    private Protein protein;
    private final EnumSet<SandwichAddOn> addOns;

    /**
     * Constructor for Sandwich object
     * @param bread Bread parameter for the sandwich object
     * @param protein Protein parameter for the sandwich object
     * @param addOns add-ons that will be included into the sandwich object
     * @param qty The number of sandwiches for the sandwich object
     */
    public Sandwich(Bread bread, Protein protein, EnumSet<SandwichAddOn> addOns, int qty) {
        super(qty);
        this.bread = bread;
        this.protein = protein;
        this.addOns = (addOns == null) ? EnumSet.noneOf(SandwichAddOn.class) : EnumSet.copyOf(addOns);
    }

    /**
     * Implement adjusted price based on sandwich prices 
     * @return price of the sandwich object 
     */
    @Override
    public double price() {
        double base;
        switch (protein) {
            case BEEF: base = 12.99;
                break;
            case CHICKEN: base = 10.99;
                break;
            case SALMON: base = 14.99;
                break;
            default: base = 0;
        }
        double extras = 0.0;
        for (SandwichAddOn ao : addOns) {
            extras += (ao == SandwichAddOn.CHEESE) ? 1.00 : 0.30;
        }
        return (base + extras) * quantity;
    }

    /**
     * Display formatted String for sandwich menu item
     * @return Formatted String for Sandwich object
     */
    @Override
    public String displayLine() {
        String ao = addOns.isEmpty() ? "no add-ons" : addOns.toString();
        return String.format("Sandwich (%s, %s, %s) x%d - %s", bread, protein, ao , quantity, Util.fmt(price()));
    }

    /**
     * To get the Bread type enum of the sandwich
     * @return bread of the sandwich object 
     */
    public Bread getBread() {
        return bread;
    }

    /**
     * To get the protein type enum of the sandwich
     * @return protein of the sandwich object
     */
    public Protein getProtein() {
        return protein;
    }

    /**
     * List of sandwich add-ons 
     * @return sandwich add-ons for sandwich object
     */
    public EnumSet<SandwichAddOn> getAddOns() {
        return EnumSet.copyOf(addOns);
    }
}

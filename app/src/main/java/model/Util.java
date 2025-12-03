package model;

import java.text.NumberFormat;
import java.util.Locale;

public class Util {
    public static final double NJ_TAX = 0.06625;
    private static final NumberFormat CURRENCY = NumberFormat.getCurrencyInstance(Locale.US);

    /**
     * Default Constructor for Util Object
     */
    private Util() {}

    /**
     * Formatted String for Currency
     * @param v double of the currency
     * @return String for the formatted currency String
     */
    public static String fmt(double v) {
        return CURRENCY.format(Math.round(v * 100.0/ 100.0));
    }
}

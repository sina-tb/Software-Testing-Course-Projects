package exceptions;

import static defines.Errors.IN_STOCK_IS_ZERO;

public class InStockZero extends Exception {
    public InStockZero() {
        super(IN_STOCK_IS_ZERO);
    }
}
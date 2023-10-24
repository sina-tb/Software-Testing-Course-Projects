package exceptions;

import static defines.Errors.QUANTITY_IS_NEGATIVE;

public class QuntityIsNegative extends Exception {
    public QuntityIsNegative() {
        super(QUANTITY_IS_NEGATIVE);
    }
}
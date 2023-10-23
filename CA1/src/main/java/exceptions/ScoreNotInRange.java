package exceptions;

import static defines.Errors.SCORE_IS_NOT_IN_RANGE;

public class ScoreNotInRange extends Exception {
    public ScoreNotInRange() {
        super(SCORE_IS_NOT_IN_RANGE);
    }
}

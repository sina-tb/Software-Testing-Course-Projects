package model;

import exceptions.NotInStock;
import exceptions.ScoreNotInRange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommodityTest {

    private static Commodity commodity;

    @BeforeEach
    public void setup() {
        commodity = new Commodity();
    }

    @ParameterizedTest
    @CsvSource({"20, -30", "10, -50"})
    public void testUpdateInStockWhenIsNotInStock(int inStock, int amount) throws NotInStock {
        commodity.setInStock(inStock);
        commodity.updateInStock(amount);
    }

    @ParameterizedTest
    @CsvSource({"20, 10", "53, 32"})
    public void testUpdateInStockWhenIsInStock(int inStock, int amount) throws NotInStock {
        commodity.setInStock(inStock);
        int expectedStock = inStock + amount;

        commodity.updateInStock(amount);

        assertEquals(expectedStock, commodity.getInStock());
    }

    @ParameterizedTest
    @ValueSource(ints = {20})
    public void testCalcRatingWithMultipleUsers(int initRate) throws ScoreNotInRange {
        commodity.setInitRate(initRate);
        commodity.addRate("sina", 7);
        commodity.addRate("ali", 5);
        commodity.addRate("hasan", 4);

        float expectedCalcRating = (16 + commodity.getInitRate()) / 4;

        assertEquals(expectedCalcRating, commodity.getRating());
    }

    @ParameterizedTest
    @ValueSource(ints = {30})
    public void testCalcRatingWithSingleUser(int initRate) throws ScoreNotInRange {
        commodity.setInitRate(initRate);
        commodity.addRate("sina", 7);

        float expectedCalcRating = (7 + commodity.getInitRate()) / 2;

        assertEquals(expectedCalcRating, commodity.getRating());
    }

    @ParameterizedTest
    @ValueSource(ints = {30})
    public void testCalcRatingWithDuplicateUser(int initRate) throws ScoreNotInRange {
        commodity.setInitRate(initRate);

        commodity.addRate("sina", 7);
        commodity.addRate("sina", 10);

        float expectedCalcRating = (10 + commodity.getInitRate()) / 2;

        assertEquals(expectedCalcRating, commodity.getRating());
    }


    static Stream<Arguments> providingUserRate() {
        return Stream.of(
                Arguments.of("sina", -23),
                Arguments.of("ali", 23)
        );
    }

    @ParameterizedTest
    @MethodSource("providingUserRate")
    public void testAddRateScoreOutOfRange(String name, int score) throws ScoreNotInRange {
        commodity.addRate(name, score);
    }
    @ParameterizedTest
    @CsvSource({"sina, 5"})
    public void testCorrectnessOfAddingRate(String username, int score) throws ScoreNotInRange {
        commodity.addRate(username, score);
        assertTrue(commodity.getUserRate().containsKey(username));
    }

    @ParameterizedTest
    @CsvSource({"sina, 5"})
    public void testScoreOfAddedRate(String username, int score) throws ScoreNotInRange {
        commodity.addRate(username, score);
        assertEquals(score, this.commodity.getUserRate().get(username));
    }

    @ParameterizedTest
    @CsvSource({"sina"})
    public void testUpdateRateOnExistingUser(String username) throws ScoreNotInRange {
        commodity.addRate(username, 3);
        commodity.addRate(username, 5);
        Map<String, Integer> userRate = commodity.getUserRate();
        assertEquals(5, userRate.get(username));
    }

}

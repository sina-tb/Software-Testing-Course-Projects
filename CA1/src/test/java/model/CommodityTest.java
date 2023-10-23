package model;

import exceptions.NotInStock;
import exceptions.ScoreNotInRange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommodityTest {

    private static Commodity commodity;
    @BeforeEach
    public void setup()
    {
        commodity = new Commodity();
    }
    @Test
    public void testUpdateInStockWhenIsNotInStock() throws NotInStock
    {
       int amount =  -( commodity.getInStock()) -1;
       commodity.updateInStock(amount);
    }

    @ParameterizedTest
    @ValueSource(ints = { 10 })
    public void testUpdateInStockWhenIsInStock(int amount) throws NotInStock
    {
        int inStock = commodity.getInStock();
        int expectedStock = inStock + amount;

        commodity.updateInStock(amount);

        assertEquals(expectedStock, commodity.getInStock());
    }
    @Test
    public void testCalcRatingAndAddRateWithInRangeScore() throws ScoreNotInRange {
        commodity.addRate("sina", 7);
        commodity.addRate("ali",5);

        float expectedCalcRating = (12 + commodity.getInitRate())/ 3;

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
    public void testAddRate(String name, int score) throws ScoreNotInRange
    {
        commodity.addRate(name,score);
    }

}

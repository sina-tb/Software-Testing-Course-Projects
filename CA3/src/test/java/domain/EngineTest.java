package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class EngineTest {

    private Engine engine;

    @BeforeEach
    public void setUp() {
        engine = new Engine();
        engine.orderHistory = new ArrayList<>();
    }

    @Test
    public void testGetAverageOrderQuantityByCustomerWithNoOrders() {
        int expected_value = 0;
        int averageQuantity = engine.getAverageOrderQuantityByCustomer(1);
        assertEquals(expected_value, averageQuantity);
    }
    @Test
    public void testGetAverageOrderQuantityByCustomerWithSingleOrder() {
        int expected_value = 10;
        Order order = new Order(1,1,2 ,10);
        engine.orderHistory.add(order);

        int averageQuantity = engine.getAverageOrderQuantityByCustomer(1);
        assertEquals(expected_value, averageQuantity);
    }
    @Test
     public void testGetAverageOrderQuantityByCustomerWithZeroThrow() {

        Order order = new Order(1,1,2 ,10);
        engine.orderHistory.add(order);

        assertThrows(ArithmeticException.class, () -> engine.getAverageOrderQuantityByCustomer(2));
    }

    @Test
    public void testGetQuantityPatternByPriceWithNoOrders() {
        int expected_value = 0;
        int result = engine.getQuantityPatternByPrice(10);
        assertEquals(expected_value, result);
    }

    @Test
    public void testGetQuantityPatternByPriceWithOrdersWithDifferentPrice() {
        int expected_value = 0;

        engine.orderHistory.add(new Order(1,1 ,20, 10));
        engine.orderHistory.add(new Order(1,1 ,10, 10));

        int result = engine.getQuantityPatternByPrice(20);
        assertEquals(expected_value, result);
    }

    @Test
    public void testGetQuantityPatternByPriceWithConsistentPattern() {
        int expected_value = 10;
        engine.orderHistory.add(new Order(1,1 ,20, 10));
        engine.orderHistory.add(new Order(2,1 ,20, 20));
        engine.orderHistory.add(new Order(3, 1,20, 30));

        int result = engine.getQuantityPatternByPrice(20);
        assertEquals(expected_value, result);
    }

    @Test
    public void testGetQuantityPatternByPriceWithInconsistentPattern() {
        int expected_value = 0;
        engine.orderHistory.add(new Order(1,1 ,20, 10));
        engine.orderHistory.add(new Order(2,1 ,20, 20));
        engine.orderHistory.add(new Order(3,1 ,20, 31));

        int result = engine.getQuantityPatternByPrice(20);
        assertEquals(expected_value, result);
    }

    @Test
    public void testGetQuantityPatternByPriceWithDifferentPrices() {
        int expected_value = 0;
        engine.orderHistory.add(new Order(1,1, 20, 10));
        engine.orderHistory.add(new Order(2,1 ,30, 20));

        int result = engine.getQuantityPatternByPrice(20);
        assertEquals(expected_value, result);
    }
    @Test
    public void testGetCustomerFraudulentQuantityWithBelowAverageOrder() {
        int expected_value = 0;
        engine.orderHistory.add(new Order(1, 1,10, 5));

        int result = engine.getCustomerFraudulentQuantity(new Order(2, 1,10, 3));
        assertEquals(expected_value, result);
    }
    @Test
    public void testGetCustomerFraudulentQuantityWithAboveAverageOrder() {
        int expected_value = 8;
        engine.orderHistory.add(new Order(1, 1,10, 5));
        engine.orderHistory.add(new Order(2, 1,10, 10));

        int result = engine.getCustomerFraudulentQuantity(new Order(3, 1,10, 15));
        assertEquals(expected_value, result);
    }
    @Test
    public void testGetCustomerFraudulentQuantityWithNoOrderHistory() {
        int expected_value = 5;
        int result = engine.getCustomerFraudulentQuantity(new Order(1,1 ,10, 5));
        assertEquals(expected_value, result);
    }
    @Test
    public void testAddOrderAndGetFraudulentQuantityWithNewOrder() {
        int result = engine.addOrderAndGetFraudulentQuantity(new Order(1, 1,10, 5));
        assertEquals(5, result);
    }
    @Test
    public void testAddOrderAndGetFraudulentQuantityWithExistingOrder() {
        engine.orderHistory.add(new Order(1, 1,10, 5));
        int result = engine.addOrderAndGetFraudulentQuantity(new Order(1, 1,10, 5));
        assertEquals(0, result);
    }

    @Test
    public void testAddOrderAndGetFraudulentQuantityWithFraudulentQuantity() {
        engine.orderHistory.add(new Order(1, 1,10, 5));
        int result = engine.addOrderAndGetFraudulentQuantity(new Order(2, 1,10, 10));
        assertEquals(5, result);
    }

    @Test
    public void testAddOrderAndGetFraudulentQuantityWithPatternQuantity() {
        int expectedQuantity = 5;
        int result = engine.addOrderAndGetFraudulentQuantity(new Order(1, 1,20, 5));
        assertEquals(expectedQuantity, result);
    }

    @Test
    public void testAddOrderAndGetFraudulentQuantityWithZeroQuantity() {
        engine.orderHistory.add(new Order(1, 1,20,5 ));
        int result = engine.addOrderAndGetFraudulentQuantity(new Order(2,1, 20, 5));
        assertEquals(0, result);
    }
}
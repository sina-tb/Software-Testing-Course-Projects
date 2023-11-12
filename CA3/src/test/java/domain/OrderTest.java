package domain;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {
    @Test
    public void testOrderGetterSetters() {
        Order order = new Order();

        order.setId(1);
        order.setCustomer(2);
        order.setPrice(3);
        order.setQuantity(4);

        assertEquals(1, order.getId());
        assertEquals(2, order.getCustomer());
        assertEquals(3, order.getPrice());
        assertEquals(4, order.getQuantity());
    }

    @Test
    public void testEqualsWithEqualOrders() {
        Order order1 = new Order(1, 1, 20, 5);
        Order order2 = new Order(1, 1, 20, 5);

        assertTrue(order1.equals(order2));
    }

    @Test
    public void testEqualsWithDifferentOrders() {
        Order order1 = new Order(1, 1, 20, 5);
        Order order2 = new Order(2, 1, 20, 5);

        assertFalse(order1.equals(order2));
    }
}
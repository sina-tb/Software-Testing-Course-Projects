package domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Order {
    int id;
    int customer;
    int price;
    int quantity;

    public Order(int id, int customer, int price,int quantity) {
        this.id = id;
        this.customer = customer;
        this.price = price;
        this.quantity = quantity;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Order order) {
            return id == order.id;
        }
        return false;
    }
}

package model;

import exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;


import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private static User user;
    @BeforeEach
    public void setup()
    {
        user = new User();
    }

    @ParameterizedTest
    @CsvSource( {"100, 30", "120, 10", "2, 3"})
    public void testAddCreditWithValidAmount(float amountToAdd, float initialCredit) throws InvalidCreditRange
    {
        user.setCredit(initialCredit);
        float expectedCredit = initialCredit + amountToAdd;

        user.addCredit(amountToAdd);

        assertEquals(expectedCredit, user.getCredit());
    }
    @ParameterizedTest
    @ValueSource(floats = {-30, -12})
    public void testAddCreditWithInvalidAmount(float invalidAmount)
    {
        assertThrows(InvalidCreditRange.class, () -> user.addCredit(invalidAmount));
    }
    @ParameterizedTest
    @CsvSource( {"100, 30", "120, 10", "30, 3"})
    public void testWithdrawCreditWithValidAmount(float initialCredit, float amountToSubtract) throws InsufficientCredit
    {
        user.setCredit(initialCredit);
        float expectedCredit = initialCredit - amountToSubtract;

        user.withdrawCredit(amountToSubtract);

        assertEquals(expectedCredit, user.getCredit());
    }
    @ParameterizedTest
    @CsvSource( {"20, 30", "10, 50", "30, 100"})
    public void testWithdrawCreditWithInvalidAmount(float initialCredit, float invalidAmount) throws InsufficientCredit
    {
        user.setCredit(initialCredit);
        assertThrows(InsufficientCredit.class, () -> user.withdrawCredit(invalidAmount));
    }
    static Stream<Arguments> ProvidingCommodity() {
        return Stream.of(
                Arguments.of(new Commodity("123"))
        );
    }
    @ParameterizedTest
    @MethodSource("ProvidingCommodity")
    public void testAddBuyItemWhenNotExistsInBuylist(Commodity commodity) throws InStockZero {
        commodity.setInStock(1);
        user.addBuyItem(commodity);
        Map<String, Integer> expectedBuyList = Map.of(commodity.getId(), 1);

        assertEquals(expectedBuyList, user.getBuyList());
    }
    @ParameterizedTest
    @MethodSource ("ProvidingCommodity")
    public void testAddBuyItemWhenExistsInBuylist(Commodity commodity) throws InStockZero {
        commodity.setInStock(1);
        user.addBuyItem(commodity);
        user.addBuyItem(commodity);

        Map<String, Integer> expectedBuyList = Map.of(commodity.getId(), 2);
        assertEquals(expectedBuyList, user.getBuyList());
    }

    @ParameterizedTest
    @MethodSource ("ProvidingCommodity")
    public void testAddBuyItemWhenInStockIsZero(Commodity commodity) {
        commodity.setInStock(0);
        assertThrows(InStockZero.class, () -> user.addBuyItem(commodity));
    }

    static Stream<Arguments> IdAndQuantityForNotExistedPurchasedList() {
        return Stream.of(
                Arguments.of("123",3, Map.of("123",3))
        );
    }
    @ParameterizedTest
    @MethodSource("IdAndQuantityForNotExistedPurchasedList")
    void testAddPurchasedItemWhenNotExistsInPurchasedList(String id, int quantity,Map <String, Integer> expectedPurchasedList) throws QuntityIsNegative {

        user.addPurchasedItem(id, quantity);
        assertEquals(expectedPurchasedList, user.getPurchasedList());
    }

    static Stream<Arguments> IdAndQuantityForExistedPurchasedList() {
        return Stream.of(
                Arguments.of("123",3,5, Map.of("123",8))
        );
    }
    @ParameterizedTest
    @MethodSource("IdAndQuantityForExistedPurchasedList")
    void testAddPurchasedItemWhenExistsInPurchasedList(String id, int initialQuantity,int additionalQuantity,Map <String, Integer> expectedPurchasedList) throws QuntityIsNegative {
        user.addPurchasedItem(id, initialQuantity);
        user.addPurchasedItem(id, additionalQuantity);

        assertEquals(expectedPurchasedList, user.getPurchasedList());
    }
    @ParameterizedTest
    @CsvSource( {"123, -23", "123, -50"})
    public void testAddPurchasedItemWithInvalidQuantity(String id,int invalidQuantity)
    {
        assertThrows(QuntityIsNegative.class, () -> user.addPurchasedItem(id, invalidQuantity));
    }
    @ParameterizedTest
    @MethodSource("ProvidingCommodity")
    void testRemoveItemFromBuyListWhenIdNotExistsInBuyList(Commodity commodity)
    {
        assertThrows(CommodityIsNotInBuyList.class, () -> user.removeItemFromBuyList(commodity));
    }
    @ParameterizedTest
    @MethodSource("ProvidingCommodity")
    void testRemoveItemFromBuyListWhenIdExistsAndRemovesItemFromBuyList(Commodity commodity) throws CommodityIsNotInBuyList, InStockZero {
        commodity.setInStock(1);
        user.addBuyItem(commodity);

        user.removeItemFromBuyList(commodity);

        Map<String, Integer> expectedBuyList = new HashMap<>();

        assertEquals(expectedBuyList, user.getBuyList());
    }
    @ParameterizedTest
    @MethodSource("ProvidingCommodity")
    void testRemoveItemFromBuyListWhenItSubtractBuyList(Commodity commodity) throws CommodityIsNotInBuyList, InStockZero {
        commodity.setInStock(1);
        user.addBuyItem(commodity);
        user.addBuyItem(commodity);

        user.removeItemFromBuyList(commodity);

        Map<String, Integer> expectedBuyList = new HashMap<>();
        expectedBuyList.put(commodity.getId(),1);

        assertEquals(expectedBuyList, user.getBuyList());
    }
}

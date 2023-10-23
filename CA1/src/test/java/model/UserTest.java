package model;

import exceptions.CommodityIsNotInBuyList;
import exceptions.InsufficientCredit;
import exceptions.InvalidCreditRange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
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
    @ValueSource ( floats = {120, 200, 2})
    public void testAddCreditWithValidAmount(float amountToAdd) throws InvalidCreditRange
    {
        float initialCredit = user.getCredit();
        float expectedCredit = initialCredit + amountToAdd;

        user.addCredit(amountToAdd);

        assertEquals(expectedCredit, user.getCredit());
    }
    @ParameterizedTest
    @ValueSource(floats = {-30})
    public void testAddCreditWithInvalidAmount(float invalidAmount) throws InvalidCreditRange
    {
        user.addCredit(invalidAmount);
    }
    @Test
    public void testWithdrawCreditWithValidAmount() throws InsufficientCredit
    {
        float initialCredit = user.getCredit();
        float amountToSubtract = user.getCredit() - 10;
        float expectedCredit = initialCredit - amountToSubtract;

        user.withdrawCredit(amountToSubtract);

        assertEquals(expectedCredit, user.getCredit());
    }
    @Test
    public void testWithdrawCreditWithInvalidAmount() throws InsufficientCredit
    {
        float invalidAmount = user.getCredit() + 1;

        user.withdrawCredit(invalidAmount);
    }
    static Stream<Arguments> ProvidingCommodity() {
        return Stream.of(
                Arguments.of(new Commodity("123"))
        );
    }
    @ParameterizedTest
    @MethodSource("ProvidingCommodity")
    public void testAddBuyItemWhenNotExistsInBuylist(Commodity commodity)
    {
        user.addBuyItem(commodity);
        Map<String, Integer> expectedBuyList = Map.of(commodity.getId(), 1);

        assertEquals(expectedBuyList, user.getBuyList());
    }
    @ParameterizedTest
    @MethodSource ("ProvidingCommodity")
    public void testAddBuyItemWhenExistsInBuylist(Commodity commodity)
    {
        user.addBuyItem(commodity);
        user.addBuyItem(commodity);

        Map<String, Integer> expectedBuyList = Map.of(commodity.getId(), 2);
        assertEquals(expectedBuyList, user.getBuyList());
    }

    static Stream<Arguments> IdAndQuantityForNotExistedPurchasedList() {
        return Stream.of(
                Arguments.of("123",3, Map.of("123",3))
        );
    }
    @ParameterizedTest
    @MethodSource("IdAndQuantityForNotExistedPurchasedList")
    void testAddPurchasedItemWhenNotExistsInPurchasedList(String id, int quantity,Map <String, Integer> expectedPurchasedList)
    {

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
    void testAddPurchasedItemWhenExistsInPurchasedList(String id, int initialQuantity,int additionalQuantity,Map <String, Integer> expectedPurchasedList)
    {
        user.addPurchasedItem(id, initialQuantity);
        user.addPurchasedItem(id, additionalQuantity);

        assertEquals(expectedPurchasedList, user.getPurchasedList());
    }
    @ParameterizedTest
    @MethodSource("ProvidingCommodity")
    void testRemoveItemFromBuyListWhenIdNotExistsInBuyList(Commodity commodity)  throws CommodityIsNotInBuyList
    {
        user.removeItemFromBuyList(commodity);
    }
    @ParameterizedTest
    @MethodSource("ProvidingCommodity")
    void testRemoveItemFromBuyListWhenIdExistsAndRemovesItemFromBuyList(Commodity commodity) throws CommodityIsNotInBuyList {
        user.addBuyItem(commodity);

        user.removeItemFromBuyList(commodity);

        Map<String, Integer> expectedBuyList = new HashMap<>();

        assertEquals(expectedBuyList, user.getBuyList());
    }
    @ParameterizedTest
    @MethodSource("ProvidingCommodity")
    void testRemoveItemFromBuyListWhenItSubtractBuyList(Commodity commodity) throws CommodityIsNotInBuyList {
        user.addBuyItem(commodity);
        user.addBuyItem(commodity);

        user.removeItemFromBuyList(commodity);

        Map<String, Integer> expectedBuyList = new HashMap<>();
        expectedBuyList.put(commodity.getId(),1);

        assertEquals(expectedBuyList, user.getBuyList());
    }
}

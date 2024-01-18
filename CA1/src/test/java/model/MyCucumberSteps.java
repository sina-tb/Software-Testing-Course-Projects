package model;

import exceptions.CommodityIsNotInBuyList;
import exceptions.InsufficientCredit;
import exceptions.InvalidCreditRange;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MyCucumberSteps {
    private Commodity commodity;
    private Exception exception;
    private User user;

    public static User createAnonymousUser() {
        return new User("sina", "123", "sinatabassi80@gmail.com",
                "2001-11-03 00:00:00", "Iran, Tehran");
    }


//addCredit

    @Given("a user with credit of {float}")
    public void aUserWithCredit(float initialCredit) {
        user = createAnonymousUser();
        user.setCredit(initialCredit);
    }


    @When("the user adds {float} to their current credit")
    public void userAddsToCurrentCreditBalance(float amount) {
        try {
            user.addCredit(amount);
        } catch (Exception e) {
            this.exception = e;
        }
    }

    @Then("the new credit should be {float}")
    public void newCreditBalanceShouldBe(float expectedCredit) {
        float actualCredit = user.getCredit();
        assertEquals(expectedCredit, actualCredit);
    }

    @Then("an InvalidCreditRange exception should be thrown")
    public void invalidCreditRangeExceptionShouldBeThrown() {
        assertNotNull(exception);
        assertTrue(exception instanceof InvalidCreditRange);
    }

/////////////////////////////////////////////////////////////////////////////////////////

//withdrawsCredit

    @When("the user withdraws {float} of current credit")
    public void userWithdraws(float withdrawnAmount) {
        try {
            user.withdrawCredit(withdrawnAmount);
        } catch (Exception e) {
            this.exception = e;
        }
    }

    @Then("an InsufficientCredit exception should be thrown")
    public void insufficientCreditExceptionShouldBeThrown() {
        assertNotNull(exception);
        assertTrue(exception instanceof InsufficientCredit);
    }


///////////////////////////////////////////////////////////////////////////////////////////////

// removeItemFrom Buy list

    @Given("a user with following buy list:")
    public void anonymousUserWithBuyList(List<List<String>> buyListData) {
        user = new User("username", "password", "email", "birthDate", "address");
        Map<String, Integer> buyList = new HashMap<>();
        buyListData.forEach(row -> {
            String commodityId = row.get(0);
            int quantity = Integer.parseInt(row.get(1));
            buyList.put(commodityId, quantity);
        });
        user.setBuyList(buyList);
    }


    @When("the user removes product with id {string} form the buy list")
    public void userRemovesFromBuyList(String commodityId) {
        this.commodity = new Commodity(); // Assuming default values
        commodity.setId(commodityId);
        commodity.setPrice(10);
        commodity.setName("P");
        try {
            user.removeItemFromBuyList(commodity);
        } catch (CommodityIsNotInBuyList e) {
            this.exception = e;
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Then("the buy list should be:")
    public void buyListShouldBe(List<List<String>> expectedBuyList) {
        if (expectedBuyList.isEmpty())
            assertTrue(user.getBuyList().isEmpty());
        else
            expectedBuyList.forEach(row -> {
                String commodityId = row.get(0);
                int expectedQuantity = Integer.parseInt(row.get(1));
                assertEquals(expectedQuantity, user.getBuyList().get(commodityId));
            });
    }

    @Then("a CommodityIsNotInButList exception should be thrown")
    public void commodityIsNotInBuyListExceptionShouldBeThrown() {
        assertNotNull(exception);
        assertTrue(exception instanceof CommodityIsNotInBuyList);
    }
/////////////////////////////////////////////////////////////////////////////////////////

}

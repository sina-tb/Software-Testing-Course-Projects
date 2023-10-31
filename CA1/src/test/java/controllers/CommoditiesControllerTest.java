package controllers;

import exceptions.NotExistentCommodity;
import exceptions.NotExistentUser;
import exceptions.ScoreNotInRange;
import model.Comment;
import model.Commodity;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import service.Baloot;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static defines.Errors.NOT_EXISTENT_USER;
import static defines.Errors.SCORE_IS_NOT_IN_RANGE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommoditiesControllerTest {
    @Mock
    private Baloot baloot;
    @InjectMocks
    private CommoditiesController commoditiesController;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCommodities() {
        ArrayList<Commodity> mockCommodities = new ArrayList<>();

        when(baloot.getCommodities()).thenReturn(mockCommodities);

        ResponseEntity<ArrayList<Commodity>> response = commoditiesController.getCommodities();
        assertAll("Checking HTTPStatus and message",
                ()->assertEquals(HttpStatus.OK, response.getStatusCode()),
                ()->assertEquals(mockCommodities, response.getBody()));

    }
    @Test
    void testGetCommodityWithExistedCommodity() throws NotExistentCommodity {
        String commodityId = "123";
        Commodity mockCommodity = new Commodity();
        when(baloot.getCommodityById(commodityId)).thenReturn(mockCommodity);

        ResponseEntity<Commodity> response = commoditiesController.getCommodity(commodityId);
        assertAll("Checking HTTPStatus and message",
                ()->assertEquals(HttpStatus.OK, response.getStatusCode()),
                ()->assertEquals(mockCommodity, response.getBody()));
    }
    @Test
    void testGetCommodityWithNotExistedCommodity() throws NotExistentCommodity {
        String commodityId = "123";

        when(baloot.getCommodityById(commodityId)).thenThrow(new NotExistentCommodity());

        ResponseEntity<Commodity> response = commoditiesController.getCommodity(commodityId);
        assertAll("Checking HTTPStatus and message",
                ()->assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                ()->assertEquals(null, response.getBody()));
    }

    @Test
    void testRateCommodityWithValidInformation() throws NotExistentCommodity {
        String commodityId = "123";
        String username = "sina";
        int rate = 4;

        Map<String, String> input = new HashMap<>();
        input.put("rate", String.valueOf(rate));
        input.put("username", username);

        Commodity mockCommodity = new Commodity();
        when(baloot.getCommodityById(commodityId)).thenReturn(mockCommodity);

        ResponseEntity<String> response = commoditiesController.rateCommodity(commodityId, input);
        assertAll("Checking HTTPStatus and message",
                ()->assertEquals(HttpStatus.OK, response.getStatusCode()),
                ()->assertEquals("rate added successfully!", response.getBody()));

        ;
    }

    @Test
    void testRateCommodityWithNotExistedCommodity() throws NotExistentCommodity {
        String commodityId = "123";
        String username = "sina";
        int rate = 4;

        Map<String, String> input = new HashMap<>();
        input.put("rate", String.valueOf(rate));
        input.put("username", username);

        when(baloot.getCommodityById(commodityId)).thenThrow(new NotExistentCommodity());

        ResponseEntity<String> response = commoditiesController.rateCommodity(commodityId, input);
        assertAll("Checking HTTPStatus and message",
                ()->assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                ()->assertEquals("Commodity does not exist.", response.getBody()));
    }

    @Test
    void testRateCommodityWithNumberFormatException() throws NumberFormatException, NotExistentCommodity {
        String commodityId = "123";
        String username = "sina";

        Map<String, String> input = new HashMap<>();
        input.put("rate", "adsf");
        input.put("username", username);

        when(baloot.getCommodityById(commodityId)).thenThrow(new NumberFormatException());

        ResponseEntity<String> response = commoditiesController.rateCommodity(commodityId, input);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    void testRateCommodityWithScoreNotInrange() throws NotExistentCommodity, ScoreNotInRange {
        String commodityId = "123";
        String username = "sina";
        int rate = 14;

        Map<String, String> input = new HashMap<>();
        input.put("rate", String.valueOf(rate));
        input.put("username", username);

        Commodity mockCommodity = mock(Commodity.class);
        when(baloot.getCommodityById(commodityId)).thenReturn(mockCommodity);
        Mockito.doThrow(new ScoreNotInRange()).when(mockCommodity).addRate(username,rate);

        ResponseEntity<String> response = commoditiesController.rateCommodity(commodityId, input);
        assertAll("Checking HTTPStatus and message",
                ()->assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                ()->assertEquals(SCORE_IS_NOT_IN_RANGE, response.getBody()));
    }
    @Test
    void addCommodityCommentWithValidInformation() throws NotExistentCommodity, NotExistentUser {
        Map<String, String> inputFake = new HashMap<>();
        String commentText = "comment";
        String username = "sina";
        String commodityId = "123";
        String userEmail = "sina@gmail.com";
        inputFake.put("comment", commentText);
        inputFake.put("username", username);

        User userFake = new User(username, "", userEmail, "", "");
        when(baloot.getUserById(username)).thenReturn(userFake);
        ResponseEntity<String> response = commoditiesController.addCommodityComment(commodityId, inputFake);
        assertAll("Checking HTTPStatus and message",
                ()->assertEquals(HttpStatus.OK, response.getStatusCode()),
                ()->assertEquals("comment added successfully!", response.getBody()));
    }
    @Test void addCommodityCommentWithNumberFormatException() throws NotExistentUser {
        Map<String, String> inputFake = new HashMap<>();

        String commentText = "comment";
        String username = "sina";
        String commodityId = "someRandomSentence";

        inputFake.put("comment", commentText);
        inputFake.put("username", username);

        when(baloot.getUserById(username)).thenReturn(new User());
        ResponseEntity<String> response = commoditiesController.addCommodityComment(commodityId, inputFake);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test void addCommodityCommentNotExistedUser() throws NotExistentUser {
        Map<String, String> inputFake = new HashMap<>();
        String commentText = "comment";
        String username = "sina";
        String commodityId = "123";

        inputFake.put("comment", commentText);
        inputFake.put("username", username);

        when(baloot.getUserById(username)).thenThrow(new NotExistentUser());
        ResponseEntity<String> response = commoditiesController.addCommodityComment(commodityId, inputFake);
        assertAll("Checking HTTPStatus and message",
                ()->assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                ()->assertEquals(NOT_EXISTENT_USER, response.getBody()));
    }
    @Test
    void testGetCommodityComment() {
        String commodityId = "123";
        ArrayList<Comment> mockComments = new ArrayList<>();
        when(baloot.getCommentsForCommodity(Integer.parseInt(commodityId))).thenReturn(mockComments);

        ResponseEntity<ArrayList<Comment>> response = commoditiesController.getCommodityComment(commodityId);
        assertAll("Checking HTTPStatus and message",
                ()->assertEquals(HttpStatus.OK, response.getStatusCode()),
                ()->assertEquals(mockComments, response.getBody()));
    }

    @Test
    void testSearchCommoditiesByName() {
        String searchOption = "name";
        String searchValue = "tempName";

        Map<String, String> input = new HashMap<>();
        input.put("searchOption", searchOption);
        input.put("searchValue", searchValue);

        ArrayList<Commodity> mockCommodities = new ArrayList<>();
        when(baloot.filterCommoditiesByName(searchValue)).thenReturn(mockCommodities);

        ResponseEntity<ArrayList<Commodity>> response = commoditiesController.searchCommodities(input);
        assertAll("Checking HTTPStatus and message",
                ()->assertEquals(HttpStatus.OK, response.getStatusCode()),
                ()->assertEquals(mockCommodities, response.getBody()));
    }

    @Test
    void testSearchCommoditiesByCategory() {
        String searchOption = "category";
        String searchValue = "tempName";

        Map<String, String> input = new HashMap<>();
        input.put("searchOption", searchOption);
        input.put("searchValue", searchValue);

        ArrayList<Commodity> mockCommodities = new ArrayList<>();
        when(baloot.filterCommoditiesByCategory(searchValue)).thenReturn(mockCommodities);

        ResponseEntity<ArrayList<Commodity>> response = commoditiesController.searchCommodities(input);
        assertAll("Checking HTTPStatus and message",
                ()->assertEquals(HttpStatus.OK, response.getStatusCode()),
                ()->assertEquals(mockCommodities, response.getBody()));


    }

    @Test
    void testSearchCommoditiesByProvider() {
        String searchOption = "provider";
        String searchValue = "tempName";

        Map<String, String> input = new HashMap<>();
        input.put("searchOption", searchOption);
        input.put("searchValue", searchValue);

        ArrayList<Commodity> mockCommodities = new ArrayList<>();
        when(baloot.filterCommoditiesByProviderName(searchValue)).thenReturn(mockCommodities);

        ResponseEntity<ArrayList<Commodity>> response = commoditiesController.searchCommodities(input);
        assertAll("Checking HTTPStatus and message",
                ()->assertEquals(HttpStatus.OK, response.getStatusCode()),
                ()->assertEquals(mockCommodities, response.getBody()));

    }

    @Test
    void testSearchCommoditiesByDefault() {
        String searchOption = "";
        String searchValue = "tempName";

        Map<String, String> input = new HashMap<>();
        input.put("searchOption", searchOption);
        input.put("searchValue", searchValue);

        ArrayList<Commodity> mockCommodities = new ArrayList<>();

        ResponseEntity<ArrayList<Commodity>> response = commoditiesController.searchCommodities(input);
        assertAll("Checking HTTPStatus and message",
                ()->assertEquals(HttpStatus.OK, response.getStatusCode()),
                ()->assertEquals(mockCommodities, response.getBody()));


    }

    @Test
    void testGetSuggestedCommodities() throws NotExistentCommodity {
        String commodityId = "123";
        Commodity mockCommodity = mock(Commodity.class);
        ArrayList<Commodity> mockSuggestedCommodities = new ArrayList<>();

        when(baloot.getCommodityById(commodityId)).thenReturn(mockCommodity);
        when(baloot.suggestSimilarCommodities(mockCommodity)).thenReturn(mockSuggestedCommodities);

        ResponseEntity<ArrayList<Commodity>> response = commoditiesController.getSuggestedCommodities(commodityId);
        assertAll("Checking HTTPStatus and message",
                ()->assertEquals(HttpStatus.OK, response.getStatusCode()),
                ()->assertEquals(mockSuggestedCommodities, response.getBody()));
    }
    @Test
    void testGetSuggestedCommoditiesWithNotExistentCommodity() throws NotExistentCommodity {
        String commodityId = "123";

        when(baloot.getCommodityById(commodityId)).thenThrow(new NotExistentCommodity());

        ResponseEntity<ArrayList<Commodity>> response = commoditiesController.getSuggestedCommodities(commodityId);
        assertAll("Checking HTTPStatus and message",
                ()->assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                ()->assertEquals(new ArrayList<>(), response.getBody()));
    }
}

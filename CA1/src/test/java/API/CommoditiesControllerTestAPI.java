package controllers;

import application.BalootApplication;
import exceptions.NotExistentCommodity;
import exceptions.NotExistentUser;
import model.Comment;
import model.Commodity;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import service.Baloot;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(classes = BalootApplication.class)
public class CommoditiesControllerTestAPI {
    @MockBean
    private Baloot baloot;
    @Autowired
    private CommoditiesController commoditiesController;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        commoditiesController.setBaloot(baloot);
    }

    @Test
    public void testGetCommodities() throws Exception {
        List<Commodity> mockCommodities = new ArrayList<>();
        Commodity commodity= new Commodity();
        commodity.setId("1");
        mockCommodities.add(commodity);

        when(baloot.getCommodities()).thenReturn((ArrayList<Commodity>) mockCommodities);

        mockMvc.perform(get("/commodities")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value("1"));
    }

    @Test
    public void testGetCommodityWithExistedCommodity() throws Exception {
        Commodity commodity= new Commodity();
        commodity.setId("1");

        when(baloot.getCommodityById("1")).thenReturn(commodity);

        mockMvc.perform(get("/commodities/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    public void testGetCommodityWithNotExistedCommodity() throws Exception {
        when(baloot.getCommodityById("1")).thenThrow(new NotExistentCommodity());

        mockMvc.perform(get("/commodities/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    @Test
    public void testRateCommodityWithoutError() throws Exception {
        Commodity commodity= new Commodity();
        commodity.setId("1");
        when(baloot.getCommodityById("1")).thenReturn(commodity);

        mockMvc.perform(post("/commodities/1/rate")
                        .content("{\"rate\": \"5\", \"username\": \"sina\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("rate added successfully!"));
    }

    @Test
    public void testRateCommodityWithNotExistedCommodity() throws Exception {
        when(baloot.getCommodityById("1")).thenThrow(new NotExistentCommodity());
        mockMvc.perform(post("/commodities/1/rate")
                        .content("{\"rate\": \"5\", \"username\": \"sina\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Commodity does not exist."));
    }

    @Test
    public void testRateCommodityWithInvalidRate() throws Exception {
        Commodity commodity= new Commodity();
        commodity.setId("1");
        when(baloot.getCommodityById("1")).thenReturn(commodity);
        mockMvc.perform(post("/commodities/1/rate")
                        .content("{\"rate\": \"13\", \"username\": \"sina\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("The score is not in range."));
    }

    @Test
    public void testRateCommodityWithInvalidFormat() throws Exception {
        mockMvc.perform(post("/commodities/1/rate")
                        .content("{\"rate\": \"invalid\", \"username\": \"sina\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Format of number is wrong"));
    }

    @Test
    public void testAddCommodityCommentWithNoError() throws Exception {
        User user = new User("sina", "123", "sina@gmail.com", "2001-11-03", "myAddress");
        when(baloot.getUserById("sina")).thenReturn(user);

        mockMvc.perform(post("/commodities/1/comment")
                        .content("{\"username\": \"sina\", \"comment\": \"The Comment\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("comment added successfully!"));
    }

    @Test
    public void testAddCommodityCommentWithNotExistedUser() throws Exception {

        when(baloot.getUserById("No User")).thenThrow(new NotExistentUser());

        mockMvc.perform(post("/commodities/1/comment")
                        .content("{\"username\": \"No User\", \"comment\": \"The Comment\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User does not exist."));
    }

    @Test
    public void testAddCommodityCommentWithInvalidNumberFormat() throws Exception {
        User user = new User("sina", "123", "sina@gmail.com", "2001-11-03", "myAddress");
        when(baloot.getUserById("sina")).thenReturn(user);

        mockMvc.perform(post("/commodities/invalid/comment")
                        .content("{\"username\": \"sina\", \"comment\": \"The Comment\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Format of number is wrong"));
    }

    @Test
    public void testGetCommodityCommentWithNoError() throws Exception {

        ArrayList<Comment> mockComments = new ArrayList<>();
        mockComments.add(new Comment(1, "sina@gmail.com", "sina", 1, "The comment"));
        when(baloot.getCommentsForCommodity(1)).thenReturn(mockComments);

        mockMvc.perform(get("/commodities/1/comment")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].userEmail").value("sina@gmail.com"))
                .andExpect(jsonPath("$[0].username").value("sina"))
                .andExpect(jsonPath("$[0].commodityId").value(1))
                .andExpect(jsonPath("$[0].text").value("The comment"));
    }

    @Test
    public void testGetCommodityCommentWithNoCommodity() throws Exception {
        when(baloot.getCommentsForCommodity(1)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/commodities/1/comment")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void testSearchCommoditiesByCommodityName() throws Exception {
        ArrayList<Commodity> mockCommodities = new ArrayList<>();
        Commodity commodity= new Commodity();
        commodity.setId("1");
        commodity.setName("Product 1");
        mockCommodities.add(commodity);
        when(baloot.filterCommoditiesByName("Product 1")).thenReturn(mockCommodities);

        mockMvc.perform(post("/commodities/search")
                        .content("{\"searchOption\": \"name\", \"searchValue\": \"Product 1\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Product 1"));
    }

    @Test
    public void testSearchCommoditiesByCategory() throws Exception {
        ArrayList<Commodity> mockCommodities = new ArrayList<>();
        Commodity commodity= new Commodity();
        commodity.setId("1");
        mockCommodities.add(commodity);
        when(baloot.filterCommoditiesByCategory("Category A")).thenReturn(mockCommodities);


        mockMvc.perform(post("/commodities/search")
                        .content("{\"searchOption\": \"category\", \"searchValue\": \"Category A\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value("1"));
    }

    @Test
    public void testSearchCommodities_ByProvider() throws Exception {
        ArrayList<Commodity> mockCommodities = new ArrayList<>();
        Commodity commodity= new Commodity();
        commodity.setId("1");
        commodity.setProviderId("Provider X");
        mockCommodities.add(commodity);
        when(baloot.filterCommoditiesByProviderName("Provider X")).thenReturn(mockCommodities);

        mockMvc.perform(post("/commodities/search")
                        .content("{\"searchOption\": \"provider\", \"searchValue\": \"Provider X\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].providerId").value("Provider X"));
    }

    @Test
    public void testSearchCommodities_InvalidSearchOption() throws Exception {
        mockMvc.perform(post("/commodities/search")
                        .content("{\"searchOption\": \"invalid\", \"searchValue\": \"Value\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void testGetSuggestedCommodities_Success() throws Exception {
        Commodity commodity1= new Commodity();
        commodity1.setId("1");

        Commodity commodity2= new Commodity();
        commodity2.setId("2");

        Commodity commodity3= new Commodity();
        commodity3.setId("3");

        when(baloot.getCommodityById("1")).thenReturn(commodity1);

        ArrayList<Commodity> mockSuggestedCommodities = new ArrayList<>();

        mockSuggestedCommodities.add(commodity2);
        mockSuggestedCommodities.add(commodity3);
        when(baloot.suggestSimilarCommodities(commodity1)).thenReturn(mockSuggestedCommodities);

        mockMvc.perform(get("/commodities/1/suggested")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("2"))
                .andExpect(jsonPath("$[1].id").value("3"));
    }

    @Test
    public void testGetSuggestedCommoditiesCommodityNotFound() throws Exception {
        when(baloot.getCommodityById("No Commodity")).thenThrow(new NotExistentCommodity());
        mockMvc.perform(get("/commodities/No Commodity/suggested")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
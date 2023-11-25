package controllers;

import application.BalootApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import service.Baloot;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;;

import static org.mockito.Mockito.when;
import model.Commodity;


import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest(classes = BalootApplication.class)
public class commoditiesControllerTest {
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
    public void testGetCommodities_Success() throws Exception {

        List<Commodity> mockCommodities = new ArrayList<>();
        Commodity commodity =new Commodity();
        commodity.setId("1");
        mockCommodities.add(commodity);

        when(baloot.getCommodities()).thenReturn((ArrayList<Commodity>) mockCommodities);

        mockMvc.perform(get("/commodities")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id",is(commodity.getId())));
    }
}
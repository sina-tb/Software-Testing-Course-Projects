package controllers;

import application.BalootApplication;
import exceptions.NotExistentUser;
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

import static defines.Errors.INVALID_CREDIT_RANGE;
import static defines.Errors.NOT_EXISTENT_USER;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@SpringBootTest(classes = BalootApplication.class)
public class UserControllerTest {
    @MockBean
    private Baloot baloot;
    @Autowired
    private UserController userController;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        userController.setBaloot(baloot);
    }

    @Test
    public void getUserTestWithValidUser() throws Exception {
        User user = new User("sina", "123", "sina@gmail.com", "2001-11-03", "myAddress");
        when(baloot.getUserById("810199322")).thenReturn(user);
        mockMvc.perform(get("/users/{id}", "810199322"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username",is(user.getUsername())))
                .andExpect(jsonPath("$.password",is(user.getPassword())))
                .andExpect(jsonPath("$.email",is(user.getEmail())))
                .andExpect(jsonPath("$.birthDate",is(user.getBirthDate())))
                .andExpect(jsonPath("$.address",is(user.getAddress())));
    }

    @Test
    public void testgetUserWithInvalidUser() throws Exception {
        when(baloot.getUserById("sina")).thenThrow(new NotExistentUser());
        mockMvc.perform(get("/users/{id}", "sina")).andExpect(status().isNotFound());
    }

    @Test
    public void testAddCreditWithValidCredit() throws Exception {
        String credit = "50.0";
        User user = new User("sina", "123", "sina@gmail.com", "2001-11-03", "myAddress");
        when(baloot.getUserById("810199322")).thenReturn(user);
        mockMvc.perform(post("/users/810199322/credit")
                        .content("{\"credit\": \"50.0\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("credit added successfully!"));
    }

    @Test
    public void testAddCreditWithInvalidCredit() throws Exception {
        User user = new User("sina", "123", "sina@gmail.com", "2001-11-03", "myAddress");
        when(baloot.getUserById("810199322")).thenReturn(user);
        mockMvc.perform(post("/users/810199322/credit")
                        .content("{\"credit\": \"-10.0\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(INVALID_CREDIT_RANGE));
    }

    @Test
    public void testAddCreditWithNotExistedUser() throws Exception {
        when(baloot.getUserById("810199322")).thenThrow(new NotExistentUser());
        mockMvc.perform(post("/users/810199322/credit")
                        .content("{\"credit\": \"30.0\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(NOT_EXISTENT_USER));
    }

    @Test
    public void testAddCreditWithInvalidNumberFormat() throws Exception {
        mockMvc.perform(post("/users/810199322/credit")
                        .content("{\"credit\": \"invalid\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Format of number is wrong"));
    }
}
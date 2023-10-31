package controllers;


import exceptions.IncorrectPassword;
import exceptions.NotExistentUser;
import exceptions.UsernameAlreadyTaken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import service.Baloot;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;

public class AuthenticationControllerTest {

    @Mock
    private Baloot baloot;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginWithValidUsernameAndPassword() throws NotExistentUser, IncorrectPassword {
        Map<String, String> input = new HashMap<>();

        input.put("username", "sina");
        input.put("password", "1234");

        Mockito.doNothing().when(baloot).login("sina", "1234");

        ResponseEntity<String> response = authenticationController.login(input);

        assertAll("Checking Status code amd message",
                () ->assertEquals(HttpStatus.OK, response.getStatusCode()),
                ()->assertEquals("login successfully!", response.getBody()));
    }

    @Test
    void testLoginWithInvalidUsername() throws NotExistentUser, IncorrectPassword {
        Map<String, String> input = new HashMap<>();

        input.put("username", "sina");
        input.put("password", "1234");

        Mockito.doThrow(new NotExistentUser()).when(baloot).login("sina", "1234");

        ResponseEntity<String> response = authenticationController.login(input);

        assertAll("Checking Status code amd message",
                () ->assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                ()->assertEquals("User does not exist.", response.getBody()));
    }
    @Test
    void testLoginWithInvalidPassword() throws NotExistentUser, IncorrectPassword {
        Map<String, String> input = new HashMap<>();

        input.put("username", "sina");
        input.put("password", "1234");

        Mockito.doThrow(new IncorrectPassword()).when(baloot).login("sina","1234");

        ResponseEntity<String> response = authenticationController.login(input);

        assertAll("Checking Status code amd message",
                () ->assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode()),
                ()->assertEquals("Incorrect password.", response.getBody()));
    }
    @Test
    void testSignupWithValidInformation() throws UsernameAlreadyTaken {
        Map<String, String> input = new HashMap<>();
        input.put("address", "resalat");
        input.put("birthDate", "7/3");
        input.put("email", "sina@gmail.com");
        input.put("username", "sina");
        input.put("password", "1234");


        Mockito.doNothing().when(baloot).addUser(any());

        ResponseEntity<String> response = authenticationController.signup(input);

        assertAll("Checking Status code amd message",
                ()->assertEquals(HttpStatus.OK, response.getStatusCode()),
                ()->assertEquals("signup successfully!", response.getBody()));
    }
    @Test
    void testSignupWithExistingUsername() throws UsernameAlreadyTaken {
        Map<String, String> input = new HashMap<>();

        input.put("address", "resalat");
        input.put("birthDate", "7/3");
        input.put("email", "sina@gmail.com");
        input.put("username", "sina");
        input.put("password", "1234");

        Mockito.doThrow(new UsernameAlreadyTaken()).when(baloot).addUser(argThat(user -> user.getUsername().equals(input.get("username"))));

        ResponseEntity<String> response = authenticationController.signup(input);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("The username is already taken.", response.getBody());

    }
}


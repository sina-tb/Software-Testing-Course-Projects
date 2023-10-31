package controllers;

import exceptions.NotExistentComment;
import model.Comment;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommentControllerTest {

    @Mock
    private Baloot baloot;

    @InjectMocks
    private CommentController commentController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLikeCommentWithExistingComment() throws NotExistentComment {
        int commentId = 123;
        String username = "sina";
        Map<String, String> input = new HashMap<>();
        input.put("username", username);

        Comment mockComment = new Comment();
        Mockito.when(baloot.getCommentById(commentId)).thenReturn(mockComment);

        ResponseEntity<String> response = commentController.likeComment(String.valueOf(commentId), input);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("The comment was successfully liked!", response.getBody());
    }
    @Test
    void testLikeCommentWithNotExistingComment() throws NotExistentComment {
        int commentId = 123;
        String username = "sina";
        Map<String, String> input = new HashMap<>();
        input.put("username", username);

        Mockito.when(baloot.getCommentById(commentId)).thenThrow( new NotExistentComment());

        ResponseEntity<String> response = commentController.likeComment(String.valueOf(commentId), input);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Comment does not exist.", response.getBody());
    }

    @Test
    void testDislikeCommentWithExistingComment() throws NotExistentComment {
        int commentId = 123;
        String username = "sina";
        Map<String, String> input = new HashMap<>();
        input.put("username", username);

        Comment mockComment = new Comment();
        Mockito.when(baloot.getCommentById(commentId)).thenReturn(mockComment);

        ResponseEntity<String> response = commentController.dislikeComment(String.valueOf(commentId), input);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("The comment was successfully disliked!", response.getBody());
    }
    @Test
    void testDislikeCommentWithNotExistingComment() throws NotExistentComment {
        int commentId = 123;
        String username = "sina";
        Map<String, String> input = new HashMap<>();
        input.put("username", username);

        Mockito.when(baloot.getCommentById(commentId)).thenThrow( new NotExistentComment());

        ResponseEntity<String> response = commentController.dislikeComment(String.valueOf(commentId), input);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Comment does not exist.", response.getBody());
    }
}
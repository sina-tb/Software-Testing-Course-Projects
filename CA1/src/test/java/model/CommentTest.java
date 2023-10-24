package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class CommentTest {

    private static Comment comment;

    @BeforeEach
    public void setup() {
        comment = new Comment();
    }

    @Test
    public void testCurrentDateFormat() {
        String currentDate = comment.getCurrentDate();
        assertTrue(currentDate.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }
    @Test
    public void testCurrentDateValue() {
        String currentDate = comment.getCurrentDate();
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        assertEquals(dateFormat.format(date), currentDate.substring(0, 10));
    }

    @Test
    public void testAddUserVoteLikeCount() {
        comment.addUserVote("sina", "like");
        comment.addUserVote("mamad", "like");
        comment.addUserVote("shirin", "like");

        int expectedLike = 3;

        assertEquals(expectedLike, comment.getLike());
    }

    @Test
    public void testAddUserVoteDisikeCount() {
        comment.addUserVote("sina", "dislike");
        comment.addUserVote("amir", "dislike");

        int expectedLike = 2;

        assertEquals(expectedLike, comment.getDislike());
    }

    @Test
    public void testAddUserVoteOneUserLikesAndDislikes() {
        comment.addUserVote("sina", "like");
        comment.addUserVote("sina", "dislike");

        int expectedLike = 0;
        int expectedDislike = 1;
        assertAll("Checking likes and dislike commented by one user",
                () -> assertEquals(expectedLike, comment.getLike()),
                () -> assertEquals(expectedDislike, comment.getDislike())
        );

    }

    @Test
    public void testAddUserVoteOneUserDislikesAndlikes() {
        comment.addUserVote("sina", "dislike");
        comment.addUserVote("sina", "like");

        int expectedLike = 1;
        int expectedDislike = 0;
        assertAll("Checking likes and dislike commented by one user",
                () -> assertEquals(expectedLike, comment.getLike()),
                () -> assertEquals(expectedDislike, comment.getDislike())
        );

    }
    @Test
    public void testAddUserVoteOneUserLikesDoubleTime() {
        comment.addUserVote("sina", "like");
        comment.addUserVote("sina", "like");

        int expectedLike = 1;
        int expectedDislike = 0;
        assertAll("Checking likes and dislike commented by one user",
                () -> assertEquals(expectedLike, comment.getLike()),
                () -> assertEquals(expectedDislike, comment.getDislike())
        );
    }

    @Test
    public void testAddUserVoteOneUserDisikesDoubleTime() {
        comment.addUserVote("sina", "dislike");
        comment.addUserVote("sina", "dislike");

        int expectedLike = 0;
        int expectedDislike = 1;
        assertAll("Checking likes and dislike commented by one user",
                () -> assertEquals(expectedLike, comment.getLike()),
                () -> assertEquals(expectedDislike, comment.getDislike())
        );
    }


}

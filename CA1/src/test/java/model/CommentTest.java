package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentTest {

    private static Comment comment;
    @BeforeEach
    public void setup()
    {
        comment = new Comment();
    }

    @Test
    public void testGetCurrentDate()
    {
        Date currentDate = new Date();
        SimpleDateFormat expectedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        assertEquals(expectedDate.format(currentDate), comment.getCurrentDate());
    }

    @Test
    public void testAddUserVote()
    {
        comment.addUserVote("sina","like");
        comment.addUserVote("ali","dislike");
        comment.addUserVote("mamad","like");
        comment.addUserVote("shirin","dislike");

        int expectedLike = 2;
        int expecctedDislike = 2;

        assertAll("Multiple assertions",
                () -> assertEquals(expectedLike,comment.getLike()),
                () -> assertEquals(expecctedDislike,comment.getDislike())
        );
    }
}

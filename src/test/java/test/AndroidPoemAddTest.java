package test;

import org.junit.Test;
import org.rest.webapp.Entity.Poem;
import org.rest.webapp.Entity.User;
import org.rest.webapp.Services.PoemService;
import org.rest.webapp.Services.UserService;

import static org.junit.Assert.assertEquals;

/**
 * Created by Ivan on 12/20/2015.
 */
public class AndroidPoemAddTest {

    @Test
    public void TestUserCorrectness() {
        UserService userService = new UserService();


        User user1 = new User();
        user1.setNickName("Q");
        user1.setFirstName("Q");
        user1.setToken("UXE=1450608546");
        user1.setEmail("q");
        //user1.setPassword("q");
        user1.setId(15);

        User user2 = userService.getByToken("UXE=1450608546");
        assertEquals(user1.getFirstName(), user2.getFirstName());
        assertEquals(user1.getNickName(), user2.getNickName());
        assertEquals(user1.getEmail(), user2.getEmail());
        assertEquals(user1.getPassword(), user2.getPassword());
        assertEquals(user1.getId(), user2.getId());
    }

    @Test
    public void TestPoemInsert() {
        PoemService poemService = new PoemService();

        Poem poem = new Poem();
        poem.setContent("Жизнь");
        poem.setId(13);
        poem.setGenre("Порошок");

        Poem poem1 = poemService.getById(13);

        assertEquals(poem.getContent(), poem1.getContent());
        assertEquals(poem.getId(), poem1.getId());
        assertEquals(poem.getGenre(), poem1.getGenre());
    }



}

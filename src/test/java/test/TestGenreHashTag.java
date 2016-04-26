import org.rest.webapp.Entity.Poem;
import org.rest.webapp.Services.PoemService;

import java.util.List;

/**
 * Created by Ivan on 4/5/2016.
 */
public class TestGenreHashTag {

    public static void main(String[] args) {

        PoemService poemService = new PoemService();


       /* List<Poem> poems = poemService.getByGenre("Порошок");

        for (Poem p: poems) {
            System.out.println(p.getGenre());
        }*/
        String hashtags = "#Балалайка #улетела";
        List<Poem> poems1 = poemService.getByHashTag(hashtags);
        for (Poem p: poems1) {
            System.out.println(p.getContent());
        }

    }



}

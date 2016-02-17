package org.rest.webapp;

import Utils.HibernateUtil;
import com.sun.jersey.core.util.Base64;
import org.hibernate.Session;
import org.rest.webapp.Entity.Poem;
import org.rest.webapp.Entity.User;
import org.rest.webapp.Services.PoemService;
import org.rest.webapp.Services.UserService;

/**
 * Created by Ivan on 10/8/2015.
 */
public class EnterPoint {

    public static void main(String[] args) {
        System.out.println("Maven + Hibernate + MySQL");

        UserService userService = new UserService();
        User user = new User();
        user.setNickName("zzz");
        user.setFirstName("zzz");
        //user.setPassword("zzz");
        user.setEmail("zzz");


        Poem p = new Poem();
        p.setContent("когда геннадий закипает\n" +
                "в него премудрая жена\n" +
                "бросает овощи и мясо\n" +
                "и втихомолку варит борщ");



        user.getPoems().add(p);
        System.out.println("SIZE: " + user.getPoems().size());

        //String string = new Base64().encode("lala");

        

        userService.persist(user);

        PoemService poemService = new PoemService();
        p.getDislikes().add(user.getId());
        p.getDislikes().add(user.getId());
        p.getDislikes().add(user.getId());
        p.getDislikes().add(user.getId());

        poemService.update(p);

    }

}
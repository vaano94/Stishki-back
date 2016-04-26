package org.rest.webapp.Services;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.rest.webapp.DAO.PoemDAO;
import org.rest.webapp.Entity.Poem;

import java.util.List;

/**
 * Created by Ivan on 10/13/2015.
 */
public class PoemService {

    private static PoemDAO poemDAO;

    public PoemService() { poemDAO = new PoemDAO(); }

    public void persist(Poem poem) {
        poemDAO.openCurrentSessionwithTransaction();
        poemDAO.persist(poem);
        poemDAO.closeCurrentSessionwithTransaction();
    }
    public void update(Poem poem) {
        poemDAO.openCurrentSessionwithTransaction();
        poemDAO.update(poem);
        poemDAO.closeCurrentSessionwithTransaction();
    }
    public void delete(Poem poem) {
        poemDAO.openCurrentSessionwithTransaction();
        poemDAO.delete(poem);
        poemDAO.closeCurrentSessionwithTransaction();
    }
    public Poem getById(long id) {
        poemDAO.openCurrentSessionwithTransaction();
        Poem poem = (Poem) poemDAO.getCurrentSession().load(Poem.class, id);
        poemDAO.closeCurrentSessionwithTransaction();
        return poem;
    }
    public List<Poem> getAll() {
        poemDAO.openCurrentSessionwithTransaction();
        List<Poem> poems = poemDAO.getCurrentSession().createCriteria(Poem.class).list();
        poemDAO.closeCurrentSessionwithTransaction();
        return poems;
    }

    public List<Poem> getNewOnes() {
        poemDAO.openCurrentSessionwithTransaction();
        List<Poem> poems = poemDAO.getCurrentSession().createCriteria(Poem.class).addOrder(Order.desc("id")).setMaxResults(40).list();
        poemDAO.closeCurrentSessionwithTransaction();
        return poems;
    }


    public List<Poem> getByGenre(String genre) {
        String[] array = genre.split(" #"); // #one #two
        poemDAO.openCurrentSessionwithTransaction();
        List<Poem> poems = poemDAO.getCurrentSession().createCriteria(Poem.class).add(Restrictions.eq("genre", genre)).list();
        poemDAO.closeCurrentSessionwithTransaction();
        return poems;
    }

    public List<Poem> getByHashTag(String hashtags) {
        String[] array = hashtags.replace("#","").split(" "); // #one #two = ["one"," two"];
        for (String entry: array) {entry.trim();
            System.out.println(entry);}

        poemDAO.openCurrentSessionwithTransaction();
        String query = "from Poem as poem where ";
        for (int i = 0 ; i<array.length; i++) {
            if (i==(array.length-1)) {
                query += ("'" +array[i]+"' in elements(poem.hashtags)");
            }
            else {
                query += ("'" +array[i]+"' in elements(poem.hashtags) and");
            }
        }
        //System.out.println(query);

        List<Poem> poems = poemDAO.getCurrentSession().createQuery(query).list();
        poemDAO.closeCurrentSessionwithTransaction();
        return poems;
    }

}

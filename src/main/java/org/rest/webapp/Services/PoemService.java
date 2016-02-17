package org.rest.webapp.Services;

import org.hibernate.criterion.Order;
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


}

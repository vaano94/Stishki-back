package org.rest.webapp.Services;

import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.rest.webapp.DAO.PoemDAO;
import org.rest.webapp.Entity.Poem;
import org.rest.webapp.Schedule.PoemScheduledService;

import java.util.ArrayList;
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
        Poem poem = (Poem) poemDAO.getCurrentSession().createCriteria(Poem.class).add(Restrictions.eq("id",id)).uniqueResult();
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
        List<Poem> poems = poemDAO.getCurrentSession().createCriteria(Poem.class).addOrder(Order.desc("id")).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).setMaxResults(40).list();
        poemDAO.closeCurrentSessionwithTransaction();
        return poems;
    }

    public List<Poem> getByOffset(int startOffset) {
        poemDAO.openCurrentSessionwithTransaction();
        Criteria criteria = poemDAO.getCurrentSession().createCriteria(Poem.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).addOrder(Order.desc("id"));
        criteria.setFirstResult(startOffset);
        criteria.setMaxResults(40);
        List<Poem> poems = criteria.list();
        poemDAO.closeCurrentSessionwithTransaction();
        return poems;
    }


    public List<Poem> getByGenre(List<String> genres, int offset) {
        //String[] array = genres.split(" "); // one two
        poemDAO.openCurrentSessionwithTransaction();

        Disjunction disjunction = Restrictions.disjunction();
        for (String s: genres) {
            disjunction.add(Restrictions.eq("genre", s));
        }
        Criteria criteria = poemDAO.getCurrentSession().createCriteria(Poem.class)
                .add(disjunction)
                .addOrder(Order.desc("id"))
                .setFirstResult(offset)
                .setMaxResults(offset + 40)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Poem> poems = criteria.list();
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

        List<Poem> poems = poemDAO.getCurrentSession().createQuery(query).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
        poemDAO.closeCurrentSessionwithTransaction();
        return poems;
    }
    public List<Poem> getPopularByOffset(int offset) {
        List<Poem> list = PoemScheduledService.popularPoems;
        int endoffset = offset+40;
        if (list.size()<=offset) {
            return new ArrayList<Poem>();
        }
        if (list.size()<=offset+40) {
            endoffset = list.size();
        }
        List<Poem> offsetList = list.subList(offset, endoffset);
        return offsetList;
    }


}

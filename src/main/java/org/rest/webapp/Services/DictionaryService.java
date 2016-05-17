package org.rest.webapp.Services;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.rest.webapp.DAO.DictionaryDAO;
import org.rest.webapp.Entity.Dictionary;

import java.util.List;

/**
 * Created by Ivan on 5/12/2016.
 */
public class DictionaryService {

    private static DictionaryDAO dictionaryDAO;

    public DictionaryService() { dictionaryDAO = new DictionaryDAO();
    }

    public void persist(Dictionary dictionary) {
        dictionaryDAO.openCurrentSessionwithTransaction();
        dictionaryDAO.persist(dictionary);
        dictionaryDAO.closeCurrentSessionwithTransaction();
    }
    public void update(Dictionary dictionary) {
        dictionaryDAO.openCurrentSessionwithTransaction();
        dictionaryDAO.update(dictionary);
        dictionaryDAO.closeCurrentSessionwithTransaction();
    }
    public void delete(Dictionary dictionary) {
        dictionaryDAO.openCurrentSessionwithTransaction();
        dictionaryDAO.delete(dictionary);
        dictionaryDAO.closeCurrentSessionwithTransaction();
    }

    public List<Dictionary> getByEnding(String ending) {
        dictionaryDAO.openCurrentSessionwithTransaction();
        Criteria criteria = dictionaryDAO.getCurrentSession().createCriteria(Dictionary.class);
        List<Dictionary> list = criteria.add(Restrictions.like("word", "%" + ending)).list();
        return list;
    }

}

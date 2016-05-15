package org.rest.webapp.Services;

import org.rest.webapp.DAO.DictionaryDAO;
import org.rest.webapp.Entity.Dictionary;

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

}

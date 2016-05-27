package org.rest.webapp.Services;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.rest.webapp.DAO.DraftDAO;
import org.rest.webapp.Entity.Draft;

import java.io.Serializable;

/**
 * Created by Ivan on 5/24/2016.
 */
public class DraftService {
    private DraftDAO draftDAO;

    public DraftService() {
        draftDAO = new DraftDAO();
    }

    public boolean deleteById(Class<?> type, Serializable id) {
        Session session = draftDAO.openCurrentSessionwithTransaction();
        Object persistentInstance = session.load(type, id);
        if (persistentInstance != null) {
            session.delete(persistentInstance);
            draftDAO.closeCurrentSessionwithTransaction();
            return true;
        }
        draftDAO.closeCurrentSessionwithTransaction();
        return false;
    }

    public Draft getDraftById(long id) {
        draftDAO.openCurrentSessionwithTransaction();
        Draft draft = (Draft) draftDAO.getCurrentSession().createCriteria(Draft.class).add(Restrictions.eq("id",id)).uniqueResult();
        draftDAO.closeCurrentSessionwithTransaction();
        return draft;
    }

    public void persist(Draft draft) {
        draftDAO.openCurrentSessionwithTransaction();
        draftDAO.persist(draft);
        draftDAO.closeCurrentSessionwithTransaction();
    }
    public void update(Draft draft) {
        draftDAO.openCurrentSessionwithTransaction();
        draftDAO.update(draft);
        draftDAO.closeCurrentSessionwithTransaction();
    }
    public void delete(Draft draft) {
        draftDAO.openCurrentSessionwithTransaction();
        draftDAO.delete(draft);
        draftDAO.closeCurrentSessionwithTransaction();
    }




}

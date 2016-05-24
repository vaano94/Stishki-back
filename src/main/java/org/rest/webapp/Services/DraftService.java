package org.rest.webapp.Services;

import org.rest.webapp.DAO.DraftDAO;
import org.rest.webapp.Entity.Draft;

/**
 * Created by Ivan on 5/24/2016.
 */
public class DraftService {
    private DraftDAO draftDAO;

    public DraftService(DraftDAO draftDAO) {
        draftDAO = new DraftDAO();
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

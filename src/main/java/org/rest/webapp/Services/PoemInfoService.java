package org.rest.webapp.Services;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.rest.webapp.DAO.PoemInfoDAO;
import org.rest.webapp.Entity.PoemInfo;

/**
 * Created by Ivan on 5/7/2016.
 */
public class PoemInfoService {

    private static PoemInfoDAO poemInfoDAO;

    public PoemInfoService() { poemInfoDAO = new PoemInfoDAO(); }

    public void persist(PoemInfo poemInfo) {
        poemInfoDAO.openCurrentSessionwithTransaction();
        poemInfoDAO.persist(poemInfo);
        poemInfoDAO.closeCurrentSessionwithTransaction();
    }
    public void update(PoemInfo poemInfo) {
        poemInfoDAO.openCurrentSessionwithTransaction();
        poemInfoDAO.update(poemInfo);
        poemInfoDAO.closeCurrentSessionwithTransaction();
    }
    public void delete(PoemInfo poemInfo) {
        poemInfoDAO.openCurrentSessionwithTransaction();
        poemInfoDAO.delete(poemInfo);
        poemInfoDAO.closeCurrentSessionwithTransaction();
    }
    public PoemInfo getById(long id) {
        poemInfoDAO.openCurrentSessionwithTransaction();
        PoemInfo poeminfo = (PoemInfo) poemInfoDAO.getCurrentSession().createCriteria(PoemInfo.class).add(Restrictions.eq("id", id)).uniqueResult();
        poemInfoDAO.closeCurrentSessionwithTransaction();
        return poeminfo;
    }
    public PoemInfo getByType(String type) {
        poemInfoDAO.openCurrentSessionwithTransaction();
        Criteria criteria = poemInfoDAO.getCurrentSession().createCriteria(PoemInfo.class);
        PoemInfo poemInfo = (PoemInfo) criteria.add(Restrictions.eq("type",type)).uniqueResult();
        return poemInfo;
    }


}

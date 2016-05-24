package org.rest.webapp.DAO;

import Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.rest.webapp.Entity.Draft;

/**
 * Created by Ivan on 5/24/2016.
 */
public class DraftDAO {

    private Session currentSession;

    private Transaction currentTransaction;

    public DraftDAO() {}

    public Session openCurrentSessionwithTransaction() {
        currentSession = HibernateUtil.getSessionFactory().openSession();
        currentTransaction = currentSession.beginTransaction();
        return currentSession;
    }
    public void closeCurrentSession() {
        currentSession.close();
    }
    public Session getCurrentSession() {
        return currentSession;
    }
    public void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
    }
    public Transaction getCurrentTransaction() {
        return currentTransaction;
    }
    public void setCurrentTransaction(Transaction currentTransaction) {
        this.currentTransaction = currentTransaction;
    }
    public void closeCurrentSessionwithTransaction() {
        currentTransaction.commit();
        currentSession.close();
    }

    public void persist(Draft draft) {
        getCurrentSession().save(draft);
    }
    public void update(Draft draft) {
        getCurrentSession().update(draft);
    }
    public void delete(Draft draft) {
        getCurrentSession().delete(draft);
    }

}

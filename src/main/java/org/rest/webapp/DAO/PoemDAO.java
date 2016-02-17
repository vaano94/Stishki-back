package org.rest.webapp.DAO;

import Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.rest.webapp.Entity.Poem;

/**
 * Created by Ivan on 10/13/2015.
 */
public class PoemDAO {

    private Session currentSession;

    private Transaction currentTransaction;

    public PoemDAO() {}

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

    public void persist(Poem poem) {
        getCurrentSession().save(poem);
    }
    public void update(Poem poem) {
        getCurrentSession().update(poem);
    }
    public void delete(Poem poem) {
        getCurrentSession().delete(poem);
    }

}

package org.rest.webapp.DAO;

import Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.rest.webapp.Entity.User;

/**
 * Created by Ivan on 10/12/2015.
 */
public class UserDAO {

    private Session currentSession;

    private Transaction currentTransaction;


    public UserDAO() {}

    public Session openCurrentSession() {
        currentSession = HibernateUtil.getSessionFactory().openSession();
        return currentSession;
    }
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

    public void persist(User user) {
        getCurrentSession().save(user);
    }
    public void update(User user) {
        getCurrentSession().update(user);
    }
    public void delete(User user) {
        getCurrentSession().delete(user);
    }


}

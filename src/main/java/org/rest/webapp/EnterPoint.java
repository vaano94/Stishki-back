package org.rest.webapp;

import Utils.HibernateUtil;
import org.hibernate.Session;
import org.rest.webapp.Entity.User;

/**
 * Created by Ivan on 10/8/2015.
 */
public class EnterPoint {

    public static void main(String[] args) {
        System.out.println("Maven + Hibernate + MySQL");
        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();
        User user = new User();



        session.save(user);
        session.getTransaction().commit();
    }

}
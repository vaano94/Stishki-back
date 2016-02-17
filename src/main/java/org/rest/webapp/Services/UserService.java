package org.rest.webapp.Services;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.rest.webapp.DAO.UserDAO;
import org.rest.webapp.Entity.User;

import java.util.List;

/**
 * Created by Ivan on 10/12/2015.
 */
public class UserService {

    private static UserDAO userDAO;

    public UserService() {
        userDAO = new UserDAO();
    }

    public void persist(User user) {
        userDAO.openCurrentSessionwithTransaction();
        userDAO.persist(user);
        userDAO.closeCurrentSessionwithTransaction();
    }

    public void update(User user) {
        userDAO.openCurrentSessionwithTransaction();
        userDAO.update(user);
        userDAO.closeCurrentSessionwithTransaction();
    }

    public void delete(User user) {
        userDAO.openCurrentSessionwithTransaction();
        userDAO.delete(user);
        userDAO.closeCurrentSessionwithTransaction();
    }

    public User getById(long id) {
        userDAO.openCurrentSessionwithTransaction();
        User user = (User) userDAO.getCurrentSession().load(User.class, id);
        userDAO.closeCurrentSessionwithTransaction();
        return user;
    }

    public User getByNickName(String name) {
        userDAO.openCurrentSessionwithTransaction();
        Criteria criteria = userDAO.getCurrentSession().createCriteria(User.class);
        User user = (User) criteria.add(Restrictions.eq("nickName", name)).uniqueResult();
        userDAO.closeCurrentSessionwithTransaction();
        return user;
    }

    public User getByToken(String token) {
        userDAO.openCurrentSessionwithTransaction();
        Criteria criteria = userDAO.getCurrentSession().createCriteria(User.class);
        User user = (User) criteria.add(Restrictions.eq("token", token)).uniqueResult();
        userDAO.closeCurrentSessionwithTransaction();
        return user;
    }



    public List<User> getAll() {
        userDAO.openCurrentSessionwithTransaction();
        List<User> list = userDAO.getCurrentSession().createCriteria(User.class).list();
        userDAO.closeCurrentSessionwithTransaction();
        return list;
    }


}

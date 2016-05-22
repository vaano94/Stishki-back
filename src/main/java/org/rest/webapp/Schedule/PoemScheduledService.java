package org.rest.webapp.Schedule;

import org.hibernate.criterion.Order;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.rest.webapp.DAO.PoemDAO;
import org.rest.webapp.Entity.Poem;

import java.util.List;

/**
 * Created by Ivan on 5/21/2016.
 */
public class PoemScheduledService implements Job {

    public static List<Poem> popularPoems = null;

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        popularPoems = getPopularPoemsAll();
        System.out.println("Fetched popular poems");
    }

    public List<Poem> getPopularPoemsAll() {
        PoemDAO poemDAO = new PoemDAO();
        poemDAO.openCurrentSessionwithTransaction();
        popularPoems = poemDAO.getCurrentSession().createCriteria(Poem.class)
                .addOrder(Order.desc("likesCount"))
                .setMaxResults(200)
                .setCacheable(true)
                .list();
        poemDAO.closeCurrentSessionwithTransaction();
        return popularPoems;
    }


}

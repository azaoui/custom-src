package org.exoplatform.hibernate.dao;

import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.hibernate.model.Conges;
import org.exoplatform.services.organization.hibernate.HibernateListAccess;
import org.hibernate.Session;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Romain Dénarié (romain.denarie@exoplatform.com) on 06/04/16.
 */
public class CongesDAO {
    private HibernateService service_;

    public static final String queryFindCongesById =
            "from org.exoplatform.hibernate.model.Conges where id = :id";

    public CongesDAO(HibernateService service)
    {
        service_ = service;
    }

    /**
     * {@inheritDoc}
     */
    public Conges createCongesInstance()
    {
        return new Conges();
    }


    /**
     * {@inheritDoc}
     */
    public void createConges(Conges conges) throws Exception
    {
        final Session session = service_.openSession();
        session.save(conges);
        session.flush();
    }

    /**
     * {@inheritDoc}
     */
    public void saveConges(Conges conges) throws Exception
    {
        Session session = service_.openSession();
        session.merge(conges);
        session.flush();
    }

    /**
     * {@inheritDoc}
     */
    public Conges removeConges(String congesId) throws Exception
    {
        Session session = service_.openSession();
        Conges foundConges= findCongesById(congesId, session);

        if (foundConges == null)
            return null;
        session.delete(foundConges);
        session.flush();
        return foundConges;
    }

    public ListAccess<Conges> findAllConges() throws Exception
    {
        String findQuery = "from o in class " + Conges.class.getName();
        String countQuery = "select count(o) from " + Conges.class.getName() + " o";

        return new HibernateListAccess<Conges>(service_, findQuery, countQuery);
    }

    /**
     * {@inheritDoc}
     */
    public Conges findCongesById(String congesId) throws Exception
    {

        Session session = service_.openSession();
        Conges conges = findCongesById(congesId, session);
        return conges;
    }


    public Conges findCongesById(String congesId, Session session) throws Exception
    {
        Conges conges = (Conges)service_.findOne(session, queryFindCongesById, congesId);
        return conges;
    }

    public ListAccess<Conges> findCongesByYear(int year) {

        GregorianCalendar firstDayOfYear = new GregorianCalendar(year,0,1,0,0,0);
        GregorianCalendar lastDayofYear = new GregorianCalendar(year,11,31,23,59,59);


        String queryFindCongesByYear =
                "from org.exoplatform.hibernate.model.Conges where (startDate >= :startDate AND startDate <= :endDate ) OR" +
                        "(endDate >= :startDate AND endDate <= :endDate )";

        String countQuery = "select count(*) from org.exoplatform.hibernate.model.Conges where (startDate >= :startDate AND startDate <= :endDate ) OR" +
                "(endDate >= :startDate AND endDate <= :endDate )";

        Map<String, Object> parameters = new HashMap<String,Object>();
        parameters.put("startDate", firstDayOfYear.getTime());
        parameters.put("endDate", lastDayofYear.getTime());

        return new HibernateListAccess<Conges>(service_, queryFindCongesByYear, countQuery, parameters);

    }

    public ListAccess<Conges> findCongesByDates(long startDate, long endDate) {
        String queryFindCongesByYear =
                "from org.exoplatform.hibernate.model.Conges where (startDate >= :startDate AND startDate <= :endDate ) OR" +
                        "(endDate >= :startDate AND endDate <= :endDate )";

        String countQuery = "select count(*) from org.exoplatform.hibernate.model.Conges where (startDate >= :startDate AND startDate <= :endDate ) OR" +
                "(endDate >= :startDate AND endDate <= :endDate )";


        GregorianCalendar start = new GregorianCalendar();
        start.setTimeInMillis(startDate);
        GregorianCalendar end = new GregorianCalendar();
        end.setTimeInMillis(endDate);




        Map<String, Object> parameters = new HashMap<String,Object>();
        parameters.put("startDate", start.getTime());
        parameters.put("endDate", end.getTime());

        return new HibernateListAccess<Conges>(service_, queryFindCongesByYear, countQuery, parameters);
    }
}
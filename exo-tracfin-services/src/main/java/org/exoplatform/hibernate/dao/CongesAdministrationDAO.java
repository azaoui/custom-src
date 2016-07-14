package org.exoplatform.hibernate.dao;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.exoplatform.hibernate.model.CongesAdministration;
import org.hibernate.Session;

import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.hibernate.model.Conges;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.organization.hibernate.HibernateListAccess;

/**
 * Created by Romain Dénarié (romain.denarie@exoplatform.com) on 06/04/16.
 */
public class CongesAdministrationDAO {
    private HibernateService service_;

    public static final String queryFindCongesAdministrationById =
            "from org.exoplatform.hibernate.model.CongesAdministration where id = :id";

    public static final String queryFindCongesAdministrationByUsername =
            "from org.exoplatform.hibernate.model.CongesAdministration where username = :username";

    public CongesAdministrationDAO(HibernateService service)
    {
        service_ = service;
    }

    /**
     * {@inheritDoc}
     */
    public CongesAdministration createCongesInstance()
    {
        return new CongesAdministration();
    }


    /**
     * {@inheritDoc}
     */
    public void createConges(CongesAdministration congesAdministration) throws Exception
    {
        final Session session = service_.openSession();
        session.save(congesAdministration);
        session.flush();
    }

    /**
     * {@inheritDoc}
     */
    public void saveConges(CongesAdministration conges) throws Exception
    {
        Session session = service_.openSession();
        session.merge(conges);
        session.flush();
    }

    /**
     * {@inheritDoc}
     */
    public CongesAdministration removeConges(String congesAdministrationId) throws Exception
    {
        Session session = service_.openSession();
        CongesAdministration foundCongesAdministration= findCongesAdministrationById(congesAdministrationId, session);

        if (foundCongesAdministration == null)
            return null;
        session.delete(foundCongesAdministration);
        session.flush();
        return foundCongesAdministration;
    }

    public ListAccess<CongesAdministration> findAllCongesAdministration() throws Exception
    {
        String findQuery = "from o in class " + CongesAdministration.class.getName();
        String countQuery = "select count(o) from " + CongesAdministration.class.getName() + " o";

        return new HibernateListAccess<CongesAdministration>(service_, findQuery, countQuery);
    }

    /**
     * {@inheritDoc}
     */
    public CongesAdministration findCongesAdministrationById(String congesAdministrationId) throws Exception
    {

        Session session = service_.openSession();
        CongesAdministration conges = findCongesAdministrationById(congesAdministrationId, session);
        return conges;
    }


    public CongesAdministration findCongesAdministrationById(String congesAdministrationId, Session session) throws Exception
    {
        CongesAdministration congesAdministration = (CongesAdministration) service_.findOne(session, queryFindCongesAdministrationById, congesAdministrationId);
        return congesAdministration;
    }


    public ListAccess<CongesAdministration> findCongesAdministrationByUser(String username) throws Exception {
        Session session = service_.openSession();
        return findCongesAdministrationByUsername(username, session);
    }

    public ListAccess<CongesAdministration> findCongesAdministrationByUsername(String username, Session session) throws Exception
    {

        String findQuery = "from o in class " + CongesAdministration.class.getName()+" where username = '"+username+"'";
        String countQuery = "select count(*) from " + CongesAdministration.class.getName() +" where username = '"+username+"'";

        return new HibernateListAccess<CongesAdministration>(service_, findQuery, countQuery);
    }

    public ListAccess<CongesAdministration> findValidators() {
        String findQuery = "from o in class " + CongesAdministration.class.getName()+" where isValidator = 1";
        String countQuery = "select count(*) from " + CongesAdministration.class.getName() +" where isValidator = 1";

        return new HibernateListAccess<CongesAdministration>(service_, findQuery, countQuery);

    }
}
package org.exoplatform.tracfin.portlet.conges;

/**
 * Created by Romain Dénarié (romain.denarie@exoplatform.com) on 08/04/16.
 */
import juzu.Path;
import juzu.Response;
import juzu.SessionScoped;
import juzu.View;
import juzu.template.Template;
import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;

import javax.inject.Inject;
import java.util.*;

public class CongesApplication {
    @Inject
    @Path("index.gtmpl")
    Template index;

    @Inject
    OrganizationService organizationService;


    @View
    public Response.Content index() {

        Map<String,String> usernames=new HashMap<String,String>();
        try {

            ListAccess<User> users = organizationService.getUserHandler().findAllUsers();
            User[] userList = users.load(0,users.getSize());
            for (User user : userList) {
                usernames.put(user.getUserName(),user.getDisplayName());
            }
        } catch (Exception e) {

        }


        GregorianCalendar start = new GregorianCalendar();
        GregorianCalendar end = (GregorianCalendar)start.clone();

        start.set(Calendar.DAY_OF_MONTH, start.getActualMinimum(Calendar.DAY_OF_MONTH));
        start.set(Calendar.HOUR_OF_DAY, start.getActualMinimum(Calendar.HOUR_OF_DAY));
        start.set(Calendar.MINUTE, start.getActualMinimum(Calendar.MINUTE));
        start.set(Calendar.SECOND, start.getActualMinimum(Calendar.SECOND));
        start.set(Calendar.MILLISECOND, start.getActualMinimum(Calendar.MILLISECOND));

        end.add(Calendar.MONTH,1);
        end.set(Calendar.DAY_OF_MONTH, end.getActualMaximum(Calendar.DAY_OF_MONTH));
        end.set(Calendar.HOUR_OF_DAY, end.getActualMaximum(Calendar.HOUR_OF_DAY));
        end.set(Calendar.MINUTE, end.getActualMaximum(Calendar.MINUTE));
        end.set(Calendar.SECOND, end.getActualMaximum(Calendar.SECOND));
        end.set(Calendar.MILLISECOND, end.getActualMaximum(Calendar.MILLISECOND));


        return index.with().set("usernames",usernames)
                .set("startDate",start).set("endDate",end).ok();
    }

    @View
    public Response.Content updateDate(String startDateInMillis) {
        Map<String,String> usernames=new HashMap<String,String>();
        try {

            ListAccess<User> users = organizationService.getUserHandler().findAllUsers();
            User[] userList = users.load(0,users.getSize());
            for (User user : userList) {
                usernames.put(user.getUserName(),user.getDisplayName());
            }
        } catch (Exception e) {

        }


        GregorianCalendar start = new GregorianCalendar();
        start.setTimeInMillis(new Long(startDateInMillis));
        GregorianCalendar end = (GregorianCalendar)start.clone();

        start.set(Calendar.DAY_OF_MONTH, start.getActualMinimum(Calendar.DAY_OF_MONTH));
        start.set(Calendar.HOUR_OF_DAY, start.getActualMinimum(Calendar.HOUR_OF_DAY));
        start.set(Calendar.MINUTE, start.getActualMinimum(Calendar.MINUTE));
        start.set(Calendar.SECOND, start.getActualMinimum(Calendar.SECOND));
        start.set(Calendar.MILLISECOND, start.getActualMinimum(Calendar.MILLISECOND));

        end.add(Calendar.MONTH,1);
        end.set(Calendar.DAY_OF_MONTH, end.getActualMaximum(Calendar.DAY_OF_MONTH));
        end.set(Calendar.HOUR_OF_DAY, end.getActualMaximum(Calendar.HOUR_OF_DAY));
        end.set(Calendar.MINUTE, end.getActualMaximum(Calendar.MINUTE));
        end.set(Calendar.SECOND, end.getActualMaximum(Calendar.SECOND));
        end.set(Calendar.MILLISECOND, end.getActualMaximum(Calendar.MILLISECOND));


        return index.with().set("usernames",usernames)
                .set("startDate",start).set("endDate",end).ok();
    }




}

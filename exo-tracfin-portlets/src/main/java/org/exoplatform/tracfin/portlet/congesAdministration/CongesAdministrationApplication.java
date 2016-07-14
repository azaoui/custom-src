package org.exoplatform.tracfin.portlet.congesAdministration;

/**
 * Created by Romain Dénarié (romain.denarie@exoplatform.com) on 08/04/16.
 */

import java.util.*;

import javax.annotation.Resource;
import javax.inject.Inject;

import juzu.*;
import juzu.plugin.ajax.Ajax;
import juzu.template.Template;

import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.hibernate.model.CongesAdministration;
import org.exoplatform.services.CongesService;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;

public class CongesAdministrationApplication {
    @Inject
    @Path("index.gtmpl")
    Template index;

    @Inject
    @Path("trForm.gtmpl")
    Template form;

    @Inject
    OrganizationService organizationService;


    @Inject
    CongesService congesService;


    @View
    public Response.Content index() {

        Map<String,String> usernames=new HashMap<String,String>();
        Map<String,CongesAdministration> congesAdministrations=new HashMap<String,CongesAdministration>();

        try {

            ListAccess<User> users = organizationService.getUserHandler().findAllUsers();
            User[] userList = users.load(0,users.getSize());
            for (User user : userList) {
                usernames.put(user.getUserName(),user.getDisplayName());
            }

            for (CongesAdministration congesAdmin : congesService.getAllCongesAdministration()) {
                congesAdministrations.put(congesAdmin.getUsername(),congesAdmin);
            }
        } catch (Exception e) {

        }

        return index.with().set("usernames",usernames).set("congesAdministrations",congesAdministrations).ok();
    }

    @Action
    @Route("/editUser")
    public void editUser(String inputUsername, String inputSolde, String inputValidator) throws Exception {

        List<CongesAdministration> congesAdministrations = congesService.getCongesAdministrationByUsername(inputUsername);
        if (congesAdministrations.size() == 0) {
            congesService.createCongesAdministration(inputUsername,new Double(inputSolde),!(inputValidator == null ||!inputValidator.equals("on")));
        } else {
            CongesAdministration congesAdministration = congesAdministrations.get(0);
            congesAdministration.setRemainingDays(new Double(inputSolde));
            if (inputValidator == null || !inputValidator.equals("on")) {
                congesAdministration.setValidator(false);
            } else {
                congesAdministration.setValidator(true);
            }
            congesService.editCongesAdministration(congesAdministration);
        }
    }



}

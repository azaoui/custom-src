package org.exoplatform.services;

import org.exoplatform.common.http.HTTPStatus;
import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.component.activity.UIBonitaActivity;
import org.exoplatform.component.activity.UIBonitaActivityBuilder;
import org.exoplatform.hibernate.dao.CongesAdministrationDAO;
import org.exoplatform.hibernate.dao.CongesDAO;
import org.exoplatform.hibernate.model.Conges;
import org.exoplatform.hibernate.model.CongesAdministration;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.rest.resource.ResourceContainer;
import org.exoplatform.social.core.activity.model.ExoSocialActivity;
import org.exoplatform.social.core.activity.model.ExoSocialActivityImpl;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.manager.ActivityManager;
import org.exoplatform.social.core.manager.IdentityManager;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Romain Dénarié (romain.denarie@exoplatform.com) on 06/04/16.
 */
/**
 * modified by Ahmed Zaoui (azaoui@exoplatform.com) 27/07/16
 *
 */
@Path("/conges/")
public class CongesService implements ResourceContainer {
	
	private static final Log LOG = ExoLogger.getLogger(CongesService.class);
	static private final String PROVIDER_ID = "organization";
    private CongesDAO congesDAO;

    private CongesAdministrationDAO congesAdministrationDAO;

    private OrganizationService organizationService;
    private ActivityManager activityManager;
    private IdentityManager identityManager;


    public CongesService(HibernateService hibernateService, OrganizationService organizationService, ActivityManager activityManager,IdentityManager identityManager) {
        congesAdministrationDAO = new CongesAdministrationDAO(hibernateService);
        congesDAO = new CongesDAO(hibernateService);
        this.activityManager = activityManager;
        this.identityManager=identityManager;
        this.organizationService=organizationService;
        
    }


    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addConges(CongesObject congesObject) throws Exception {
    	
        try {

            Conges conges = new Conges();
            conges.setUserName(congesObject.getUserName());
            conges.setStartDate(new Date(congesObject.getStartDate()));
            conges.setEndDate(new Date(congesObject.getEndDate()));
            conges.setFirstDayHalf(congesObject.getIsFirstDayHalf());
            conges.setEndDayHalf(congesObject.getIsEndDayHalf());
            conges.setOneDayHalf(congesObject.getOneDayHalf());
            conges.setNbDays(congesObject.getNbDays());
            conges.setValidatorUserName(congesObject.getValidatorUsername());
            conges.setType(congesObject.getType());
            conges.setReason(congesObject.getReason());

            congesDAO.createConges(conges);

            if (congesObject.getType().contains("congés")) {
                ListAccess<CongesAdministration> congesAdmins = congesAdministrationDAO.findCongesAdministrationByUser(congesObject.getUserName());
                CongesAdministration congesAdmin = congesAdmins.load(0, 1)[0];
                if(congesObject.getReason().contains("pérenne")){
                     congesAdmin.setSoldeCetPerenne(congesAdmin.getSoldeCetPerenne() - congesObject.getNbDays());
                }
                if(congesObject.getReason().contains("historique")){
                     congesAdmin.setSoldeCetHistorique(congesAdmin.getSoldeCetHistorique() - congesObject.getNbDays());
                }
                if(congesObject.getReason().contains("ordinaires")){
                    congesAdmin.setSoldeConge(congesAdmin.getSoldeConge() - congesObject.getNbDays());
               }

                congesAdministrationDAO.saveConges(congesAdmin);

            }
            
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(HTTPStatus.INTERNAL_ERROR).build();
        }
    }


    @GET
    @Path("/getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllConges() throws Exception {
        try {
            ListAccess<Conges> allConges = congesDAO.findAllConges();

            List<CongesObject> result = new ArrayList<CongesObject>();
            for (Conges conges : allConges.load(0,allConges.getSize())) {
                result.add(new CongesObject(conges));
            }

            return Response.ok(result, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(HTTPStatus.INTERNAL_ERROR).build();
        }
    }

    @GET
    @Path("/getFromYear/{year}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFromYear(@PathParam("year") String year) throws Exception {
        try {
            ListAccess<Conges> congesByYear = congesDAO.findCongesByYear(new Integer(year).intValue());

            List<CongesObject> result = new ArrayList<CongesObject>();
            for (Conges conges : congesByYear.load(0,congesByYear.getSize())) {
                result.add(new CongesObject(conges));
            }

            return Response.ok(result, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(HTTPStatus.INTERNAL_ERROR).build();
        }
    }

    @GET
    @Path("/getFromYearGroupByUsers/{year}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFromYearGroupByUsers(@PathParam("year") String year) throws Exception {
        try {
            ListAccess<Conges> congesByYear = congesDAO.findCongesByYear(new Integer(year).intValue());

            Map<String,List<CongesObject>> result = new HashMap<String,List<CongesObject>>();
            for (Conges conges : congesByYear.load(0,congesByYear.getSize())) {
                String username = conges.getUserName();
                if (!result.containsKey(username)) {
                    List<CongesObject> userResults = new ArrayList<CongesObject>();
                    userResults.add(new CongesObject(conges));
                    result.put(username,userResults);
                } else {
                    List actualResult = result.get(username);
                    actualResult.add(new CongesObject(conges));
                    result.put(username,actualResult);
                }
            }

            return Response.ok(result, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(HTTPStatus.INTERNAL_ERROR).build();
        }
    }


    @GET
    @Path("/getByDateGroupByUsers/{startDate}/{endDate}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByDateGroupByUsers(@PathParam("startDate") String startDate, @PathParam("endDate") String endDate) throws Exception {
        try {


            ListAccess<Conges> congesByYear = congesDAO.findCongesByDates(new Long(startDate).longValue(),new Long(endDate).longValue());

            Map<String,List<CongesObject>> result = new HashMap<String,List<CongesObject>>();
            for (Conges conges : congesByYear.load(0,congesByYear.getSize())) {
                String username = conges.getUserName();
                if (!result.containsKey(username)) {
                    List<CongesObject> userResults = new ArrayList<CongesObject>();
                    userResults.add(new CongesObject(conges));
                    result.put(username,userResults);
                } else {
                    List actualResult = result.get(username);
                    actualResult.add(new CongesObject(conges));
                    result.put(username,actualResult);
                }
            }

            return Response.ok(result, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(HTTPStatus.INTERNAL_ERROR).build();
        }
    }

    @GET
    @Path("/getValidators")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getValidators() throws Exception {
        try {

            List<ValidatorObject> result=new ArrayList<ValidatorObject>();
            ListAccess<CongesAdministration> validators = congesAdministrationDAO.findValidators();
            for (CongesAdministration conges : validators.load(0,validators.getSize())) {
                ValidatorObject val = new ValidatorObject();

                val.setLabel(organizationService.getUserHandler().findUserByName(conges.getUsername()).getDisplayName());
                val.setValue(conges.getUsername());
                result.add(val);
            }


            return Response.ok(result, MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*")
            	      .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
            	      .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With").build();
        } catch (Exception e) {
            return Response.status(HTTPStatus.INTERNAL_ERROR).build();
        }
    }

    @GET
    @Path("/getSolde/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSolde(@PathParam("username") String username) throws Exception {
        try {
            List<CongesAdministration> administrations=getCongesAdministrationByUsername(username);
            Double result = new Double(0);
            if (administrations.size() != 0) {
                result=administrations.get(0).getSoldeConge();
                LOG.info("Le solde de congé de "+username+": est: "+result);
            }
            return Response.ok(result.toString(), MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*")
      .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
      .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With").build();
        } catch (Exception e) {
            return Response.status(HTTPStatus.INTERNAL_ERROR).build();
        }
    }
    
    
    @GET
    @Path("/getSoldeCetPerenne/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSoldeCetPerenne(@PathParam("username") String username) throws Exception {
        try {
            List<CongesAdministration> administrations=getCongesAdministrationByUsername(username);
            Double result = new Double(0);
            if (administrations.size() != 0) {
                result=administrations.get(0).getSoldeCetPerenne();
                LOG.info("Le solde Cet Perenne de "+username+": est: "+result);
            }
            return Response.ok(result.toString(), MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*")
      .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
      .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With").build();
        } catch (Exception e) {
            return Response.status(HTTPStatus.INTERNAL_ERROR).build();
        }
    }
    
    
    @GET
    @Path("/getSoldeCetHistorique/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSoldeCET(@PathParam("username") String username) throws Exception {
        try {
            List<CongesAdministration> administrations=getCongesAdministrationByUsername(username);
            Double result = new Double(0);
            if (administrations.size() != 0) {
                result=administrations.get(0).getSoldeCetHistorique();
                LOG.info("Le solde Cet Historique de "+username+": est: "+result);
            }
            return Response.ok(result.toString(), MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*")
      .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
      .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With").build();
        } catch (Exception e) {
            return Response.status(HTTPStatus.INTERNAL_ERROR).build();
        }
    }
    
    
    
    @POST
    @Path("/notify")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response notifyConge(ValidationObject validationObject) throws Exception {
    	
    	 ExoSocialActivity activity = new ExoSocialActivityImpl();
        try {

     
            activity.setUserId(identityManager.getOrCreateIdentity(PROVIDER_ID, validationObject.userName,
                    true).getId());
            
           String  currentValidator= (validationObject.superValidatorUsername.length()>0)  ? validationObject.superValidatorUsername :validationObject.userName ;
            
			String resumee = "Demande de congé."
					+ "<br/>"
					+ "Demandeur : " + " @" + validationObject.userName
					+ " Validateur : " + "@"
					+ currentValidator;
			activity.setTitle(resumee);
			activity.setType(UIBonitaActivity.ACTIVITY_TYPE);
			Map<String, String> templateParams = new HashMap<String, String>();
			templateParams.put(UIBonitaActivityBuilder.USER_NAME_PARAM,validationObject.getUserName());
			templateParams.put(UIBonitaActivityBuilder.VALIDATOR_NAME_PARAM,validationObject.validatorUsername);
			templateParams.put(UIBonitaActivityBuilder.REASON_PARAM,validationObject.reason);
			templateParams.put(UIBonitaActivityBuilder.STARTDATE_PARAM,Long.toString(validationObject.startDate));
			templateParams.put(UIBonitaActivityBuilder.ENDDATE_PARAM,Long.toString(validationObject.endDate));
			templateParams.put(UIBonitaActivityBuilder.NBDAYS_PARAM,Double.toString(validationObject.nbDays));
			templateParams.put(UIBonitaActivityBuilder.TYPE_PARAM,validationObject.type);
			templateParams.put(UIBonitaActivityBuilder.IS_APPROVED_PARAM,Boolean.toString(validationObject.isApprouved));
			templateParams.put(UIBonitaActivityBuilder.REFUSAL_REASON,validationObject.refusalReason);
			templateParams.put(UIBonitaActivityBuilder.STATE,validationObject.state);
			templateParams.put(UIBonitaActivityBuilder.SUPER_VALIDATOR_USER_NAME, validationObject.superValidatorUsername);
			
			activity.setTemplateParams(templateParams);
            activity.setBody("Demande de conge: " +validationObject.getUserName() +" descp: "+validationObject.validatorUsername+" has a rason"+validationObject.reason );
            Identity identity = identityManager.getOrCreateIdentity(PROVIDER_ID, validationObject.userName,
                    true);
            activityManager.saveActivityNoReturn(identity, activity);
            
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(HTTPStatus.INTERNAL_ERROR).build();
        }
    }
    



    public List<CongesAdministration> getAllCongesAdministration() throws Exception {
        try {
            ListAccess<CongesAdministration> allCongesAdministration = congesAdministrationDAO.findAllCongesAdministration();
            CongesAdministration[] result = allCongesAdministration.load(0,allCongesAdministration.getSize());
            return Arrays.asList(result);
        } catch (Exception e) {
            return new ArrayList<CongesAdministration>();
        }
    }

    public List<CongesAdministration> getCongesAdministrationByUsername(String username) {
        try {
            ListAccess<CongesAdministration> allCongesAdministration = congesAdministrationDAO.findCongesAdministrationByUser(username);
            CongesAdministration[] result = allCongesAdministration.load(0,allCongesAdministration.getSize());
            return Arrays.asList(result);
        } catch (Exception e) {
            return null;
        }

    }

    public void editCongesAdministration(CongesAdministration congesAdministration) throws Exception{
        congesAdministrationDAO.saveConges(congesAdministration);
    }

    public CongesAdministration createCongesAdministration(String username, double soldeConge,double soldeCetPerenne, double soldeCetHistorique, boolean isValidator) throws Exception {
        try {

            CongesAdministration congesAdministration=congesAdministrationDAO.createCongesInstance();
            congesAdministration.setSoldeConge(soldeConge);
            congesAdministration.setSoldeCetPerenne(soldeCetPerenne);
            congesAdministration.setSoldeCetHistorique(soldeCetHistorique);
            congesAdministration.setValidator(isValidator);
            congesAdministration.setUsername(username);
            congesAdministrationDAO.createConges(congesAdministration);

            return congesAdministration;
        } catch (Exception e) {
            return null;
        }
    }


    public static class CongesObject{
        private String userName;
        private long startDate;
        private long endDate;
        private boolean isFirstDayHalf;
        private boolean isEndDayHalf;
        private String oneDayHalf;
        private String validatorUsername;
        private double nbDays;
        private String type;
        private String reason;

        public CongesObject() {

        }
        public CongesObject(Conges conges) {
            this.userName=conges.getUserName();
            this.startDate=conges.getStartDate().getTime();
            this.endDate=conges.getEndDate().getTime();
            this.isFirstDayHalf=conges.isFirstDayHalf();
            this.isEndDayHalf=conges.isEndDayHalf();
            this.oneDayHalf=conges.getOneDayHalf();
            this.validatorUsername=conges.getValidatorUserName();
            this.nbDays=conges.getNbDays();
            this.type=conges.getType();
            this.reason=conges.getReason();
        }



        public boolean getIsFirstDayHalf() {
            return isFirstDayHalf;
        }

        public void setIsFirstDayHalf(boolean firstDayHalf) {
            isFirstDayHalf = firstDayHalf;
        }


        public String getValidatorUsername() {
            return validatorUsername;
        }

        public void setValidatorUsername(String validatorUsername) {
            this.validatorUsername = validatorUsername;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public long getStartDate() {
            return startDate;
        }

        public void setStartDate(long startDate) {
            this.startDate = startDate;
        }

        public long getEndDate() {
            return endDate;
        }

        public void setEndDate(long endDate) {
            this.endDate = endDate;
        }

        public boolean getIsEndDayHalf() {
            return isEndDayHalf;
        }

        public void setIsEndDayHalf(boolean endDayHalf) {
            isEndDayHalf = endDayHalf;
        }

        public String getOneDayHalf() {
            return oneDayHalf;
        }

        public void setOneDayHalf(String oneDayHalf) {
            this.oneDayHalf = oneDayHalf;
        }


        public double getNbDays() {
            return nbDays;
        }

        public void setNbDays(double nbDays) {
            this.nbDays = nbDays;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }


    public static class ValidatorObject {
        private String label;
        private String value;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
    public static class ValidationObject {
    	
    	public ValidationObject() {

        }
    	
		public ValidationObject(String userName, long startDate, long endDate,
				boolean isFirstDayHalf, boolean isEndDayHalf,
				String oneDayHalf, String validatorUsername, double nbDays,
				String type, String reason, boolean isApprouved,
				String refusalReason, String state,String superValidatorUsername) {
			this.userName = userName;
			this.startDate = startDate;
			this.endDate = endDate;
			this.isFirstDayHalf = isFirstDayHalf;
			this.isEndDayHalf = isEndDayHalf;
			this.oneDayHalf = oneDayHalf;
			this.validatorUsername = validatorUsername;
			this.nbDays = nbDays;
			this.type = type;
			this.reason = reason;
			this.isApprouved = isApprouved;
			this.refusalReason = refusalReason;
			this.state=state;
			this.superValidatorUsername=superValidatorUsername;
		}
		private String userName;
        private long startDate;
        private long endDate;
        private boolean isFirstDayHalf;
        private boolean isEndDayHalf;
        private String oneDayHalf;
        private String validatorUsername;
        private double nbDays;
        private String type;
        private String reason;
        private boolean isApprouved;
        private String refusalReason;
        private String state;
        private String superValidatorUsername;
        public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public long getStartDate() {
			return startDate;
		}
		public void setStartDate(long startDate) {
			this.startDate = startDate;
		}
		public long getEndDate() {
			return endDate;
		}
		public void setEndDate(long endDate) {
			this.endDate = endDate;
		}
		public boolean isFirstDayHalf() {
			return isFirstDayHalf;
		}
		public void setFirstDayHalf(boolean isFirstDayHalf) {
			this.isFirstDayHalf = isFirstDayHalf;
		}
		public boolean isEndDayHalf() {
			return isEndDayHalf;
		}
		public void setEndDayHalf(boolean isEndDayHalf) {
			this.isEndDayHalf = isEndDayHalf;
		}
		public String getOneDayHalf() {
			return oneDayHalf;
		}
		public void setOneDayHalf(String oneDayHalf) {
			this.oneDayHalf = oneDayHalf;
		}
		public String getValidatorUsername() {
			return validatorUsername;
		}
		public void setValidatorUsername(String validatorUsername) {
			this.validatorUsername = validatorUsername;
		}
		public double getNbDays() {
			return nbDays;
		}
		public void setNbDays(double nbDays) {
			this.nbDays = nbDays;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getReason() {
			return reason;
		}
		public void setReason(String reason) {
			this.reason = reason;
		}
		public boolean isApprouved() {
			return isApprouved;
		}
		public void setApprouved(boolean isApprouved) {
			this.isApprouved = isApprouved;
		}
		public String getRefusalReason() {
			return refusalReason;
		}
		public void setRefusalReason(String refusalReason) {
			this.refusalReason = refusalReason;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getSuperValidatorUsername() {
			return superValidatorUsername;
		}

		public void setSuperValidatorUsername(String superValidatorUsername) {
			this.superValidatorUsername = superValidatorUsername;
		}

    	
    }
}

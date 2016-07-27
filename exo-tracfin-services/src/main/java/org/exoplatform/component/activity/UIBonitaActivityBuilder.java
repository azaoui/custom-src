package org.exoplatform.component.activity;


import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.social.core.activity.model.ExoSocialActivity;
import org.exoplatform.social.webui.activity.BaseUIActivity;
import org.exoplatform.social.webui.activity.BaseUIActivityBuilder;

import java.util.Map;

/**
 * @author azaoui@exoplatform.com
 *
 */

public class UIBonitaActivityBuilder extends BaseUIActivityBuilder {
	  private static final Log LOG = ExoLogger.getLogger(UIBonitaActivityBuilder.class);

	  public static final String USER_NAME_PARAM = "userName";
	  public static final String VALIDATOR_NAME_PARAM = "validator";
	  public static final String REASON_PARAM = "reason";
	  public static final String STARTDATE_PARAM = "satartDate";
	  public static final String ENDDATE_PARAM="endDate";
	  public static final String NBDAYS_PARAM="nbDays";
	  public static final String TYPE_PARAM="type";
	  public static final String IS_APPROVED_PARAM="isApproved";
	  public static final String REFUSAL_REASON="refusalREason";
	  public static final String STATE="state";
	  public static final String SUPER_VALIDATOR_USER_NAME="superValidatorUsername";
	  
	  @Override
	  protected void extendUIActivity(BaseUIActivity uiActivity, ExoSocialActivity activity) {
	    UIBonitaActivity uiBonitaActivity = (UIBonitaActivity) uiActivity;
	    Map<String, String> templateParams = activity.getTemplateParams();
	    uiBonitaActivity.setUserName(templateParams.get(USER_NAME_PARAM));
	    uiBonitaActivity.setValidatorUserName(templateParams.get(VALIDATOR_NAME_PARAM));
	    uiBonitaActivity.setReason(templateParams.get(REASON_PARAM));
	    uiBonitaActivity.setStartDate(templateParams.get(STARTDATE_PARAM));
	    uiBonitaActivity.setEndDate(templateParams.get(ENDDATE_PARAM));
	    uiBonitaActivity.setNbDays(templateParams.get(NBDAYS_PARAM));
	    uiBonitaActivity.setType(templateParams.get(TYPE_PARAM));
	    uiBonitaActivity.setIsApproved(templateParams.get(IS_APPROVED_PARAM));
	    uiBonitaActivity.setRefusalReason(templateParams.get(REFUSAL_REASON));
	    uiBonitaActivity.setState(templateParams.get(STATE));
	    uiBonitaActivity.setSuperValidatorUsername(templateParams.get(SUPER_VALIDATOR_USER_NAME));
	  }
	}
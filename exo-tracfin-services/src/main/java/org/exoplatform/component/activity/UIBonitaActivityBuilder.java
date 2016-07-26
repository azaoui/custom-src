package org.exoplatform.component.activity;


import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.social.core.activity.model.ExoSocialActivity;
import org.exoplatform.social.webui.activity.BaseUIActivity;
import org.exoplatform.social.webui.activity.BaseUIActivityBuilder;

import java.util.Map;

public class UIBonitaActivityBuilder extends BaseUIActivityBuilder {
	  private static final Log LOG = ExoLogger.getLogger(UIBonitaActivityBuilder.class);

	  public static final String USER_NAME_PARAM = "description";
	  public static final String VALIDATOR_NAME_PARAM = "validator";
	  public static final String REASON_PARAM = "reason";
	  public static final String STARTDATE_PARAM = "satartDate";
	  public static final String ENDDATE_PARAM="endDate";
	  public static final String NBDAYS_PARAM="nbDays";
	  public static final String TYPE_PARAM="type";

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
	  }
	}
package org.exoplatform.component.activity;

import org.exoplatform.social.webui.activity.BaseUIActivity;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;


@ComponentConfig(
        lifecycle = UIFormLifecycle.class,
        template = "classpath:groovy/social/webui/activity/UIBonitaActivity.gtmpl",
        events = {
        @EventConfig(listeners = BaseUIActivity.ToggleDisplayCommentFormActionListener.class),
        @EventConfig(listeners = BaseUIActivity.LikeActivityActionListener.class),
        @EventConfig(listeners = BaseUIActivity.LoadLikesActionListener.class),
        @EventConfig(listeners = BaseUIActivity.SetCommentListStatusActionListener.class),
        @EventConfig(listeners = BaseUIActivity.PostCommentActionListener.class),
        @EventConfig(listeners = BaseUIActivity.DeleteActivityActionListener.class),
        @EventConfig(listeners = BaseUIActivity.DeleteCommentActionListener.class)
}
)
public class UIBonitaActivity extends BaseUIActivity {

	  public static final String ACTIVITY_TYPE = "BONITA_ACTIVITY";

	    private String userName = "";
	    private String validatorUserName = "";
	    private String reason = "";
	    private String startDate = "";
	    private String endDate = "";
	    private String nbDays = "";
	    private String type = "";

	    
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getValidatorUserName() {
			return validatorUserName;
		}
		public void setValidatorUserName(String validatorUserName) {
			this.validatorUserName = validatorUserName;
		}
		public String getReason() {
			return reason;
		}
		public void setReason(String reason) {
			this.reason = reason;
		}
		public String getStartDate() {
			return startDate;
		}
		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}
		public String getEndDate() {
			return endDate;
		}
		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}
		public String getNbDays() {
			return nbDays;
		}
		public void setNbDays(String nbDays) {
			this.nbDays = nbDays;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}


	}
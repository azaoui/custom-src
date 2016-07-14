package org.exoplatform.hibernate.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;


/**
 * Created by Romain Dénarié (romain.denarie@exoplatform.com) on 06/04/16.
 */

@Entity
public class Conges {


    @Id
    @GeneratedValue
    private Long id;

    private String userName;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    private boolean isFirstDayHalf;

    private boolean isEndDayHalf;

    private String oneDayHalf;

    private double nbDays;

    private String validatorUserName;

    private String type;

    private String reason;

    public Conges() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date  getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isFirstDayHalf() {
        return isFirstDayHalf;
    }

    public void setFirstDayHalf(boolean firstDayHalf) {
        isFirstDayHalf = firstDayHalf;
    }

    public boolean isEndDayHalf() {
        return isEndDayHalf;
    }

    public void setEndDayHalf(boolean endDayHalf) {
        isEndDayHalf = endDayHalf;
    }

    public String getOneDayHalf() {
        return oneDayHalf;
    }

    public void setOneDayHalf(String oneDayHalf) {
        this.oneDayHalf = oneDayHalf;
    }

    public String getValidatorUserName() {
        return validatorUserName;
    }

    public void setValidatorUserName(String validatorUserName) {
        this.validatorUserName = validatorUserName;
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

    public double getNbDays() {
        return nbDays;
    }
}

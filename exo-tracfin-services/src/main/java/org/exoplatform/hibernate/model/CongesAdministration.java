package org.exoplatform.hibernate.model;

import java.util.Date;

import javax.persistence.*;


/**
 * Created by Romain Dénarié (romain.denarie@exoplatform.com) on 06/04/16.
 */

@Entity
public class CongesAdministration {


    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private boolean isValidator;

    private double remainingDays;



    public boolean isValidator() {
        return isValidator;
    }

    public void setValidator(boolean validator) {
        isValidator = validator;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getRemainingDays() {
        return remainingDays;
    }

    public void setRemainingDays(double remainingDays) {
        this.remainingDays = remainingDays;
    }
}

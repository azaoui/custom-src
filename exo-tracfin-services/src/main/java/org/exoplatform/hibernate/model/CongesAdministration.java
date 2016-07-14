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

    private double soldeConge;
    
    private double soldeCetPerenne;
    
    private double soldeCetHistorique;



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

    public double getSoldeConge() {
        return soldeConge;
    }

    public void setSoldeConge(double soldeConge) {
        this.soldeConge = soldeConge;
    }

	public double getSoldeCetPerenne() {
		return soldeCetPerenne;
	}

	public void setSoldeCetPerenne(double soldeCetPerenne) {
		this.soldeCetPerenne = soldeCetPerenne;
	}

	public double getSoldeCetHistorique() {
		return soldeCetHistorique;
	}

	public void setSoldeCetHistorique(double soldeCetHistorique) {
		this.soldeCetHistorique = soldeCetHistorique;
	}

}

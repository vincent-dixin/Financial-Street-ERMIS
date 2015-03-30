package com.fhd.risk.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fhd.fdc.commons.orm.hibernate.IdEntity;

@Entity
@Table(name = "T_RM_RISKS_RISKS")
public class RiskRelaRisk extends IdEntity implements Serializable{

    private static final long serialVersionUID = -6468045549242686513L;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RISK_ID")
    private Risk risk;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RELA_RISK_ID")
    private Risk relaRisk;
    
    @Column(name = "ETYPE")
    private String type;
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Risk getRisk() {
        return risk;
    }

    public void setRisk(Risk risk) {
        this.risk = risk;
    }

    public Risk getRelaRisk() {
        return relaRisk;
    }

    public void setRelaRisk(Risk relaRisk) {
        this.relaRisk = relaRisk;
    }

    

}

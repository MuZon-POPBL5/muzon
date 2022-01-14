package com.muZon.aplicacion.entity;

import java.io.Serializable;
import java.util.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
public class GrafanaMetrics implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Integer id;

    @Temporal(TemporalType.TIMESTAMP)
	private Date click_date;

    public Date getSqlTimeStamp(Date click_date) {
        return click_date;
    }

    public void setSqlTimestamp(Date click_date) {
        this.click_date = click_date;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((click_date == null) ? 0 : click_date.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GrafanaMetrics other = (GrafanaMetrics) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (click_date == null) {
            if (other.click_date != null)
                return false;
        } else if (!click_date.equals(other.click_date))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "grafana_metrics [id=" + id + ", click_date" + click_date + "]";
    }


 


   


}

package com.muZon.aplicacion.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Product implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6353963609310956029L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String category;

    @Lob
    @Column
    private String imgSrc;

    @Column
    private float price;

    @Column
    private int quantity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sellerId", referencedColumnName = "id")
    private User sellerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public User getSellerId() {
        return sellerId;
    }

    public void setSellerId(User sellerId) {
        this.sellerId = sellerId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((imgSrc == null) ? 0 : imgSrc.hashCode());
        result = prime * result + ((quantity == 0) ? 0 : quantity);
        result = prime * result + ((price == 0) ? 0 : Math.round(price));
        result = prime * result + ((sellerId == null) ? 0 : sellerId.hashCode());
        result = prime * result + ((category == null) ? 0 : category.hashCode());
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
        Product other = (Product) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (imgSrc == null) {
            if (other.imgSrc != null)
                return false;
        } else if (!imgSrc.equals(other.imgSrc))
            return false;
        if (quantity == 0) {
            if (other.quantity != -1)
                return false;
        } else if (quantity != (other.quantity))
            return false;
        if (price == 0) {
            if (other.price != -1)
                return false;
        } else if (price != (other.price))
            return false;
        if (sellerId == null) {
            if (other.sellerId != null)
                return false;
        } else if (!sellerId.equals(other.sellerId))
            return false;
        if (category == null) {
            if (other.category != null)
                return false;
        } else if (!category.equals(other.category))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Role [id=" + id + ", name=" + name + ", description=" + description +
                ", imgSrc=" + imgSrc + ", quantity=" + quantity + ", price=" + price
                + ", sellerId=" + sellerId + ", category=" + category + "]";
    }
}


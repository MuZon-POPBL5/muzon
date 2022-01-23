package com.muZon.aplicacion.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Cart implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6353963609310956029L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @Column
    private float price;

    @Column
    private int quantity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boughtId", referencedColumnName = "id")
    private User boughtId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId", referencedColumnName = "id")
    private Product productId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public User getBoughtId() {
        return boughtId;
    }

    public void setBoughtId(User boughtId) {
        this.boughtId = boughtId;
    }

    public Product getProductId() {
        return productId;
    }

    public void setProductId(Product productId) {
        this.productId = productId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((quantity == 0) ? 0 : quantity);
        result = prime * result + ((price == 0) ? 0 : Math.round(price));
        result = prime * result + ((boughtId == null) ? 0 : boughtId.hashCode());
        result = prime * result + ((productId == null) ? 0 : productId.hashCode());
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
        Cart other = (Cart) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
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
        if (boughtId == null) {
            if (other.boughtId != null)
                return false;
        } else if (!boughtId.equals(other.boughtId))
            return false;
        if (productId == null) {
            if (other.productId != null)
                return false;
        } else if (!productId.equals(other.productId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Cart [id=" + id + ", boughtId=" + boughtId + ", productId=" + productId + 
        ", quantity=" + quantity + ", price=" + price + "]";
    }
}


package com.sumit.retailapis.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.PrePersist;
import org.mongodb.morphia.utils.IndexDirection;

import java.util.Date;

/**
 * Created by sumitb_mdi on 10/31/15.
 */


@JsonIgnoreProperties("difference")
@Entity(value = "item", noClassnameStored = true)  //by default the fully classified class name is added in the
                                                   //JSON notation. (Which is used in creating POJO from JSON; in
                                                   //complex inheritance cases.)
public class Item {

    //Properties
    @Id
    @JsonProperty
    private ObjectId id;

    @JsonProperty
    private String title;

    @JsonProperty
    private float price;

    @JsonProperty("purchase_year")
    private int purchaseYear;

    @JsonProperty("original_price")
    private float originalPrice;

    @Indexed(
        name = "locality",
        value = IndexDirection.GEO2D
    )
    @JsonProperty
    private float[] locality;
    //To Supply [latitude, longitude] in this field.
    //locality field is intentionally being hardly embedded
    //into this class; to use the geo-spatial power of mongodb.


    @JsonProperty("pics_link")
    private String[] picsLink;

    @JsonProperty
    private Date createdAt;

    @JsonProperty
    private Date lastUpdatedAt;


    @JsonProperty
    private float difference;  // origninalPrice - price


    //Getters and Setters
    public String getId() {
        return id.toString();
    }

    public void setId(String id) {
        this.id = new ObjectId(id);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getPurchaseYear() {
        return purchaseYear;
    }

    public void setPurchaseYear(int purchaseYear) {
        this.purchaseYear = purchaseYear;
    }

    public float getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(float originalPrice) {
        this.originalPrice = originalPrice;
    }

    public float[] getLocality() {
        return locality;
    }

    public void setLocality(float[] locality) {
        this.locality = locality;
    }

    public String[] getPicsLink() {
        return picsLink;
    }

    public void setPicsLink(String[] picsLink) {
        this.picsLink = picsLink;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(Date lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public float getDifference() { return difference; }



    //Other methods
    @PrePersist
    public void prePersist() {
        this.createdAt = (createdAt == null) ? new Date() : createdAt;
        this.lastUpdatedAt = (lastUpdatedAt == null) ? createdAt : new Date();

        this.difference = this.originalPrice - this.price;
    }

}

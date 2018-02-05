/**
 * Copyright (c) 2018. By Riya for CMPUT 301 as Assignment 1
 */

package com.example.riyariya.riya1_subbook;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Holds information about the subscription
 * @author riya1
 * @version 1.0
 */
public class Subscription {
    private String name; //textual, up to 20 characters implemented in layout
    private Date dateStarted;
    private BigDecimal monthlyCharge; //non-negative currency value implemented in layout
    private String comment; //textual, up to 30 characters implemented in layout

    Subscription(String name, Date dateStarted, BigDecimal monthlyCharge,String comment){
        this.name = name;
        this.dateStarted = dateStarted;
        this.monthlyCharge = monthlyCharge.setScale(2, RoundingMode.HALF_EVEN);
        this.comment = comment;
    }

    /**
     * Getter for name
     * @return
     */
    public String getName(){
        return name;
    }

    /**
     * Setter for name
     * @param name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Getter for comment
     * @return string
     */
    public String getComment(){
        return comment;
    }

    /**
     * Setter for comment
     * @param comment
     */
    public void setComment(String comment){
        this.comment = comment;
    }

    /**
     * Getter for dateStarted
     * @return date
     */
    public Date getDateStarted(){
        return dateStarted;
    }

    /**
     * Setter for dateStarted
     * @param dateStarted
     */
    public void setDateStarted(Date dateStarted){
        this.dateStarted = dateStarted;
    }

    /**
     * Getter for monthlyCharge
     * @return BigDecimal
     */
    public BigDecimal getMonthlyCharge(){
        return monthlyCharge;
    }

    /**
     * Setter for monthlyCharge
     * @param monthlyCharge
     */
    public void setMonthlyCharge(BigDecimal monthlyCharge){
        this.monthlyCharge = monthlyCharge.setScale(2, RoundingMode.HALF_EVEN);;
    }

    /**
     * Displays the summary of the subscription
     * @return
     */
    @Override
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return name + "| "+ format.format(dateStarted).toString() + "| " + monthlyCharge.toString();
    }
}

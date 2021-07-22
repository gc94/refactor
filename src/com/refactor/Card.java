package com.refactor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class Card {

    private Long id;
    private String iban;
    private String pin;
    private boolean active = true;
    private String type = "BRONZE"; // SILVER / GOLD / PLATINUM
    private String typeDescription; // related to type ^
    private Date dateCreated;
    private BigDecimal balance;
    private String currency = "RON"; // EUR / USD / CHF
    private Set<String> virtualIbans; // used only for PLATINUM cards

    public Card() {

    }

    public void deactivate() {
        if("BRONZE".equals(type)) {
            System.out.println("This feature is not supported by BRONZE cards!");
        } else {
            active = false;
        }
    }

    public void createVirtualIban() {
        if(!"PLATINUM".equals(type)) {
            System.out.println("This feature is only for PLATINUM cards");
        } else {
            String iban = UUID.randomUUID().toString();
            virtualIbans.add(iban);
            System.out.println("Your virtual iban has been created: " + iban);
        }
    }

    //GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Set<String> getVirtualIbans() {
        return virtualIbans;
    }

    public void setVirtualIbans(Set<String> virtualIbans) {
        this.virtualIbans = virtualIbans;
    }
}

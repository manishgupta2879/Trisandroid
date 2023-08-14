package com.pcs.tim.myapplication;

public class PartialViewCardStatus {
    private String registerDate;
    private String collectCardDate ;
    private String cardExpiredDate;
    private String cardStatus ;

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getCollectCardDate() {
        return collectCardDate;
    }

    public void setCollectCardDate(String collectCardDate) {
        this.collectCardDate = collectCardDate;
    }

    public String getCardExpiredDate() {
        return cardExpiredDate;
    }

    public void setCardExpiredDate(String cardExpiredDate) {
        this.cardExpiredDate = cardExpiredDate;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }
}

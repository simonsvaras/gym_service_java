package com.gym.gymmanagementsystem.dto;


import lombok.Getter;

@Getter
public class CardResponse {

    public enum CardStatus {
        NOT_REGISTERED,
        UNASSIGNED,
        ASSIGNED
    }

    private CardStatus status;
    private Integer userID; // bude null pokud není přiřazena

    public CardResponse() {}

    public CardResponse(CardStatus status, Integer userID) {
        this.status = status;
        this.userID = userID;
    }

    public void setStatus(CardStatus status) {
        this.status = status;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }
}
package model;

import java.time.LocalDate;

/**
 * Created by computer on 13.11.2017.
 */
public class Card {
    private Long cardId;
    private String cardType;
    private String cardNumber;
    private LocalDate cardDate;
    private Long userId;
    private Long accountId;

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public LocalDate getCardDate() {
        return cardDate;
    }

    public void setCardDate(LocalDate cardDate) {
        this.cardDate = cardDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}

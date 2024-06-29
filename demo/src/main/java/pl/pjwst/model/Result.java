package pl.pjwst.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Result {
    @Id
    @GeneratedValue
    private Long id;
    private String currency;
    private String currencyCode;
    private double avgRate;
    private int days;
    private LocalDateTime requestDate;

    public Result(String currency, String currencyCode, double avgRate, int days, LocalDateTime requestDate) {
        this.currency = currency;
        this.currencyCode = currencyCode;
        this.avgRate = avgRate;
        this.days = days;
        this.requestDate = requestDate;
    }

    public Result() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public double getAvgRate() {
        return avgRate;
    }

    public void setAvgRate(double avgRate) {
        this.avgRate = avgRate;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }
}


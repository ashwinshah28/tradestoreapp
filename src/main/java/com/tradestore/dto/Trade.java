package com.tradestore.dto;

import java.time.LocalDate;
import java.util.Objects;

public class Trade implements Comparable<Trade> {
    private String tradeId;
    private Integer version;
    private String counterPartyId;
    private String bookId;
    private LocalDate maturityDate;
    private LocalDate createdDate;
    private Character expired;

    public Trade() {
    }

    public Trade(String tradeId, Integer version, String counterPartyId, String bookId, LocalDate maturityDate, LocalDate createdDate, Character expired) {
        this.tradeId = tradeId;
        this.version = version;
        this.counterPartyId = counterPartyId;
        this.bookId = bookId;
        this.maturityDate = maturityDate;
        this.createdDate = createdDate;
        this.expired = expired;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(final String tradeId) {
        this.tradeId = tradeId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(final Integer version) {
        this.version = version;
    }

    public String getCounterPartyId() {
        return counterPartyId;
    }

    public void setCounterPartyId(final String counterPartyId) {
        this.counterPartyId = counterPartyId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(final String bookId) {
        this.bookId = bookId;
    }

    public LocalDate getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(final LocalDate maturityDate) {
        this.maturityDate = maturityDate;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Character getExpired() {
        return expired;
    }

    public boolean isExpired() {
        return (this.expired == 'Y' || this.expired == 'y');
    }

    public void setExpired(final Character expired) {
        this.expired = expired;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "tradeId='" + tradeId + '\'' +
                ", version=" + version +
                ", counterPartyId='" + counterPartyId + '\'' +
                ", bookId='" + bookId + '\'' +
                ", maturityDate=" + maturityDate +
                ", createdDate=" + createdDate +
                ", expired=" + expired +
                '}';
    }


    @Override
    public int compareTo(Trade t) {
        int compareValue = this.getTradeId().compareTo(t.getTradeId());
        if (compareValue != 0) {
            return compareValue;
        }
        compareValue = t.getVersion().compareTo(this.getVersion());
        return compareValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tradeId, version);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trade trade = (Trade) o;
        return Objects.equals(tradeId, trade.tradeId) && Objects.equals(version, trade.version);
    }
}

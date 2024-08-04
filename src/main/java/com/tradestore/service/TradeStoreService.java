package com.tradestore.service;

import com.tradestore.dto.Trade;
import com.tradestore.exception.BusinessValidationException;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

public class TradeStoreService {
    private final TreeSet<Trade> tradeStore = new TreeSet<>((Trade t1, Trade t2) -> {
        int compareValue = t1.getTradeId().compareTo(t2.getTradeId());
        if (compareValue != 0) {
            return compareValue;
        }
        compareValue = t2.getVersion().compareTo(t1.getVersion());
        return compareValue;
    });

    public void addTradeOnInit(List<Trade> tradeList) {
        tradeStore.addAll(tradeList);
    }

    public void printTradeStoreContents() {
        tradeStore.forEach(System.out::println);
    }

    public void addOrUpdateTradeList(List<Trade> tradeList) {
        for (int index = 0; index < tradeList.size(); index++) {
            Trade trade = tradeList.get(index);
            try {
                System.out.println("\nProcessing Trade Record: " + (index + 1));
                addOrUpdateTrade(trade);
                System.out.println("Trade Record at Count: " + (index + 1) + " is added/updated.");
            } catch (BusinessValidationException e) {
                System.out.println("Trade Record at Count: " + +(index + 1) + " cannot be added/updated." + e.getMessage());
            }
        }
    }

    public void addOrUpdateTrade(Trade trade) throws BusinessValidationException {
        applyBusinessValidation(trade);
        Trade existingTrade = searchTrade(tradeStore, trade);
        if (existingTrade == null) {
            System.out.println("Adding new trade." + trade);
            tradeStore.add(trade);
        } else {
            //override existing trade attributes, assuming created date stays same.
            existingTrade.setCounterPartyId(trade.getCounterPartyId());
            existingTrade.setBookId(trade.getBookId());
            existingTrade.setMaturityDate(trade.getMaturityDate());
            existingTrade.setExpired(trade.getExpired());
            System.out.println("Overriding Existing trade: " + existingTrade + " with new trade details: " + trade);
        }
    }

    private void applyBusinessValidation(Trade trade) throws BusinessValidationException {
        LocalDate todayDate = LocalDate.now();
        if (trade.getMaturityDate().isBefore(todayDate)) {
            throw new BusinessValidationException("Error: Received trade has maturity date in the past." + trade);
        }
    }

    public Trade searchTrade(TreeSet<Trade> tradeStore, Trade searchTrade) throws BusinessValidationException {

        for (Trade trade : tradeStore) {
            if (trade.getTradeId().equals(searchTrade.getTradeId())) {
                if (trade.getVersion() > searchTrade.getVersion()) {
                    System.out.println("Stale trade received cannot process.");
                    throw new BusinessValidationException("Error: Stale trade received. Trade Version is less than the latest trade in the store." + searchTrade);
                } else if (Objects.equals(trade.getVersion(), searchTrade.getVersion())) {
                    System.out.println("Trade exists." + searchTrade);
                    return trade;
                }
            }
        }
        System.out.println("Trade does not exists." + searchTrade);
        return null;
    }

    public void updateMaturityExpiredTrades() {
        boolean tradesUpdated = false;
        for (Trade trade : tradeStore) {
            if (trade.getMaturityDate().isBefore(LocalDate.now()) && !trade.isExpired()) {
                trade.setExpired('Y');
                System.out.println("Trade Maturity Date is in past, updated trade as expired. " + trade);
                tradesUpdated = true;
            }
        }
        if (tradesUpdated) {
            System.out.println("\nDo you want to continue (Type y or n) \n");
        }
    }

    public List<Trade> getAllTrades() {
        return tradeStore.stream().toList();
    }
}

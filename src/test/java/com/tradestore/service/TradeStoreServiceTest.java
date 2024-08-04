package com.tradestore.service;

import com.tradestore.dto.Trade;
import com.tradestore.exception.BusinessValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TradeStoreServiceTest {

    final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    TradeStoreService tradeStoreService;
    Trade trade1 = null;
    Trade trade2 = null;
    Trade trade3 = null;
    Trade trade4 = null;
    Trade trade5 = null;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        tradeStoreService = new TradeStoreService();
        trade1 = new Trade("T1", 1, "CP-1", "B1", LocalDate.parse("08/02/2024", dtf), LocalDate.parse("08/02/2024", dtf), 'N');
        trade2 = new Trade("T1", 2, "CP-2", "B2", LocalDate.parse("08/03/2024", dtf), LocalDate.parse("08/03/2024", dtf), 'N');
        trade3 = new Trade("T1", 3, "CP-3", "B3", LocalDate.now(), LocalDate.now(), 'N');
        trade4 = new Trade("T2", 1, "CP-2", "B4", LocalDate.now(), LocalDate.now(), 'N');
        trade5 = new Trade("T2", 3, "CP-2", "B5", LocalDate.now(), LocalDate.now(), 'N');

        List<Trade> tradeList = new ArrayList<>();
        tradeList.add(trade1);
        tradeList.add(trade2);
        tradeList.add(trade3);
        tradeList.add(trade4);
        tradeList.add(trade5);
        tradeStoreService.addTradeOnInit(tradeList);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {

    }

    @Test
    void addOrUpdateNewTradeHappyScenario() {
        Trade trade = new Trade("T5", 1, "CP-2", "B6", LocalDate.now(), LocalDate.now(), 'N');
        Assertions.assertDoesNotThrow(() -> tradeStoreService.addOrUpdateTrade(trade));
        List<Trade> tradeList = tradeStoreService.getAllTrades();
        boolean matchFound = false;
        for (Trade t1 : tradeList) {
            if (trade.equals(t1)) {

                assertTrue(trade.getCounterPartyId().equals(t1.getCounterPartyId()) && trade.getBookId().equals(t1.getBookId())
                        && trade.isExpired() == t1.isExpired() && trade.getMaturityDate().equals(t1.getMaturityDate()) && trade.getCreatedDate().equals(t1.getCreatedDate()));
                matchFound = true;
            }
        }
        assertTrue(matchFound);
    }

    @Test
    void overrideExistingTrade() {
        Trade trade = new Trade("T1", 3, "CP-4", "B6", LocalDate.now(), LocalDate.now(), 'N');
        Assertions.assertDoesNotThrow(() -> tradeStoreService.addOrUpdateTrade(trade));
        List<Trade> tradeList = tradeStoreService.getAllTrades();
        boolean matchFound = false;
        for (Trade t1 : tradeList) {
            if (trade.equals(t1)) {

                assertTrue(trade.getCounterPartyId().equals(t1.getCounterPartyId()) && trade.getBookId().equals(t1.getBookId())
                        && trade.isExpired() == t1.isExpired() && trade.getMaturityDate().equals(t1.getMaturityDate()) && trade.getCreatedDate().equals(t1.getCreatedDate()));
                matchFound = true;
            }
        }
        assertTrue(matchFound);
    }

    @Test
    void testUpdateMaturityExpiredTrades() {
        tradeStoreService.updateMaturityExpiredTrades();
        List<Trade> tradeList = tradeStoreService.getAllTrades();
        boolean matchFound = false;
        for (Trade t1 : tradeList) {
            if (trade1.equals(t1)) {
                matchFound = true;
                assertTrue(t1.isExpired());
            }
        }
        assertTrue(matchFound);
    }

    @Test
    void addOrUpdateTradeList() {

        Trade staleTrade = new Trade("T2", 2, "CP-2", "B6", LocalDate.now(), LocalDate.now(), 'N');
        Trade trade = new Trade("T4", 2, "CP-2", "B6", LocalDate.now(), LocalDate.now(), 'N');

        List<Trade> newTradeList = new ArrayList<>();
        newTradeList.add(staleTrade);
        newTradeList.add(trade);

        tradeStoreService.addOrUpdateTradeList(newTradeList);
        List<Trade> tradeList = tradeStoreService.getAllTrades();
        boolean matchFound = false;
        for (Trade t1 : tradeList) {
            if (trade.equals(t1)) {
                matchFound = true;
                assertTrue(trade.getCounterPartyId().equals(t1.getCounterPartyId()) && trade.getBookId().equals(t1.getBookId())
                        && trade.isExpired() == t1.isExpired() && trade.getMaturityDate().equals(t1.getMaturityDate()) && trade.getCreatedDate().equals(t1.getCreatedDate()));
            }
        }
        assertTrue(matchFound);

    }

    @Test
    void exceptionTestingOldVersion() {
        Trade trade = new Trade("T2", 2, "CP-2", "B6", LocalDate.now(), LocalDate.now(), 'N');
        Exception exception = assertThrows(BusinessValidationException.class, () -> tradeStoreService.addOrUpdateTrade(trade));
        assertEquals("Error: Stale trade received. Trade Version is less than the latest trade in the store." + trade, exception.getMessage());
    }

    @Test
    void exceptionTestingMaturityDateLessThanCurrentDate() {
        Trade trade = new Trade("T5", 2, "CP-2", "B6", LocalDate.parse("08/02/2024", dtf), LocalDate.parse("08/02/2024", dtf), 'N');
        Exception exception = assertThrows(BusinessValidationException.class, () -> tradeStoreService.addOrUpdateTrade(trade));
        assertEquals("Error: Received trade has maturity date in the past." + trade, exception.getMessage());
    }

    @Test
    void printTradeList() {
        Assertions.assertDoesNotThrow(() -> tradeStoreService.printTradeStoreContents());
    }
}

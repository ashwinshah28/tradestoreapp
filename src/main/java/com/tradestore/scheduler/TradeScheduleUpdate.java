package com.tradestore.scheduler;

import com.tradestore.service.TradeStoreService;

import java.util.TimerTask;

public class TradeScheduleUpdate extends TimerTask {

    TradeStoreService tradeStore;

    public TradeScheduleUpdate(TradeStoreService tradeStore) {
        this.tradeStore = tradeStore;
    }

    @Override
    public void run() {
        tradeStore.updateMaturityExpiredTrades();
    }
}

package com.tradestore;

import com.tradestore.consumer.TradeConsumer;
import com.tradestore.dto.Trade;
import com.tradestore.scheduler.TradeScheduleUpdate;
import com.tradestore.service.TradeStoreService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;

/* This is the main java application class for TradeStoreApplication */
public class TradeStoreApplication {
    TradeStoreService tradeStore = new TradeStoreService();
    TradeConsumer tradeConsumer = new TradeConsumer();

    public static void main(String[] args) {
        TradeStoreApplication tradeStoreApplication = new TradeStoreApplication();
        tradeStoreApplication.init();

        Scanner scan = new Scanner(System.in);
        char ch;
        do {
            System.out.println("\nTrade Store Application Menu\n");
            System.out.println("1. Add Trades");
            System.out.println("2. Print Trades ");
            System.out.println("3. Exit Application ");
            System.out.print("Please select your option: ");
            int choice = scan.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Specify file path with trades to be added: ");
                    String filepath = scan.next();
                    List<Trade> tradeList = tradeStoreApplication.tradeConsumer.readTradesFromFile(filepath);
                    if (tradeList != null) {
                        System.out.println("Processing trade file....");
                        tradeStoreApplication.tradeStore.addOrUpdateTradeList(tradeList);
                    }
                    break;
                case 2:
                    tradeStoreApplication.tradeStore.printTradeStoreContents();
                    break;
                case 3:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Wrong Entry \n ");
                    break;
            }
            System.out.println("\nDo you want to continue (Type y or n) \n");
            ch = scan.next().charAt(0);
        } while (ch == 'Y' || ch == 'y');

        System.out.println("Exiting Main Menu. Demon Timer Task continues to run in the background. Kindly please terminate the application.");
    }

    private void init() {

        System.out.println("Initializing Trade Store Application");
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        //Dummy trades for representation
        Trade trade1 = new Trade("T1", 1, "CP-1", "B1", LocalDate.parse("08/02/2024", dtf), LocalDate.parse("08/02/2024", dtf), 'N');
        Trade trade2 = new Trade("T1", 2, "CP-2", "B2", LocalDate.parse("08/03/2024", dtf), LocalDate.parse("08/03/2024", dtf), 'N');
        Trade trade3 = new Trade("T1", 3, "CP-3", "B3", LocalDate.now(), LocalDate.now(), 'N');
        Trade trade4 = new Trade("T2", 1, "CP-2", "B4", LocalDate.now(), LocalDate.now(), 'N');
        Trade trade5 = new Trade("T2", 3, "CP-2", "B5", LocalDate.now(), LocalDate.now(), 'N');

        List<Trade> tradeList = new ArrayList<>();
        tradeList.add(trade1);
        tradeList.add(trade2);
        tradeList.add(trade3);
        tradeList.add(trade4);
        tradeList.add(trade5);

        tradeStore.addTradeOnInit(tradeList);
        tradeStore.printTradeStoreContents();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TradeScheduleUpdate(tradeStore), 120000, 120000);
    }
}
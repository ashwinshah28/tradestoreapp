package com.tradestore.consumer;

import com.tradestore.dto.Trade;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/* Java class to consume trades from a file.
File format: tradeId | version | counterPartyId | bookId | maturityDate | createdDate | expired
*/
public class TradeConsumer {

    final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public List<Trade> readTradesFromFile(String filePath) {
        List<Trade> tradeList = new ArrayList<>();
        int recordCount = 1;
        try {
            File file = new File(filePath);
            if (file.isFile()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = null;

                // read file line by line
                while ((line = br.readLine()) != null) {

                    // split the line by delimiter |
                    String[] parts = line.split("\\|");

                    String tradeId = parts[0].trim();
                    String version = parts[1].trim();
                    String counterPartyId = parts[2].trim();
                    String bookId = parts[3].trim();
                    String maturityDate = parts[4].trim();
                    String createdDate = parts[5].trim();
                    String expired = parts[6].trim();
                    Trade trade = new Trade();
                    trade.setTradeId(tradeId);
                    trade.setVersion(Integer.parseInt(version));
                    trade.setCounterPartyId(counterPartyId);
                    trade.setBookId(bookId);
                    trade.setMaturityDate(getDate(maturityDate));
                    trade.setCreatedDate(getDate(createdDate));
                    trade.setExpired(expired.charAt(0));
                    tradeList.add(trade);
                    recordCount++;
                }
                return tradeList;
            } else {
                System.out.println("Error adding trades. Invalid file path specified");
            }
        } catch (Exception exception) {
            System.out.println("Error reading trade file. Record Count:  " + recordCount + " Error Message: " + exception.getMessage());
        }
        return null;
    }

    private LocalDate getDate(String dateStr) {
        return LocalDate.parse(dateStr, dtf);
    }
}

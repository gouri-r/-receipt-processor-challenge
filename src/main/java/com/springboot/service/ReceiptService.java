package com.springboot.service;

import com.springboot.model.Receipt;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service

public class ReceiptService {

    private final Map<String, Integer> receiptPoints = new HashMap<>();

    public String processReceipt(Receipt receipt) {
        String id = UUID.randomUUID().toString();
        int points = calculatePoints(receipt);
        receiptPoints.put(id, points);
        System.out.println(receiptPoints);
        return id;
    }

    public int getPoints(String id) {
    	 System.out.println("get method"+receiptPoints);
    	 System.out.println(id);
        return receiptPoints.getOrDefault(id, 0);
    }

    private int calculatePoints(Receipt receipt) {
        int points = 0;

        // Rule 1: One point for every alphanumeric character in the retailer name.
        String retailer = receipt.getRetailer();
        for (char c : retailer.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                points++;
            }
        }
        System.out.println("Points after retailer name rule: " + points);

        // Rule 2: 50 points if the total is a round dollar amount with no cents.
        double total = Double.parseDouble(receipt.getTotal());
        if (total == Math.floor(total)) {
            points += 50;
        }
        System.out.println("Points after round dollar amount rule: " + points);

        // Rule 3: 25 points if the total is a multiple of 0.25.
        if (total % 0.25 == 0) {
            points += 25;
        }
        System.out.println("Points after multiple of 0.25 rule: " + points);

        // Rule 4: 5 points for every two items on the receipt.
        int itemCount = receipt.getItems().size();
        points += (itemCount / 2) * 5;
        System.out.println("Points after item count rule: " + points);

        // Rule 5: If the trimmed length of the item description is a multiple of 3,
        // multiply the price by 0.2 and round up to the nearest integer.
        for (Receipt.Item item : receipt.getItems()) {
            String description = item.getShortDescription().trim();
            if (description.length() % 3 == 0) {
                double price = Double.parseDouble(item.getPrice());
                points += Math.ceil(price * 0.2);
            }
        }
        System.out.println("Points after item description rule: " + points);

        // Rule 6: 6 points if the day in the purchase date is odd.
        String[] dateParts = receipt.getPurchaseDate().split("-");
        int day = Integer.parseInt(dateParts[2]);
        if (day % 2 != 0) {
            points += 6;
        }
        System.out.println("Points after odd day rule: " + points);

        // Rule 7: 10 points if the time of purchase is after 2:00pm and before 4:00pm.
        String[] timeParts = receipt.getPurchaseTime().split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);
        if (hour == 14 || (hour == 15 && minute < 60)) {
            points += 10;
        }
        System.out.println("Points after time of purchase rule: " + points);

        return points;
    }
}

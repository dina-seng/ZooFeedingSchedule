package com.zoo.exceptions;

import java.time.format.DateTimeFormatter;

public class ZooException extends Exception {
    
    private String timestamp; 

    // Constructor
    public ZooException(String message) {
        // Call the parent constructor to set the message
        super(message);
        this.timestamp = java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); // Capture the time of the exception

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ZooException{");
        sb.append("timestamp=").append(timestamp);
        sb.append('}');
        return sb.toString();
    }
    

}

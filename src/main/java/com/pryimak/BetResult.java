package com.pryimak;

import lombok.Data;

@Data
public class BetResult {
    private String message;
    private Account account;
    private long realNumber;

    @Override
    public String toString() {
        return "BetResult{" +
                "message='" + message + '\'' +
                ", account=" + account +
                ", realNumber=" + realNumber +
                '}';
    }
}

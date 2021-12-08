package com.pryimak;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Account {
    private String id;
    private double money;
    private String deletionTime;
}

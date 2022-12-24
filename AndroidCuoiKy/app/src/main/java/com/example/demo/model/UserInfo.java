package com.example.demo.model;

public class UserInfo {
    private String Name,Key;
    private Float Balance;
    public UserInfo() {
    }

    public UserInfo(String name, String key, Float balance) {
        Name = name;
        Key = key;
        Balance = balance;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public Float getBalance() {
        return Balance;
    }

    public void setBalance(Float balance) {
        Balance = balance;
    }
}

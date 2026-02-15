package com.example.backgammonfinal;

public class Player {
    private String username;
    private long points;

    // חובה: קונסטרקטור ריק עבור Firebase
    public Player() {}

    public Player(String username, long points) {
        this.username = username;
        this.points = points;
    }

    // ה-Getters חייבים להתאים בדיוק לשמות השדות ב-Firebase
    public String getUsername()
    {
        return username;
    }
    public long getPoints()
    {
        return points;
    }
}
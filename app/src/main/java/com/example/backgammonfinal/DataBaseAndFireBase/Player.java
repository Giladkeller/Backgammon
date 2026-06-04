package com.example.backgammonfinal.DataBaseAndFireBase;

public class Player {
    private String username;
    private long points;

    // קונסטרקטור ריק עבור Firebase
    public Player() {}

    public Player(String username, long points) {
        this.username = username;
        this.points = points;
    }

    public String getUsername()
    {
        return username;
    }
    public long getPoints()
    {
        return points;
    }
}
package com.fireball.game;

public class Score {
    private int points;
    private int mult;
    private static final int CANDLE_POINTS = 100;

    Score() {
        points = 0;
        mult = 1;
    }

    Score(int s, int m) {
        points = s;
        mult = m;
    }

    public void addScore() {
        points += CANDLE_POINTS * mult;
        mult *= 2;
    }

    public int getPoints() {return points;}

    public int clearScore() {
        int tmp = points;
        points = 0;
        return tmp;
    }
}

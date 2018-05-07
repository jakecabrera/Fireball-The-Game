package com.fireball.game;

public class Score {
    private int score;
    private int mult;
    private static final int CANDLE_POINTS = 100;

    Score() {
        score = 0;
        mult = 1;
    }

    Score(int s, int m) {
        score = s;
        mult = m;
    }

    public void addScore() {
        score += CANDLE_POINTS * mult++;
    }

    public int getScore() {return score;}

    public Score clearScore() {
        int tmp = score;
        score = 0;
        return new Score(tmp, mult);
    }
}

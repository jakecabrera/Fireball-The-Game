package com.fireball.game;

public class Stats {
    private double timeSinceStarted;
    private int score;
    private boolean isPlaying;

    Stats() {
        timeSinceStarted = 0;
        score = 0;
        isPlaying = false;
    }

    public int getScore() {return score;}

    public double getTime() {return timeSinceStarted;}

    public void startTimer() {isPlaying = true;}

    public void update(float deltaTime) {
        if (isPlaying) {
            timeSinceStarted += deltaTime;
        }
    }

    public void addScore(Score s) {
        score += s.clearScore();
    }
}

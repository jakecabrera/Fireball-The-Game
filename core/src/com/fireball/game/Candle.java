package com.fireball.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Candle implements Animatable {
    private Texture unlitCandle;
    private Texture litCandleSheet;
    private Animation<TextureRegion> litCandleAnimation;
    private float stateTime = 0;
    private int x = 400, y = 50;
    private boolean isLit = false;

    public Candle() {
        unlitCandle = new Texture("unlitCandle.png");
        litCandleSheet = new Texture("litCandle.png");

        TextureRegion[][] tmp = TextureRegion.split(litCandleSheet, litCandleSheet.getWidth(), litCandleSheet.getHeight() / 5);
        TextureRegion[] litCandleFrames = new TextureRegion[5];
        int index = 0;
        for (int i = 0; i < 5; i++) {
            litCandleFrames[index++] = tmp[i][0];
        }

        litCandleAnimation = new Animation<TextureRegion>(0.083f, litCandleFrames);
        stateTime = 0f;
    }

    public void ignite() {
        isLit = !isLit;
    }

    @Override
    public void animate(SpriteBatch spriteBatch, float deltaTime) {
        stateTime += deltaTime;
        if (isLit) {
            spriteBatch.draw(litCandleAnimation.getKeyFrame(stateTime, true), x, y);
        } else {
            spriteBatch.draw(unlitCandle, x, y);
        }
    }

    @Override
    public void dispose() {
        unlitCandle.dispose();
        litCandleSheet.dispose();
    }
}

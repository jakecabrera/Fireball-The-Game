package com.fireball.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Fireball implements Animatable {
    private Animation<TextureRegion> animation;
    private Texture fireballSheet;
    private float stateTime;
    private int x = 0, y = 0;

    public Fireball() {
        fireballSheet = new Texture("fireball.png");
        TextureRegion[][] tmp = TextureRegion.split(fireballSheet, fireballSheet.getWidth(), fireballSheet.getHeight() / 5);
        TextureRegion[] fireballFrames = new TextureRegion[5];

        int index = 0;
        for (int i = 0; i < 5; i++) {
            fireballFrames[index++] = tmp[i][0];
        }

        animation = new Animation<TextureRegion>(0.083f, fireballFrames);
        stateTime = 0f;
    }

    @Override
    public void animate(SpriteBatch spriteBatch, float deltaTime) {
        stateTime += deltaTime;
        spriteBatch.draw(animation.getKeyFrame(stateTime, true), x, y);
    }

    @Override
    public void dispose() {
        fireballSheet.dispose();
    }
}

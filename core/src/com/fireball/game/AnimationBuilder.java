package com.fireball.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationBuilder {
    public static Animation<TextureRegion> build(Texture texture, final int ROWS, final int COLS, float duration) {
        TextureRegion[][] tmp = TextureRegion.split(texture,
                texture.getWidth() / COLS,
                texture.getHeight() / ROWS);
        TextureRegion[] frames = new TextureRegion[ROWS * COLS];
        int i = 0;
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                frames[i++] = tmp[row][col];
            }
        }
        return new Animation<TextureRegion>(duration, frames);
    }
}

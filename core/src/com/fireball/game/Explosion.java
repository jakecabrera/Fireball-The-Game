package com.fireball.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Explosion extends GameObject {
    private static Animation<TextureRegion> animation;
    private static Texture spriteSheet;
    private float stateTime;
    private float offsetX, offsetY;

    // Sprite sheet frame rows and cols
    private static final int FRAME_COLS = 2;
    private static final int FRAME_ROWS = 3;

    public Explosion(float x, float y, float rot, float offX, float offY) {
        this.x = x;
        this.y = y;
        rotation = rot;
        offsetX = offX;
        offsetY = offY;
        stateTime = 0f;
    }

    public static void build() {
        spriteSheet = new Texture("explosion.png");
        animation = AnimationBuilder.build(spriteSheet, FRAME_ROWS, FRAME_COLS, 0.067f);
    }

    @Override
    public void draw(SpriteBatch spriteBatch, float deltaTime) {
        stateTime += deltaTime;

        if (animation.isAnimationFinished(stateTime)) {
            return;
        }

        TextureRegion keyFrame = animation.getKeyFrame(stateTime, false);
        Sprite sprite = new Sprite(keyFrame);
        sprite.setScale(SCALE);
        sprite.setRotation(rotation);
        sprite.setPosition(x, y);
        sprite.setOrigin(offsetX,offsetY);
        sprite.draw(spriteBatch);
    }

    @Override
    public boolean finished() {
        return animation.isAnimationFinished(stateTime);
    }

    public static void dispose() {
        spriteSheet.dispose();
    }
}

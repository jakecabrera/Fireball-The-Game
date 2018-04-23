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

    private static final float SCALE = 0.05f;

    public Explosion(float x, float y, float rot, float offX, float offY) {
        this.x = x;
        this.y = y;
        this.rot = rot;
        offsetX = offX;
        offsetY = offY;
        stateTime = 0f;
    }

    public static void build() {
        spriteSheet = new Texture("explosion.png");
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet,
                spriteSheet.getWidth() / FRAME_COLS,
                spriteSheet.getHeight() / FRAME_ROWS);
        TextureRegion[] spriteFrames = new TextureRegion[FRAME_ROWS * FRAME_COLS];

        int index = 0;
        for (int row = 0; row < FRAME_ROWS; row++) {
            for (int col = 0; col < FRAME_COLS; col++) {
                spriteFrames[index++] = tmp[row][col];
            }
        }

        animation = new Animation<TextureRegion>(0.067f, spriteFrames);
    }

    @Override
    public void animate(SpriteBatch spriteBatch, float deltaTime) {
        stateTime += deltaTime;

        if (animation.isAnimationFinished(stateTime)) {
            return;
        }

        TextureRegion keyFrame = animation.getKeyFrame(stateTime, false);
        Sprite sprite = new Sprite(keyFrame);
        sprite.setScale(SCALE);
        //sprite.setRotation(rot);
        sprite.setPosition(x, y);
        sprite.setOrigin(0f, 0f);
        sprite.draw(spriteBatch);
    }

    public void animate(SpriteBatch spriteBatch, float deltaTime, ShapeRenderer shapeRenderer) {
        stateTime += deltaTime;

        if (animation.isAnimationFinished(stateTime)) {
            return;
        }

        TextureRegion keyFrame = animation.getKeyFrame(stateTime, false);
        Sprite sprite = new Sprite(keyFrame);
        sprite.setScale(SCALE);
        //sprite.setRotation(rot);
        sprite.setPosition(x - offsetX, y - offsetY - 1);
        sprite.setOrigin(0f, 0f);
        sprite.draw(spriteBatch);

//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.RED);
//        shapeRenderer.circle(x, y, 2);
//        shapeRenderer.end();
    }

    public boolean finished() {
        return animation.isAnimationFinished(stateTime);
    }
}

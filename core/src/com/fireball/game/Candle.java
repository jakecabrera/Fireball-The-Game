package com.fireball.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.codeandweb.physicseditor.PhysicsShapeCache;

public class Candle extends GameObject {
    private static Texture unlitCandle;
    private static Texture litCandleSheet;
    private static Animation<TextureRegion> litCandleAnimation;
    private float stateTime;
    private boolean isLit = false;
    private float timeLit = 0f;

    // Sprite sheet frame rows and cols
    private static final int FRAME_ROWS = 5;
    private static final int FRAME_COLS = 1;

    Candle(World world, PhysicsShapeCache physicsBodies, float x, float y) {
        this.x = x;
        this.y = y;
        w = litCandleSheet.getWidth() / FRAME_COLS;
        h = litCandleSheet.getHeight() / FRAME_ROWS;
        stateTime = 0f;

//        body = physicsBodies.createBody("unlitCandle", world, SCALE, SCALE);
        body = physicsBodies.createBody("candle", world, SCALE, SCALE);
        body.setUserData(this);
        body.setTransform(x, y, 0);
    }

    public static void build() {
        unlitCandle = new Texture("unlitCandle.png");
        litCandleSheet = new Texture("litCandle.png");
        litCandleAnimation = AnimationBuilder.build(litCandleSheet, FRAME_ROWS, FRAME_COLS, .083f);
    }

    public void ignite() {
        if (timeLit == 0) {
            timeLit = stateTime;
            isLit = !isLit;
        }
    }

    @Override
    public void animate(SpriteBatch spriteBatch, float deltaTime) {
        if (body == null) return;

        if (stateTime - timeLit > 0.1f) timeLit = 0;

        Vector2 position = body.getPosition();
        x = position.x;
        y = position.y;

        stateTime += deltaTime;
        if (isLit) {
            TextureRegion keyFrame = litCandleAnimation.getKeyFrame(stateTime, true);
            spriteBatch.draw(keyFrame, x, y, w * SCALE, h * SCALE);
        } else {
            spriteBatch.draw(unlitCandle, x, y, w * SCALE, h * SCALE);
        }
    }

    public static void dispose() {
        unlitCandle.dispose();
        litCandleSheet.dispose();
    }
}

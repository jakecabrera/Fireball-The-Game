package com.fireball.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends GameObject {
    private static Animation<TextureRegion> idleAnimation;
    private static Animation<TextureRegion> spellAnimation;
    private static Texture idleSheet;
    private static Texture spellSheet;
    private float stateTime;
    private boolean isCasting;

    // Sprite sheet frame rows and cols
    private static final int IDLE_FRAME_ROWS = 2;
    private static final int IDLE_FRAME_COLS = 2;
    private static final int SPELL_FRAME_ROWS = 7;
    private static final int SPELL_FRAME_COLS = 1;

    // Scaling
    private final float SCALE = 0.05f;

    public Player() {
        x = -2;
        y = 0f;
        w = idleSheet.getWidth() / IDLE_FRAME_COLS;
        h = idleSheet.getHeight() / IDLE_FRAME_ROWS;
        isCasting = false;
        stateTime = 0f;
    }

    public static void build() {
        idleSheet = new Texture("stickzard.png");
        spellSheet = new Texture("stickzardThrowFireball.png");

        TextureRegion[][] tmpIdle = TextureRegion.split(idleSheet,
                idleSheet.getWidth() / IDLE_FRAME_COLS,
                idleSheet.getHeight() / IDLE_FRAME_ROWS);
        TextureRegion[][] tmpSpell = TextureRegion.split(spellSheet,
                spellSheet.getWidth() / SPELL_FRAME_COLS,
                spellSheet.getHeight() / SPELL_FRAME_ROWS);

        TextureRegion[] idleFrames = new TextureRegion[IDLE_FRAME_COLS * IDLE_FRAME_ROWS];
        TextureRegion[] spellFrames = new TextureRegion[SPELL_FRAME_COLS * SPELL_FRAME_ROWS];

        int index = 0;
        for (int row = 0; row < IDLE_FRAME_ROWS; row++) {
            for (int col = 0; col < IDLE_FRAME_COLS; col++) {
                idleFrames[index++] = tmpIdle[row][col];
            }
        }

        index = 0;
        for (int row = 0; row < SPELL_FRAME_ROWS; row++) {
            for (int col = 0; col < SPELL_FRAME_COLS; col++) {
                spellFrames[index++] = tmpSpell[row][col];
            }
        }

        idleAnimation = new Animation<TextureRegion>(0.333f, idleFrames);
        spellAnimation = new Animation<TextureRegion>(0.1f, spellFrames);
    }

    public void castSpell() {
        isCasting = true;
        stateTime = 0f;
        //return new Fireball();
    }

    @Override
    public void animate(SpriteBatch spriteBatch, float deltaTime) {
        stateTime += deltaTime;

        // updates
        if (isCasting && spellAnimation.isAnimationFinished(stateTime)) {
            isCasting = false;
            stateTime = 0f;
        }

        // Actual animation
        if (isCasting) {
            spriteBatch.draw(spellAnimation.getKeyFrame(stateTime, false), x, y, w * SCALE, h * SCALE);
        } else {
            spriteBatch.draw(idleAnimation.getKeyFrame(stateTime, true), x, y, w * SCALE, h * SCALE);
        }
    }

    public static void dispose() {
        idleSheet.dispose();
        spellSheet.dispose();
    }
}

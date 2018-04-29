package com.fireball.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.codeandweb.physicseditor.PhysicsShapeCache;

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
        idleAnimation = AnimationBuilder.build(idleSheet, IDLE_FRAME_ROWS, IDLE_FRAME_COLS, 0.333f);
        spellAnimation = AnimationBuilder.build(spellSheet, SPELL_FRAME_ROWS, SPELL_FRAME_COLS, .1f);
    }

    public Fireball castSpell(float vx, float vy, World world, PhysicsShapeCache physicsBodies) {
        isCasting = true;
        stateTime = 0f;
        return new Fireball(world, physicsBodies, 7, 5, vx, vy);
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

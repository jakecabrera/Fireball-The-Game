package com.fireball.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player implements Animatable {
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> spellAnimation;
    private Texture idleSheet;
    private Texture spellSheet;
    private float stateTime;
    private int x = 50, y = 50;
    private boolean isCasting;

    public Player() {
        idleSheet = new Texture("stickzard.png");
        spellSheet = new Texture("stickzardThrowFireball.png");

        TextureRegion[][] tmp = TextureRegion.split(idleSheet, idleSheet.getWidth() / 2, idleSheet.getHeight() / 2);
        TextureRegion[][] tmpSpell = TextureRegion.split(spellSheet, spellSheet.getWidth() / 2, spellSheet.getHeight() / 4);

        TextureRegion[] idleFrames = new TextureRegion[4];
        TextureRegion[] spellFrames = new TextureRegion[8];

        int index = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                idleFrames[index++] = tmp[i][j];
            }
        }

        index = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                spellFrames[index++] = tmpSpell[i][j];
            }
        }

        idleAnimation = new Animation<TextureRegion>(0.333f, idleFrames);
        spellAnimation = new Animation<TextureRegion>(0.1f, spellFrames);

        isCasting = false;
        stateTime = 0f;
    }

    public void castSpell() {
        isCasting = true;
        stateTime = 0f;
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
            spriteBatch.draw(spellAnimation.getKeyFrame(stateTime, false), x, y);
        } else {
            spriteBatch.draw(idleAnimation.getKeyFrame(stateTime, true), x, y);
        }
    }

    @Override
    public void dispose() {
        idleSheet.dispose();
        spellSheet.dispose();
    }
}

package com.fireball.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.text.DecimalFormat;

public class GUIPlay implements Animatable {
    BitmapFont scoreFont;
    Stats stats;

    GUIPlay(BitmapFont bitmapFont, Stats s) {
        scoreFont = bitmapFont;
        stats = s;
    }

    @Override
    public void draw(SpriteBatch spriteBatch, float deltaTime) {
//        scoreFont.setColor(Color.WHITE);
        scoreFont.setUseIntegerPositions(false);
//        scoreFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        scoreFont.getData().setScale(0.05f);
        DecimalFormat df = new DecimalFormat("#0.0");
//        scoreFont.draw(spriteBatch, "Time: " + df.format(stats.getTime()) + "s",
//                FireballTheGame.WORLD_WIDTH / 2, FireballTheGame.WORLD_HEIGHT - 1,
//                0f, 1, false);
        scoreFont.draw(spriteBatch, "Score: " + stats.getScore(),
                FireballTheGame.WORLD_WIDTH / 2, FireballTheGame.WORLD_HEIGHT - 1,
                0f, 1, false);
    }
}

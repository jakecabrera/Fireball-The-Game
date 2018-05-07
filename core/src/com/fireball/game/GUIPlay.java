package com.fireball.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class GUIPlay implements Animatable {
    BitmapFont scoreFont;

    GUIPlay(BitmapFont bitmapFont) {
        scoreFont = bitmapFont;
    }

    @Override
    public void draw(SpriteBatch spriteBatch, float deltaTime) {
//        scoreFont.setColor(Color.WHITE);
        scoreFont.setUseIntegerPositions(false);
//        scoreFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        scoreFont.getData().setScale(1f);
        scoreFont.draw(spriteBatch, "Hello World!", FireballTheGame.WORLD_WIDTH / 2, FireballTheGame.WORLD_HEIGHT / 2,
                0f, 1, false);
    }
}

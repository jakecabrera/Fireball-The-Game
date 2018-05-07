package com.fireball.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class GUImaster implements Animatable {
    private static FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private static BitmapFont bitmapFont;
    GUIPlay guiPlay;

    GUImaster() {
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 10;
        bitmapFont = generator.generateFont(parameter);

        guiPlay = new GUIPlay(bitmapFont);
    }

    public static void build() {
        bitmapFont = new BitmapFont();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/roboto/Roboto-Black.ttf"));
    }

    @Override
    public void draw(SpriteBatch spriteBatch, float deltaTime) {
        guiPlay.draw(spriteBatch, deltaTime);
    }

    public static void dispose() {
        bitmapFont.dispose();
        generator.dispose();
    }
}
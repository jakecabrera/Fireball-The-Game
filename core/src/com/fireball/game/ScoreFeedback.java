package com.fireball.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;

public class ScoreFeedback extends GameObject {
    private static FreeTypeFontGenerator generator;
    private static BitmapFont bitmapFont;
    private static final float LIFESPAN = 1f;
    private BitmapFontCache font;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private Vector2 pos;
    private float speed;
    private int points;
    private float stateTime;

    ScoreFeedback(Vector2 position, Score score) {
        points = score.getPoints();
        speed = 0.1f;
        pos = position;
        stateTime = 0f;

        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;

        bitmapFont = generator.generateFont(parameter);
        bitmapFont.setUseIntegerPositions(false);
        bitmapFont.getData().setScale(0.05f);
        font = bitmapFont.getCache();
    }

    public static void build() {
        bitmapFont = new BitmapFont();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/roboto/Roboto-Black.ttf"));
    }

    public static void dispose() {
        generator.dispose();
        bitmapFont.dispose();
    }

    @Override
    public boolean finished() {
        return stateTime >= LIFESPAN;
    }

    @Override
    public void draw(SpriteBatch spriteBatch, float deltaTime) {
        stateTime += deltaTime;
        pos.y += speed;
//        bitmapFont.draw(spriteBatch, "+" + points, pos.x, pos.y,
//                0f, 1, false);
        font.setColor(.8f, 1f, .8f, 1f);
        font.setText("+" + points, pos.x, pos.y,
                0f, 1, false);
        font.setAlphas((1 - stateTime / LIFESPAN > 0) ? 1 - stateTime / LIFESPAN : 0f);
        font.draw(spriteBatch);
    }
}

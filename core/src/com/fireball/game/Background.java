package com.fireball.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Background implements Animatable{
    private static Texture backgroundTile;
    private static Texture forestTiles;
    private static Texture tileset;
    private Sprite background, forest, playArea;
    private float stateTime;

    public Background() {
        stateTime = 0f;
        background = new Sprite(backgroundTile);
        forest = new Sprite(forestTiles);
        playArea = new Sprite(tileset);
    }

    public static void build() {
        backgroundTile = new Texture("country-platform-back.png");
        forestTiles = new Texture("country-platform-forest.png");
        tileset = new Texture("country-platform-tiles-example.png");
    }

    public static void dispose() {
        backgroundTile.dispose();
        forestTiles.dispose();
        tileset.dispose();
    }

    @Override
    public void animate(SpriteBatch spriteBatch, float deltaTime) {
        stateTime += deltaTime;
        background.setBounds(0, 0, FireballTheGame.WORLD_WIDTH, FireballTheGame.WORLD_HEIGHT);
        forest.setBounds(0, 0, FireballTheGame.WORLD_WIDTH, FireballTheGame.WORLD_HEIGHT);
        playArea.setBounds(0, 0, FireballTheGame.WORLD_WIDTH, FireballTheGame.WORLD_HEIGHT);

        background.draw(spriteBatch);
        //playArea.draw(spriteBatch);
    }
}

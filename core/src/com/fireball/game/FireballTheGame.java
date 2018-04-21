package com.fireball.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class FireballTheGame extends ApplicationAdapter {
	Animation<TextureRegion> candleAnimation;
	Animation<TextureRegion> fireballAnimation;
	Texture litCandleSheet;
	Texture fireballSheet;
	Player player;
	Candle candle;
	Fireball fireball;
	Viewport viewport;
	Camera camera;

	SpriteBatch spriteBatch;
	ShapeRenderer shapeRenderer;

	float stateTime;
	float fireStateTime;
	
	@Override
	public void create () {
		player = new Player();
		candle = new Candle();
		fireball = new Fireball();
		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		camera = new OrthographicCamera(1280, 720);
		camera.position.set(1280/ 2, 720/2, 0);
		camera.update();
		//viewport = new FitViewport(720, 480, camera);
		spriteBatch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);

		Gdx.input.setInputProcessor(new GestureDetector(new GestureDetector.GestureListener() {
            @Override
            public boolean touchDown(float x, float y, int pointer, int button) {
                return false;
            }

            @Override
            public boolean tap(float x, float y, int count, int button) {
                return false;
            }

            @Override
            public boolean longPress(float x, float y) {
                return false;
            }

            @Override
            public boolean fling(float velocityX, float velocityY, int button) {
                player.castSpell();
                candle.ignite();
                return false;
            }

            @Override
            public boolean pan(float x, float y, float deltaX, float deltaY) {
                return false;
            }

            @Override
            public boolean panStop(float x, float y, int pointer, int button) {
                return false;
            }

            @Override
            public boolean zoom(float initialDistance, float distance) {
                return false;
            }

            @Override
            public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
                return false;
            }

            @Override
            public void pinchStop() {

            }
        }));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(.7f, 0.7f, 0.7f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		float dt = Gdx.graphics.getDeltaTime();

		spriteBatch.begin();
		player.animate(spriteBatch, dt);
		candle.animate(spriteBatch, dt);
		fireball.animate(spriteBatch, dt);
		spriteBatch.end();
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.rect(10, 430, 50, 50);
		shapeRenderer.end();
	}
	
	@Override
	public void dispose () {
		spriteBatch.dispose();
		player.dispose();
		candle.dispose();
		fireball.dispose();
		litCandleSheet.dispose();
	}
}

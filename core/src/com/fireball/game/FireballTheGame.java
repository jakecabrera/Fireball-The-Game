package com.fireball.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FireballTheGame extends ApplicationAdapter {
	Animation<TextureRegion> candleAnimation;
	Animation<TextureRegion> fireballAnimation;
	Texture litCandleSheet;
	Texture fireballSheet;

	Player player;

	SpriteBatch spriteBatch;

	float stateTime;
	float fireStateTime;
	
	@Override
	public void create () {
		player = new Player();
		litCandleSheet = new Texture("candlelit.png");
		fireballSheet = new Texture("fireball.png");

		TextureRegion[][] tmpCanLit = TextureRegion.split(litCandleSheet,
				litCandleSheet.getWidth() / 1, litCandleSheet.getHeight() / 5);
		TextureRegion[][] tmpFireball = TextureRegion.split(fireballSheet, fireballSheet.getWidth(), fireballSheet.getHeight() / 5);

		TextureRegion[] litCandleFrames = new TextureRegion[5];
		TextureRegion[] fireballFrames = new TextureRegion[5];

		int index = 0;
		for (int i = 0; i < 5; i++) {
			litCandleFrames[index] = tmpCanLit[i][0];
			fireballFrames[index++] = tmpFireball[i][0];
		}

		candleAnimation = new Animation<TextureRegion>(0.1f, litCandleFrames);
		fireballAnimation = new Animation<TextureRegion>(0.083f, fireballFrames);

		spriteBatch = new SpriteBatch();
		stateTime = 0f;
		fireStateTime = 0f;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(.7f, 0.7f, 0.7f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stateTime += Gdx.graphics.getDeltaTime();
		fireStateTime += Gdx.graphics.getDeltaTime();

		TextureRegion currentCandleFrame = candleAnimation.getKeyFrame(stateTime, true);
		TextureRegion currentFireball = fireballAnimation.getKeyFrame(fireStateTime, true);

		spriteBatch.begin();
		if (Gdx.input.justTouched()) {
			player.castSpell();
		}
		player.animate(spriteBatch, Gdx.graphics.getDeltaTime());
		spriteBatch.draw(currentCandleFrame, 400, 50);
		spriteBatch.end();
	}
	
	@Override
	public void dispose () {
		spriteBatch.dispose();
		player.dispose();
		litCandleSheet.dispose();
	}
}

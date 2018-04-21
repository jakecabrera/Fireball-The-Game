package com.fireball.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FireballTheGame extends ApplicationAdapter {
	Animation<TextureRegion> walkAnimation;
	Animation<TextureRegion> spellAnimation;
	Animation<TextureRegion> candleAnimation;
	Animation<TextureRegion> fireballAnimation;
	Texture walkSheet;
	Texture spellSheet;
	Texture litCandleSheet;
	Texture fireballSheet;

	TextureRegion player;

	SpriteBatch spriteBatch;

	float stateTime;
	float playerStateTime;
	float fireStateTime;
	boolean casting = false;
	
	@Override
	public void create () {
		walkSheet = new Texture("stickzard.png");
		spellSheet = new Texture("stickzardThrowFireball.png");
		litCandleSheet = new Texture("candlelit.png");
		fireballSheet = new Texture("fireball.png");

		TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / 2, walkSheet.getHeight() / 2);
		TextureRegion[][] tmpSpell = TextureRegion.split(spellSheet, spellSheet.getWidth() / 2, spellSheet.getHeight() / 4);
		TextureRegion[][] tmpCanLit = TextureRegion.split(litCandleSheet,
				litCandleSheet.getWidth() / 1, litCandleSheet.getHeight() / 5);
		TextureRegion[][] tmpFireball = TextureRegion.split(fireballSheet, fireballSheet.getWidth(), fireballSheet.getHeight() / 5);

		TextureRegion[] walkFrames = new TextureRegion[4];
		TextureRegion[] spellFrames = new TextureRegion[8];
		TextureRegion[] litCandleFrames = new TextureRegion[5];
		TextureRegion[] fireballFrames = new TextureRegion[5];

		int index = 0;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				walkFrames[index++] = tmp[i][j];
			}
		}

		index = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 2; j++) {
				spellFrames[index++] = tmpSpell[i][j];
			}
		}

		index = 0;
		for (int i = 0; i < 5; i++) {
			litCandleFrames[index] = tmpCanLit[i][0];
			fireballFrames[index++] = tmpFireball[i][0];
		}


		walkAnimation = new Animation<TextureRegion>(0.333f, walkFrames);
		spellAnimation = new Animation<TextureRegion>(0.1f, spellFrames);
		candleAnimation = new Animation<TextureRegion>(0.1f, litCandleFrames);
		fireballAnimation = new Animation<TextureRegion>(0.083f, fireballFrames);

		spriteBatch = new SpriteBatch();
		stateTime = 0f;
		playerStateTime = 0f;
		fireStateTime = 0f;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(.7f, 0.7f, 0.7f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stateTime += Gdx.graphics.getDeltaTime();
		playerStateTime += Gdx.graphics.getDeltaTime();
		fireStateTime += Gdx.graphics.getDeltaTime();

		TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, true);
		TextureRegion currentCandleFrame = candleAnimation.getKeyFrame(stateTime, true);
		TextureRegion currentSpellFrame = spellAnimation.getKeyFrame(playerStateTime, false);
		TextureRegion currentFireball = fireballAnimation.getKeyFrame(fireStateTime, true);

		spriteBatch.begin();
		if (Gdx.input.justTouched()) {
			casting = true;
			playerStateTime = 0f;
		}

		if (spellAnimation.isAnimationFinished(playerStateTime)) {
			playerStateTime = 0f;
			casting = false;
		}

		if (casting) {
			player = currentSpellFrame;
		} else {
			player = currentFrame;
		}
		spriteBatch.draw(player, 50, 50);
		spriteBatch.draw(currentCandleFrame, 400, 50);
		spriteBatch.end();
	}
	
	@Override
	public void dispose () {
		spriteBatch.dispose();
		walkSheet.dispose();
		litCandleSheet.dispose();
	}
}

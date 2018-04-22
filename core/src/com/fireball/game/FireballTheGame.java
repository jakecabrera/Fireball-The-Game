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
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.codeandweb.physicseditor.PhysicsShapeCache;

import java.util.ArrayList;

public class FireballTheGame extends ApplicationAdapter {
	Animation<TextureRegion> candleAnimation;
	Animation<TextureRegion> fireballAnimation;
	ArrayList<Fireball> fireballs;
	Texture litCandleSheet;
	Texture fireballSheet;
	Player player;
	Candle candle;
	Fireball fireball;
	Viewport viewport;
	Camera camera;

	SpriteBatch spriteBatch;
	ShapeRenderer shapeRenderer;

	// Box2D world stuff
	World world;
	static final float STEP_TIME = 1f / 60f;
	static final int VELOCITY_ITERATIONS = 6;
	static final int POSITION_ITERATIONS = 2;
	float accumulator = 0;
	PhysicsShapeCache physicsBodies;
	Box2DDebugRenderer debugRenderer;
	final float SCALE = 0.05f;
	Body ground;

	float stateTime;
	float fireStateTime;
	
	@Override
	public void create () {
		Box2D.init();
		world = new World(new Vector2(0, -10), true);
		physicsBodies = new PhysicsShapeCache("physics.xml");
		debugRenderer = new Box2DDebugRenderer();

		// Build the resources and animations for the game
		Candle.build();
		Fireball.build();
		Player.build();

		player = new Player();
		candle = new Candle(world, physicsBodies);
		fireball = new Fireball(world, physicsBodies);
		fireballs = new ArrayList<Fireball>();
		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		camera = new OrthographicCamera();
//		camera.position.set(500, 500, 0);
//		camera.update();
		viewport = new FitViewport(50, 28, camera);
//		viewport.update(100, 100, true);
//		spriteBatch.setProjectionMatrix(camera.combined);
//		shapeRenderer.setProjectionMatrix(camera.combined);

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
		stepWorld();
		Gdx.gl.glClearColor(.5f, 0.5f, 0.5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		float dt = Gdx.graphics.getDeltaTime();

		spriteBatch.begin();
		player.animate(spriteBatch, dt);
		candle.animate(spriteBatch, dt);
		fireball.animate(spriteBatch, dt);
		spriteBatch.end();
//		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//		shapeRenderer.setColor(Color.RED);
//		shapeRenderer.rect(10, 430, 50, 50);
//		shapeRenderer.end();
		debugRenderer.render(world, camera.combined);
	}
	
	@Override
	public void dispose () {
		spriteBatch.dispose();
		Player.dispose();
		Candle.dispose();
		Fireball.dispose();
		litCandleSheet.dispose();
		world.dispose();
		debugRenderer.dispose();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);

		viewport.update(width, height, true);
		spriteBatch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
		createGround();
	}

	private Body createBody(String name, float x, float y, float rotation) {
		Body body = physicsBodies.createBody(name, world, SCALE, SCALE);
		body.setTransform(x, y, rotation);
		return body;
	}

	private void createGround() {
		if (ground != null) world.destroyBody(ground);

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.StaticBody;

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.friction = 1;

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(camera.viewportWidth, 1);

		fixtureDef.shape = shape;

		ground = world.createBody(bodyDef);
		ground.createFixture(fixtureDef);
		ground.setTransform(0, 0, 0);

		shape.dispose();
	}

	private void stepWorld() {
		float delta = Gdx.graphics.getDeltaTime();

		accumulator += Math.min(delta, 0.25f);

		if (accumulator >= STEP_TIME) {
			accumulator -= STEP_TIME;

			world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
		}
	}
}

package com.fireball.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.codeandweb.physicseditor.PhysicsShapeCache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FireballTheGame extends ApplicationAdapter {
    // Game object arrays
	private ArrayList<GameObject> destroyables;
	private ArrayList<GameObject> gameObjects;

	// Main player
	private Player player;

	// Display
	private Background background;
	private Viewport viewport;
	private Camera camera;
	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;

	// Box2D world stuff
	private World world;
	private PhysicsShapeCache physicsBodies;
	private static final float STEP_TIME = 1f / 60f;
	private static final int VELOCITY_ITERATIONS = 6;
	private static final int POSITION_ITERATIONS = 2;
	private float accumulator = 0;
	private Box2DDebugRenderer debugRenderer;
	private Body ground;

	// Screen constants
    static final float WORLD_HEIGHT = 32;
    static final float WORLD_WIDTH = 50;
	
	@Override
	public void create () {
		Box2D.init();
		world = new World(new Vector2(0, -100), true);
		physicsBodies = new PhysicsShapeCache("physics.xml");
		debugRenderer = new Box2DDebugRenderer();

		// Build the resources and animations for the game
		Candle.build();
		Fireball.build();
		Player.build();
		Explosion.build();
		Background.build();

		// Initialize game objects
		background = new Background();
		player = new Player();
		destroyables = new ArrayList<GameObject>();
		gameObjects = new ArrayList<GameObject>();

		// Create some candles
		gameObjects.add(new Candle(world, physicsBodies, 40, 0f));
		gameObjects.add(new Candle(world, physicsBodies, 30, 0f));
		gameObjects.add(new Candle(world, physicsBodies, 20, 0f));

		// Initialize Display
		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		camera = new OrthographicCamera();
		viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

		// for processing touch input
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
                if (velocityX > 0) {
					gameObjects.add(player.castSpell(velocityX, velocityY, world, physicsBodies));
				}
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

		// for handling collisions
		world.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				Object a = contact.getFixtureA().getBody().getUserData();
				Object b = contact.getFixtureB().getBody().getUserData();

				Fireball fireball = null;
				Candle candle = null;

				if (a.getClass() == Fireball.class) {
					fireball = (Fireball) a;
					if (b.getClass() == Candle.class) {
						candle = (Candle) b;
					}
				} else if (b.getClass() == Fireball.class) {
					fireball = (Fireball) b;
					if (a.getClass() == Candle.class) {
						candle = (Candle) a;
					}
				}

				if (fireball != null && !fireball.finished() && candle != null) {
					candle.ignite();
					gameObjects.remove(fireball);
					if (!destroyables.contains(fireball)) destroyables.add(fireball);
					gameObjects.add(fireball.explode());
				}
			}

			@Override
			public void endContact(Contact contact) {

			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {

			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {

			}
		});
	}

	@Override
	public void render () {
		stepWorld();
		destroyDeadBodies();
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		float dt = Gdx.graphics.getDeltaTime();

		// remove anything that doesn't need to be in the world anymore
		cleanup(gameObjects);

		// draw and animate all things in the game
		spriteBatch.begin();
		background.animate(spriteBatch, dt);
		player.animate(spriteBatch, dt);
		animate(gameObjects, dt);
		spriteBatch.end();

		// Debug stuff
//		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//		shapeRenderer.setColor(Color.GREEN);
//		shapeRenderer.circle(5,5,1);
//		shapeRenderer.end();
//		debugRenderer.render(world, camera.combined);
	}
	
	@Override
	public void dispose () {
		spriteBatch.dispose();
		Player.dispose();
		Candle.dispose();
		Fireball.dispose();
		Explosion.dispose();
		Background.dispose();
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

	/**
	 * Creates the physics ground that bodies can bounce off of
	 */
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
		ground.setUserData(true);

		shape.dispose();
	}

	/**
	 * Simulate the world as a 60fps game
	 */
	private void stepWorld() {
		float delta = Gdx.graphics.getDeltaTime();

		accumulator += Math.min(delta, 0.25f);

		if (accumulator >= STEP_TIME) {
			accumulator -= STEP_TIME;

			world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
		}
	}

	/**
	 * Animate all of the objects in a list. Has to be called in between the spriteBatch
	 * begin and end calls
	 * @param animatables as list of objects to be animated
	 * @param deltaTime as time that has passed since last call
	 */
	private void animate(List<? extends Animatable> animatables, float deltaTime) {
		for (Animatable animatable: animatables) animatable.animate(spriteBatch, deltaTime);
	}

	/**
	 * Remove all of the gameObjects from a list of gameObjects that are finished
	 * @param gameObjects as list to cleanup
	 */
	private void cleanup(List<GameObject> gameObjects) {
		List<GameObject> toRemove = new ArrayList<GameObject>();
		List<GameObject> toAdd = new ArrayList<GameObject>();
		for (GameObject gameObject: gameObjects) {
			if (gameObject.finished()) {
				GameObject lastWords = gameObject.clean();
				if (lastWords != null) toAdd.add(lastWords);
				toRemove.add(gameObject);
			}
		}

		destroyables.addAll(toRemove);
		gameObjects.removeAll(toRemove);
		gameObjects.addAll(toAdd);
	}

	/**
	 * Destroy all the bodies of the finished gameObjects in the world
	 */
	private void destroyDeadBodies() {
	    for (Iterator<GameObject> iter = destroyables.iterator(); iter.hasNext();) {
	        GameObject gameObject = iter.next();
	        if (gameObject != null && gameObject.hasBody()) {
                Body body = gameObject.getBody();
	            world.destroyBody(body);
                body.setUserData(null);
                gameObject.destroyBody();
            }
			destroyables.remove(gameObject);
        }
	}
}

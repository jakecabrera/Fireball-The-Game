package com.fireball.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import com.codeandweb.physicseditor.PhysicsShapeCache;

public class Fireball extends GameObject {
    private static Animation<TextureRegion> animation;
    private static Texture spriteSheet;
    private float stateTime;
    private boolean exploded;
    private float rotation;
    private float offsetX, offsetY;
    private float bOffsetX, bOffsetY;

    // test
    private float r, k;

    // Sprite sheet frame rows and cols
    private static final int FRAME_ROWS = 5;
    private static final int FRAME_COLS = 1;

    // how long a fireball lives
    private static final float LIFESPAN = 5f;

    // Scaling
    private final float SCALE = 0.05f;
    private final float SPEED = 0.02f;

    public Fireball(World world, PhysicsShapeCache physicsBodies, float x, float y, float vx, float vy) {
        this.x = x;
        this.y = y;
        this.rotation = 0f;
        w = spriteSheet.getWidth() / FRAME_COLS * SCALE;
        h = spriteSheet.getHeight() / FRAME_ROWS * SCALE;
        rot = (float) Math.toDegrees(Math.atan2(-(double)vy, (double)vx));

        exploded = false;
        stateTime = 0f;

        body = physicsBodies.createBody("fireballNoAnim", world, SCALE, SCALE);
        body.setUserData(this);
        Vector2 vel = body.getLinearVelocity();
        vel.x = vx * SPEED;
        vel.y = -vy * SPEED;
        body.setLinearVelocity(vel);
        //body.setTransform(newX, newY, (float)Math.toRadians(rot));
        body.setTransform(x, y, 0f);

        Vector2 circlePos = ((CircleShape) body.getFixtureList().get(0).getShape()).getPosition();
        r = (float)Math.sqrt(Math.pow(circlePos.x, 2) + Math.pow(circlePos.y, 2));
        k = (float)Math.atan(circlePos.y / circlePos.x);
        offsetX = circlePos.x;
        offsetY = circlePos.y;
    }

    public static void build() {
        spriteSheet = new Texture("fireball.png");
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet,
                spriteSheet.getWidth() / FRAME_COLS,
                spriteSheet.getHeight() / FRAME_ROWS);
        TextureRegion[] fireballFrames = new TextureRegion[FRAME_ROWS * FRAME_COLS];

        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            fireballFrames[index++] = tmp[i][0];
        }

        animation = new Animation<TextureRegion>(0.083f, fireballFrames);
    }

    @Override
    public void animate(SpriteBatch spriteBatch, float deltaTime) {
        if (body == null || y < -10 || stateTime > LIFESPAN) {
            exploded = true;
            return;
        }

        Vector2 vel = body.getLinearVelocity();
        Vector2 pos = body.getPosition();
        float bodyAngle = body.getAngle();

        rotation = (float) Math.toDegrees(Math.atan2((double)vel.y, (double)vel.x)) - 90;

        float oX = pos.x + (float) (r * Math.cos(k + bodyAngle));
        float oY = pos.y + (float) (r * Math.sin(k + bodyAngle));
        float ox2 = (float) (r * Math.cos(k + Math.toRadians(rotation)));
        float oy2 = (float) (r * Math.sin(k + Math.toRadians(rotation)));

        bOffsetX = ox2;
        bOffsetY = oy2;

        x = oX - ox2 - offsetX;
        y = oY - oy2 - offsetY;

        stateTime += deltaTime;
        TextureRegion keyFrame = animation.getKeyFrame(stateTime, true);
        Sprite sprite = new Sprite(keyFrame);
        sprite.setOrigin(offsetX, offsetY);
        sprite.setPosition(x,y);
        sprite.setRotation(rotation);
        sprite.setScale(SCALE);
        System.out.println(sprite.getOriginX());
        sprite.draw(spriteBatch);
    }

    public Explosion explode(float x, float y) {
        exploded = true;
        Vector2 circlePos = ((CircleShape) body.getFixtureList().get(0).getShape()).getPosition();
        return new Explosion(x, y, rot, circlePos.x, circlePos.y);
    }

    public Explosion explode() {
        exploded = true;
//        Vector2 circlePos = ((CircleShape) body.getFixtureList().get(0).getShape()).getPosition();
//        return new Explosion(x, y, rot, circlePos.x, circlePos.y);
//        return new Explosion(x + bOffsetX, y + bOffsetY, 0f, offsetX, offsetY);
        return new Explosion(body.getPosition().x, body.getPosition().y, 0f, offsetX, offsetY);
    }

    public boolean finished() {
        return exploded;
    }

    public static void dispose() {
        spriteSheet.dispose();
    }
}

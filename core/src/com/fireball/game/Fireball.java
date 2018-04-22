package com.fireball.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.codeandweb.physicseditor.PhysicsShapeCache;

public class Fireball extends GameObject {
    private static Animation<TextureRegion> animation;
    private static Texture spriteSheet;
    private float stateTime;
    private boolean exploded;
    private float vx, vy;

    // Sprite sheet frame rows and cols
    private static final int FRAME_ROWS = 5;
    private static final int FRAME_COLS = 1;

    // Scaling
    private final float SCALE = 0.05f;
    private final float SPEED = 0.02f;

    public Fireball(World world, PhysicsShapeCache physicsBodies, float x, float y, float vx, float vy) {
        this.x = x;
        this.y = y;
        this.vx = vx * SPEED;
        this.vy = vy * SPEED;
        w = spriteSheet.getWidth() / FRAME_COLS;
        h = spriteSheet.getHeight() / FRAME_ROWS;
        rot = (float) Math.toDegrees(Math.atan2(-(double)vy, (double)vx)) - 90;
        System.out.println(rot);
        exploded = false;
        stateTime = 0f;

        body = physicsBodies.createBody("fireballNoAnim", world, SCALE, SCALE);
        body.setUserData(this);
        Vector2 vel = body.getLinearVelocity();
        vel.x = vx * SPEED;
        vel.y = -vy * SPEED;
        body.setLinearVelocity(vel);
        body.setTransform(x, y, (float)Math.toRadians(rot));
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
        if (body == null) return;
        Vector2 position = body.getPosition();
        Vector2 vel = body.getLinearVelocity();
        x = position.x;
        y = position.y;
        //rot = (float) Math.toDegrees(body.getAngle());
        rot = (float) Math.toDegrees(Math.atan2((double)vel.y, (double)vel.x)) - 90;
        body.setTransform(position, (float)Math.toRadians(rot));

        stateTime += deltaTime;
        TextureRegion keyFrame = animation.getKeyFrame(stateTime, true);
        Sprite sprite = new Sprite(keyFrame);
        sprite.setOrigin(0f,0f);
        sprite.setPosition(x, y);
        sprite.setScale(SCALE);
        sprite.setRotation(rot);
        sprite.draw(spriteBatch);
    }

    public Explosion explode(World world) {
        exploded = true;
        //world.destroyBody(body);
        return new Explosion(x, y, rot);
    }

    public boolean finished() {
        return exploded;
    }

    public static void dispose() {
        spriteSheet.dispose();
    }
}

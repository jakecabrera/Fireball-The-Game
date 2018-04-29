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
    private static final float LIFESPAN = 5f;
    private static Animation<TextureRegion> animation;
    private static Texture spriteSheet;

    private final float SPEED = 0.02f;
    private float stateTime;
    private boolean exploded;
    private float offsetX, offsetY;
    private float r, k;

    // Sprite sheet frame rows and cols
    private static final int FRAME_ROWS = 5;
    private static final int FRAME_COLS = 1;

    public Fireball(World world, PhysicsShapeCache physicsBodies, float x, float y, float vx, float vy) {
        this.x = x;
        this.y = y;
        this.rotation = 0f;
        w = spriteSheet.getWidth() / FRAME_COLS * SCALE;
        h = spriteSheet.getHeight() / FRAME_ROWS * SCALE;
        rotation = (float) Math.toDegrees(Math.atan2(-(double)vy, (double)vx));
        exploded = false;
        stateTime = 0f;

        // Create physics body
        body = physicsBodies.createBody("fireballNoAnim", world, SCALE, SCALE);
        body.setUserData(this);
        Vector2 vel = body.getLinearVelocity();
        vel.x = vx * SPEED;
        vel.y = -vy * SPEED;
        body.setLinearVelocity(vel);
        body.setTransform(x, y, 0f);

        // Get constants for the object
        Vector2 circlePos = ((CircleShape) body.getFixtureList().get(0).getShape()).getPosition();
        r = (float)Math.sqrt(Math.pow(circlePos.x, 2) + Math.pow(circlePos.y, 2));
        k = (float)Math.atan(circlePos.y / circlePos.x);
        offsetX = circlePos.x;
        offsetY = circlePos.y;
    }

    public static void build() {
        spriteSheet = new Texture("fireball.png");
        animation = AnimationBuilder.build(spriteSheet, FRAME_ROWS, FRAME_COLS, 0.083f);
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

        // Some math to figure out where to place the sprite relative to it's physical
        // body and rotation with regard to it's velocity
        rotation = (float) Math.toDegrees(Math.atan2((double)vel.y, (double)vel.x)) - 90;
        float bodyRelPosX = pos.x + (float) (r * Math.cos(k + bodyAngle));
        float bodyRelPosY = pos.y + (float) (r * Math.sin(k + bodyAngle));
        float textureOffsetX = (float) (r * Math.cos(k + Math.toRadians(rotation)));
        float textureOffsetY = (float) (r * Math.sin(k + Math.toRadians(rotation)));

        x = bodyRelPosX - textureOffsetX - offsetX;
        y = bodyRelPosY - textureOffsetY - offsetY;

        // Set up the sprite and draw it
        stateTime += deltaTime;
        TextureRegion keyFrame = animation.getKeyFrame(stateTime, true);
        Sprite sprite = new Sprite(keyFrame);
        sprite.setOrigin(offsetX, offsetY);
        sprite.setRotation(rotation);
        sprite.setScale(SCALE);
        sprite.setPosition(x,y);
        sprite.draw(spriteBatch);
    }

    public Explosion explode() {
        exploded = true;
        return new Explosion(x, y, rotation, offsetX, offsetY);
    }

    @Override
    public boolean finished() {
        return exploded;
    }

    public static void dispose() {
        spriteSheet.dispose();
    }

    @Override
    public GameObject clean() {
        super.clean();

        if (body != null) {
            return explode();
        }

        return null;
    }
}

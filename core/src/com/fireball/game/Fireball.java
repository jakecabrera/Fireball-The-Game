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
        w = spriteSheet.getWidth() / FRAME_COLS * SCALE;
        h = spriteSheet.getHeight() / FRAME_ROWS * SCALE;
        rot = (float) Math.toDegrees(Math.atan2(-(double)vy, (double)vx));

        float newX = x - ((float) Math.cos(Math.toRadians(rot)));
        float newY = y - ((float) Math.sin(Math.toRadians(rot)));

        exploded = false;
        stateTime = 0f;

        body = physicsBodies.createBody("fireballNoAnim", world, SCALE, SCALE);
        body.setUserData(this);
        Vector2 vel = body.getLinearVelocity();
        vel.x = vx * SPEED;
        vel.y = -vy * SPEED;
        body.setLinearVelocity(vel);
        //body.setTransform(newX, newY, (float)Math.toRadians(rot));
        body.setTransform(x, y, 90f);
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
        if (body == null || y < 0) {
            exploded = true;
            return;
        }

        Vector2 vel = body.getLinearVelocity();

        Vector2 circlePos = ((CircleShape) body.getFixtureList().get(0).getShape()).getPosition();
        Vector2 pos = body.getPosition();
        float bodyAngle = body.getAngle();

        rot = (float) Math.toDegrees(Math.atan2((double)vel.y, (double)vel.x)) - 90;

        x = pos.y + circlePos.x;
        y = pos.x + circlePos.y;

        stateTime += deltaTime;
        TextureRegion keyFrame = animation.getKeyFrame(stateTime, true);
        Sprite sprite = new Sprite(keyFrame);
        sprite.setOrigin(circlePos.x, circlePos.y);
        sprite.setOriginBasedPosition(pos.x + (float) (circlePos.x * Math.cos(bodyAngle)), pos.y + circlePos.y);
        sprite.setRotation(rot);
        sprite.setScale(SCALE);
        //sprite.setBounds((float)(pos.x - w * Math.cos(bodyAngle)), pos.y, w, h);
        sprite.draw(spriteBatch);
    }

    public Explosion explode(float x, float y) {
        exploded = true;
        Vector2 circlePos = ((CircleShape) body.getFixtureList().get(0).getShape()).getPosition();
        return new Explosion(x, y, rot, circlePos.x, circlePos.y);
    }

    public boolean finished() {
        return exploded;
    }

    public static void dispose() {
        spriteSheet.dispose();
    }
}

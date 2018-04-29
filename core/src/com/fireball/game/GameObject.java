package com.fireball.game;

import com.badlogic.gdx.physics.box2d.Body;

public abstract class GameObject implements Animatable {
    protected float x, y;
    protected float w, h;
    protected float rotation;
    protected Body body = null;
    protected final float SCALE = 0.05f;

    public Body getBody() {
        return body;
    }

    public boolean hasBody() {return body != null;}

    public void destroyBody() {
        body = null;
    }

    public boolean finished() {return false;}

    public GameObject clean() {
        return null;
    }
}

package com.fireball.game;

import com.badlogic.gdx.physics.box2d.Body;

public abstract class GameObject implements Animatable {
    protected float x, y;
    protected float w, h;
    protected float rot;
    protected Body body = null;

    public Body getBody() {
        return body;
    }

    public void destroyBody() {
        body = null;
    }
}

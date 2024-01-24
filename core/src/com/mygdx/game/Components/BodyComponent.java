package com.mygdx.game.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool.Poolable;

public class BodyComponent implements Component, Poolable{
    public Body body;
    public boolean isDead = false;

    @Override
    public void reset() {
        body = null;
        isDead = false;
    }
}
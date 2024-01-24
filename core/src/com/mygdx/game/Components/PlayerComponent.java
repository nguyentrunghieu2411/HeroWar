package com.mygdx.game.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Pool.Poolable;
public class PlayerComponent implements Component, Poolable{
    public OrthographicCamera cam = null;
    public boolean onPlatform = false;
    public boolean onSpring = false;
    public boolean isDead = false;
    public float shootDelay = 0.5f;
    public float attackDelay = 1f;
    public float timeSinceLastShot = 0f;
    public float timeSinceLastAttack = 0f;

    public boolean IsDirectionRight = true;

    @Override
    public void reset() {
        cam = null;
        onPlatform = false;
        onSpring = false;
        isDead = false;
        shootDelay = 0.5f;
        timeSinceLastShot = 0f;
    }
}
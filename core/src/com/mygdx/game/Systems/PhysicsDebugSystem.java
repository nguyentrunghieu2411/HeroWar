package com.mygdx.game.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;


public class PhysicsDebugSystem extends IteratingSystem {

    private Box2DDebugRenderer debugRenderer;
    private World world;
    private OrthographicCamera camera;
    private boolean debug = false;   // turn off with false on with true

    @SuppressWarnings("unchecked")
    public PhysicsDebugSystem(World world, OrthographicCamera camera){
        super(Family.all().get());
        debugRenderer = new Box2DDebugRenderer();
        this.world = world;
        this.camera = camera;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (debug) debugRenderer.render(world, camera.combined);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }
}
package com.mygdx.game.Systems;


import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.Components.BodyComponent;
import com.mygdx.game.Components.WallComponent;
import com.mygdx.game.Managers.Mapper;

public class WallSystem extends IteratingSystem{
    private Entity player;

    @SuppressWarnings("unchecked")
    public WallSystem(Entity player) {
        super(Family.all(WallComponent.class).get());
        this.player = player;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // get current y level of player entity
        float currentyLevel = player.getComponent(BodyComponent.class).body.getPosition().y;
        // get the body component of the wall we're updating
        Body bod = Mapper.bodyMapper.get(entity).body;
        //set the walls y position to match the player
        bod.setTransform(bod.getPosition().x, currentyLevel, bod.getAngle());
    }
}
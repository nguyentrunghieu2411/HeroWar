package com.mygdx.game.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.Components.BodyComponent;
import com.mygdx.game.Components.WaterFloorComponent;
import com.mygdx.game.Managers.Mapper;

public class WaterFloorSystem extends IteratingSystem {
    private Entity player;
    //private ComponentMapper<BodyComponent> bm = ComponentMapper.getFor(BodyComponent.class);

    public WaterFloorSystem(Entity player) {
        super(Family.all(WaterFloorComponent.class).get());
        this.player = player;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // get current y level of player entity
        float currentyLevel = player.getComponent(BodyComponent.class).body.getPosition().y;
        // get the body component of the wall we're updating
        Body bod = Mapper.bodyMapper.get(entity).body;

        float speed = (currentyLevel / 300);

        speed = speed>1?1:speed;

        bod.setTransform(bod.getPosition().x, bod.getPosition().y+speed, bod.getAngle());
    }

}
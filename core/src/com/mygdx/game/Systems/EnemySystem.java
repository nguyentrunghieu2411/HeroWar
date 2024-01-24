package com.mygdx.game.Systems;


import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.Components.BodyComponent;
import com.mygdx.game.Components.EnemyComponent;
import com.mygdx.game.Managers.Mapper;

public class EnemySystem extends IteratingSystem{

    //private ComponentMapper<EnemyComponent> em;
    //private ComponentMapper<BodyComponent> bodm;

    @SuppressWarnings("unchecked")
    public EnemySystem(){
        super(Family.all(EnemyComponent.class).get());
        //em = ComponentMapper.getFor(EnemyComponent.class);
        //bodm = ComponentMapper.getFor(BodyComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        EnemyComponent enemyCom = Mapper.enemyMapper.get(entity);		// get EnemyComponent
        BodyComponent bodyCom = Mapper.bodyMapper.get(entity);	// get B2dBodyComponent

        // get distance of enemy from its original start position (pad center)
        float distFromOrig = Math.abs(enemyCom.xPosCenter - bodyCom.body.getPosition().x);

        // if distance > 1 swap direction
        enemyCom.isGoingLeft = (distFromOrig > 1)? !enemyCom.isGoingLeft:enemyCom.isGoingLeft;

        // set speed base on direction
        float speed = enemyCom.isGoingLeft?-0.01f:0.01f;

        // apply speed to body
        bodyCom.body.setTransform(bodyCom.body.getPosition().x + speed,
                bodyCom.body.getPosition().y,
                bodyCom.body.getAngle());
        if(enemyCom.isDead) {
            bodyCom.isDead = true;
        }
    }
}
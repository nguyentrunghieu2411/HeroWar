package com.mygdx.game.Systems;



import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.Components.BodyComponent;
import com.mygdx.game.Components.BulletComponent;
import com.mygdx.game.Managers.Mapper;

public class BulletSystem extends IteratingSystem{
    Entity player;

    @SuppressWarnings("unchecked")
    public BulletSystem(Entity player){
        super(Family.all(BulletComponent.class).get());
        this.player = player;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        //get box 2d body and bullet components
        BodyComponent b2body = Mapper.bodyMapper.get(entity);
        BulletComponent bullet = Mapper.bulletMapper.get(entity);

        // apply bullet velocity to bullet body
        b2body.body.setLinearVelocity(bullet.xVel, bullet.yVel);
        // get player pos
        BodyComponent playerBodyComp = Mapper.bodyMapper.get(player);
        float px = playerBodyComp.body.getPosition().x;
        float py = playerBodyComp.body.getPosition().y;

        //get bullet pos
        float bx = b2body.body.getPosition().x;
        float by = b2body.body.getPosition().y;

        // if bullet is 20 units away from player on any axis then it is probably off screen
        if(bx - px > 20 || by - py > 20){
            bullet.isDead = true;
        }

        //check if bullet is dead
        if(bullet.isDead){
            b2body.isDead = true;
        }
    }
}
package com.mygdx.game.Systems;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Managers.KeyboardController;
import com.mygdx.game.Components.BodyComponent;
import com.mygdx.game.Components.PlayerComponent;
import com.mygdx.game.Components.StateComponent;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.Managers.Mapper;
import com.mygdx.game.Map.LevelFactory;

import java.util.Map;

// cập nhật trạng thái và tốc độ của player khi nhận input
public class PlayerControlSystem extends IteratingSystem{

   // ComponentMapper<PlayerComponent> playerMap;
   // ComponentMapper<BodyComponent> bodyMap;
   // ComponentMapper<StateComponent> stateMap;
    KeyboardController controller;
    private LevelFactory lvlFactory;

    @SuppressWarnings("unchecked")
    public PlayerControlSystem(KeyboardController keyCon, LevelFactory lvlFactory) {
        super(Family.all(PlayerComponent.class).get());
        controller = keyCon;
       // playerMap = ComponentMapper.getFor(PlayerComponent.class);
        //bodyMap = ComponentMapper.getFor(BodyComponent.class);
        //stateMap = ComponentMapper.getFor(StateComponent.class);
        this.lvlFactory = lvlFactory;
    }
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BodyComponent entityBody = Mapper.bodyMapper.get(entity);
        StateComponent state = Mapper.stateMapper.get(entity);
        PlayerComponent player = Mapper.playerMapper.get(entity);

        player.timeSinceLastShot -= deltaTime;
        //kiểm tra trạng thái của entity dựa trên tốc độ của nó

        //nhảy
        if(entityBody.body.getLinearVelocity().y > 0 && state.get() != StateComponent.STATE_FALLING){
            state.set(StateComponent.STATE_FALLING);
        }

        // đang ở trên đất
        if(entityBody.body.getLinearVelocity().y == 0){
            //tiếp đất sau khi nhảy
            if(state.get() == StateComponent.STATE_FALLING){
                state.set(StateComponent.STATE_NORMAL);
            }
            // đang di chuyển trên đất
            if(entityBody.body.getLinearVelocity().x != 0 && state.get() != StateComponent.STATE_MOVING){
                state.set(StateComponent.STATE_MOVING);
            }
        }

        if(entityBody.body.getLinearVelocity().y < 0 && state.get() == StateComponent.STATE_FALLING){
            // player is actually falling. check if they are on platform
            if(player.onPlatform){
                //overwrite old y value with 0 t stop falling but keep x vel
                entityBody.body.setLinearVelocity(entityBody.body.getLinearVelocity().x, 0f);
            }
        }

        // make player jump very high
        if(player.onSpring){
            entityBody.body.applyLinearImpulse(0, 175f, entityBody.body.getWorldCenter().x,entityBody.body.getWorldCenter().y, true);
            state.set(StateComponent.STATE_JUMPING);
            player.onSpring = false;
        }



        //khi ấn nút thì sẽ thay đổi vận tốc của entity
        if(controller.left){
            entityBody.body.setLinearVelocity(MathUtils.lerp(entityBody.body.getLinearVelocity().x, -5f, 0.2f),entityBody.body.getLinearVelocity().y);
        }
        if(controller.right){
            entityBody.body.setLinearVelocity(MathUtils.lerp(entityBody.body.getLinearVelocity().x, 5f, 0.2f),entityBody.body.getLinearVelocity().y);
        }

        if(!controller.left && ! controller.right){
            entityBody.body.setLinearVelocity(MathUtils.lerp(entityBody.body.getLinearVelocity().x, 0, 0.1f),entityBody.body.getLinearVelocity().y);
        }

        if(controller.up &&
                (state.get() == StateComponent.STATE_NORMAL || state.get() == StateComponent.STATE_MOVING)){
            //b2body.body.applyForceToCenter(0, 3000,true);
            entityBody.body.applyLinearImpulse(0, 75f, entityBody.body.getWorldCenter().x,entityBody.body.getWorldCenter().y, true);
            state.set(StateComponent.STATE_JUMPING);
        }

        if(controller.isMouse1Down){ // if mouse button is pressed
            // user wants to fire
            if(player.timeSinceLastShot <= 0){ // check the player hasn't just shot
                //player can shoot so do player shoot
                Vector3 mousePos = new Vector3(controller.mouseLocation.x,controller.mouseLocation.y,0); // get mouse position
                player.cam.unproject(mousePos); // convert position from screen to box2d world position
                float speed = 10f;  // set the speed of the bullet
                float shooterX = entityBody.body.getPosition().x; // get player location
                float shooterY = entityBody.body.getPosition().y; // get player location
                float velx = mousePos.x - shooterX; // get distance from shooter to target on x plain
                float vely = mousePos.y  - shooterY; // get distance from shooter to target on y plain
                float length = (float) Math.sqrt(velx * velx + vely * vely); // get distance to target direct
                if (length != 0) {
                    velx = velx / length;  // get required x velocity to aim at target
                    vely = vely / length;  // get required y velocity to aim at target
                }
                // create a bullet
                lvlFactory.createBullet(shooterX, shooterY, velx*speed, vely*speed);
                //reset timeSinceLastShot
                player.timeSinceLastShot = player.shootDelay;
            }
        }
    }
}
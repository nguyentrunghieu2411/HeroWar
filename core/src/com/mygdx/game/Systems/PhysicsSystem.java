package com.mygdx.game.Systems;

import com.badlogic.ashley.core.*;
import com.mygdx.game.Components.BodyComponent;
import com.mygdx.game.Components.PositionComponent;

import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Managers.Mapper;


// xử lí quy trình cập nhật vật lí cho thế giới box2d và cập nhật trạng thái của entity
public class PhysicsSystem extends IteratingSystem {

    private static final float MAX_STEP_TIME = 1/45f;
    private static float accumulator = 0f;

    private World world;
    private Array<Entity> bodiesQueue;

    PooledEngine engine;

    //private ComponentMapper<BodyComponent> bodyMap = ComponentMapper.getFor(BodyComponent.class);
    //private ComponentMapper<PositionComponent> positionMap = ComponentMapper.getFor(PositionComponent.class);

    @SuppressWarnings("unchecked")
    public PhysicsSystem(World world, PooledEngine engine) {
        super(Family.all(BodyComponent.class, PositionComponent.class).get());
        this.world = world;
        this.bodiesQueue = new Array<>();
        this.engine = engine;
    }

    // cập nhật lại position component cho giống body khi body thay đổi vị trí
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        // khi thời gian giữa các khung hình quá lớn thì cần phải cập nhật thế giới nhiều lần
        
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        if(accumulator >= MAX_STEP_TIME) {
            // điều chỉnh time step của box2d world
            world.step(MAX_STEP_TIME, 6, 2);
            accumulator -= MAX_STEP_TIME;

            //Entity Queue
            // cập nhật các position component của entity cho khớp với body
            for (Entity entity : bodiesQueue) {
                PositionComponent tfm = Mapper.positionMapper.get(entity);
                BodyComponent bodyComp = Mapper.bodyMapper.get(entity);
                Vector2 position = bodyComp.body.getPosition();
                tfm.position.x = position.x;
                tfm.position.y = position.y;
                tfm.rotation = bodyComp.body.getAngle() * MathUtils.radiansToDegrees;
                if(bodyComp.isDead){
                    System.out.println("Removing a body and entity");
                    world.destroyBody(bodyComp.body);
                    engine.removeEntity(entity);
                }
            }
        }
        bodiesQueue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        bodiesQueue.add(entity);
    }
}
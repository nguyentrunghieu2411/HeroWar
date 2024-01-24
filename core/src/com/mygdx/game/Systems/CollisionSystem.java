package com.mygdx.game.Systems;

import com.mygdx.game.Components.*;


import com.mygdx.game.Managers.Mapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

// xử lí việc sau khi các va chạm xảy ra
public class CollisionSystem  extends IteratingSystem {
    //private final ComponentMapper<EnemyComponent> enemyMapper;
    //ComponentMapper<CollisionComponent> collisionMapper; // mapper (khác cấu trúc dl map) dùng để lấy 1 component từ 1 entity (dòng 29) hiệu suât hơn cách trực tiếp(dòng 35)
    ///ComponentMapper<PlayerComponent> playerMapper;


    @SuppressWarnings("unchecked")
    public CollisionSystem() {
        // chỉ xử lí các entity có các thuộc tính này
        super(Family.all(CollisionComponent.class).get());

        //collisionMapper = ComponentMapper.getFor(CollisionComponent.class);
        //playerMapper = ComponentMapper.getFor(PlayerComponent.class);
        //enemyMapper = ComponentMapper.getFor(EnemyComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // get entity collision component
        CollisionComponent cc = Mapper.collisionMapper.get(entity); // dùng mapper sẽ hiệu suất hơn dùng như ở dòng get TypeComponet(dòng 35 lúc đoạn comment này được viết)

        // lấy đối tượng va chạm với entity
        Entity collidedEntity = cc.collisionEntity;

        PlayerComponent player = Mapper.playerMapper.get(entity);
        // kiểm tra nó thuộc loại đối tượng nào dựa trên đó xử lí va chạm
        if(collidedEntity != null){
            TypeComponent colliedEntityType = collidedEntity.getComponent(TypeComponent.class);
            if(colliedEntityType != null){
                TypeComponent entityType = entity.getComponent(TypeComponent.class);
                if(entityType.type == TypeComponent.PLAYER)
                {
                    switch(colliedEntityType.type){
                        case TypeComponent.ENEMY:
                            System.out.println("player hit enemy");
                            player.isDead = true;
                            int score = (int) player.cam.position.y;
                            System.out.println(score);
                            break;
                        case TypeComponent.SCENERY:
                            //do player hit scenery thing
                            player.onPlatform = true;
                            System.out.println("player hit scenery");
                            break;
                        case TypeComponent.SPRING:
                            //do player hit other thing
                            player.onSpring = true;
                            System.out.println("player hit spring: bounce up");
                            break;
                        case TypeComponent.BULLET:
                            System.out.println("Player just shot. bullet in player atm");
                            break;
                        case TypeComponent.OTHER:
                            //do player hit other thing
                            System.out.println("player hit other");
                            break; //technically this isn't needed
                    }
                } else if (entityType.type == TypeComponent.ENEMY) {
                    switch(colliedEntityType.type){
                        case TypeComponent.PLAYER:
                           // System.out.println("enemy hit player");
                            break;
                        case TypeComponent.ENEMY:
                           // System.out.println("enemy hit enemy");
                            break;
                        case TypeComponent.SCENERY:
                           // System.out.println("enemy hit scenery");
                            break;
                        case TypeComponent.SPRING:
                            //System.out.println("enemy hit spring");
                            break;
                        case TypeComponent.OTHER:
                            //System.out.println("enemy hit other");
                            break;
                        case TypeComponent.BULLET:
                            EnemyComponent enemy = Mapper.enemyMapper.get(entity);
                            enemy.isDead = true;
                            collidedEntity.getComponent(BulletComponent.class).isDead = true;
                            System.out.println("enemy got shot");
                        default:
                            System.out.println("No matching type found");
                    }
                }

                cc.collisionEntity = null; // đặt lại để dùng cho lần va chạm mới
            }
        }

    }

}
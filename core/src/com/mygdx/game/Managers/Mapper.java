package com.mygdx.game.Managers;
import com.badlogic.ashley.core.ComponentMapper;
import com.mygdx.game.Components.*;
public class Mapper {
    public static ComponentMapper<PlayerComponent> playerMapper = ComponentMapper.getFor(PlayerComponent.class);;
    public static ComponentMapper<EnemyComponent> enemyMapper = ComponentMapper.getFor(EnemyComponent.class);;
    public static ComponentMapper<BodyComponent> bodyMapper = ComponentMapper.getFor(BodyComponent.class);;
    public static ComponentMapper<CollisionComponent> collisionMapper = ComponentMapper.getFor(CollisionComponent.class);;
    public static ComponentMapper<AnimationComponent> animationMapper = ComponentMapper.getFor(AnimationComponent.class);;
    public static ComponentMapper<BulletComponent> bulletMapper = ComponentMapper.getFor(BulletComponent.class);;
    public static ComponentMapper<StateComponent> stateMapper = ComponentMapper.getFor(StateComponent.class);;
    public static ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);;
    public static ComponentMapper<TextureComponent> textureMapper = ComponentMapper.getFor(TextureComponent.class);;
    public static ComponentMapper<TypeComponent> typeMapper = ComponentMapper.getFor(TypeComponent.class);;
    public static ComponentMapper<WaterFloorComponent> waterMapper = ComponentMapper.getFor(WaterFloorComponent.class);;
}

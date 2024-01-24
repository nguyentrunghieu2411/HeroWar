package com.mygdx.game.Systems;



import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.Components.PlayerComponent;
import com.mygdx.game.Components.PositionComponent;
import com.mygdx.game.Managers.Mapper;
import com.mygdx.game.LevelFactory;

public class LevelGenerationSystem extends IteratingSystem {

    // get transform component so we can check players height
    //private ComponentMapper<PositionComponent> tm = ComponentMapper.getFor(PositionComponent.class);
    private LevelFactory lf;

    public LevelGenerationSystem(LevelFactory lvlFactory){
        super(Family.all(PlayerComponent.class).get());
        lf = lvlFactory;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent trans = Mapper.positionMapper.get(entity);
        int currentPosition = (int) trans.position.y ;
        // nếu tọa độ y của player + 7 > level cao nhất của map hiện tại thì generate thêm
        if((currentPosition + 7) > lf.currentLevel){
            lf.generateLevel(currentPosition + 7);
        }
    }
}
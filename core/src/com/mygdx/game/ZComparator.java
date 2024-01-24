package com.mygdx.game;

import com.mygdx.game.Components.PositionComponent;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import java.util.Comparator;


public class ZComparator implements Comparator<Entity> {
    private ComponentMapper<PositionComponent> posMapper;

    public ZComparator(){
        posMapper = ComponentMapper.getFor(PositionComponent.class);
    }

    @Override
    //so sánh dựa trên biến z của position
    // z bé thì vẽ trước lớn vẽ sau
    public int compare(Entity entityA, Entity entityB) {

        if (posMapper.get(entityA) == null || posMapper.get(entityA).position == null) {
            throw new NullPointerException("PositionComponent of entityA is null or its position is null");
        }

        if (posMapper.get(entityB) == null || posMapper.get(entityB).position == null) {
            throw new NullPointerException("PositionComponent of entityB is null or its position is null");
        }
        float az = posMapper.get(entityA).position.z;
        float bz = posMapper.get(entityB).position.z;
        int res = 0;
        if(az > bz){
            res = 1;
        }else if(az < bz){
            res = -1;
        }
        return res;
    }
}
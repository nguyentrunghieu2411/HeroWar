package com.mygdx.game.Systems;

import com.mygdx.game.Components.TextureComponent;
import com.mygdx.game.Components.StateComponent;
import com.mygdx.game.Components.AnimationComponent;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

// lấy ra frame thích hợp cho đối tượng
public class AnimationSystem extends IteratingSystem {

    ComponentMapper<TextureComponent> texMapper;
    ComponentMapper<AnimationComponent> animationMapper;
    ComponentMapper<StateComponent> stateMapper;

    @SuppressWarnings("unchecked")
    public AnimationSystem(){
        super(Family.all(TextureComponent.class,
                AnimationComponent.class,
                StateComponent.class).get());

        texMapper = ComponentMapper.getFor(TextureComponent.class);
        animationMapper = ComponentMapper.getFor(AnimationComponent.class);
        stateMapper = ComponentMapper.getFor(StateComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        AnimationComponent ani = animationMapper.get(entity);
        StateComponent state = stateMapper.get(entity);

        // trong intMap có chứa animation cho trạng thái state không
        // nếu có thì đặt region của textureComponent là hình ảnh tương ứng với state và time
        if(ani.animations.containsKey(state.get())){
            TextureComponent tex = texMapper.get(entity);
            tex.region = ani.animations.get(state.get()).getKeyFrame(state.time, state.isLooping);
        }
        // update thời gian
        state.time += deltaTime;
    }
}
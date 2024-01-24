package com.mygdx.game.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
// Store player state
public class StateComponent implements Component, Poolable {
    public static final int STATE_NORMAL = 0;
    public static final int STATE_JUMPING = 1;
    public static final int STATE_FALLING = 2;
    public static final int STATE_MOVING = 3;
    public static final int STATE_ATTACK = 4;
    public static final int STATE_DEFEND = 5;

    private int state = 0;
    public float time = 0.0f; // bộ đếm để xác định frame nào trong 1 animation
    public boolean isLooping = false;

    //get set trạng thái state
    public void set(int newState){
        state = newState;
        time = 0.0f;
    }

    public int get(){
        return state;
    }

    @Override
    public void reset() {
        state = 0;
        time = 0.0f;
        isLooping = false;
    }
}
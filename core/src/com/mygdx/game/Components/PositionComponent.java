package com.mygdx.game.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool.Poolable;

//tọa độ của entity = tọa độ tâm của nó
//khi đặt vậy thì đối tượng có thể xoay xung quanh tâm của nó (tức tọa độ ta đã đặt)
public class PositionComponent implements Component , Poolable {

    public final Vector3 position = new Vector3();
    public final Vector2 scale = new Vector2(1.0f, 1.0f); // tỉ lệ với kích thước thật
    public float rotation = 1f;
    public boolean isHidden = false; // trong suốt

    @Override
    public void reset() {
        rotation = 0.0f;
        isHidden = false;
    }
}

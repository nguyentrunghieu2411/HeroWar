package com.mygdx.game.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.IntMap;

public class AnimationComponent implements Component, Poolable {

    // 1 IntMap (key là int) chứa các animation của các trạng thái state của entity ( đại diện là các int)
    public IntMap<Animation<TextureRegion>> animations = new IntMap<>();

    @Override
    public void reset() {
        animations.clear();
    }
}
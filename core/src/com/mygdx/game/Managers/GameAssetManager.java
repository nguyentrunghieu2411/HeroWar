package com.mygdx.game.Managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameAssetManager {
    public AssetManager manager ;
    public final String gameImages = "images/game.atlas";
    public final String loadingImages = "images/loading.atlas";
    // Sound Effect
    public final String pingSound = "sound/ping.mp3";
    public final String waterSound = "sound/water.wav";

    // Music
    public final String playingSong = "music/rain.mp3";
    public final String skin = "skin/uiskin.json";


    public GameAssetManager() {
        manager = new AssetManager();
    }

//    public void queueAddImages(){
//        manager.load(playerImage, Texture.class);
//        manager.load(enemyImage, Texture.class);
//    }
    public void queueAddImages(){
        manager.load(gameImages, TextureAtlas.class);
    }

    public void queueAddLoadingImages(){
        manager.load(loadingImages, TextureAtlas.class);
    }

    public void queueAddSounds(){
        manager.load(waterSound , Sound.class);
        manager.load(pingSound, Sound.class);
    }

    public void queueAddMusic(){
        manager.load(playingSong, Music.class);
    }

    public void queueAddSkin(){
        SkinParameter params = new SkinParameter("skin/uiskin.atlas");
        manager.load(skin, Skin.class, params);
    }
    public void queueAddFonts(){
    }

    public void queueAddParticleEffects(){
    }
}

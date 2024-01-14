package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;



public class MyGdxGame extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	public Texture menuImage;
	public AppSetting setting;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont(); // use libGDX's default Arial font
		font.getData().setScale(1);//set font size (default is 1)
		setting = new AppSetting();
		this.menuImage = new Texture(Gdx.files.internal("images.jpg"));
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render() {
		super.render(); // important!
	}


	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
	}

	public AppSetting getPreferences() {
		return this.setting;
	}
}

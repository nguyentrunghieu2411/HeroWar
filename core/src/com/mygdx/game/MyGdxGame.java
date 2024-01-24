package com.mygdx.game;

import com.badlogic.gdx.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Managers.AppSetting;
import com.mygdx.game.Managers.GameAssetManager;
import com.mygdx.game.Screens.GameScreen;
import com.mygdx.game.Screens.LoadingScreen;
import com.mygdx.game.Screens.MainMenuScreen;
import com.mygdx.game.Screens.SettingScreen;


public class MyGdxGame extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	public Texture menuImage;
	public AppSetting setting;
	public GameAssetManager assetManager;


	private LoadingScreen loadingScreen;
	private SettingScreen settingScreen;
	private MainMenuScreen mainMenuScreen;
	private GameScreen gameScreen;
	//private EndScreen endScreen;

	public final static int MAINMENU = 0;
	public final static int SETTING = 1;
	public final static int GAMESCREEN = 2;
	public final static int ENDGAME = 3;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont(); // use libGDX's default Arial font
		font.getData().setScale(1);//set font size (default is 1)
		setting = new AppSetting();
		assetManager = new GameAssetManager();
		loadingScreen = new LoadingScreen(this);
		setScreen(loadingScreen);
	}


	@Override
	public void render() {
		super.render(); // important!
	}


	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
		assetManager.manager.dispose();
	}

	public AppSetting getPreferences() {
		return this.setting;
	}

	public void changeScreen(int screen){
		switch(screen){
			case MAINMENU:
				if(mainMenuScreen == null) mainMenuScreen = new MainMenuScreen(this); // added (this)
				this.setScreen(mainMenuScreen);
				break;
			case SETTING:
				if(settingScreen == null) settingScreen = new SettingScreen(this); // added (this)
				this.setScreen(settingScreen);
				break;
			case GAMESCREEN:
				if(gameScreen == null) gameScreen = new GameScreen(this); //added (this)
				this.setScreen(gameScreen);
				break;
//			case ENDGAME:
//				if(endScreen == null) endScreen = new EndScreen(this);  // added (this)
//				this.setScreen(endScreen);
//				break;
		}
	}
}

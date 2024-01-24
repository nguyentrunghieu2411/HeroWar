package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Managers.LoadingBarActor;
import com.mygdx.game.MyGdxGame;

public class LoadingScreen implements Screen {



    public MyGdxGame game;
    private final Stage stage;
    private TextureAtlas.AtlasRegion title;
    private TextureAtlas.AtlasRegion dash;

    public final int IMAGE = 0;		// loading images
    public final int FONT = 1;		// loading fonts
    public final int PARTY = 2;		// loading particle effects
    public final int SOUND = 3;		// loading sounds
    public final int MUSIC = 4;		// loading music

    private int currentLoadingStage = 0;

    // timer for exiting loading screen
    public float countDown = 5f; // 5 seconds of waiting before menu screen
    private Animation<AtlasRegion> flameAnimation;
    private Table loadingTable;
    private AtlasRegion background;
    private AtlasRegion copyright;

    public LoadingScreen(MyGdxGame game){
        this.game = game;
        stage = new Stage(new ScreenViewport());

        loadLoadingAssets();
        // initiate queueing of images but don't start loading
        game.assetManager.queueAddImages();
        System.out.println("Loading images....");
    }

    @Override
    public void show() {

        Image titleImage = new Image(title);
        Table table = new Table();
        table.setBackground(new TiledDrawable(background));
        table.setFillParent(true);
        table.setDebug(false);

        loadingTable = new Table();
        loadingTable.add(new LoadingBarActor(dash,flameAnimation));
        loadingTable.add(new LoadingBarActor(dash,flameAnimation));
        loadingTable.add(new LoadingBarActor(dash,flameAnimation));
        loadingTable.add(new LoadingBarActor(dash,flameAnimation));
        loadingTable.add(new LoadingBarActor(dash,flameAnimation));
        loadingTable.add(new LoadingBarActor(dash,flameAnimation));
        loadingTable.add(new LoadingBarActor(dash,flameAnimation));
        loadingTable.add(new LoadingBarActor(dash,flameAnimation));
        loadingTable.add(new LoadingBarActor(dash,flameAnimation));
        loadingTable.add(new LoadingBarActor(dash,flameAnimation));


        table.add(titleImage).align(Align.center).pad(10, 0, 0, 0).colspan(10);
        table.row(); // move to next row
        table.add(loadingTable).width(400);
        table.row();
        Image copyrightImage = new Image(copyright);
        table.add(copyrightImage).align(Align.center).pad(200, 0, 0, 0).colspan(10);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1); //  clear the screen
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // check if the asset manager has finished loading
        if (game.assetManager.manager.update()) { // Load some, will return true if done loading
            currentLoadingStage+= 1;
            if(currentLoadingStage <= 5){
                loadingTable.getCells().get((currentLoadingStage-1)*2).getActor().setVisible(true);  // new
                loadingTable.getCells().get((currentLoadingStage-1)*2+1).getActor().setVisible(true);
            }
            
            switch(currentLoadingStage){
                case FONT:
                    System.out.println("Loading fonts....");
                    game.assetManager.queueAddFonts(); // first load done, now start fonts
                    break;
                case PARTY:
                    System.out.println("Loading Particle Effects....");
                    game.assetManager.queueAddParticleEffects(); // fonts are done now do party effects
                    break;
                case SOUND:
                    System.out.println("Loading Sounds....");
                    game.assetManager.queueAddSounds();
                    break;
                case MUSIC:
                    System.out.println("Loading fonts....");
                    game.assetManager.queueAddMusic();
                    break;
                case 5:
                    System.out.println("Finished"); // all done
                    break;
            }
            if (currentLoadingStage >5){
                countDown -= delta;  // timer to stay on loading screen for short preiod once done loading
                currentLoadingStage = 5;  // cap loading stage to 5 as will use later to display progress bar anbd more than 5 would go off the screen
                if(countDown < 0){ // countdown is complete
                    game.changeScreen(MyGdxGame.MAINMENU);  /// go to menu screen
                }
            }
        }
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    private void loadLoadingAssets() {
        game.assetManager.queueAddLoadingImages();
        game.assetManager.manager.finishLoading();

// get images used to display loading progress
        TextureAtlas atlas = game.assetManager.manager.get("images/loading.atlas");
        title = atlas.findRegion("staying-alight-logo");
        dash = atlas.findRegion("loading-dash");
        flameAnimation = new Animation<>(0.07f, atlas.findRegions("flames/flames"), Animation.PlayMode.LOOP);
        background = atlas.findRegion("flamebackground");
        copyright = atlas.findRegion("copyright");
    }
}

package com.mygdx.game.Screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Managers.KeyboardController;
import com.mygdx.game.Map.LevelFactory;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Physics.GameContactListener;
import com.mygdx.game.Systems.*;
import com.mygdx.game.Utils;


public class GameScreen implements Screen {

    private final KeyboardController controller;
    private final TextureAtlas atlas;
    private final PooledEngine engine;
    //private final BodyFactory bodyFactory;
    private final Sound ping;
    private final Sound waterSound;
    private final LevelFactory lvlFactory;
    public AtlasRegion playerTexture;
    MyGdxGame game;

    SpriteBatch batch;

    private final OrthographicCamera cam;
 
    private Music song;


    public GameScreen(final MyGdxGame game) {
        this.game = game;
        controller = new KeyboardController();
        game.assetManager.queueAddSounds();
        game.assetManager.manager.finishLoading();
        atlas = game.assetManager.manager.get("images/game.atlas", TextureAtlas.class);
        ping = game.assetManager.manager.get("sound/ping.mp3");
        waterSound = game.assetManager.manager.get("sound/water.wav");

        batch = new SpriteBatch();
        // Create our new rendering system
        RenderingSystem renderingSystem = new RenderingSystem(batch);
        cam = renderingSystem.getCamera();
        batch.setProjectionMatrix(cam.combined);

        //create a pooled engine
        engine = new PooledEngine();

        lvlFactory = new LevelFactory(engine,atlas);
        lvlFactory.world.setContactListener(new GameContactListener());
        // add all the relevant systems our engine should run
        engine.addSystem(new AnimationSystem());
        engine.addSystem(new PhysicsSystem(lvlFactory.world, engine));
        engine.addSystem(renderingSystem);
        engine.addSystem(new PhysicsDebugSystem(lvlFactory.world, renderingSystem.getCamera()));
        engine.addSystem(new CollisionSystem());
        engine.addSystem(new PlayerControlSystem(controller,lvlFactory));
        engine.addSystem(new EnemySystem());
        Entity player = lvlFactory.createPlayer(atlas.findRegion("player"),cam);
        engine.addSystem(new WaterFloorSystem(player));
        engine.addSystem(new WallSystem(player));
        engine.addSystem(new BulletSystem(player));
        engine.addSystem(new LevelGenerationSystem(lvlFactory));

        lvlFactory.createFloor();
        lvlFactory.createWaterFloor();

        int wallWidth = (int) (1*RenderingSystem.PPM);
        int wallHeight = (int) (60*RenderingSystem.PPM);
        TextureRegion wallRegion = Utils.makeTextureRegion(wallWidth, wallHeight, "222222FF");
        lvlFactory.createWalls(wallRegion);
        // create some game objects
//        createPlayer();
//        createPlatform(24,12);
//        createPlatform(2,7);
//        createPlatform(7,2);
//        createPlatform(7,7);
//
//        createFloor();


//        int wFloorWidth = (int) (40*RenderingSystem.PPM);
//        int wFloorHeight = (int) (10*RenderingSystem.PPM);
//        TextureRegion wFloorRegion = Utils.makeTextureRegion(wFloorWidth, wFloorHeight, "11113380");
//        lvlFactory.createWaterFloor(wFloorRegion);
    }

    @Override
    public void show() {
        song = game.assetManager.manager.get("music/rain.mp3");
        song.play();
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        engine.update(delta);
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
        song.dispose();
    }
//    private void createPlayer(){
//
//        // Create the Entity and all the components that will go in the entity
//        Entity entity = engine.createEntity();
//        BodyComponent playerBody = engine.createComponent(BodyComponent.class);
//        PositionComponent position = engine.createComponent(PositionComponent.class);
//        TextureComponent texture = engine.createComponent(TextureComponent.class);
//        PlayerComponent player = engine.createComponent(PlayerComponent.class);
//        CollisionComponent colComp = engine.createComponent(CollisionComponent.class);
//        TypeComponent type = engine.createComponent(TypeComponent.class);
//        StateComponent stateCom = engine.createComponent(StateComponent.class);
//
//        // create the data for the components and add them to the components
//        // đặt bán kính là 1 vì ảnh là 32x32 1 mét ứng với 32 pixel như đã định nghĩa ở render system
//        // nếu muốn đặt bán kính lớn hơn thì ta phải đặt lại scale ở position component của entity để có thể vẽ ra đúng tỉ lệ
//        playerBody.body = bodyFactory.makeCirclePolyBody(10,10,1, BodyFactory.STONE, BodyType.DynamicBody,true);
//        // set object position (x,y,z) z used to define draw order 0 first drawn
//        position.position.set(10,10,0);
//        texture.region = atlas.findRegion("player");
//        type.type = TypeComponent.PLAYER;
//        stateCom.set(StateComponent.STATE_NORMAL);
//        playerBody.body.setUserData(entity);
//
//        // add the components to the entity
//        entity.add(playerBody);
//        entity.add(position);
//        entity.add(texture);
//        entity.add(player);
//        entity.add(colComp);
//        entity.add(type);
//        entity.add(stateCom);
//
//        // add the entity to the engine
//        engine.addEntity(entity);
//    }
//    private void createPlatform(float x, float y){
//        Entity entity = engine.createEntity();
//        BodyComponent platfromBody = engine.createComponent(BodyComponent.class);
//        platfromBody.body = bodyFactory.makeBoxPolyBody(x, y, 3, 0.2f, BodyFactory.STONE, BodyType.StaticBody);
//        TextureComponent texture = engine.createComponent(TextureComponent.class);
//        texture.region = atlas.findRegion("player");
//        TypeComponent type = engine.createComponent(TypeComponent.class);
//        type.type = TypeComponent.SCENERY;
//        platfromBody.body.setUserData(entity);
//
//        entity.add(platfromBody);
//        entity.add(texture);
//        entity.add(type);
//
//        engine.addEntity(entity);
//
//    }
//
//    private void createFloor(){
//        Entity entity = engine.createEntity();
//        BodyComponent b2dbody = engine.createComponent(BodyComponent.class);
//        b2dbody.body = bodyFactory.makeBoxPolyBody(0, 0, 100, 0.2f, BodyFactory.STONE, BodyType.StaticBody);
//        TextureComponent texture = engine.createComponent(TextureComponent.class);
//        texture.region = atlas.findRegion("player");
//        TypeComponent type = engine.createComponent(TypeComponent.class);
//        type.type = TypeComponent.SCENERY;
//
//        b2dbody.body.setUserData(entity);
//
//        entity.add(b2dbody);
//        entity.add(texture);
//        entity.add(type);
//
//        engine.addEntity(entity);
//    }
}

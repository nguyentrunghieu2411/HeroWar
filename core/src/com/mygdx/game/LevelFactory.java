package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.Map.OpenSimplexNoise;
import com.mygdx.game.Systems.RenderingSystem;
import com.mygdx.game.Utils;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Components.*;
import com.mygdx.game.Physics.BodyFactory;
import com.mygdx.game.Physics.GameContactListener;

public class LevelFactory {
    private final TextureAtlas atlas;
    private final TextureRegion enemyTex;
    private final TextureRegion platformTex;
    private BodyFactory bodyFactory;
    public World world;
    private PooledEngine engine;
    private OpenSimplexNoise openSim;
    public int currentLevel = 0;
    private TextureRegion floorTex;
    private TextureRegion bulletTex;
    private TextureRegion waterTex;

    public LevelFactory(PooledEngine en, TextureAtlas atlas){
        engine = en;
        this.atlas = atlas;
        floorTex = atlas.findRegion("reallybadlydrawndirt");
        enemyTex = atlas.findRegion("enemy");
        bulletTex = Utils.makeTextureRegion(10,10,"444444FF");
        platformTex = atlas.findRegion("platform");
        waterTex = atlas.findRegion("water");
        bulletTex = Utils.makeTextureRegion(2 * RenderingSystem.PPM, 0.1f * RenderingSystem.PPM, "221122FF");
        world = new World(new Vector2(0,-10f), true);
        world.setContactListener(new GameContactListener());
        bodyFactory = BodyFactory.getInstance(world);

        // create a new SimplexNoise (seed)
        openSim = new OpenSimplexNoise(MathUtils.random(2000L));


    }


    /** Creates a pair of platforms per level up to yLevel
     * @param ylevel tọa độ y của vật + 7
     */
    public void generateLevel(int ylevel){
        while(ylevel > currentLevel){
            for(int i = 1; i < 5; i ++){
                generateSingleColumn(i);
            }
            currentLevel++;
        }
    }

    private float genNForL(int level, int height){
        return (float)openSim.eval(height, level);
    }

    // tạo ra những vùng nảy
    public void createBouncyPlatform(float x, float y){
        Entity entity = engine.createEntity();
        // create body component
        BodyComponent b2dbody = engine.createComponent(BodyComponent.class);
        b2dbody.body = bodyFactory.makeBoxPolyBody(x, y, .5f, 0.5f, BodyFactory.STONE, BodyType.StaticBody);
        //make it a sensor so not to impede movement
        bodyFactory.makeAllFixturesSensors(b2dbody.body);

        // give it a texture..todo get another texture and anim for springy action
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        texture.region = floorTex;

        TypeComponent type = engine.createComponent(TypeComponent.class);
        type.type = TypeComponent.SPRING;

        b2dbody.body.setUserData(entity);
        entity.add(b2dbody);
        entity.add(texture);
        entity.add(type);
        engine.addEntity(entity);

    }

    public void createPlatform(float x, float y){
        Entity entity = engine.createEntity();
        BodyComponent b2dbody = engine.createComponent(BodyComponent.class);
        b2dbody.body = bodyFactory.makeBoxPolyBody(x, y, 1.5f, 0.2f, BodyFactory.STONE, BodyType.StaticBody);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        texture.region = platformTex;
        PositionComponent position = engine.createComponent(PositionComponent.class);
        position.position.set(x,y,0);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        type.type = TypeComponent.SCENERY;
        b2dbody.body.setUserData(entity);
        entity.add(b2dbody);
        entity.add(texture);
        entity.add(type);
        entity.add(position);
        engine.addEntity(entity);
    }

    public void createFloor(){
        Entity entity = engine.createEntity();
        BodyComponent b2dbody = engine.createComponent(BodyComponent.class);
        PositionComponent position = engine.createComponent(PositionComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);

        position.position.set(20,0,0);
        texture.region = floorTex;
        type.type = TypeComponent.SCENERY;
        b2dbody.body = bodyFactory.makeBoxPolyBody(20, -16, 40, 25  , BodyFactory.STONE, BodyType.StaticBody);

        entity.add(b2dbody);
        entity.add(texture);
        entity.add(position);
        entity.add(type);

        b2dbody.body.setUserData(entity);

        engine.addEntity(entity);
    }


    public Entity createPlayer(TextureRegion tex, OrthographicCamera cam){

        Entity entity = engine.createEntity();
        BodyComponent b2dbody = engine.createComponent(BodyComponent.class);
        PositionComponent position = engine.createComponent(PositionComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        PlayerComponent player = engine.createComponent(PlayerComponent.class);
        CollisionComponent colComp = engine.createComponent(CollisionComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        StateComponent stateCom = engine.createComponent(StateComponent.class);
        AnimationComponent animCom = engine.createComponent(AnimationComponent.class);


        player.cam = cam;
        b2dbody.body = bodyFactory.makeCirclePolyBody(10,1,1, BodyFactory.STONE, BodyType.DynamicBody,true);
        // set object position (x,y,z) z used to define draw order 0 first drawn
        Animation<TextureRegion> anim = new Animation<>(0.1f,atlas.findRegions("flame_a"));
        animCom.animations.put(StateComponent.STATE_NORMAL, anim);
        animCom.animations.put(StateComponent.STATE_MOVING, anim);
        animCom.animations.put(StateComponent.STATE_JUMPING, anim);
        animCom.animations.put(StateComponent.STATE_FALLING, anim);
        animCom.animations.put(StateComponent.STATE_ATTACK, anim);
        anim.setPlayMode(Animation.PlayMode.LOOP);

        position.position.set(10,1,0);
        position.scale.x = 1.25f;
        position.scale.y = 1.25f;
        texture.region = tex;
        type.type = TypeComponent.PLAYER;
        stateCom.set(StateComponent.STATE_NORMAL);
        b2dbody.body.setUserData(entity);

        entity.add(b2dbody);
        entity.add(position);
        entity.add(texture);
        entity.add(player);
        entity.add(colComp);
        entity.add(type);
        entity.add(stateCom);
        entity.add(animCom);

        engine.addEntity(entity);
        return entity;
    }

    public Entity createPlayer(TextureAtlas atlas, OrthographicCamera cam){

        Entity entity = engine.createEntity();
        BodyComponent b2dbody = engine.createComponent(BodyComponent.class);
        PositionComponent position = engine.createComponent(PositionComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        PlayerComponent player = engine.createComponent(PlayerComponent.class);
        CollisionComponent colComp = engine.createComponent(CollisionComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        StateComponent stateCom = engine.createComponent(StateComponent.class);
        AnimationComponent animCom = engine.createComponent(AnimationComponent.class);

        player.cam = cam;
        b2dbody.body = bodyFactory.makeCirclePolyBody(10,1,1, BodyFactory.STONE, BodyType.DynamicBody,true);
        // set object position (x,y,z) z used to define draw order 0 first drawn
        Animation<TextureRegion> idle = new Animation<>(0.1f,Utils.spriteSheetToFrames(atlas.findRegion("Combat Ready Idle"), 5 , 1));
        Animation<TextureRegion> jump = new Animation<>(0.1f,Utils.spriteSheetToFrames(atlas.findRegion("Jump"), 4 , 1));
        Animation<TextureRegion> fall = new Animation<>(0.1f,Utils.spriteSheetToFrames(atlas.findRegion("Fall"), 4 , 1));
        Animation<TextureRegion> run = new Animation<>(0.1f,Utils.spriteSheetToFrames(atlas.findRegion("Run"), 6 , 1));
        Animation<TextureRegion> attack = new Animation<>(0.1f,Utils.spriteSheetToFrames(atlas.findRegion("Attack 1"), 10 , 1));
        Animation<TextureRegion> defend = new Animation<>(0.1f,Utils.spriteSheetToFrames(atlas.findRegion("Shield Raise"), 5 , 1));
        animCom.animations.put(StateComponent.STATE_NORMAL, idle);
        animCom.animations.put(StateComponent.STATE_MOVING, run);
        animCom.animations.put(StateComponent.STATE_JUMPING, jump);
        animCom.animations.put(StateComponent.STATE_FALLING, fall);
        animCom.animations.put(StateComponent.STATE_ATTACK, attack);
        animCom.animations.put(StateComponent.STATE_DEFEND, defend);
        idle.setPlayMode(Animation.PlayMode.LOOP);
        run.setPlayMode(Animation.PlayMode.LOOP);

        position.position.set(10,1,0);
        position.scale.x = 1.25f;
        position.scale.y = 1.25f;
        texture.region = idle.getKeyFrame(0);
        type.type = TypeComponent.PLAYER;
        stateCom.set(StateComponent.STATE_NORMAL);
        b2dbody.body.setUserData(entity);

        entity.add(b2dbody);
        entity.add(position);
        entity.add(texture);
        entity.add(player);
        entity.add(colComp);
        entity.add(type);
        entity.add(stateCom);
        entity.add(animCom);

        engine.addEntity(entity);
        return entity;
    }

    /**
     * Creates the water entity that steadily moves upwards towards player
     * @return
     */
    public Entity createWaterFloor(){
        Entity entity = engine.createEntity();
        BodyComponent b2dbody = engine.createComponent(BodyComponent.class);
        PositionComponent position = engine.createComponent(PositionComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        WaterFloorComponent waterFloor = engine.createComponent(WaterFloorComponent.class);

        type.type = TypeComponent.ENEMY;
        texture.region = waterTex;
        b2dbody.body = bodyFactory.makeBoxPolyBody(20,-15,40,15, BodyFactory.STONE, BodyType.KinematicBody,true);
        position.position.set(20,-15,0);
        entity.add(b2dbody);
        entity.add(position);
        entity.add(texture);
        entity.add(type);
        entity.add(waterFloor);

        b2dbody.body.setUserData(entity);

        engine.addEntity(entity);

        return entity;
    }

    public Entity createEnemy(TextureRegion tex, float x, float y){
        Entity entity = engine.createEntity();
        BodyComponent b2dbody = engine.createComponent(BodyComponent.class);
        PositionComponent position = engine.createComponent(PositionComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        EnemyComponent enemy = engine.createComponent(EnemyComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        CollisionComponent collision = engine.createComponent(CollisionComponent.class);

        b2dbody.body = bodyFactory.makeCirclePolyBody(x,y,1, BodyFactory.STONE, BodyType.KinematicBody,true);
        position.position.set(x,y,0);
        texture.region = tex;
        enemy.xPosCenter = x;
        type.type = TypeComponent.ENEMY;


        entity.add(b2dbody);
        entity.add(position);
        entity.add(texture);
        entity.add(enemy);
        entity.add(type);
        entity.add(collision);

        b2dbody.body.setUserData(entity);
        engine.addEntity(entity);

        return entity;
    }

    public void createBullet(float x, float y, float xVel, float yVel) {
        Entity entity = engine.createEntity();

        BodyComponent b2dbody = engine.createComponent(BodyComponent.class);
        PositionComponent position = engine.createComponent(PositionComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        CollisionComponent colComp = engine.createComponent(CollisionComponent.class);
        BulletComponent bul = engine.createComponent(BulletComponent.class);
        AnimationComponent animCom = engine.createComponent(AnimationComponent.class);
        StateComponent stateCom = engine.createComponent(StateComponent.class);

        b2dbody.body = bodyFactory.makeCirclePolyBody(x,y,0.5f, BodyFactory.STONE, BodyType.DynamicBody,true);
        b2dbody.body.setBullet(true); // increase physics computation to limit body travelling through other objects
        bodyFactory.makeAllFixturesSensors(b2dbody.body); // make bullets sensors so they don't move player
        position.position.set(x,y,0);
        texture.region = bulletTex;
        type.type = TypeComponent.BULLET;
        Animation<TextureRegion> anim = new Animation<>(0.05f,Utils.spriteSheetToFrames(atlas.findRegion("FlameSpriteAnimation"), 7, 1));
        anim.setPlayMode(Animation.PlayMode.LOOP);
        animCom.animations.put(0, anim);
        bul.xVel = xVel;
        bul.yVel = yVel;

        entity.add(bul);
        entity.add(colComp);
        entity.add(b2dbody);
        entity.add(position);
        entity.add(texture);
        entity.add(animCom);
        entity.add(stateCom);
        entity.add(type);

        b2dbody.body.setUserData(entity);

        engine.addEntity(entity);
    }

    public void createWalls(TextureRegion tex){

        for(int i = 0; i < 2; i++){
            System.out.println("Making wall "+i);
            Entity entity = engine.createEntity();
            BodyComponent b2dbody = engine.createComponent(BodyComponent.class);
            PositionComponent position = engine.createComponent(PositionComponent.class);
            TextureComponent texture = engine.createComponent(TextureComponent.class);
            TypeComponent type = engine.createComponent(TypeComponent.class);
            WallComponent wallComp = engine.createComponent(WallComponent.class);

            //make wall
            b2dbody.body = b2dbody.body = bodyFactory.makeBoxPolyBody(0+(i*40),30,1,60, BodyFactory.STONE, BodyType.KinematicBody,true);
            position.position.set(0+(i*40), 30, 0);
            texture.region = tex;
            type.type = TypeComponent.SCENERY;

            entity.add(b2dbody);
            entity.add(position);
            entity.add(texture);
            entity.add(type);
            entity.add(wallComp);
            b2dbody.body.setUserData(entity);

            engine.addEntity(entity);
        }
    }
    private void generateSingleColumn(int i) {
        int offset = 10 * i;
        int range = 15;
        if (genNForL(i, currentLevel) > -0.5f) {
            createPlatform(genNForL(i * 100, currentLevel) * range + offset, currentLevel * 2);
            if (genNForL(i * 200, currentLevel) > 0.3f) {
                // add bouncy platform
                createBouncyPlatform(genNForL(i * 100, currentLevel) * range + offset, currentLevel * 2);
            }
            // only make enemies above level 7 (stops insta deaths)
            if (currentLevel > 7) {
                if (genNForL(i * 300, currentLevel) > 0.2f) {
                    // add an enemy
                    createEnemy(enemyTex, genNForL(i * 100, currentLevel) * range + offset, currentLevel * 2 + 1);
                }
            }
        }
    }
}
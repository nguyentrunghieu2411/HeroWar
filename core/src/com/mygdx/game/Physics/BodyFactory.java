package com.mygdx.game.Physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class BodyFactory {

    private World world;
    private static BodyFactory thisInstance;

    public static final int STEEL = 0;
    public static final int WOOD = 1;
    public static final int RUBBER = 2;
    public static final int STONE = 3;
    private final float DEGTORAD = 0.0174533f;
    private BodyFactory(World world){
        this.world = world;
    }
    public static BodyFactory getInstance(World world){
        if(thisInstance == null){
            thisInstance = new BodyFactory(world);
        }
        return thisInstance;
    }

    static public FixtureDef makeFixture(int material, Shape shape){
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        switch(material){
            case STEEL:
                fixtureDef.density = 1f; // mật độ khối lượng - khối lượng riêng
                fixtureDef.friction = 0.3f; // hệ số ma sát
                fixtureDef.restitution = 0.1f; // độ nảy
                break;
            case WOOD:
                fixtureDef.density = 0.5f;
                fixtureDef.friction = 0.7f;
                fixtureDef.restitution = 0.3f;
                break;
            case RUBBER:
                fixtureDef.density = 1f;
                fixtureDef.friction = 0f;
                fixtureDef.restitution = 1f;
                break;
            case STONE:
                fixtureDef.density = 1f;
                fixtureDef.friction = 0.9f;
                fixtureDef.restitution = 0.01f;
            default:
                fixtureDef.density = 7f;
                fixtureDef.friction = 0.5f;
                fixtureDef.restitution = 0.3f;
        }
        return fixtureDef;
    }

    /**
     * Make a Circle object in our world.
     * The makeCirclePolyBody method takes a few arguments. The arguments are:
     *
     * @param posx     The x position the body will be in our world
     * @param posy     The y position our body will be in the world
     * @param radius   The size of our circle’s radius. The circle will be twice this value wide.
     * @param material The material type we just defined earlier(Steel, Wood, Rubber, Stone)
     * @param bodyType The type of body (DynamicBody, StaticBody or KinematicBody)
     */
    public Body makeCirclePolyBody(float posx, float posy, float radius, int material, BodyType bodyType) {
        return makeCirclePolyBody(posx, posy, radius, material, bodyType, false);
    }

    /** Make a Circle object in our world.
     * The makeCirclePolyBody method takes a few arguments. The arguments are:
     * @param  posx The x position the body will be in our world
     * @param posy The y position our body will be in the world
     * @param radius The size of our circle’s radius. The circle will be twice this value wide.
     * @param material The material type we just defined earlier(Steel, Wood, Rubber, Stone)
     * @param bodyType The type of body (DynamicBody, StaticBody or KinematicBody)
     * @param fixedRotation This will be true if we want our body to never rotate.*/
    public Body makeCirclePolyBody(float posx, float posy, float radius, int material, BodyType bodyType, boolean fixedRotation){
        // create a definition
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.type = bodyType;
        boxBodyDef.position.x = posx;
        boxBodyDef.position.y = posy;
        boxBodyDef.fixedRotation = fixedRotation;

        //create the body to attach said definition
        Body boxBody = world.createBody(boxBodyDef);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius /2);
        boxBody.createFixture(makeFixture(material,circleShape));
        circleShape.dispose();
        return boxBody;
    }

    public Body makeBoxPolyBody(float posx, float posy, float width, float height,int material, BodyType bodyType) {
        return makeBoxPolyBody(posx, posy, width, height, material, bodyType, false);
    }

    //hinh chu nhat
    public Body makeBoxPolyBody(float posx, float posy, float width, float height, int material, BodyType bodyType, boolean fixedRotation){
        // create a definition
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.type = bodyType;
        boxBodyDef.position.x = posx;
        boxBodyDef.position.y = posy;
        boxBodyDef.fixedRotation = fixedRotation;

        //create the body to attach said definition
        Body boxBody = world.createBody(boxBodyDef);
        PolygonShape poly = new PolygonShape();
        poly.setAsBox(width/2, height/2);
        boxBody.createFixture(makeFixture(material,poly));
        poly.dispose();

        return boxBody;
    }

    /** Tạo 1 đối tượng body hình đa giác
     * @param vertices các vector 2 chiều có tọa độ là các điểm nối đa giác (theo chuẩn)
     * @param posx  tọa độ trọng tâm
     * @param posy  tọa độ trọng tâm
     * @param material vật liệu tạo ra
     * @param bodyType loại body .*/
    public Body makePolygonShapeBody(Vector2[] vertices, float posx, float posy, int material, BodyType bodyType){
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.type = bodyType;
        boxBodyDef.position.x = posx;
        boxBodyDef.position.y = posy;
        Body boxBody = world.createBody(boxBodyDef);

        PolygonShape polygon = new PolygonShape();
        polygon.set(vertices);
        boxBody.createFixture(makeFixture(material,polygon));
        polygon.dispose();

        return boxBody;
    }

    /** Tạo 1 hình nón tầm nhìn gắn với 1 đối tượng body
     * @param body đối tượng body được gắn tầm nhìn
     * @param size kích thước của hình nón tầm nhìn .*/
    public void makeConeSensor(Body body, float size){

        FixtureDef fixtureDef = new FixtureDef();
        //fixtureDef.isSensor = true; // will add in future

        PolygonShape polygon = new PolygonShape();

        float radius = size;
        Vector2[] vertices = new Vector2[5];
        vertices[0] = new Vector2(0,0);
        for (int i = 2; i < 6; i++) {
            float angle = (float) (i  / 6.0 * 145 * DEGTORAD); // convert degrees to radians
            vertices[i-1] = new Vector2( radius * ((float)Math.cos(angle)), radius * ((float)Math.sin(angle)));
        }
        polygon.set(vertices);
        fixtureDef.shape = polygon;
        body.createFixture(fixtureDef);
        polygon.dispose();
    }

    public void makeAllFixturesSensors(Body bod){
        for(Fixture fix :bod.getFixtureList()){
            fix.setSensor(true); //phát hiện va chạm mà không tham gia vào các phản ứng vật lý như tạo lực hay bị cản trở.
        }
    }

}
package com.mygdx.game.Systems;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Components.PlayerComponent;
import com.mygdx.game.Components.TextureComponent;
import com.mygdx.game.Components.PositionComponent;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Managers.Mapper;
import com.mygdx.game.ZComparator;

import java.util.Comparator;

// vẽ các entity
public class RenderingSystem extends SortedIteratingSystem{

    static public final float PPM = 32.0f; // Pixel Per Meter Tỉ lệ giữa pixel và mét trong thế giới game.

    // this gets the height and width of our camera frustrum based off the width and height of the screen and our pixel per meter ratio
    // lấy kích thước của cửa sổ camera tính theo mét
    static final float FRUSTUM_WIDTH = Gdx.graphics.getWidth()/PPM;
    static final float FRUSTUM_HEIGHT = Gdx.graphics.getHeight()/PPM;

    public static final float PIXELS_TO_METRES = 1.0f / PPM; // get the ratio for converting pixels to metres

    // static method to get screen width in metres
    private static Vector2 meterDimensions = new Vector2();
    private static Vector2 pixelDimensions = new Vector2();
    // lấy kích thước cửa sổ hiển thị theo mét
    public static Vector2 getScreenSizeInMeters(){
        meterDimensions.set(Gdx.graphics.getWidth()*PIXELS_TO_METRES,
                Gdx.graphics.getHeight()*PIXELS_TO_METRES);
        return meterDimensions;
    }

    // theo pixel
    public static Vector2 getScreenSizeInPixesl(){
        pixelDimensions.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        return pixelDimensions;
    }

    // convenience method to convert pixels to meters
    public static float PixelsToMeters(float pixelValue){
        return pixelValue * PIXELS_TO_METRES;
    }

    private SpriteBatch batch; // a reference to our spritebatch
    private final Array<Entity> renderQueue; // an array used to allow sorting of images allowing us to draw images on top of each other
    //private Comparator<Entity> comparator; // giúp kiểm soát thứ tự vẽ các đối tượng dựa trên z position của positionComponent
    private OrthographicCamera cam; // a reference to our camera

    // component mappers to get components from entities
    private TextureRegion texture;

    @SuppressWarnings("unchecked")
    public RenderingSystem(SpriteBatch batch) {
        // gets all entities with a TransofmComponent and TextureComponent
        super(Family.all(PositionComponent.class, TextureComponent.class).get(), new ZComparator());

        //comparator = new ZComparator();
        //creates out componentMappers
        // create the array for sorting entities
        renderQueue = new Array<>();

        this.batch = batch;  // set our batch to the one supplied in constructor

        // set up the camera to match our screen size
        cam = new OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
        cam.position.set(FRUSTUM_WIDTH / 2f, FRUSTUM_HEIGHT / 2f, 0);// đặt camera đúng vị trí
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // sort the renderQueue based on z index
        //renderQueue.sort(comparator);// sắp xếp tăng dần // không cần thiết nữa vì Sorted Iterating System đã xếp hộ rồi

        // update camera and sprite batch
        cam.update();
        batch.setProjectionMatrix(cam.combined); // cập nhật trạng thái camera để batch có thể thay đổi theo camera
        batch.enableBlending();
        batch.begin();

        // loop through each entity in our render queue
        for (Entity entity : renderQueue) {
            TextureComponent tex = Mapper.textureMapper.get(entity);
            PositionComponent p = Mapper.positionMapper.get(entity);
            PlayerComponent player = Mapper.playerMapper.get(entity);
            if (tex.region == null || p.isHidden) {
                continue;
            }

            if(player != null) {
                texture = new TextureRegion(tex.region);
                cam.position.set(p.position.x, p.position.y + 3, 0);
                if(!player.IsDirectionRight) {
                    texture.flip(true,false);
                }
            }

            float width = tex.region.getRegionWidth();
            float height = tex.region.getRegionHeight();

            float originX = width/2f;
            float originY = height/2f;

            if(player == null) {
                batch.draw(tex.region,
                        p.position.x - originX, p.position.y - originY,
                        originX, originY,
                        width, height,
                        PixelsToMeters(p.scale.x), PixelsToMeters(p.scale.y),
                        p.rotation);
            }
            else {
                batch.draw(texture,
                        p.position.x - originX, p.position.y - originY,
                        originX, originY,
                        width, height,
                        PixelsToMeters(p.scale.x), PixelsToMeters(p.scale.y),
                        p.rotation);
            }


        }

        batch.end();
        renderQueue.clear();

    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        if (entity != null) {
            renderQueue.add(entity);
        }
    }

    // convenience method to get camera
    public OrthographicCamera getCamera() {
        return cam;
    }
}

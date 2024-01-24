package com.mygdx.game.Screens;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.MyGdxGame;


public class GameDemoScreen implements Screen {
    final MyGdxGame game;
    boolean isPause = false; // quản lí pause game
    Texture dropImage;
    Texture bucketImage;
    Sound dropSound;
    Music rainMusic;
    OrthographicCamera camera;
    Rectangle bucket;
    Array<Rectangle> raindrops;
    long lastDropTime;
    int dropsGathered;
    boolean movingLeft = false;
    boolean movingRight = false;
    Stage stage;//quản lí UI

    public GameDemoScreen(final MyGdxGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        // load the images for the droplet and the bucket, 64x64 pixels each
        dropImage = new Texture(Gdx.files.internal("drop.png"));
        bucketImage = new Texture(Gdx.files.internal("bucket.png"));

        // load the drop sound effect and the rain background "music"
        dropSound = Gdx.audio.newSound(Gdx.files.internal("water.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        rainMusic.setLooping(true);

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        // create a Rectangle to logically represent the bucket
        bucket = new Rectangle();
        bucket.x = 800 / 2 - 64 / 2; // center the bucket horizontally
        bucket.y = 20; // bottom left corner of the bucket is 20 pixels above
        // the bottom screen edge
        bucket.width = 64;
        bucket.height = 64;

        // create the raindrops array and spawn the first raindrop
        raindrops = new Array<Rectangle>();
        spawnRaindrop();

    }

    private void spawnRaindrop() {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, 800 - 64);
        raindrop.y = 480;
        raindrop.width = 64;
        raindrop.height = 64;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(float delta) {
        //tô màu đen cho màn hình
        Gdx.gl.glClearColor(0f, 0f, 0f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);

        // begin a new batch and draw the bucket and
        // all drops
        // in ra các đối tượng game
        game.batch.begin();
        game.font.draw(game.batch, "Drops Collected: " + dropsGathered, 0, 480);
        game.batch.draw(bucketImage, bucket.x, bucket.y , bucket.width, bucket.height);
        for (Rectangle raindrop : raindrops) {
            game.batch.draw(dropImage, raindrop.x, raindrop.y);
        }
        game.batch.end();

        //in ra các đối tượng UI
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        // Update logic game
        if (movingLeft && !isPause) {
            bucket.x -= 200 * Gdx.graphics.getDeltaTime();
        }
        if (movingRight && !isPause) {
            bucket.x += 200 * Gdx.graphics.getDeltaTime();
        }

        // make sure the bucket stays within the screen bounds
        if (bucket.x < 0)
            bucket.x = 0;
        if (bucket.x > 800 - 64)
            bucket.x = 800 - 64;

        // khi khoảng thời gian vượt quá 1s thì thêm 1 giọt nước
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000 && !isPause)
            spawnRaindrop();

        // move the raindrops, remove any that are beneath the bottom edge of
        // the screen or that hit the bucket. In the later case we increase the
        // value our drops counter and add a sound effect.

        // update vị trí toàn bộ giọt nước
        if(!isPause) {
            Iterator<Rectangle> iter = raindrops.iterator();
            while (iter.hasNext()) {
                Rectangle raindrop = iter.next();
                raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
                // rơi ra ngoài màn hình
                if (raindrop.y + 64 < 0)
                    iter.remove();
                // chạm vào xô
                if (raindrop.overlaps(bucket)) {
                    dropsGathered++;
                    if (game.setting.isSoundEffectsEnabled()) {
                        dropSound.play(game.setting.getSoundVolume());
                    }
                    iter.remove();
                }
            }
        }

        //Back to Menu
        if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        // start the playback of the background music
        // when the screen is shown
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        TextButton pause = new TextButton("pause",skin);
        pause.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(!isPause) {
                    pause();
                    isPause = true; // Cập nhật trạng thái tạm dừng
                } else {
                    resume();
                    isPause = false; // Cập nhật trạng thái tiếp tục
                }

                return true; // return true để logic game không xử lí input này nữa
            }
        });
        pause.setPosition(stage.getWidth() - pause.getWidth(), stage.getHeight() - pause.getHeight());
        stage.addActor(pause);

        if(game.setting.isMusicEnabled()) {
            rainMusic.setVolume(game.setting.getMusicVolume());
            rainMusic.play();
        }

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);//ưu tiên xử lí input cho stage trước logic game.
        multiplexer.addProcessor(new InputAdapter() {

            /**Xử lí input 2 phím di chuyển trái phải
             * Update vị trí xô (200 pixel/s).*/
            @Override
            public boolean keyDown(int keycode) {
                switch (keycode) {
                    case Keys.LEFT:
                        movingLeft = true;
                        return true;
                    case Keys.RIGHT:
                        movingRight = true;
                        return true;
                }
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                switch (keycode) {
                    case Keys.LEFT:
                        movingLeft = false;
                        return true;
                    case Keys.RIGHT:
                        movingRight = false;
                        return true;
                }
                return false;
            }

            /**Xử lí input chuột bấm
             * Update vị trí xô ở vị trí chuột.*/
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (!isPause) {
                    Vector3 touchPos = new Vector3();
                    touchPos.set(screenX, screenY, 0);
                    camera.unproject(touchPos);
                    bucket.x = touchPos.x - 64 / 2;
                }
                return true;
            }

            /**Xử lí input chuột giữ - kéo
             * Update vị trí xô ở vị trí chuột.*/
            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                if (!isPause) {
                    Vector3 touchPos = new Vector3();
                    touchPos.set(screenX, screenY, 0);
                    camera.unproject(touchPos);
                    bucket.x = touchPos.x - 64 / 2;
                }
                return true;
            }
        });

        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void pause() {
        rainMusic.pause();
    }

    @Override
    public void resume() {
        rainMusic.play();
    }

    @Override
    public void dispose() {
        // Giải phóng các texture
        dropImage.dispose();
        bucketImage.dispose();

        // Giải phóng âm thanh và nhạc nền
        dropSound.dispose();
        rainMusic.dispose();

        // Giải phóng stage (quản lý UI)
        stage.dispose();

    }
}

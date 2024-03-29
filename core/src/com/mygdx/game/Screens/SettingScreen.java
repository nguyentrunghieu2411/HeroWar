package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.MainMenuScreen;

public class SettingScreen implements Screen {

    public MyGdxGame game;
    private final Stage stage;

    public SettingScreen(final MyGdxGame game){
        this.game = game;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        Table table = new Table();
        // các nhãn văn bản
        Label titleLabel = new Label("Preferences", skin, "title");
        Label volumeMusicLabel = new Label("Music: ", skin);
        Label volumeSoundLabel = new Label("Sound: ", skin);
        Label musicOnOffLabel = new Label("Music: ", skin);
        Label soundOnOffLabel = new Label("Sound: ", skin);

        //tạo 2 thanh trượt chỉnh volume
        final Slider volumeMusicSlider = new Slider( 0f, 1f, 0.1f,false, skin );
        volumeMusicSlider.setValue( game.getPreferences().getMusicVolume() );
        volumeMusicSlider.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                game.getPreferences().setMusicVolume( volumeMusicSlider.getValue() );
                return false;
            }
        });

        final Slider soundMusicSlider = new Slider( 0f, 1f, 0.1f,false, skin );
        soundMusicSlider.setValue( game.getPreferences().getSoundVolume() );
        soundMusicSlider.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                game.getPreferences().setSoundVolume( soundMusicSlider.getValue() );
                return false;
            }
        });

        //tạo 2 thanh switch on/off
        final CheckBox musicCheckbox = new CheckBox(null, skin, "switch");
        musicCheckbox.setChecked( game.getPreferences().isMusicEnabled() );
        musicCheckbox.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = musicCheckbox.isChecked();
                game.getPreferences().setMusicEnabled( enabled );
                return false;
            }
        });

        final CheckBox soundEffectsCheckbox = new CheckBox(null, skin, "switch");
        soundEffectsCheckbox.setChecked( game.getPreferences().isSoundEffectsEnabled() );
        soundEffectsCheckbox.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = soundEffectsCheckbox.isChecked();
                game.getPreferences().setSoundEffectsEnabled( enabled );
                return false;
            }
        });

        //nút back to menu
        final TextButton backButton = new TextButton("Back", skin,"round"); // the extra argument here "small" is used to set the button to the smaller version instead of the big default version
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        table.add(titleLabel).left();
        table.row().pad(10,0,10,0);
        table.add(volumeMusicLabel).left();
        table.add(volumeMusicSlider);
        table.row().pad(10,0,10,0);
        table.add(musicOnOffLabel).left();
        table.add(musicCheckbox);
        table.row().pad(10,0,10,0);
        table.add(volumeSoundLabel).left();
        table.add(soundMusicSlider);
        table.row().pad(10,0,10,0);
        table.add(soundOnOffLabel).left();
        table.add(soundEffectsCheckbox);
        table.row().pad(10,0,10,0);
        table.add(backButton).colspan(2);
        table.setFillParent(true);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        this.dispose();
    }

    @Override
    public void dispose() {

    }
}

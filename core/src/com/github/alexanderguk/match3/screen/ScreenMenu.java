package com.github.alexanderguk.match3.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.alexanderguk.match3.Match3Main;
import com.sun.javafx.applet.Splash;

public class ScreenMenu implements Screen {
    private Stage stage;
    private Skin skin;
    private Table table;
    private TextButton buttonPlay;
    private TextButton buttonExit;
    private Label title;

    public ScreenMenu(SpriteBatch batch) {
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), batch);
        skin = new Skin(Gdx.files.internal("skins/menuSkin.json"),
                new TextureAtlas(Gdx.files.internal("skins/menuSkin.pack")));

        table = new Table();
        buttonPlay = new TextButton("Play", skin);
        buttonExit = new TextButton("Exit", skin);
        title = new Label("Match 3",skin);
    }

    @Override
    public void show() {
        buttonPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Match3Main.getInstance().showGame();
            }
        });
        buttonExit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Exit");
                Gdx.app.exit();
            }
        });

        table.add(title).padBottom(40).row();
        table.add(buttonPlay).row();
        table.add(buttonExit).row();

        table.setFillParent(true);
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
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
        stage.dispose();
        skin.dispose();
    }
}

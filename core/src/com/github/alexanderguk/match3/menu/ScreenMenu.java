package com.github.alexanderguk.match3.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.alexanderguk.match3.Match3Main;

public class ScreenMenu implements Screen {
    private Viewport viewport;
    private Stage stage;
    private Skin skin;
    private Table table;
    private TextButton buttonPlay;
    private TextButton buttonScoreBoard;
    private TextButton buttonExit;
    private Label title;
    private Label authorLabel;
    private Label authorLinkLabel;

    public ScreenMenu(SpriteBatch batch) {
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport, batch);
        skin = new Skin(Gdx.files.internal("skins/menuSkin.json"),
                new TextureAtlas(Gdx.files.internal("skins/menuSkin.pack")));

        buttonPlay = new TextButton("Play", skin);
        buttonScoreBoard = new TextButton("Score Board", skin);
        buttonExit = new TextButton("Exit", skin);
        title = new Label("Match 3",skin);

        table = new Table();

        table.add(title).padBottom(20).row();
        table.add(buttonPlay).padBottom(20).row();
        table.add(buttonScoreBoard).padBottom(20).row();
        table.add(buttonExit).padBottom(50).row();
        table.setFillParent(true);

        authorLabel = new Label("Created by ", skin, "author");
        authorLinkLabel = new Label("Alexander Guk", skin, "authorLink");

        authorLinkLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://github.com/alexanderguk");
            }
        });

        Table repoLink = new Table();
        repoLink.add(authorLabel);
        repoLink.add(authorLinkLabel);
        repoLink.setFillParent(true);
        repoLink.center().bottom();

        stage.addActor(table);
        stage.addActor(repoLink);

        buttonPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Match3Main.getInstance().showGame();
            }
        });
        buttonScoreBoard.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Match3Main.getInstance().showScoreBoard();
            }
        });
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void show() {
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
        viewport.update(width, height);
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

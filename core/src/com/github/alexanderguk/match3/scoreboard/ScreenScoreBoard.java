package com.github.alexanderguk.match3.scoreboard;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
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
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.alexanderguk.match3.Match3Main;

public class ScreenScoreBoard implements Screen {
    private Viewport viewport;
    private Stage stage;
    private Skin skin;
    private Table table;
    private Table scoreBoardTable;
    private TextButton buttonMenu;
    private Label title;

    public ScreenScoreBoard(SpriteBatch batch) {
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport, batch);
        skin = new Skin(Gdx.files.internal("skins/scoreBoardSkin.json"),
                new TextureAtlas(Gdx.files.internal("skins/scoreBoardSkin.pack")));

        buttonMenu = new TextButton("Menu", skin);
        title = new Label("Score Board", skin);
        System.out.println("ScreenScoreBoard.show()");
        buttonMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Match3Main.getInstance().showMenu();
            }
        });

        table = new Table(skin);
        scoreBoardTable = new Table(skin);

        table.add(title).padBottom(20).row();
        table.add(scoreBoardTable).padBottom(20).row();
        table.add(buttonMenu).padBottom(20).row();
        table.setFillParent(true);

        stage.addActor(table);
    }

    @Override
    public void show() {
        loadScoreBoard();
        Gdx.input.setInputProcessor(stage);
    }

    private void loadScoreBoard() {
        Net.HttpRequest saveRequest = new Net.HttpRequest(Net.HttpMethods.POST);
        saveRequest.setUrl("http://URL/test/score.php?action=getjson");
        saveRequest.setHeader("Content-Type", "application/json");
        saveRequest.setHeader("Accept", "application/json");

        Gdx.net.sendHttpRequest(saveRequest, new Net.HttpResponseListener() {
            @Override
            public void cancelled() {
                Gdx.app.log("Net", "Cancelled");
            }
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse)
            {
                Gdx.app.log("Net", "response code was " + httpResponse.getStatus().getStatusCode());
                String string = httpResponse.getResultAsString();
                JsonValue root;
                try {
                    root = new JsonReader().parse(string);
                    JsonValue data = root.get("data");
                    int i = 0;
                    scoreBoardTable.clear();
                    for (JsonValue record : data) {
                        i++;
                        scoreBoardTable.add(new Label("" + i, skin, "scoreBoard"));
                        scoreBoardTable.add(new Label(record.get("name").asString(), skin, "scoreBoard"));
                        scoreBoardTable.add(new Label(record.get("score").asString(), skin, "scoreBoard"));
                        scoreBoardTable.row();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable t)
            {
                Gdx.app.log("Net", "Failed");
            }
        });
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

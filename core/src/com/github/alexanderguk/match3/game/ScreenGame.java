package com.github.alexanderguk.match3.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.alexanderguk.match3.Match3Main;

public class ScreenGame implements Screen {
    private Viewport viewport;
    private Stage stage;
    private GameField gameField;
    private Skin skin;
    private Table scoreTable;
    private Table gameOverTable;
    private Table sendScoreTable;
    private Label scoreLabel;
    private Label timeLabel;
    private Label gameOverLabel;
    private Label scoreSendLabel;
    private TextButton buttonScoreRestart;
    private TextButton buttonGameOverRestart;
    private TextButton buttonSendScore;
    private TextButton buttonMenu;
    private TextButton buttonScoreBoard;
    private TextField nickname;

    private final int SEND_SCORE_ROW_HEIGHT = 38;

    public ScreenGame(SpriteBatch batch) {
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport, batch);
        gameField = new GameField();

        skin = new Skin(Gdx.files.internal("skins/gameSkin.json"),
                new TextureAtlas(Gdx.files.internal("skins/gameSkin.pack")));

        buttonScoreRestart = new TextButton("Restart", skin, "score");
        buttonScoreRestart.setWidth(100);
        buttonMenu = new TextButton("Menu", skin, "score");
        buttonMenu.setWidth(100);

        buttonGameOverRestart = new TextButton("Restart", skin);
        buttonGameOverRestart.setWidth(100);
        buttonSendScore = new TextButton("Send score", skin, "score");
        buttonSendScore.setWidth(120);
        buttonScoreBoard = new TextButton("Score Board", skin);
        buttonScoreBoard.setWidth(100);

        scoreLabel = new Label("Score: 0", skin, "score");
        timeLabel = new Label("Time: " + gameField.getRoundTime(), skin, "score");

        gameOverLabel = new Label("Game Over", skin, "gameover");
        gameOverLabel.setAlignment(Align.center);
        nickname = new TextField("Player", skin);
        scoreSendLabel = new Label("Your score has been sent!", skin, "scoreSend");

        buttonScoreRestart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Match3Main.getInstance().showGame();
            }
        });
        buttonGameOverRestart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Match3Main.getInstance().showGame();
            }
        });
        buttonMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Match3Main.getInstance().showMenu();
            }
        });
        buttonSendScore.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sendScoreTable.clear();
                sendScoreTable.add(scoreSendLabel).height(SEND_SCORE_ROW_HEIGHT);

                Net.HttpRequest httpPost = new Net.HttpRequest(Net.HttpMethods.POST);
                httpPost.setUrl("http://URL/test/score.php?action=save&name=" + nickname.getText() +
                        "&score=" + gameField.getScore());

                Gdx.net.sendHttpRequest(httpPost, new Net.HttpResponseListener() {
                    @Override
                    public void cancelled() {
                        String status = "cancelled";
                        nickname.setText(status);
                    }

                    @Override
                    public void handleHttpResponse(Net.HttpResponse httpResponse) {
                        String status = httpResponse.getResultAsString();
                        nickname.setText(status);
                    }

                    @Override
                    public void failed(Throwable t) {
                        String status = "failed";
                        nickname.setText(status);
                    }
                });
            }
        });
        buttonScoreBoard.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Match3Main.getInstance().showScoreBoard();
            }
        });
        nickname.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((TextField) event.getTarget()).setText("");
            }
        });

        scoreTable = new Table();
        scoreTable.setFillParent(true);
        scoreTable.pad(10);
        scoreTable.add(timeLabel).width(120).pad(10);
        scoreTable.add(scoreLabel).width(120).pad(10);
        scoreTable.add(buttonScoreRestart).width(120).pad(10);
        scoreTable.add(buttonMenu).width(120).pad(10);
        scoreTable.top();

        gameOverTable = new Table();
        gameOverTable.setFillParent(true);
        gameOverTable.add(gameOverLabel).padBottom(10).row();
        gameOverTable.add(buttonGameOverRestart).row();
        sendScoreTable = new Table();
        gameOverTable.add(sendScoreTable).row();
        gameOverTable.add(buttonScoreBoard).row();
        gameOverTable.center();
        gameOverTable.setVisible(false);

        stage.addActor(scoreTable);
        for (Block block : gameField.getBlockList()) {
            stage.addActor(block);
        }
        stage.addActor(gameOverTable);
    }

    @Override
    public void show() {
        gameField.reset();
        gameField.shuffle();
        gameField.update();
        nickname.setText("Player");
        gameOverTable.setVisible(false);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        scoreLabel.setText("Score: " + gameField.getScore());
        timeLabel.setText("Time: " + (int) gameField.getRoundTime());
        if (gameField.isGameOver() && !gameOverTable.isVisible()) {
            gameOverLabel.setText("Game Over\nScore: " + gameField.getScore());
            gameOverTable.setVisible(true);
            sendScoreTable.clear();
            sendScoreTable.add(nickname).height(SEND_SCORE_ROW_HEIGHT);
            sendScoreTable.add(buttonSendScore).height(SEND_SCORE_ROW_HEIGHT);
        }

        gameField.act(delta);

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
    }
}

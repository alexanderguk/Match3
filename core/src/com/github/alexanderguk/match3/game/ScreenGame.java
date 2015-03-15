package com.github.alexanderguk.match3.game;

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
    private Label scoreLabel;
    private Label timeLabel;
    private Label gameOverLabel;
    private TextButton buttonScoreRestart;
    private TextButton buttonGameOverRestart;

    public ScreenGame(SpriteBatch batch) {
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport, batch);
        gameField = new GameField();

        skin = new Skin(Gdx.files.internal("skins/gameSkin.json"),
                new TextureAtlas(Gdx.files.internal("skins/gameSkin.pack")));
        buttonScoreRestart = new TextButton("Restart", skin, "score");
        buttonScoreRestart.setWidth(100);
        buttonGameOverRestart = new TextButton("Restart", skin);
        buttonGameOverRestart.setWidth(100);
        scoreLabel = new Label("Score: 0", skin, "score");
        timeLabel = new Label("Time: " + gameField.getRoundTime(), skin, "score");
        gameOverLabel = new Label("Game Over", skin, "gameover");
    }

    @Override
    public void show() {
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

        scoreTable = new Table();
        scoreTable.setFillParent(true);
        scoreTable.pad(10);
        scoreTable.add(timeLabel).width(160).pad(10);
        scoreTable.add(scoreLabel).width(160).pad(10);
        scoreTable.add(buttonScoreRestart).width(160).pad(10);
        scoreTable.top();

        gameOverTable = new Table();
        gameOverTable.setFillParent(true);
        gameOverTable.add(gameOverLabel).padBottom(10).row();
        gameOverLabel.setAlignment(Align.center);
        gameOverTable.add(buttonGameOverRestart).row();
        gameOverTable.center();
        gameOverTable.setVisible(false);

        stage.addActor(scoreTable);
        for (Block block : gameField.getBlockList()) {
            stage.addActor(block);
        }
        stage.addActor(gameOverTable);

        gameField.reset();
        gameField.shuffle();
        gameField.update();

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        scoreLabel.setText("Score: " + gameField.getScore());
        timeLabel.setText("Time: " + (int) gameField.getRoundTime());
        if (gameField.isGameOver()) {
            gameOverLabel.setText("Game Over\nScore: " + gameField.getScore());
            gameOverTable.setVisible(true);
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

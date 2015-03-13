package com.github.alexanderguk.match3;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.alexanderguk.match3.screen.ScreenGame;
import com.github.alexanderguk.match3.screen.ScreenMenu;

public class Match3Main extends Game {
    private ScreenMenu screenMenu;
    private ScreenGame screenGame;
    private SpriteBatch batch;

    private static Match3Main instance = new Match3Main();

    private Match3Main() {
    }

    public static Match3Main getInstance() {
        return instance;
    }

	@Override
	public void create () {
        batch = new SpriteBatch();
        screenMenu = new ScreenMenu(batch);
        screenGame = new ScreenGame(batch);

        setScreen(screenMenu);
	}

    @Override
    public void dispose() {
        batch.dispose();
        super.dispose();
    }

    public void showMenu() {
        setScreen(screenMenu);
    }

    public void showGame() {
        setScreen(screenGame);
    }
}

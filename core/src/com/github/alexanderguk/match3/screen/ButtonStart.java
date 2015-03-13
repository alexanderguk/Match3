package com.github.alexanderguk.match3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ButtonStart extends Actor {
    private BitmapFont font;

    public ButtonStart(BitmapFont font) {
        this.font = font;
        setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 + 20);
        setSize(100, 30);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        font.draw(batch, "Start", getX(), getY());
    }
}

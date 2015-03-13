package com.github.alexanderguk.match3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.github.alexanderguk.match3.Match3Main;

public class ButtonExit extends Button {
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;

    public ButtonExit(BitmapFont font) {
        this.font = font;
        setBounds(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 - 20, 200, 200);
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("down");
                Gdx.app.exit();
                return true;
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        font.draw(batch, "Exit", getX(), getY());
    }


}

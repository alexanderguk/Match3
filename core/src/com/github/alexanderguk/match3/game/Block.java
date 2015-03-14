package com.github.alexanderguk.match3.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Block extends Actor {
    private Sprite sprite;
    private Sprite activeBlockSprite;
    private boolean isActive;

    public Block(Sprite activeBlockSprite) {
        this(null, activeBlockSprite);
    }

    public Block(Sprite sprite, Sprite activeBlockSprite) {
        this.sprite = sprite;
        this.activeBlockSprite = activeBlockSprite;
        this.isActive = false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (sprite != null) {
            sprite.setPosition(getX(), getY());
            sprite.draw(batch);
            if (isActive) {
                activeBlockSprite.setPosition(getX(), getY());
                activeBlockSprite.draw(batch);
            }
        }
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
}

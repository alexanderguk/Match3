package com.github.alexanderguk.match3.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameField {
    private TextureAtlas textureAtlas;
    private List<Sprite> spriteList;
    private Sprite activeBlockSprite;

    private final int GAME_FIELD_WIDTH = 8;
    private final int GAME_FIELD_HEIGHT = 8;
    private Block gameField[][];
    private Block activeBlock;

    public GameField() {
        textureAtlas = new TextureAtlas(Gdx.files.internal("atlases/gamePack.pack"));
        activeBlockSprite = textureAtlas.createSprite("block_active");
        spriteList = new ArrayList<Sprite>();
        spriteList.add(textureAtlas.createSprite("block_red"));
        spriteList.add(textureAtlas.createSprite("block_orange"));
        spriteList.add(textureAtlas.createSprite("block_green"));
        spriteList.add(textureAtlas.createSprite("block_teal"));

        gameField = new Block [GAME_FIELD_WIDTH][GAME_FIELD_HEIGHT];
        int topLeftX = Gdx.graphics.getWidth() / 2 - (int)((GAME_FIELD_WIDTH / 2f) * spriteList.get(0).getWidth());
        int topLeftY = Gdx.graphics.getHeight() / 2 + (int)((GAME_FIELD_HEIGHT / 2f) * spriteList.get(0).getHeight() -
                spriteList.get(0).getHeight());
        for (int i = 0; i < GAME_FIELD_WIDTH; ++i) {
            for (int j = 0; j < GAME_FIELD_HEIGHT; ++j) {
                gameField[i][j] = new Block(activeBlockSprite);
                gameField[i][j].setBounds(topLeftX + spriteList.get(0).getWidth() * j,
                        topLeftY - spriteList.get(0).getHeight() * i, 64, 64);
                gameField[i][j].addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        GameField.this.clickOn((Block) event.getTarget());
                    }
                });
            }
        }

        shuffle();
    }

    public void shuffle() {
        Random random = new Random();
        for (int i = 0; i < GAME_FIELD_WIDTH; ++i) {
            for (int j = 0; j < GAME_FIELD_HEIGHT; ++j) {
                gameField[i][j].setSprite(spriteList.get(random.nextInt(spriteList.size())));
            }
        }
    }

    public List<Block> getBlockList() {
        List<Block> blockList = new ArrayList<Block>();
        for (int i = 0; i < GAME_FIELD_WIDTH; ++i) {
            for (int j = 0; j < GAME_FIELD_HEIGHT; ++j) {
                blockList.add(gameField[i][j]);
            }
        }
        return blockList;
    }

    private void clickOn(Block block) {
        if (activeBlock == null) {
            activeBlock = block;
            block.setActive(true);
        } else if (activeBlock == block) {
            activeBlock = null;
            block.setActive(false);
        } else {
            // check for match 3
        }
    }
}

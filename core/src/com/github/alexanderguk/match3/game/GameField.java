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

    private final int GAME_FIELD_WIDTH = 6;
    private final int GAME_FIELD_HEIGHT = 6;
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
        spriteList.add(textureAtlas.createSprite("block_purple"));

        gameField = new Block [GAME_FIELD_WIDTH][GAME_FIELD_HEIGHT];
        int topLeftX = Gdx.graphics.getWidth() / 2 - (int)((GAME_FIELD_WIDTH / 2f) * spriteList.get(0).getWidth());
        int topLeftY = Gdx.graphics.getHeight() / 2 + (int)((GAME_FIELD_HEIGHT / 2f) * spriteList.get(0).getHeight() -
                spriteList.get(0).getHeight());
        for (int i = 0; i < GAME_FIELD_HEIGHT; ++i) {
            for (int j = 0; j < GAME_FIELD_WIDTH; ++j) {
                gameField[i][j] = new Block(j, i, activeBlockSprite);
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
        Sprite prevSprite = null;
        for (int i = 0; i < GAME_FIELD_HEIGHT; ++i) {
            for (int j = 0; j < GAME_FIELD_WIDTH; ++j) {
                Sprite curSprite = getRandomSprite();
                while (curSprite == prevSprite) {
                    curSprite = getRandomSprite();
                }
                gameField[i][j].setSprite(curSprite);
                prevSprite = curSprite;
            }
        }
    }

    public List<Block> getBlockList() {
        List<Block> blockList = new ArrayList<Block>();
        for (int i = 0; i < GAME_FIELD_HEIGHT; ++i) {
            for (int j = 0; j < GAME_FIELD_WIDTH; ++j) {
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
        } else if ((block.getGameFieldX() == activeBlock.getGameFieldX() - 1 &&
                block.getGameFieldY() == activeBlock.getGameFieldY()) ||
                (block.getGameFieldX() == activeBlock.getGameFieldX() + 1 &&
                block.getGameFieldY() == activeBlock.getGameFieldY()) ||
                (block.getGameFieldX() == activeBlock.getGameFieldX() &&
                block.getGameFieldY() == activeBlock.getGameFieldY() - 1) ||
                (block.getGameFieldX() == activeBlock.getGameFieldX() &&
                block.getGameFieldY() == activeBlock.getGameFieldY() + 1)) {
            Sprite tempSprite = block.getSprite();
            block.setSprite(activeBlock.getSprite());
            activeBlock.setSprite(tempSprite);
            if (!update()) {
                tempSprite = block.getSprite();
                block.setSprite(activeBlock.getSprite());
                activeBlock.setSprite(tempSprite);
            } else {
                activeBlock.setActive(false);
                activeBlock = null;
            }
        } else {
            activeBlock.setActive(false);
            activeBlock = block;
            block.setActive(true);
        }
    }

    public boolean update() {
        boolean isUpdated = false;
        List<Block> chain = findChain();
        while (chain != null) {
            isUpdated = true;
            deleteChain(chain);
            chain = findChain();
        }
        return isUpdated;
    }

    private void deleteChain(List<Block> chain) {
        for (Block currentBlock : chain) {
            for (int i = currentBlock.getGameFieldY(); i > 0; i--) {
                Sprite tempSprite = gameField[i][currentBlock.getGameFieldX()].getSprite();
                gameField[i][currentBlock.getGameFieldX()].setSprite(gameField[i - 1][currentBlock.getGameFieldX()].getSprite());
                gameField[i - 1][currentBlock.getGameFieldX()].setSprite(tempSprite);
            }
            gameField[0][currentBlock.getGameFieldX()].setSprite(getRandomSprite());
        }
    }

    private Sprite getRandomSprite() {
        Random random = new Random();
        return spriteList.get(random.nextInt(spriteList.size()));
    }

    private List<Block> findChain() {
        List<Block> currentChain = new ArrayList<Block>();
        List<Block> usedBlocks = new ArrayList<Block>();
        for (int i = 0; i < GAME_FIELD_HEIGHT; ++i) {
            for (int j = 0; j < GAME_FIELD_WIDTH; ++j) {
                if (!usedBlocks.contains(gameField[i][j])) {
                    currentChain.clear();
                    buildChain(usedBlocks, currentChain, gameField[i][j]);
                    currentChain = checkChain(currentChain);
                    if (currentChain.size() >= 3) {
                        return currentChain;
                    }
                }
            }
        }
        return null;
    }

    private List<Block> checkChain(List<Block> chain) {
        List<Block> checkedBlockList = new ArrayList<Block>();
        for (Block currentBlock : chain) {
            if (!checkedBlockList.contains(currentBlock)) {
                List<Block> currentRow = new ArrayList<Block>();
                // Vertical check
                for (int i = 0; i < GAME_FIELD_HEIGHT; ++i) {
                    if (chain.contains(gameField[i][currentBlock.getGameFieldX()])) {
                        currentRow.add(gameField[i][currentBlock.getGameFieldX()]);
                    } else {
                        if (currentRow.size() >= 3) {
                            checkedBlockList.addAll(currentRow);
                        }
                        currentRow.clear();
                    }
                }
                if (currentRow.size() >= 3) {
                    checkedBlockList.addAll(currentRow);
                }
                currentRow.clear();
                // Horizontal check
                for (int i = 0; i < GAME_FIELD_WIDTH; ++i) {
                    if (chain.contains(gameField[currentBlock.getGameFieldY()][i])) {
                        currentRow.add(gameField[currentBlock.getGameFieldY()][i]);
                    } else {
                        if (currentRow.size() >= 3) {
                            checkedBlockList.addAll(currentRow);
                        }
                        currentRow.clear();
                    }
                }
                if (currentRow.size() >= 3) {
                    checkedBlockList.addAll(currentRow);
                }
                currentRow.clear();
            }
        }
        return checkedBlockList;
    }

    private void buildChain(List<Block> usedBlocks, List<Block> currentChain, Block block) {
        usedBlocks.add(block);
        Block nextBlock;
        nextBlock = stepRight(currentChain, block);
        if (nextBlock != null) {
            buildChain(usedBlocks, currentChain, nextBlock);
        }
        nextBlock = stepLeft(currentChain, block);
        if (nextBlock != null) {
            buildChain(usedBlocks, currentChain, nextBlock);
        }
        nextBlock = stepUp(currentChain, block);
        if (nextBlock != null) {
            buildChain(usedBlocks, currentChain, nextBlock);
        }
        nextBlock = stepDown(currentChain, block);
        if (nextBlock != null) {
            buildChain(usedBlocks, currentChain, nextBlock);
        }
    }

    private Block stepRight(List<Block> currentChain, Block block) {
        if (block.getGameFieldX() + 1 < GAME_FIELD_WIDTH &&
                gameField[block.getGameFieldY()][block.getGameFieldX() + 1].getSprite() == block.getSprite() &&
                !currentChain.contains(gameField[block.getGameFieldY()][block.getGameFieldX() + 1])) {
            currentChain.add(gameField[block.getGameFieldY()][block.getGameFieldX() + 1]);
            return gameField[block.getGameFieldY()][block.getGameFieldX() + 1];
        }
        return null;
    }

    private Block stepLeft(List<Block> currentChain, Block block) {
        if (block.getGameFieldX() - 1 >= 0 &&
                gameField[block.getGameFieldY()][block.getGameFieldX() - 1].getSprite() == block.getSprite() &&
                !currentChain.contains(gameField[block.getGameFieldY()][block.getGameFieldX() - 1])) {
            currentChain.add(gameField[block.getGameFieldY()][block.getGameFieldX() - 1]);
            return gameField[block.getGameFieldY()][block.getGameFieldX() - 1];
        }
        return null;
    }

    private Block stepUp(List<Block> currentChain, Block block) {
        if (block.getGameFieldY() - 1 >= 0 &&
                gameField[block.getGameFieldY() - 1][block.getGameFieldX()].getSprite() == block.getSprite() &&
                !currentChain.contains(gameField[block.getGameFieldY() - 1][block.getGameFieldX()])) {
            currentChain.add(gameField[block.getGameFieldY() - 1][block.getGameFieldX()]);
            return gameField[block.getGameFieldY() - 1][block.getGameFieldX()];
        }
        return null;
    }

    private Block stepDown(List<Block> currentChain, Block block) {
        if (block.getGameFieldY() + 1 < GAME_FIELD_HEIGHT &&
                gameField[block.getGameFieldY() + 1][block.getGameFieldX()].getSprite() == block.getSprite() &&
                !currentChain.contains(gameField[block.getGameFieldY() + 1][block.getGameFieldX()])) {
            currentChain.add(gameField[block.getGameFieldY() + 1][block.getGameFieldX()]);
            return gameField[block.getGameFieldY() + 1][block.getGameFieldX()];
        }
        return null;
    }
}

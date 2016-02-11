package com.insta.games.tetris.model;

import com.insta.games.tetris.ui.screen.PlayField;
import com.insta.games.tetris.logic.GameController;

import java.util.Arrays;

/**
 * Created by Julien on 8/2/16.
 */
public class Tetromino {

    //types pieces
    public final static int I = 1;
    public final static int O = 2;
    public final static int T = 3;
    public final static int S = 4;
    public final static int Z = 5;
    public final static int J = 6;
    public final static int L = 7;

    public int type;

    public static final String TAG = Tetromino.class.getName();

    private boolean[][] gridI = {
            {false, true, false, false},
            {false, true, false, false},
            {false, true, false, false},
            {false, true, false, false}};

    private boolean[][] gridO = {
            {false, false, false, false},
            {true, true, false, false},
            {true, true, false, false}};

    private boolean[][] gridT = {
            {false, true, false},
            {true, true, true},
            {false, false, false}};

    private boolean[][] gridS = {
            {false, true, true},
            {true, true, false},
            {false, false, false}};

    private boolean[][] gridZ = {
            {true, true, false},
            {false, true, true},
            {false, false, false}};

    private boolean[][] gridJ = {
            {true, false, false},
            {true, true, true},
            {false, false, false}};

    private boolean[][] gridL = {
            {false, false, true},
            {true, true, true},
            {false, false, false}};

    public boolean[][] grid;

    public int posX;
    public int posY;
    private PlayField playField;
    private GameController gameController;
    public boolean moved;

    public boolean isFalling() {
        return falling;
    }

    private boolean falling;

    public int delay = 800;

    public long lastFallTime;

    public Tetromino(PlayField playField, GameController gameController) {
        this.playField = playField;
        this.gameController = gameController;

    }

    public synchronized void init(int type) {
        this.type = type;
        this.falling = true;
        delay = (int) (800 * (Math.pow(.86, (double) playField.level)));
        if (delay < 33) {
            delay = 33;
        }

        switch (type) {
            case I:
                grid = new boolean[4][4];
                grid = deepCopy(gridI);
                break;
            case O:
                grid = new boolean[3][4];
                grid = deepCopy(gridO);
                break;
            case T:
                grid = new boolean[3][3];
                grid = deepCopy(gridT);
                break;
            case S:
                grid = new boolean[3][3];
                grid = deepCopy(gridS);
                break;
            case Z:
                grid = new boolean[3][3];
                grid = deepCopy(gridZ);
                break;
            case J:
                grid = new boolean[3][3];
                grid = deepCopy(gridJ);
                break;
            case L:
                grid = new boolean[3][3];
                grid = deepCopy(gridL);
                break;
        }
        playField.setTetrominoGrid(grid);
        posX = playField.playfield[0].length / 2 - grid[0].length / 2;
        if (grid[0].length < 4) posX--;
        posY = 0;
    }

    public void fall(boolean force) {
        if (force || System.currentTimeMillis() - lastFallTime >= delay) {
            boolean possible = true;
            falling = true;
            if (!force)
                lastFallTime = System.currentTimeMillis();
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    if (grid[i][j]) {
                        int blockRow = posY + i;
                        int blockCol = posX + j;
                        if (blockRow >= playField.blocks.length - 1) {
                            possible = false;
                        } else if (playField.blocks[blockRow + 1][blockCol]) {
                            possible = false;
                        }
                    }
                }
            }
            if (possible) {
                posY++;
                moved = true;
            } else {
                gameController.tetrominoSpawned = false;
                falling = false;
                if (posY == 0) {
                    gameController.gameOver();
                }
            }
        }
    }

    public synchronized void move(int deltaX, int deltaY) {
        int newPosX = posX + deltaX;
        int newPosY = posY + deltaY;
        boolean possible = true;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j]) {
                    int blockRow = newPosY + i;
                    int blockCol = newPosX + j;
                    if (blockRow >= playField.blocks.length || blockCol < 0 || blockCol >= playField.blocks[0].length) {
                        possible = false;
                    } else if (playField.blocks[blockRow][blockCol]) {
                        possible = false;
                    }
                }
            }
        }
        if (possible) {
            posX = newPosX;
            posY = newPosY;
        }
    }

    public synchronized void rotate() {
        if (grid.length == grid[0].length) {
            int size = grid.length;

            boolean[][] rotatedGrid = new boolean[size][size];

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    rotatedGrid[j][size - 1 - i] = grid[i][j];
                }
            }
            boolean possible = true;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (rotatedGrid[i][j]) {
                        int blockRow = posY + i;
                        int blockCol = posX + j;
                        if (blockRow >= playField.blocks.length || blockCol < 0 || blockCol >= playField.blocks[0].length) {
                            possible = false;
                        } else if (playField.blocks[blockRow][blockCol]) {
                            possible = false;
                        }
                    }
                }
            }
            if (possible) {
                grid = Arrays.copyOf(rotatedGrid, rotatedGrid.length);
                moved = true;
            }
        }
    }

    public static boolean[][] deepCopy(boolean[][] original) {
        if (original == null) {
            return null;
        }

        final boolean[][] result = new boolean[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return result;
    }
}

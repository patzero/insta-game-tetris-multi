package com.insta.games.tetris.ui.screen;

import java.util.Arrays;

import com.insta.games.tetris.model.Tetromino;

/**
 * Tetris play field,
 * 10 cells wide and 22 cells tall.
 *
 * Created by Julien on 8/2/16.
 */
public class PlayField {

    public int[][] playfield = new int[22][10]; //used for rendering
    public boolean[][] blocks = new boolean[22][10];  // used for object detection

    private boolean[][] oldGrid;

    private int oldPosX;
    private int oldPosY;

    public int score = 0;
    public int level = 0;

    public void setTetrominoGrid(boolean[][] tetrominoGrid) {
        this.tetrominoGrid = tetrominoGrid;
        oldGrid = null;
    }

    public void reset() {
        score = 0;
        level = 0;
        playfield = new int[22][10]; //used for rendering
        blocks = new boolean[22][10];  // used for object detection
    }

    private boolean[][] tetrominoGrid;

    public synchronized void update(Tetromino tetromino) {
        if (null != tetromino) {
            int posX = tetromino.posX;
            int posY = tetromino.posY;
            tetrominoGrid = tetromino.grid.clone();
            int nRows = tetrominoGrid.length;
            int nCols = tetrominoGrid[0].length;
            for (int i = 0; i < nRows; i++) {
                for (int j = 0; j < nCols; j++) {
                    if (oldGrid != null && tetromino.isFalling()) {
                        if (oldGrid[i][j]) {
                            playfield[oldPosY + i][oldPosX + j] = 0;
                        }
                    }
                    if (tetrominoGrid[i][j]) {
                        playfield[posY + i][posX + j] = tetromino.type;
                        if (!tetromino.isFalling()) {
                            blocks[posY + i][posX + j] = true;
                        }
                    }
                }
            }
            oldGrid = Arrays.copyOf(tetrominoGrid, tetrominoGrid.length);
            oldPosX = posX;
            oldPosY = posY;
        }
    }

    public void dispose() {
    }
}

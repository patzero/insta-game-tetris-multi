package com.insta.games.tetris.logic;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.Random;

import com.insta.games.tetris.ui.Assets;
import com.insta.games.tetris.model.Tetromino;
import com.insta.games.tetris.TetrisGame;
import com.insta.games.tetris.ui.screen.GameScreen;
import com.insta.games.tetris.ui.screen.PlayField;

/**
 * Created by Julien on 8/2/16.
 */
public class GameController {

    private GameScreen gameScreen;
    private PlayField playField;
    public boolean tetrominoSpawned = false;
    private Tetromino tetromino;
    public GameState gameState;
    public Tetromino nextTetromino;
    private int levelRowsRemoved;
    public Stage windowStage;
    private Preferences prefs;
    private static Random rand;

    private boolean moved;


    public GameController(GameScreen gameScreen, PlayField playField) {
        this.gameScreen = gameScreen;
        this.playField = playField;
        this.rand = new Random();
        init();
    }

    private void init() {
        prefs = Gdx.app.getPreferences("Tetris");
        gameState = GameState.Running;
        tetromino = new Tetromino(playField, this);
        nextTetromino = new Tetromino(playField, this);
        levelRowsRemoved = 0;
        windowStage = new Stage();

    }

    public void update() {
        switch (gameState) {
            case Pause:
                tetromino.fall(false);
                break;
            case Running:
                checkRows();
                moved = false;
                if (!tetrominoSpawned)
                    spawnTetromino();

                if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                    tetromino.rotate();
                    moved = true;
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                    tetromino.move(-1, 0);
                    moved = true;
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                    tetromino.move(1, 0);
                    moved = true;
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                    tetromino.fall(true);
                    moved = true;
                }

                /*if (Gdx.input.justTouched() || Gdx.input.isTouched()) {
                    tetromino.rotate();
                    moved = true;
                }

                if (Gdx.input.justTouched() ||  Gdx.input.isTouched()) {
                    tetromino.fall(true);
                    moved = true;
                }*/

                if (!moved)
                    tetromino.fall(false);
                playField.update(tetromino);
                break;
            case GameOver:
                break;
        }
        windowStage.act();
    }

    public void gamePause() {
        gameState = GameState.Pause;
    }

    public void gamePlay() {
        gameState = GameState.Running;
    }

    public void onTap() {
        if (tetromino != null && tetromino.grid != null) {

            int gx = Gdx.input.getX();
            int gy = Gdx.input.getY();
            System.out.println(gx + ":" + gy);

            // check if the tap is inside the play field
            /*if (gx > gameScreen.playfieldWidth) {
                System.out.println("tap inside playfield");
            }*/
            tetromino.rotate();
            moved = true;
        }
    }

    public void onLeft() {
        if (tetromino != null && tetromino.grid != null) {
            tetromino.move(-1, 0);
            moved = true;
        }
    }

    public void onRight() {
        if (tetromino != null && tetromino.grid != null) {
            tetromino.move(1, 0);
            moved = true;
        }
    }

    public void onDown() {
        if (tetromino != null && tetromino.grid != null) {
            tetromino.fall(true);
            moved = true;
        }
    }
    private void checkRows() {
        int rowsRemoved = 0;
        if (null == tetromino || System.currentTimeMillis() - tetromino.lastFallTime >= tetromino.delay) {
            boolean checkAgain = false;
            for (int i = 0; i < playField.blocks.length; i++) {
                if (checkAgain) {
                    i -= 1;
                }
                boolean full = true;
                for (int j = 0; j < playField.blocks[0].length; j++) {
                    if (!playField.blocks[i][j]) {
                        full = false;
                    }
                }
                if (full) {
                    removeRow(i);
                    rowsRemoved++;
                    levelRowsRemoved++;
                    checkAgain = true;
                } else {
                    checkAgain = false;
                }
            }
        }

        if (rowsRemoved > 0) {
            play(Assets.instance.sounds.rowCleared);
        }

        switch (rowsRemoved) {
            case 1:
                playField.score += 40 * (playField.level + 1);
                break;
            case 2:
                playField.score += 100 * (playField.level + 1);
                break;
            case 3:
                playField.score += 300 * (playField.level + 1);
                break;
            case 4:
                playField.score += 1200 * (playField.level + 1);
                break;
        }
        // Au bout de 10 lignes supprimé, on lvl up
        if (levelRowsRemoved >= 10) {
            playField.level++;
            levelRowsRemoved = 0;
            play(Assets.instance.sounds.levelUp);
        }
    }

    private void removeRow(int row) {
        for (int j = 0; j < playField.blocks[0].length; j++) {
            playField.blocks[row][j] = false;
            playField.playfield[row][j] = 0;
        }
        for (int i = row; i > 0; i--) {
            for (int j = 0; j < playField.blocks[0].length; j++) {
                if (playField.blocks[i - 1][j]) {
                    playField.blocks[i - 1][j] = false;
                    playField.blocks[i][j] = true;
                    playField.playfield[i][j] = playField.playfield[i - 1][j];
                    playField.playfield[i - 1][j] = 0;
                }
            }
        }
    }

    public void gameOver() {
        gameState = GameState.GameOver;
        play(Assets.instance.sounds.gameOver);
    }

    private void spawnTetromino() {
        if (System.currentTimeMillis() - tetromino.lastFallTime >= tetromino.delay) {
            if (nextTetromino.type == 0) {
                tetromino.init(randInt(1, 7));
            } else {
                tetromino.init(nextTetromino.type);
            }
            nextTetromino.init(randInt(1, 7));
            tetrominoSpawned = true;
        }
    }

    private void play(Sound sound) {
        if (prefs.getBoolean("sound", false)) {
            sound.play();
        }
    }

    /**
     * Returns a pseudo-random number between min and max, inclusive.
     * The difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param min Minimum value
     * @param max Maximum value.  Must be greater than min.
     * @return Integer between min and max, inclusive.
     * @see Random#nextInt(int)
     */
    // Retourne une valeur aleatoire entre min et max
    public static int randInt(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }

    public enum GameState {
        Login, Running, GameOver, Pause //,Intro, Options
    }
}

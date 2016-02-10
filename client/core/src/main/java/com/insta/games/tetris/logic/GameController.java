package com.insta.games.tetris.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.Random;

import com.insta.games.tetris.ui.Assets;
import com.insta.games.tetris.model.Tetromino;
import com.insta.games.tetris.TetrisGame;
import com.insta.games.tetris.ui.screen.PlayField;

/**
 * Created by Julien on 8/2/16.
 */
public class GameController {

    private final TetrisGame game;
    private PlayField playField;
    public boolean tetrominoSpawned = false;
    private Tetromino tetromino;
    public GameState gameState;
    public Tetromino nextTetromino;
    private int levelRowsRemoved;
    public Stage windowStage;
    private Preferences prefs;
    private static Random rand;

    public GameController(TetrisGame game, PlayField playField) {
        this.game = game;
        this.playField = playField;
        this.rand = new Random();
        init();
    }

    private void init() {
        prefs = Gdx.app.getPreferences("Tetris");
        gameState = GameState.Login;
        tetromino = new Tetromino(playField, this);
        nextTetromino = new Tetromino(playField, this);
        levelRowsRemoved = 0;
        windowStage = new Stage();
    }


    public void update() {
        switch (gameState) {
            case Login:
                checkMenuControls();
                break;
            case Running:
                checkRows();
                boolean moved = false;
                if (!tetrominoSpawned) spawnTetromino();
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
                    //tetromino.move(0, 1);
                    tetromino.fall(true);
                    moved = true;
                }
                if (Gdx.input.justTouched() && game.gameScreen != null) {
                    int gx = Gdx.input.getX();
                    int gy = Gdx.input.getY();
                    if (gx > game.gameScreen.leftArrowScreenX &&
                            gx < game.gameScreen.leftArrowScreenX + game.gameScreen.controlScreenWidth &&
                            gy > game.gameScreen.leftArrowScreenY &&
                            gy < game.gameScreen.leftArrowScreenY +  game.gameScreen.controlScreenWidth) {
                        tetromino.move(-1, 0);
                        moved = true;
                    } else if (gx > game.gameScreen.rightArrowScreenX &&
                            gx < game.gameScreen.rightArrowScreenX +  game.gameScreen.controlScreenWidth &&
                            gy > game.gameScreen.rightArrowScreenY &&
                            gy < game.gameScreen.rightArrowScreenY +  game.gameScreen.controlScreenWidth) {
                        tetromino.move(1, 0);
                        moved = true;
                    } else if (gx > game.gameScreen.rotateArrowScreenX &&
                            gx < game.gameScreen.rotateArrowScreenY +  game.gameScreen.controlScreenWidth &&
                            gy > game.gameScreen.rotateArrowScreenY &&
                            gy < game.gameScreen.rotateArrowScreenY +  game.gameScreen.controlScreenWidth) {
                        tetromino.rotate();
                        moved = true;
                    }
                }
                if ((Gdx.input.justTouched() || Gdx.input.isTouched()) && game.gameScreen != null) {
                    int gx = Gdx.input.getX();
                    int gy = Gdx.input.getY();
                    if (gx > game.gameScreen.downArrowScreenX &&
                            gx < game.gameScreen.downArrowScreenX +  game.gameScreen.controlScreenWidth &&
                            gy > game.gameScreen.downArrowScreenY &&
                            gy < game.gameScreen.downArrowScreenY + game.gameScreen.controlScreenWidth) {
                        tetromino.fall(true);
                        moved = true;
                    }
                }
                if (!moved)
                    tetromino.fall(false);
                playField.update(tetromino);
                break;
            case GameOver:
                checkMenuControls();
                break;
        }
        windowStage.act();
    }

    private void checkMenuControls() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY) || Gdx.input.justTouched()) {
            int gx = Gdx.input.getX();
            int gy = Gdx.input.getY();
            if (gx > game.gameScreen.playScreenX &&
                    gx < game.gameScreen.playScreenX + game.gameScreen.controlScreenWidth &&
                    gy > game.gameScreen.playScreenY &&
                    gy < game.gameScreen.playScreenY + game.gameScreen.controlScreenWidth) {
                playField.reset();
                gameState = GameState.Running;

                // Using swipe gesture to move the tetris block
                SimpleDirectionGestureDetector simpleDirectionGestureDetector = new SimpleDirectionGestureDetector(new SimpleDirectionGestureDetector.DirectionListener() {
                    @Override
                    public void onLeft() {
                        tetromino.move(-1, 0);
                    }

                    @Override
                    public void onRight() {
                        tetromino.move(1, 0);
                    }

                    @Override
                    public void onUp() {

                    }

                    @Override
                    public void onDown() {
                        tetromino.fall(true);
                    }

                    @Override
                    public void onTap() {
                        tetromino.rotate();
                    }
                });

                Gdx.input.setInputProcessor(simpleDirectionGestureDetector);
            }

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

    public void dispose() {
        playField.dispose();
        if (windowStage != null) {
            windowStage.dispose();
        }
    }

    public enum GameState {
        Login, Running, GameOver //,Intro, Options
    }


    /**
     * User input controller
     * overide the methods to using swipe gesture left, right, up, down and tap
     */
    public static class SimpleDirectionGestureDetector extends GestureDetector {
        public interface DirectionListener {
            void onLeft();

            void onRight();

            void onUp();

            void onDown();

            void onTap();
        }

        public SimpleDirectionGestureDetector(DirectionListener directionListener) {
            super(new DirectionGestureListener(directionListener));
        }

        private static class DirectionGestureListener implements GestureListener{
            DirectionListener directionListener;

            public DirectionGestureListener(DirectionListener directionListener){
                this.directionListener = directionListener;
            }

            @Override
            public boolean touchDown(float x, float y, int pointer, int button) {
                return false;
            }

            @Override
            public boolean tap(float x, float y, int count, int button) {
                directionListener.onTap();
                return true;
            }

            @Override
            public boolean longPress(float x, float y) {
                return false;
            }

            @Override
            public boolean fling(float velocityX, float velocityY, int button) {
                if(Math.abs(velocityX)>Math.abs(velocityY)){
                    if(velocityX>0){
                        directionListener.onRight();
                    }else{
                        directionListener.onLeft();
                    }
                }else{
                    if(velocityY>0){
                        directionListener.onDown();
                    }else{
                        directionListener.onUp();
                    }
                }
                return true;
            }

            @Override
            public boolean pan(float x, float y, float deltaX, float deltaY) {
                return false;
            }

            @Override
            public boolean panStop(float x, float y, int pointer, int button) {
                return false;
            }

            @Override
            public boolean zoom(float initialDistance, float distance) {
                return false;
            }

            @Override
            public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
                return false;
            }

        }

    }

}

package com.insta.games.tetris;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.insta.games.tetris.logic.GameController;
import com.insta.games.tetris.ui.Assets;
import com.insta.games.tetris.ui.screen.GameScreen;
import com.insta.games.tetris.ui.screen.MainScreen;
import com.insta.games.tetris.ui.screen.PlayField;

public class TetrisGame extends Game {

    private GameController gameController;
    public GameScreen gameScreen;

    protected final float gameWidth = 355f;
    private final float gameHeight = 625f;

    private boolean paused;
    protected int screenWidth;
    protected int screenHeight;

    @Override
    public void create() {

        //screenWidth = Gdx.graphics.getWidth();
        //screenHeight = Gdx.graphics.getHeight();

        /*PlayField playField = new PlayField();

        gameController = new GameController(this, playField);
        gameScreen = new GameScreen(playField, gameController, gameWidth, gameHeight);*/

        //paused = false;

        Assets.instance.init(new AssetManager());
        this.setScreen(new MainScreen(this, gameWidth, gameHeight));
    }

    @Override
    public synchronized void render() {
        super.render();

       /* if (!paused) {
            gameController.update();
        }

        gameScreen.render();*/
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        //gameScreen.resize(width, height);
    }

    @Override
    public void pause() {
        super.pause();
        //paused = true;
    }

    @Override
    public void resume() {
        super.resume();
        //paused = false;
    }

    @Override
    public void dispose() {
        super.dispose();
        //gameScreen.dispose();
        //gameController.dispose();
    }

}

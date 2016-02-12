package com.insta.games.tetris;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.insta.games.tetris.ui.Assets;
import com.insta.games.tetris.ui.screen.MainScreen;

public class TetrisGame extends Game {

    public final float gameWidth = 355f;
    public final float gameHeight = 625f;

    @Override
    public void create() {
        // lunch main screen
        Assets.instance.init(new AssetManager());
        this.setScreen(new MainScreen(this, gameWidth, gameHeight));
    }

    @Override
    public synchronized void render() {
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}

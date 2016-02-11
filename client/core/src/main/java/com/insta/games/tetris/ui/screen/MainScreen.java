package com.insta.games.tetris.ui.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.insta.games.tetris.TetrisGame;
import com.insta.games.tetris.logic.GameController;
import com.insta.games.tetris.ui.Assets;

/**
 * Created by Pat on 10/02/2016.
 */
public class MainScreen implements Screen {

    private TetrisGame game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Vector3 touchPoint;
    public GameScreen gameScreen;
    private float gameWidth;
    private float gameHeight;
    boolean showTouchePoint;

    public MainScreen(TetrisGame game, float gameWidth, float gameHeight) {
        this.game = game;
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;

        this.batch = new SpriteBatch();
        this.touchPoint = new Vector3();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, gameWidth, gameHeight);
    }

    boolean touched(Rectangle r){
        if (!Gdx.input.justTouched())
            return false;

        camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
        return r.contains(touchPoint.x, touchPoint.y);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        // Lunch game screen when user pressed Play button
        if (touched(Assets.mainScreenPlayButton)){
            gameScreen = new GameScreen(game);
            game.setScreen(gameScreen);
        }

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Draw background mainscreen
        batch.draw(Assets.mainScreen, 0, 0, gameWidth, gameHeight);
        batch.end();

        /** show touch zone ** FOR DEBUG USAGE **/
        showTouchePoint = false;
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            //System.out.println("Space key pressed");
            showTouchePoint = true;
        }
        if (showTouchePoint) {
            // Play button
            ShapeRenderer shape = new ShapeRenderer();
            shape.setProjectionMatrix(camera.combined);
            shape.begin(ShapeRenderer.ShapeType.Line);
            shape.rect(87, 12, 187, 61);
            shape.setColor(Color.BLUE);
            shape.end();
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}

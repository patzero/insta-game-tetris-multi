package com.insta.games.tetris.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener {

    public static final String TAG = Assets.class.getName();
    public static final Assets instance = new Assets();
    private AssetManager assetManager;
    public AssetTetromino tetromino;
    public AssetControls controls;
    public AssetSounds sounds;

    // Screen
    //public static TextureRegion mainScreen;
    public static Texture mainScreen;

    // Touch point
    public static Rectangle mainScreenPlayButton;


    private Assets() {
    }

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
        // set asset manager error handler
        assetManager.setErrorListener(this);

        mainScreen = new Texture(Gdx.files.internal("tetris/images/main_screen.png"));
        mainScreenPlayButton = new Rectangle(87, 12, 187, 61);


        assetManager.load("tetris/sounds/level_up.wav", Sound.class);
        assetManager.load("tetris/sounds/row_cleared.wav", Sound.class);
        assetManager.load("tetris/sounds/game_over.wav", Sound.class);

        // start loading assets and wait until finished
        assetManager.finishLoading();

        Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
        for (String a : assetManager.getAssetNames()) {
            Gdx.app.debug(TAG, "asset: " + a);
        }

        // create game resource objects
        tetromino = new AssetTetromino();
        controls = new AssetControls();
        sounds = new AssetSounds();
    }

    public class AssetTetromino {
        public final Texture elementBlueSquare;
        public final Texture elementCyanSquare;
        public final Texture elementGreenSquare;
        public final Texture elementGreySquare;
        public final Texture elementOrangeSquare;
        public final Texture elementPurpleSquare;
        public final Texture elementRedSquare;
        public final Texture elementYellowSquare;

        public AssetTetromino() {
            elementBlueSquare = new Texture(Gdx.files.internal("tetris/images/blue_bloc.png"));
            elementCyanSquare = new Texture(Gdx.files.internal("tetris/images/cyan_bloc.png"));
            elementGreenSquare = new Texture(Gdx.files.internal("tetris/images/green_bloc.png"));
            elementGreySquare = new Texture(Gdx.files.internal("tetris/images/white_bloc.png"));
            elementOrangeSquare = new Texture(Gdx.files.internal("tetris/images/orange_bloc.png"));
            elementPurpleSquare = new Texture(Gdx.files.internal("tetris/images/purple_bloc.png"));
            elementRedSquare = new Texture(Gdx.files.internal("tetris/images/red_bloc.png"));
            elementYellowSquare = new Texture(Gdx.files.internal("tetris/images/yellow_bloc.png"));
        }
    }

    public class AssetControls {
        public final Texture play;

        public AssetControls() {
            play = new Texture(Gdx.files.internal("tetris/images/play.png"));
        }
    }

    public class AssetSounds {

        public final Sound levelUp;
        public final Sound rowCleared;
        public final Sound gameOver;

        public AssetSounds() {
            levelUp = assetManager.get("tetris/sounds/level_up.wav", Sound.class);
            rowCleared = assetManager.get("tetris/sounds/row_cleared.wav", Sound.class);
            gameOver = assetManager.get("tetris/sounds/game_over.wav", Sound.class);
        }
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset '" + asset.toString() + "'", throwable);

    }

    @Override
    public void dispose() {
        assetManager.dispose();

    }

}

/**
 * This file is part of WANTED: Bad-ou-Alyve.
 *
 * WANTED: Bad-ou-Alyve is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WANTED: Bad-ou-Alyve is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WANTED: Bad-ou-Alyve.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.badoualy.badoualyve.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.github.badoualy.badoualyve.GameEngine;
import com.github.badoualy.badoualyve.listener.OnFightFinishedListener;
import com.github.badoualy.badoualyve.listener.OnSignedListener;
import com.github.badoualy.badoualyve.model.FightResult;
import com.github.badoualy.badoualyve.model.Player;
import com.github.badoualy.badoualyve.ui.screen.FixedFpsScreen;
import com.github.badoualy.badoualyve.ui.stage.HomeStage;
import com.github.badoualy.badoualyve.ui.stage.IntroStage;

import de.tomgrill.gdxdialogs.core.GDXDialogs;
import de.tomgrill.gdxdialogs.core.GDXDialogsSystem;
import de.tomgrill.gdxdialogs.core.dialogs.GDXButtonDialog;
import de.tomgrill.gdxdialogs.core.listener.ButtonClickListener;

/** Main class for GDX framework, handle displaying different screens, and our game engine */
public class WantedGame extends Game implements OnSignedListener, OnFightFinishedListener {

    public static final String TITLE = "WANTED: Bad-ou-Alyve";

    // Pixel-real size
    public static int WIDTH;
    public static int HEIGHT;

    // V_ for virtual, it's the camera size, not the real one
    public static int V_WIDTH = 1024; // Default value, need tweak on android
    public static final int V_HEIGHT = 720;

    private GdxUtils gdxUtils;
    private GDXDialogs dialogs;

    private GameEngine gameEngine;

    private boolean playerSignedIn = false;
    private int fightResult = 0;

    @Override
    public void create() {
        // We can't use only static method here, due to how android handles resources / static, it may causes bugs such as
        // assets not correctly reloaded after a "home button" press then re-opening the app
        gdxUtils = new GdxUtils();
        Texture.setAssetManager(gdxUtils.assetManager);

        dialogs = GDXDialogsSystem.install();

        WIDTH = Gdx.graphics.getWidth();
        HEIGHT = Gdx.graphics.getHeight();

        // Adjust width to keep good ratio on mobile screens
        V_WIDTH = (V_HEIGHT * WIDTH) / HEIGHT;

        // Our "controller" class
        gameEngine = new GameEngine(this, this);

        // First screen, login
        displayIntroScreen();
    }

    @Override
    public void dispose() {
        super.dispose();
        gdxUtils.assetManager.dispose();
    }

    @Override
    public void render() {
        // Main-loop method, automatically called by gdx

        if (gameEngine.getPlayer() != null && !playerSignedIn) {
            // Means the user just signed in, go to home screen
            playerSignedIn = true;
            displayHomeScreen();
        } else if (fightResult != 0) {
            // A fight result just came in, go back to home screen
            displayHomeScreen();
            showResultDialog(fightResult);
            fightResult = 0;
        }

        // This call will render the currently displaying screen
        super.render();
    }

    private void displayIntroScreen() {
        gdxUtils.loadIntroAssets();
        gdxUtils.assetManager.finishLoading();

        setScreen(new FixedFpsScreen(new IntroStage(gameEngine), 30));
    }

    private void displayHomeScreen() {
        gdxUtils.loadHomeAssets();
        gdxUtils.assetManager.finishLoading();

        setScreen(new FixedFpsScreen(new HomeStage(gameEngine), 30));// 30 is way more than enough for a home screen
    }

    @Override
    public void onSignedIn(Player player) {
        // Do something interesting...
    }

    @Override
    public void onFightFinished(int result) {
        fightResult = result;
    }

    private void showResultDialog(int result) {
        String messageContent = "";
        switch (result) {
            case FightResult.NO_OPPONENT_FOUND:
                messageContent = "No opponent found, probably no one on the server...";
                break;
            case FightResult.VICTORY:
                messageContent = "You won!";
                break;
            case FightResult.DEFEAT:
                messageContent = "You lost!";
                break;
        }

        final GDXButtonDialog dialog = dialogs.newDialog(GDXButtonDialog.class);
        dialog.setTitle("Fight result");
        dialog.setMessage(messageContent);
        dialog.setClickListener(new ButtonClickListener() {
            @Override
            public void click(int button) {
                dialog.dismiss();
            }
        });

        dialog.addButton("OK");
        dialog.build().show();
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    ////////////// SOME USEFUL STATIC METHODS TO ACCESS VALUES FROM ANYWHERE //////////////
    ///////////////////////////////////////////////////////////////////////////////////////
    public static WantedGame game() {
        return ((WantedGame) Gdx.app.getApplicationListener());
    }

    public static GdxUtils gdxUtils() {
        return ((WantedGame) Gdx.app.getApplicationListener()).gdxUtils;
    }

    public static Player player() {
        return ((WantedGame) Gdx.app.getApplicationListener()).gameEngine.getPlayer();
    }
}
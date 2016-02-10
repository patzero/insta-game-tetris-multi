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
package com.github.badoualy.badoualyve.ui.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.badoualy.badoualyve.listener.OnSignInListener;
import com.github.badoualy.badoualyve.ui.AssetsUtils;
import com.github.badoualy.badoualyve.ui.actor.Dialog;
import com.github.badoualy.badoualyve.ui.screen.FixedFpsScreen;

import static com.github.badoualy.badoualyve.ui.WantedGame.HEIGHT;
import static com.github.badoualy.badoualyve.ui.WantedGame.WIDTH;
import static com.github.badoualy.badoualyve.ui.WantedGame.game;

public class IntroStage extends Stage {

    private ShapeRenderer shapeRenderer;
    private Dialog dialog;
    private Music startSound;

    private OnSignInListener listener;

    public IntroStage(OnSignInListener listener) {
        // The clean way to be able to call a method when an event occurs is to wrap this event into a listener, even if we know the target
        // will always be the GameEngine. This way, it doesn't create any dependency to it, and can be easily changed at any time
        this.listener = listener;

        initActors();
        startSound = Gdx.audio.newMusic(Gdx.files.internal(AssetsUtils.SOUND_START));
        Gdx.input.setOnscreenKeyboardVisible(true);
    }

    @Override
    public void dispose() {
        Gdx.input.setOnscreenKeyboardVisible(false);
        super.dispose();
    }

    private void initActors() {
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setColor(0.8f, 0.8f, 0.8f, 1f);

        dialog = new Dialog();
        dialog.setTitle("Login - Select your character name");
        dialog.setPosition(getWidth() / 2 - dialog.getWidth() / 2, getHeight() / 2 - dialog.getHeight() / 2);
        addActor(dialog);
    }

    @Override
    public void draw() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(0, 0, WIDTH, HEIGHT);
        shapeRenderer.end();

        super.draw();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public boolean keyDown(int keyCode) {
        if (keyCode == Input.Keys.ENTER && dialog.getInputValue().length() > 0) {
            startSound.setOnCompletionListener(new Music.OnCompletionListener() {
                @Override
                public void onCompletion(Music music) {
                    music.dispose();
                    listener.onSignIn(dialog.getInputValue());
                }
            });
            startSound.play();

            game().setScreen(new FixedFpsScreen(new LoadingStage(), 30));
        } else if (keyCode == Input.Keys.BACKSPACE) {
            dialog.setInputValue(dialog.getInputValue().substring(0, dialog.getInputValue().length() - 1));
        }

        return super.keyDown(keyCode);
    }

    @Override
    public boolean keyTyped(char character) {
        if ((character >= 'a' && character <= 'z')
                || (character >= 'A' && character <= 'Z')
                || (character >= '0' && character <= '9')) {
            dialog.keyTyped(character);
            return true;
        }

        return super.keyTyped(character);
    }
}

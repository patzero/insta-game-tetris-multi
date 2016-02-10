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
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.github.badoualy.badoualyve.listener.OnFightListener;
import com.github.badoualy.badoualyve.ui.AssetsUtils;
import com.github.badoualy.badoualyve.ui.actor.FightButton;
import com.github.badoualy.badoualyve.ui.actor.StatDialog;
import com.github.badoualy.badoualyve.ui.screen.FixedFpsScreen;

import static com.github.badoualy.badoualyve.ui.WantedGame.*;

public class HomeStage extends Stage {

    // References to super attributes
    private OrthographicCamera cam;
    private Batch batch;

    // Actors
    private Image background;
    private StatDialog dialogStat;
    private FightButton btFight;

    private OnFightListener listener;

    public HomeStage(OnFightListener listener) {
        // The clean way to be able to call a method when an event occurs is to wrap this event into a listener, even if we know the target
        // will always be the GameEngine. This way, it doesn't create any dependency to it, and can be easily changed at any time
        this.listener = listener;

        initViewport();
        initActors();
    }

    private void initViewport() {
        getViewport().setWorldSize(V_WIDTH, V_HEIGHT);

        cam = new OrthographicCamera();
        cam.setToOrtho(false, V_WIDTH, V_HEIGHT); // false for yDown => (0, 0) is bottom-left corner
        getViewport().setCamera(cam); // Set stage default camera

        batch = getBatch();
    }

    private void initActors() {
        background = gdxUtils().createImageFromTexture(AssetsUtils.BG_HOME);

        // Create a dialog in the upper-right corner
        dialogStat = new StatDialog(player());
        dialogStat.setPosition(WIDTH - dialogStat.getWidth(), HEIGHT - dialogStat.getHeight());
        dialogStat.setTitle(player().name);

        // Our match-making button, wrap the listener to change stage
        btFight = new FightButton(player().name, new OnFightListener() {
            @Override
            public void onFight() {
                game().setScreen(new FixedFpsScreen(new LoadingStage(), 30));
                listener.onFight();
            }
        });
        btFight.setPosition(10, getHeight() - btFight.getHeight() - 10);

        // The order matters! It defines the order of acting/drawing
        addActor(background);
        addActor(dialogStat);
        addActor(btFight);
    }

    @Override
    public void draw() {
        // If you need to do some operations, like update the camera position, do it here

        // Important, will draw all your actors
        super.draw();
    }

    @Override
    public void act(float delta) {
        // Compute, update values from your world, etc...
        // Be careful whether you act before or after your actors (thus the super call)

        // This will call Actor#act for all attached actors
        super.act(delta);
    }
}

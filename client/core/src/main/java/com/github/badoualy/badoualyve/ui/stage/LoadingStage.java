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
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.github.badoualy.badoualyve.ui.AssetsUtils;

import static com.github.badoualy.badoualyve.ui.WantedGame.HEIGHT;
import static com.github.badoualy.badoualyve.ui.WantedGame.WIDTH;
import static com.github.badoualy.badoualyve.ui.WantedGame.gdxUtils;

public class LoadingStage extends Stage {

    private ShapeRenderer shapeRenderer;
    private Image icLoading;

    public LoadingStage() {
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setColor(0.8f, 0.8f, 0.8f, 1f);

        icLoading = gdxUtils().createImageFromTexture(AssetsUtils.IC_LOADING);
        icLoading.setPosition(getWidth() / 2 - icLoading.getWidth() / 2, getHeight() / 2 - icLoading.getHeight() / 2);
        icLoading.setOrigin(icLoading.getWidth() / 2, icLoading.getHeight() / 2);

        addActor(icLoading);
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
        if (icLoading.getParent() == getRoot())
            icLoading.setRotation(icLoading.getRotation() + delta * 200);
        super.act(delta);
    }
}

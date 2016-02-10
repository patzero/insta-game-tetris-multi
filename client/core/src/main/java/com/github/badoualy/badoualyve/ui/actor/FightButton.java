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
package com.github.badoualy.badoualyve.ui.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.github.badoualy.badoualyve.listener.OnFightListener;
import com.github.badoualy.badoualyve.ui.AssetsUtils;
import com.github.badoualy.badoualyve.ui.WantedGame;

import static com.github.badoualy.badoualyve.ui.WantedGame.gdxUtils;

// We also could've make FightButton override ImageButton and override its draw method
public class FightButton extends Group {

    private ImageButton btFight;
    private BitmapFont font;

    private String playerName;

    private OnFightListener listener;

    public FightButton(String playerName, final OnFightListener listener) {
        this.playerName = playerName;
        this.listener = listener;

        btFight = new ImageButton(new TextureRegionDrawable(new TextureRegion(gdxUtils().getTexture(AssetsUtils.BG_BT_DUEL))));
        font = WantedGame.gdxUtils().getDefaultFont();

        setSize(btFight.getWidth(), btFight.getHeight());
        addActor(btFight);

        btFight.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.onFight();
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        font.draw(batch, playerName, getX() + 10, getY() + getHeight() / 2 + 10);
        font.draw(batch, "Click here to fight", getWidth() - 145, getY() + getHeight() / 2 + 10);
    }
}

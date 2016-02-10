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
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.github.badoualy.badoualyve.model.Player;
import com.github.badoualy.badoualyve.ui.AssetsUtils;
import com.github.badoualy.badoualyve.ui.WantedGame;

import java.util.ArrayList;
import java.util.List;

import static com.github.badoualy.badoualyve.ui.WantedGame.gdxUtils;

public class StatDialog extends Group {

    private Player player;

    private Image bg;
    private BitmapFont font, smallFont;

    private String title;
    private List<String> stats = new ArrayList<String>();

    private float titleWidth;

    public StatDialog(Player player) {
        this.player = player;

        bg = new Image(gdxUtils().getTexture(AssetsUtils.BG_STATS));
        setSize(bg.getWidth(), bg.getHeight());
        addActor(bg);

        font = WantedGame.gdxUtils().getDefaultFont();
        smallFont = WantedGame.gdxUtils().getDefaultFontSmall();

        // Compute title width
        GlyphLayout layout = new GlyphLayout(); // Don't do this every frame! Store it as member
        layout.setText(font, player.name);
        titleWidth = layout.width;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Update stat values to display
        stats.clear();
        stats.add("Attack: " + player.attack);
        stats.add("Defense: " + player.defense);
        stats.add("Speed: " + player.speed);
        stats.add("Hp: " + player.hp);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if (title != null)
            font.draw(batch, title, getX() + getWidth() / 2 - titleWidth / 2, getTop() - 25);

        for (int i = 0; i < stats.size(); i++)
            smallFont.draw(batch, stats.get(i), getX() + 70, getY() + 150 - (i * smallFont.getLineHeight()));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;

        // Compute title width
        GlyphLayout layout = new GlyphLayout(); // Don't do this every frame! Store it as member
        layout.setText(font, player.name);
        titleWidth = layout.width;
    }
}

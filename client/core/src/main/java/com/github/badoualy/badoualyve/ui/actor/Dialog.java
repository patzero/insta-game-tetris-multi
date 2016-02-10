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
import com.github.badoualy.badoualyve.ui.AssetsUtils;

import static com.github.badoualy.badoualyve.ui.WantedGame.gdxUtils;

public class Dialog extends Group {

    private String title = "";
    private String message = "Press [ENTER] when ready";
    private String inputValue = "";

    private BitmapFont font;
    private Image bg;

    private float titleWidth = 0f;
    private float messageWith = 0f;
    private float inputWidth = 0f;

    private float counter = 0f;
    private boolean displaying = true;

    public Dialog() {
        font = gdxUtils().getDefaultFont();
        bg = gdxUtils().createImageFromTexture(AssetsUtils.BG_DIALOG);
        setSize(bg.getWidth(), bg.getHeight());
        addActor(bg);

        // Compute title width
        GlyphLayout layout = new GlyphLayout(); // Don't do this every frame! Store it as member
        layout.setText(font, inputValue);
        inputWidth = layout.width;

        // Compute message width
        layout = new GlyphLayout(); // Don't do this every frame! Store it as member
        layout.setText(font, message);
        messageWith = layout.width;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        // Center top title
        font.draw(batch, title, getX() + getWidth() / 2 - titleWidth / 2, getTop() - 30);

        // Blinking message
        if (displaying)
            font.draw(batch, message, getX() + getWidth() / 2 - messageWith / 2, getY() + 80);

        // Current input
        font.draw(batch, inputValue, getX() + getWidth() / 2 - inputWidth / 2, getTop() - 150);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Used to blink the message
        counter += delta;
        if (counter >= 0.44f) {
            counter = 0;
            displaying = !displaying;
        }
    }

    public void setTitle(String title) {
        this.title = title;

        // Compute new title width
        GlyphLayout layout = new GlyphLayout(); // Don't do this every frame! Store it as member
        layout.setText(font, title);
        titleWidth = layout.width;
    }

    public String getTitle() {
        return title;
    }

    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
        // Compute new input width
        GlyphLayout layout = new GlyphLayout(); // Don't do this every frame! Store it as member
        layout.setText(font, inputValue);
        inputWidth = layout.width;
    }

    public void keyTyped(char c) {
        inputValue += c;
        // Compute new input width
        GlyphLayout layout = new GlyphLayout(); // Don't do this every frame! Store it as member
        layout.setText(font, inputValue);
        inputWidth = layout.width;
    }

    public String getInputValue() {
        return inputValue;
    }
}

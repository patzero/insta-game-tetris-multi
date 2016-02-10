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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/** Utils class to makes it easy to load resources from assets */
public final class GdxUtils {

    /** Game's asset manager */
    public final AssetManager assetManager = new AssetManager();

    private BitmapFont defaultFontSmall, defaultFont;

    public GdxUtils() {

    }

    /** Preload all the assets necessary for the intro stage */
    public void loadIntroAssets() {
        assetManager.load(AssetsUtils.BG_DIALOG, Texture.class);
        assetManager.load(AssetsUtils.IC_LOADING, Texture.class);
    }

    /** Preload all the assets necessary for the home stage */
    public void loadHomeAssets() {
        assetManager.load(AssetsUtils.BG_HOME, Texture.class);
        assetManager.load(AssetsUtils.BG_STATS, Texture.class);
        assetManager.load(AssetsUtils.BG_BT_DUEL, Texture.class);
    }

    /** Shortcut new Image(getTexture(name)) */
    public Image createImageFromTexture(String name) {
        return new Image(getTexture(name));
    }

    /** Shortcut assetManager.get(name, Texture.class) */
    public Texture getTexture(String name) {
        return assetManager.get(name, Texture.class);
    }

    /** Shortcut assetManager.get(name, TextureAtlas.class) */
    public TextureAtlas getTextureAtlas(String name) {
        return assetManager.get(name, TextureAtlas.class);
    }

    /** Shortcut assetManager.get(name, BitmapFont.class) */
    public BitmapFont getFont(String name) {
        return assetManager.get(name, BitmapFont.class);
    }

    private void initDefaultFontIfNeeded() {
        if (defaultFont == null) {
            // Use gdx-freetype extension to create the default font
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(AssetsUtils.FONT));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.color = new Color(0.4f, 0.4f, 0.4f, 1f);

            // Small font
            parameter.size = 22;
            defaultFontSmall = generator.generateFont(parameter); // font size 22

            // Normal font
            parameter.size = 26;
            defaultFont = generator.generateFont(parameter); // font size 26

            generator.dispose(); // don't forget to dispose to avoid memory leaks!
        }
    }

    public BitmapFont getDefaultFont() {
        initDefaultFontIfNeeded();
        return defaultFont;
    }

    public BitmapFont getDefaultFontSmall() {
        initDefaultFontIfNeeded();
        return defaultFontSmall;
    }
}
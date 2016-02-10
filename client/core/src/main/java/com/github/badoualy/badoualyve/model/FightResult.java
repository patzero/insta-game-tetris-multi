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
package com.github.badoualy.badoualyve.model;

import com.google.gson.annotations.SerializedName;

/** Maps the response received by the server after a fight */
public class FightResult {

    public static final short VICTORY = 1;
    public static final short DEFEAT = 2;
    public static final short NO_OPPONENT_FOUND = -1;

    public short result;
    // @SerializedName indicates to Gson that when converting this object from/to a json, the field name should be "user" and not "player"
    @SerializedName("user")
    public Player player;

    public FightResult() {

    }

    public FightResult(short result, Player player) {
        this.result = result;
        this.player = player;
    }
}

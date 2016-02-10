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
package com.github.badoualy.badoualyve;

import com.github.badoualy.badoualyve.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameEngine {

    // Utils
    private static final Random random = new SecureRandom();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private List<User> userList;
    private File saveFile;

    public GameEngine(String dbPath) {
        userList = new ArrayList<>();
        saveFile = new File(dbPath);
        load();
    }

    public User generateNewUser(String name) {
        User user = new User();
        user.name = getFreeName(name);
        user.token = new BigInteger(130, random).toString(32).replace(" ", "");

        user.attack = 100 + random.nextInt(50);
        user.defense = 100 + random.nextInt(50);
        user.speed = 100 + random.nextInt(30);
        user.hp = 1000 + random.nextInt(200);
        user.lastUpdateTime = System.currentTimeMillis();

        userList.add(user);
        return user;
    }

    private String getFreeName(String preferredName) {
        String name = preferredName;
        while (userAlreadyExists(name))
            name = preferredName + random.nextInt(999);
        return name;
    }

    private boolean userAlreadyExists(String name) {
        return userList.stream().anyMatch(u -> u.name.equalsIgnoreCase(name));
    }

    public User findUser(String name) {
        return userList.stream().filter(u -> u.name.equalsIgnoreCase(name)).findFirst().orElseGet(null);
    }

    public void updateUserStatsAndSave(User user) {
        updateUserStats(user);
        save();
    }

    /**
     * Automatic evolution (time)
     */
    public void updateUserStats(User user) {
        long delta = System.currentTimeMillis() - user.lastUpdateTime;

        user.attack += (delta * 20) / 500;// +20 every 500 ms
        user.defense += (delta * 15) / 500;// +15 every 500 ms
        user.speed += (delta * 25) / 750;// +25 every 500 ms
        user.hp += (delta * 20) / 650;// +20 every 650 ms

        user.lastUpdateTime = System.currentTimeMillis();
    }

    /**
     * Evolution after a fight won
     */
    private void evolveUser(User user) {
        user.attack += 40 + random.nextInt(20);
        user.defense += 20 + random.nextInt(15);
        user.speed += 10 + random.nextInt(10);
        user.hp += 30 + random.nextInt(10);

        updateUserStats(user); // Time evolution (will update lastUpdateTime)
    }

    /**
     * @return true if the attacker won
     */
    public boolean resolveFight(User attacker, User defenser) {
        updateUserStats(attacker);
        updateUserStats(defenser);

        User first = attacker.speed >= defenser.speed ? attacker : defenser;
        User second = first == attacker ? defenser : attacker;

        // Reset current hp to max hp
        first.currHp = first.hp;
        second.currHp = second.hp;

        // Fight
        do {
            if (resolveAttack(first, second))
                break; // No double-KO
            resolveAttack(second, first);
        } while (first.currHp > 0 && second.currHp > 0);

        boolean wonFight = attacker.currHp > 0;
        if (wonFight)
            evolveUser(attacker);

        updateUserStats(attacker);
        updateUserStats(defenser);
        return wonFight;
    }

    /**
     * @return true if the defenser lost the fight (hp are 0)
     **/
    private boolean resolveAttack(User attacker, User defenser) {
        defenser.currHp -= attacker.attack - (0.5f * defenser.defense);
        if (defenser.currHp < 0)
            defenser.currHp = 0;
        return defenser.currHp == 0;
    }

    /**
     * Match-making algorithm
     */
    public User findOpponent(User user) {
        // Stupid algorithm, just looking for opponent with closest attack
        return userList.stream().filter(u -> u != user).min((u1, u2) -> (int) Math.abs(u1.attack - u2.attack)).orElse(null);
    }

    public void load() {
        try {
            String s = FileUtils.readFileToString(saveFile, "UTF-8");

            Type listType = new TypeToken<ArrayList<User>>() {
            }.getType();
            userList = new Gson().fromJson(s, listType);
        } catch (Exception e) {
        }
    }

    public void save() {
        try {
            FileUtils.writeStringToFile(saveFile, toJson());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String toJson() {
        return gson.toJson(userList);
    }
}

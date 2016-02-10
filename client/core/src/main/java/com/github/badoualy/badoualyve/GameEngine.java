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

import com.github.badoualy.badoualyve.listener.OnFightFinishedListener;
import com.github.badoualy.badoualyve.listener.OnFightListener;
import com.github.badoualy.badoualyve.listener.OnSignInListener;
import com.github.badoualy.badoualyve.listener.OnSignedListener;
import com.github.badoualy.badoualyve.model.FightResult;
import com.github.badoualy.badoualyve.model.Player;
import com.github.badoualy.badoualyve.net.NetworkOperation;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class GameEngine implements OnSignInListener, OnFightListener {

    // REST-API
    private static final String URL = "http://localhost:8080";
    private static final String URL_CONNECT = URL + "/connect/%s";
    private static final String URL_GET_SELF = URL + "/users/%s";
    private static final String URL_FIGHT = URL + "/users/%s/fight";

    private Player player;

    private OnSignedListener playerSignedInListener;
    private OnFightFinishedListener fightFinishedListener;

    public GameEngine(OnSignedListener playerSignedInListener, OnFightFinishedListener fightFinishedListener) {
        this.playerSignedInListener = playerSignedInListener;
        this.fightFinishedListener = fightFinishedListener;
    }

    public void start() {
        // Get updates from server on 500 ms interval
        Observable.interval(500, TimeUnit.MILLISECONDS)
                  .subscribeOn(Schedulers.computation())
                  .observeOn(Schedulers.computation())
                  .doOnNext(new Action1<Long>() {
                      @Override
                      public void call(Long aLong) {
                          //getSelf(); // Uncomment this to get self from server instead of manual "simulation"
                          updateUserStats();
                      }
                  }).subscribe();
    }

    /**
     * Simulates Automatic evolution (time)
     * This method was copied from the server's code
     */
    public void updateUserStats() {
        long delta = System.currentTimeMillis() - player.lastUpdateTime;

        player.attack += (delta * 20) / 500;// +20 every 500 ms
        player.defense += (delta * 15) / 500;// +15 every 500 ms
        player.speed += (delta * 25) / 750;// +25 every 500 ms
        player.hp += (delta * 20) / 650;// +20 every 650 ms
    }

    @Override
    public void onSignIn(String name) {
        new NetworkOperation<Player>(URL_CONNECT, name, Player.class)
                .execute()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        // TODO....
                        System.exit(0);
                    }
                })
                .doOnSuccess(new Action1<Player>() {
                    @Override
                    public void call(Player player) {
                        System.out.println("Connected as " + player.name);
                        GameEngine.this.player = player;
                        start();

                        // Notify the UI
                        playerSignedInListener.onSignedIn(player);
                    }
                }).subscribe();
    }

    @Override
    public void onFight() {
        new NetworkOperation<FightResult>(URL_FIGHT, player.name, FightResult.class)
                .execute()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .delay(1500, TimeUnit.MILLISECONDS)
                .doOnSuccess(new Action1<FightResult>() {
                    @Override
                    public void call(FightResult fightResult) {
                        if (fightResult.player == null && fightResult.result != FightResult.NO_OPPONENT_FOUND)
                            throw new RuntimeException("Weird stuff happened");
                        player.copyFrom(fightResult.player);

                        // Notify the UI
                        fightFinishedListener.onFightFinished(fightResult.result);
                    }
                }).subscribe();
    }

    private void getSelf() {
        new NetworkOperation<Player>(URL_GET_SELF, player.name, Player.class)
                .execute()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .doOnSuccess(new Action1<Player>() {
                    @Override
                    public void call(Player player) {
                        // We have to use this method to keep the same player instance, because the UI is using this instance
                        GameEngine.this.player.copyFrom(player);
                    }
                }).subscribe();
    }

    public Player getPlayer() {
        return player;
    }
}

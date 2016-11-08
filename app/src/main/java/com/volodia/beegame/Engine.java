package com.volodia.beegame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Volodia on 07.11.2016.
 */

public class Engine {

    public static final int QUEEN_BEE_COUNT = 1;
    public static final int WORKER_BEE_COUNT = 5;
    public static final int DRONE_BEE_COUNT = 8;
    private final GameActivity activity;

    List<IBee> bees;
    List<IBee> aliveBees;
    Random random = new Random();

    boolean gameInProgress;

    public Engine(GameActivity activity) {
        this.activity = activity;
    }


    public void setBees(List<IBee> bees) {
        this.bees = bees;
        aliveBees = new ArrayList<>(bees);
    }

    public void onStartClick() {
        restartGame();
    }

    public void restartGame() {
        gameInProgress = true;
        for (IBee bee : bees) {
            bee.resurrect();
        }
        aliveBees = new ArrayList<>(bees);
    }

    public void onHitClick() {
        if (aliveBees == null || aliveBees.size() == 0) throw new IllegalStateException();
        int i = random.nextInt(aliveBees.size());
        IBee bee = aliveBees.get(i);
        if (bee.hitBee()) {
            aliveBees.remove(i);
            if (bee.getBeeType() == BeeType.QUEEN) {
                endGame();
            }
        }
    }

    private void endGame() {
        gameInProgress = false;
        if (activity != null) activity.gameEnded();
    }

    public boolean gameInProgress() {
        return gameInProgress;
    }

    public List<IBee> getAliveBees() {
        return aliveBees;
    }

    public List<IBee> getBees() {
        return bees;
    }
}

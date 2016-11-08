package com.volodia.beegame;

/**
 * Created by Volodia on 07.11.2016.
 */

public class Bee implements IBee {

    private int lifespan;
    private int pointsPerHit;
    private BeeType beeType;

    private int currentHealth;

    public Bee(int lifespan, int pointsPerHit, BeeType beeType) {
        this.lifespan = lifespan;
        this.pointsPerHit = pointsPerHit;
        this.beeType = beeType;
        currentHealth = lifespan;
    }

    public boolean hitBee() {
        currentHealth -= pointsPerHit;
        return currentHealth <= 0;
    }

    @Override
    public void resurrect() {
        currentHealth = lifespan;
    }

    public int getLifespan() {
        return lifespan;
    }

    public int getPointsPerHit() {
        return pointsPerHit;
    }

    public BeeType getBeeType() {
        return beeType;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

}

package com.volodia.beegame;

/**
 * Created by Volodia on 07.11.2016.
 */

public interface IBee {

    public boolean hitBee();

    void resurrect();

    public int getLifespan();

    public BeeType getBeeType();

    public int getCurrentHealth();

    public int getPointsPerHit();

}

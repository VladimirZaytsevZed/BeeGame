package com.volodia.beegame;

/**
 * Created by Volodia on 07.11.2016.
 */

public class BeeFactory {

    public static Bee createBee(BeeType beeType) {
        switch (beeType) {
            case QUEEN:
                return new Bee(100, 8, BeeType.QUEEN);
            case WORKER:
                return new Bee(75, 10, BeeType.WORKER);
            case DRONE:
                return new Bee(50, 12, BeeType.DRONE);
            default:
                return null;
        }
    }
}

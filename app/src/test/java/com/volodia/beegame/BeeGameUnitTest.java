package com.volodia.beegame;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


import java.util.ArrayList;
import java.util.List;

import static com.volodia.beegame.Engine.DRONE_BEE_COUNT;
import static com.volodia.beegame.Engine.WORKER_BEE_COUNT;
import static org.junit.Assert.*;

public class BeeGameUnitTest {

    Engine engine;

    @Before
    public void setUp() {
        engine = new Engine(null);
    }

    public List<IBee> createBees() {
        List<IBee> bees = new ArrayList<>();
        for (int i = 0; i < DRONE_BEE_COUNT; i++) {
            bees.add(BeeFactory.createBee(BeeType.DRONE));
        }
        for (int i = 0; i < WORKER_BEE_COUNT; i++) {
            bees.add(BeeFactory.createBee(BeeType.WORKER));
        }
        bees.add(BeeFactory.createBee(BeeType.QUEEN));
        return bees;
    }


    public void testAliveBee(IBee bee) throws Exception {
        assertEquals(bee.getCurrentHealth(), bee.getLifespan());
        assertTrue(bee.getLifespan() > 0 && bee.getPointsPerHit() > 0 && bee.getLifespan() > bee.getPointsPerHit());
    }


    public void testHittedBee(IBee bee) throws Exception {
        assertNotEquals(bee.getCurrentHealth(), bee.getLifespan());
        assertTrue(bee.getLifespan() > 0 && bee.getPointsPerHit() > 0 && bee.getLifespan() > bee.getPointsPerHit());
    }


    public void testHittedOrNotBee(IBee bee) throws Exception {
        assertTrue(bee.getCurrentHealth() <= bee.getLifespan());
        assertTrue(bee.getLifespan() > 0 && bee.getPointsPerHit() > 0 && bee.getLifespan() > bee.getPointsPerHit());
    }

    @Test
    public void testBee() throws Exception {
        testBee(BeeFactory.createBee(BeeType.DRONE));
        testBee(BeeFactory.createBee(BeeType.WORKER));
        testBee(BeeFactory.createBee(BeeType.QUEEN));
    }



    public void testBee(IBee bee) throws Exception {
        testAliveBee(bee);
        while (bee.hitBee()) {}

        testHittedBee(bee);

        bee.resurrect();
        assertTrue(bee.getCurrentHealth() == bee.getLifespan());
        testAliveBee(bee);
    }


    @Test
    public void testEngine() throws Exception {

        assertFalse(engine.gameInProgress());
        assertNull(engine.getAliveBees());
        assertNull(engine.getBees());

        engine.setBees(createBees());

        for (IBee bee : engine.getBees()) {
            testAliveBee(bee);
        }

        for (IBee bee : engine.getAliveBees()) {
            testAliveBee(bee);
        }

        assertFalse(engine.gameInProgress());
        assertEquals(engine.getAliveBees().size(), engine.getBees().size());

        engine.onStartClick();
        while (engine.gameInProgress) {
            for (int i = 0; i < engine.aliveBees.size(); i++) {
                engine.onHitClick();
            }
            for (IBee bee : engine.getAliveBees()) {
                testHittedOrNotBee(bee);
            }
        }
        assertTrue(engine.getAliveBees().size() < engine.getBees().size());

        for (IBee bee : engine.getBees()) {
            if (bee.getBeeType() == BeeType.QUEEN) {
                assertTrue(bee.getCurrentHealth()<=0);
            }
        }
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testException() throws Exception {
        Engine engine = new Engine(null);
        thrown.expect(IllegalStateException.class);
        engine.onHitClick();
    }


}
package tutorial;

import bugwars.user.*;

public class Queen extends MyUnit {

    Direction[] allDirections = Direction.values();

    Queen(UnitController unitController){
        super(unitController);
    }


    public void play() {
        move();
        trySpawn();
        countCocoonUnits();
    }

    public void move() {
        UnitInfo[] enemies = uc.senseUnits(uc.getOpponent());

        if(enemies.length == 0) {
            /* We move randomly if there are no enemies (this can be improved a lot).*/
            moveRandom();
        }
        else {
            /* We are a high value non combat unit and we are in danger!
               Our micro code guarantees that we try to escape from them! */
            micro.doMicro();
        }
    }

    void moveRandom() {
        Direction newDirection = Helper.getRandomDirection();
        if(uc.canMove(newDirection)) {
            uc.move(newDirection);
        }
    }

    void trySpawn() {
        UnitInfo[] enemies = uc.senseUnits(uc.getOpponent());

        // let's try to get new allies :)
        // first decide what unit type we need
        UnitType spawnType = null;
        int numAnts = counters.read(Helper.getCounterIndex(UnitType.ANT));
        int numSeenCookies = foodTracker.getSeenCookies();
        //We spawn an ant only if there are less than 20, we see no enemies and there are unclaimed cookies available to mine
        if(enemies.length == 0 && numSeenCookies > 0 &&
                (numAnts == 0 || numSeenCookies/numAnts >= 3) && numAnts < 20) {
            spawnType = UnitType.ANT;
        }
        else {
            spawnType = UnitType.BEETLE;
        }

        // then we try to spawn it
        if(spawnType != null) {
            for (Direction direction: allDirections) {
                if(uc.canSpawn(direction, spawnType)) {
                    uc.spawn(direction, spawnType);
                }
            }
        }

    }

    void countCocoonUnits() {
        UnitInfo[] units = uc.senseUnits(uc.getTeam());
        for (UnitInfo unit: units) {
            if(unit.isCocoon()) counters.increaseValueByOne(Helper.getCounterIndex(unit.getType()));
        }
    }
}

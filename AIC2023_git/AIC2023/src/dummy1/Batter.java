package dummy1;

import aic2023.engine.Unit;
import aic2023.user.*;

public class Batter extends MyUnit {
    Location assignedSpot;
    Batter(UnitController unitController) {
        super(unitController);
        assignedSpot = getInitialAssignedSpot();
    }

    public void runRound() {
        Location closestLocationOfEnemyNearby = getLocationOfClosestEnemyNearby();
        if (closestLocationOfEnemyNearby != null) {
            aggressiveRound(closestLocationOfEnemyNearby);
        } else {
            goToAssignedSpot();
        }
        updateMap();
    }

    public Location getLocationOfClosestEnemyNearby() {
        float myVision = uc.getType().getStat(UnitStat.VISION_RANGE);
        UnitInfo[] enemies = uc.senseUnits(myVision, uc.getOpponent());
        for (UnitInfo ui : enemies) {
            return ui.getLocation();
        }
        return null;
    }

    public void aggressiveRound(Location target) {
        Direction targetDir = uc.getLocation().directionTo(target);
        if (uc.canBat(targetDir, GameConstants.MAX_STRENGTH)) {
            uc.bat(targetDir, GameConstants.MAX_STRENGTH);
        }
        pf.moveTo(target);
    }

    public Location getInitialAssignedSpot() {
        Location[] ourPitchers = getOurPitchers();
        //Location enemyHQ = getEnemyHQ();
        if (ourPitchers != null) {
            return ourPitchers[0];
        }
        //if (enemyHQ != null) {
        //    return enemyHQ;
        //}
        return null;
    }

    public Location[] getOurPitchers() {
        int[] livingPitcherIndexes = getLivingPitcherIndexes();
        Location[] output = new Location[livingPitcherIndexes.length];
        for (int i = 0; i < output.length; ++i) {
            output[i] = decodeLocation(livingPitcherIndexes[i]);
        }
        return output;
    }

    public int[] getLivingPitcherIndexes() {
        int num = numPitchers();
        int[] output = new int[num];
        int j = 0;
        for (int i = 19900; i < 20000 && num > 0; ++i) {
            if (uc.read(i) > 0) {
                num = num - 1;
                output[j] = i + 100;
                j += 1;
            }
        }
        return output;
    }

    int numPitchers(){
        return uc.read(19899);
    }

    //public Location getEnemyHQ() {
    //    return null;
    //}

    public void goToAssignedSpot() {
        if (assignedSpot == null) {
            doPutiVuelta();
        } else if (!uc.canSenseLocation(assignedSpot)) {
            pf.moveTo(assignedSpot);
        } else if (canSenseAnotherBatter()) {
            changeAssignedLocation();
        } else {
            patrolAssignedSpot();
        }
    }

    public void doPutiVuelta() {
        int randomNumberDir = (int)(Math.random()*8);
        Direction dir = Direction.values()[randomNumberDir];
        for (int i = 0; i < 8; ++i) {
            if (uc.canMove(dir)) uc.move(dir);
            else dir = dir.rotateRight();
        }
        assignedSpot = getInitialAssignedSpot();
    }

    public boolean canSenseAnotherBatter() {
        float myVision = uc.getType().getStat(UnitStat.VISION_RANGE);
        UnitInfo[] allies = uc.senseUnits(myVision, uc.getTeam());
        for (UnitInfo ally : allies) {
            if (ally.getType() == UnitType.BATTER) {
                return true;
            }
        }
        return false;
    }

    public void changeAssignedLocation() {
        Location[] ourPitchers = getOurPitchers();
        //Location enemyHQ = getEnemyHQ();
        int index = getindexOfLocationInArray(ourPitchers);
        if (index != -1) {
            //if (index == ourPitchers.length - 1)
                //assignedSpot = enemyHQ;
            //else
            assignedSpot = ourPitchers[index + 1];
        //} else if (assignedSpot == enemyHQ && enemyHQ != null) {
            //if (ourPitchers != null) {
                //assignedSpot = ourPitchers[0];
            //}
        } else { //null
            assignedSpot = getInitialAssignedSpot();
        }
    }

    public int getindexOfLocationInArray(Location[] array) {
        for (int i = 0; i < array.length; ++i) {
            if (assignedSpot == array[i]) return i;
        }
        return -1;
    }

    public void patrolAssignedSpot() {
        Direction dir = uc.getLocation().directionTo(assignedSpot);
        Direction[] dirs = {
                dir.rotateRight().rotateRight(),
                dir.rotateRight(),
                dir.rotateLeft().rotateLeft(),
                dir.rotateLeft(),
                dir,
                dir.rotateRight().rotateRight().rotateRight(),
                dir.rotateRight().rotateRight().rotateRight().rotateRight(),
                dir.rotateRight().rotateRight().rotateRight().rotateRight().rotateRight()
        };
        for (Direction d : dirs) {
            if (uc.canMove(d)) uc.move(d);
        }
    }

    Location decodeLocation(int n){
        int encodedLocation = uc.read(n);
        int x = encodedLocation/120;
        int y = encodedLocation%120;
        return new Location(x-uc.read(0), y-uc.read(1));
    }

    int encodeLocation(Location loc){
        return 120*(loc.x+uc.read(0))+loc.y+uc.read(1);
    }

    void updateMap() {
        MapObject objective;
        int n;
        for (Location loc : uc.senseObjects(MapObject.LOCATION, 1000)){
            n = encodeLocation(loc);
            objective = uc.senseObjectAtLocation(loc, false);
            if (objective == MapObject.GRASS){
                uc.write(n, 4);
            }
            else if (objective == MapObject.WATER){
                uc.write(n, 1);
            }
            else if (objective == MapObject.STADIUM){
                uc.write(n, 2);
            }
            else if (objective == MapObject.BASE){
                uc.write(n, 3);
            }
        }
    }
}

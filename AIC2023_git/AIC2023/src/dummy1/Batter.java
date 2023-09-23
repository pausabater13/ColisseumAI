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
        Location[] ourBasesAndStadiums = getOurBasesAndStadiums();
        Location enemyHQ = getEnemyHQ();
        if (ourBasesAndStadiums != null) {
            return ourBasesAndStadiums[0];
        }
        if (enemyHQ != null) {
            return enemyHQ;
        }
        return null;
    }

    public Location[] getOurBasesAndStadiums() {
        int encodedLocationOfFirstPlace = uc.read(20000);
        int encodedLocationOfSecondPlace = uc.read(20001);
        if (encodedLocationOfFirstPlace == 0 && encodedLocationOfSecondPlace == 0) {
            return null;
        }
        if (encodedLocationOfSecondPlace == 0) {
            return new Location[]{decodeLocation(20000)};
        }
        if (encodedLocationOfFirstPlace == 0) {
            return new Location[]{decodeLocation(20001)};
        }
        return new Location[]{decodeLocation(20000), decodeLocation(20001)};
    }

    public Location getEnemyHQ() {
        return null;
    }

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
        Location[] ourBasesAndStadiums = getOurBasesAndStadiums();
        Location enemyHQ = getEnemyHQ();
        int index = getindexOfLocationInArray(ourBasesAndStadiums);
        if (index != -1) {
            if (index == ourBasesAndStadiums.length - 1)
                assignedSpot = enemyHQ;
            else
                assignedSpot = ourBasesAndStadiums[index + 1];
        } else if (assignedSpot == enemyHQ && enemyHQ != null) {
            if (ourBasesAndStadiums != null) {
                assignedSpot = ourBasesAndStadiums[0];
            }
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

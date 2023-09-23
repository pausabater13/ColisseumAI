package dummy1;

import aic2023.user.*;

public class Batter extends MyUnit {
    Batter(UnitController unitController) {
        super(unitController);
    }

    public void runRound() {
        Location closestLocationOfEnemyNearby = getLocationOfClosestEnemyNearby();
        if (closestLocationOfEnemyNearby != null) {
            aggressiveRound(closestLocationOfEnemyNearby);
        } else {

        }
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
}

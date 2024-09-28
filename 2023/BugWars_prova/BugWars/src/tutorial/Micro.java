package tutorial;

import bugwars.user.*;

public class Micro {

    UnitController uc;
    Direction[] directions = Direction.values();

    public Micro(UnitController uc){
        this.uc = uc;
    }

    //generic method for attacking
    public boolean tryGenericAttack() {
        UnitInfo[] enemies = uc.senseUnits(uc.getOpponent());
        UnitInfo bestUnitToAttack;

        if(enemies.length > 0) {
            bestUnitToAttack = getGenericBestUnitToAttack(enemies);
            if(bestUnitToAttack != null) {
                uc.attack(bestUnitToAttack);
                return true;
            }
        }
        return false;
    }

    public UnitInfo getGenericBestUnitToAttack(UnitInfo[] units) {

        UnitInfo unitToAttack = null;
        for (UnitInfo unit: units) {
            //skip if we can't attack
            if (!uc.canAttack(unit)) continue;
            // we choose the unit with the least health!
            // there is a lot to improve here, go on and try :)
            if((unitToAttack == null || unitToAttack.getHealth() > unit.getHealth())) {
                unitToAttack = unit;
            }
        }

        return unitToAttack;
    }

    public void doMicro() {

        // initialize the micros
        MicroInfo[] microInfos = new MicroInfo[directions.length];
        for(Direction dir : directions) {
            microInfos[dir.ordinal()] = new MicroInfo(dir);
        }

        // update micros for each enemy
        UnitInfo[] enemies = uc.senseUnits(uc.getOpponent());
        for(UnitInfo enemy: enemies) {
            for(MicroInfo mi : microInfos) {
                mi.update(enemy);
            }
        }

        // choose best micro
        MicroInfo bestMicro = microInfos[Direction.ZERO.ordinal()];
        for(MicroInfo micro: microInfos) {
            if(micro.imBetterThan(bestMicro)) {
                bestMicro = micro;
            }
        }

        // move
        if (uc.canMove(bestMicro.dir)) uc.move(bestMicro.dir);
    }


    /*
        Helper class used to store all relevant information about a given location. It includes a custom comparator to find
        which is the best location to go according to our micro.
     */

    public class MicroInfo {
        int numEnemies;
        int minDistEnemy;
        Location loc;
        Direction dir;
        boolean canMove;

        public MicroInfo(Direction dir) {
            this.dir = dir;
            canMove = uc.canMove(dir);
            loc = uc.getLocation().add(dir);
            numEnemies = 0;
            minDistEnemy = Integer.MAX_VALUE;
        }

        void update(UnitInfo unit) {
            Location enemyLocation = unit.getLocation();
            int d = enemyLocation.distanceSquared(loc);
            if (d <= unit.getType().getAttackRangeSquared() && !uc.isObstructed(loc, enemyLocation)) numEnemies++;
            if (d < minDistEnemy) minDistEnemy = d;
        }

        //custom comparison method
        boolean imBetterThan(MicroInfo other) {
            if (!canMove) return false;
            if(numEnemies < other.numEnemies) return true;
            if(numEnemies > other.numEnemies) return false;

            return minDistEnemy > other.minDistEnemy;
        }
    }
}

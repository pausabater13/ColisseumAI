package dummy1;

import aic2023.user.*;

public class Pitcher extends MyUnit {
    Direction unitDirection;
    int myLoc;
    Pitcher(UnitController uc) {
        super(uc);
        Location baseLoc = decodeLocation(3);
        Location myLoc = uc.getLocation();
        this.unitDirection = baseLoc.directionTo(myLoc);
        for (int i = 0; i<100; i++) {
            if (uc.read(20000+i) == 0) {
                this.myLoc = 20000+i;
                uc.write(20000+i, 1);
            }
        }
    }

    public void runRound() {
        /*enemy team*/
        Team opponent = uc.getOpponent();

        /*Get random direction*/

        int randomNumberDir = (int)(Math.random()*8);
        Direction dir = Direction.values()[randomNumberDir];

        if (uc.canMove(unitDirection)) uc.move(unitDirection);
        else {
            Location target = getClosestBaseOrStadium(uc);
            if (target != null) {
                /*Try to get closer to the target */
                Direction targetDir = uc.getLocation().directionTo(target);
                if (uc.canMove(targetDir)) uc.move(targetDir);
                    /*Otherwise move random*/
                else if (uc.canMove(dir)) uc.move(dir);
            }
            /*If there is no target, also move random*/

            else if (uc.canMove(dir)) uc.move(dir);
            else if (uc.canMove(rotateLeft(dir))) uc.move(rotateLeft(dir));
        }

        uc.write(myLoc, encodeLocation(uc.getLocation())); // Marca on està localitzat
        uc.write(myLoc-100, 1); // Marca que continua viu
    }

    int encodeLocation(Location loc){
        return 120*(loc.x+uc.read(0))+loc.y+uc.read(1);
    }

    Location decodeLocation(int n){
        int encodedLocation = uc.read(n);
        int x = encodedLocation/120;
        int y = encodedLocation%120;
        return new Location(x-uc.read(0), y-uc.read(1));
    }

    /**
     * Returns a location with a base or stadium that does nt have a pitcher on top. If there is none
     * inside vision range, it returns null.
     */
    Location getClosestBaseOrStadium(UnitController uc){
        float myVision = uc.getType().getStat(UnitStat.VISION_RANGE);
        Location[] bases = uc.senseObjects(MapObject.BASE, myVision);
        Location base = getFirstAvailable(uc, bases);
        if (base != null) return base;

        Location[] stadiums = uc.senseObjects(MapObject.STADIUM, myVision);
        return getFirstAvailable(uc, stadiums);
    }

    /**
     * Returns the first location of the array that does not have one of our pitchers on top.
     * We should also look at the IDs to make sure it is not this unit the one that's standing on top.
     */
    Location getFirstAvailable(UnitController uc, Location[] locs){
        for (Location loc : locs){
            UnitInfo unit = uc.senseUnitAtLocation(loc);
            if (unit == null || unit.getTeam() != uc.getTeam() || uc.getType() != UnitType.PITCHER || unit.getID() == uc.getInfo().getID()) return loc;
        }
        return null;
    }
}

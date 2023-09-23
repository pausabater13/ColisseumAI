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
            if (uc.read(19900+i) == 0) {
                this.myLoc = 20000+i;
                uc.write(this.myLoc, 1);
            }
        }
    }

    public void runRound() {
        /*enemy team*/
        Team opponent = uc.getOpponent();

        /*Get random direction*/

        int randomNumberDir = (int)(Math.random()*8);
        Direction dir = Direction.values()[randomNumberDir];
        int randomNumberStraight = (int)(Math.random()*6);


        Location target = getClosestBaseOrStadium(uc);
        if (target != null) {
            /*Try to get closer to the target */
            Direction targetDir = uc.getLocation().directionTo(target);
            if (uc.canMove(targetDir)) uc.move(targetDir);
                /*Otherwise move random*/
            else if (uc.canMove(unitDirection)&& randomNumberStraight!=1 && randomNumberStraight!=2)
                uc.move(unitDirection);
            if (uc.canMove(unitDirection.rotateRight())&& randomNumberStraight==1) {
                uc.move(unitDirection.rotateRight());
            } else if (uc.canMove(unitDirection.rotateRight().rotateRight())&& randomNumberStraight==2) {
                uc.move(unitDirection.rotateRight().rotateRight());
            } else if (uc.canMove(unitDirection.rotateLeft())&& randomNumberStraight==1) {
                uc.move(unitDirection.rotateLeft());
            } else if (uc.canMove(unitDirection.rotateLeft().rotateLeft())&& randomNumberStraight==1){
                uc.move(unitDirection.rotateLeft().rotateLeft());
            }else if (uc.canMove(unitDirection.rotateRight().rotateRight().rotateRight())&& randomNumberStraight==2){
                uc.move(unitDirection.rotateRight().rotateRight().rotateRight());
            } else if (uc.canMove(unitDirection.rotateLeft().rotateLeft().rotateLeft())&& randomNumberStraight==2) {
                uc.move(unitDirection.rotateLeft().rotateLeft().rotateLeft());
            }
        }
        else if (uc.canMove(unitDirection)&& randomNumberStraight!=1 && randomNumberStraight!=2)
            uc.move(unitDirection);
        if (uc.canMove(unitDirection.rotateRight())&& randomNumberStraight==1) {
            uc.move(unitDirection.rotateRight());
        } else if (uc.canMove(unitDirection.rotateRight().rotateRight())&& randomNumberStraight==2) {
            uc.move(unitDirection.rotateRight().rotateRight());
        }
        else if (uc.canMove(unitDirection.rotateLeft())&& randomNumberStraight==1) {
            uc.move(unitDirection.rotateLeft());
        } else if (uc.canMove(unitDirection.rotateLeft().rotateLeft())&& randomNumberStraight==1){
            uc.move(unitDirection.rotateLeft().rotateLeft());
        }else if (uc.canMove(unitDirection.rotateRight().rotateRight().rotateRight())&& randomNumberStraight==2){
            uc.move(unitDirection.rotateRight().rotateRight().rotateRight());
        } else if (uc.canMove(unitDirection.rotateLeft().rotateLeft().rotateLeft())&& randomNumberStraight==2) {
            uc.move(unitDirection.rotateLeft().rotateLeft().rotateLeft());
        }/*If there is no target, also move random*/
        else if (uc.canMove(dir)) uc.move(dir);

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

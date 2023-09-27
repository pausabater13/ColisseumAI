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
        int k = (int)(Math.random()*10);

        Location target = getClosestBaseOrStadium(uc);

        Direction checkedDir;
        //If sees an agresive unit: RUN!!
        if (closeBatters(uc) != null) checkedDir = goDirection(closeBatters(uc));
            //First option: bases or stadiums
        else if (target != null) {
            /*Try to get closer to the target */
            Direction targetDir = uc.getLocation().directionTo(target);
            if(uc.canMove(targetDir))checkedDir = targetDir;
            else checkedDir = goDirection(targetDir);
        } //Second options: keep direction
        else if(computeDirection() != null) checkedDir = goDirection(averageDir(unitDirection, computeDirection()));
        else checkedDir = goDirection(unitDirection);
        /*If there is no target, also move random*/
        /*If there is no target, also move random*/
        unitDirection = checkedDir;
        if (uc.canMove(checkedDir)) uc.move(unitDirection);

        uc.write(myLoc, encodeLocation(uc.getLocation())); // Marca on està localitzat
        uc.write(myLoc-100, 2); // Marca que continua viu
    }
    Direction averageDir(Direction a, Direction b){
        return  Direction.getDirection((int)(a.dx+b.dx), (int)(a.dy+b.dy));
    }


    Direction goDirection (Direction dir){
        Direction rDir = dir.rotateRight(); Direction rrDir = rDir.rotateRight(); Direction rrrDir = rrDir.rotateRight();
        Direction lDir = dir.rotateLeft(); Direction llDir = lDir.rotateLeft(); Direction lllDir = llDir.rotateLeft();

        //0.25 probability of turning diagonally
        if ((int)(Math.random()*4)==1) {
            //Choose left or right direction
            if ((int)(Math.random()*2)==1)
                if (uc.canMove(rDir)) dir = rDir;
                else if (uc.canMove(lDir)) dir = lDir;
                else if (uc.canMove(lDir)) dir = lDir;
                else if (uc.canMove(rDir)) dir = rDir;
        } else if(!uc.canMove(dir)){
            if ((int)(Math.random()*2)==1) {
                if (uc.canMove(rrDir)) {
                    dir = rrDir;
                } else if (uc.canMove(llDir)) {
                    dir = llDir;
                }
            } else if (uc.canMove(llDir)) {
                dir = llDir;
            } else if (uc.canMove(rrDir)) {
                dir = rrDir;
            } else if ((int)(Math.random()*2)==1) {
                if (uc.canMove(rrrDir)) {
                    dir = rrrDir;
                } else if (uc.canMove(lllDir)) {
                    dir = lllDir;
                }
            } else if (uc.canMove(lllDir)) {
                dir = lllDir;
            } else if (uc.canMove(rrrDir)) {
                dir = rrrDir;
            }
        }
        return dir;
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

    Direction closeBatters(UnitController uc){
        float myVision = uc.getType().getStat(UnitStat.VISION_RANGE)/2;
        UnitInfo[] enemies = uc.senseUnits(myVision, uc.getOpponent());

        return batterDirection(uc, enemies, UnitType.BATTER);
    }

    Direction batterDirection(UnitController uc, UnitInfo[] enemies, UnitType unitType){
        for (UnitInfo enemy : enemies){
            UnitInfo unit = uc.senseUnitAtLocation(enemy.getLocation());
            if (unit == null || (unit.getTeam() != uc.getTeam() && uc.getType() == UnitType.BATTER)) {
                return unit.getLocation().directionTo(uc.getLocation());
            }
        }
        return null;
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

    Direction computeDirection(){
        Location loc = uc.getLocation();
        Location[] possibleLocations = new Location[8];
        possibleLocations[0] = new Location(loc.x+4, loc.y);//EAST
        possibleLocations[1] = new Location(loc.x, loc.y-4);//SOUTH
        possibleLocations[2] = new Location(loc.x-4, loc.y);//WEST
        possibleLocations[3] = new Location(loc.x, loc.y+4);//NORTH
        int counter = 0;
        int[] possibilities = new int[] {1,1,1,1};
        for (int i=0; i<4; i++){
            if (uc.isOutOfMap(possibleLocations[i])){
                possibilities[i]=0;
                counter += 1; // Obs: can't be >2
            }
        }
        Direction result;
        if (counter == 2){
            if(possibilities[0]==1){
                if(possibilities[1]==1) return Direction.SOUTHEAST;
                if(possibilities[3]==1) return Direction.NORTHEAST;
                return Direction.EAST;
            } if(possibilities[2]==1){
                if(possibilities[1]==1) return Direction.SOUTHWEST;
                if(possibilities[3]==1) return Direction.NORTHWEST;
                return Direction.WEST;
            } if(possibilities[1]==1) return Direction.SOUTH;
            return Direction.NORTH;
        } else {
            if (possibilities[0]==0) return Direction.WEST;
            if (possibilities[1]==0) return Direction.NORTH;
            if (possibilities[2]==0) return Direction.EAST;
            if (possibilities[3]==0) return Direction.SOUTH;
        }
        return null;
    }

}
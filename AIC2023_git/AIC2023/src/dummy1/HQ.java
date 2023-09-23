package dummy1;

import aic2023.user.*;

public class HQ extends MyUnit {

    HQ(UnitController uc) {
        super(uc);
    }

    public void runRound() {
        if (uc.getRound() == 0){
            initialMap();
            Direction[] pitcherDirections = getDirections(); // suposem que es pot spawnejar allà
            for (int i =0; i < pitcherDirections.length; i++){
                if (uc.canRecruitUnit(UnitType.PITCHER, pitcherDirections[i]))
                    uc.recruitUnit(UnitType.PITCHER, pitcherDirections[i]);
            }
            uc.write(3, encodeLocation(uc.getLocation()));
            if (pitcherDirections[0] == Direction.NORTH) uc.write(2, 1);
            if (pitcherDirections[0] == Direction.EAST) uc.write(2, 2);
            if (pitcherDirections[0] == Direction.SOUTH) uc.write(2, 3);
            if (pitcherDirections[0] == Direction.WEST) uc.write(2, 4);
        };
        int dirInt = uc.read(1);
        Direction dir = Direction.ZERO;

        if (dirInt == 1) dir = Direction.NORTH;
        if (dirInt == 2) dir = Direction.EAST;
        if (dirInt == 3) dir = Direction.SOUTH;
        if (dirInt == 4) dir = Direction.WEST;

        /*If this unit is a HQ, try to recruit a pitcher following direction dir*/
        if (uc.canRecruitUnit(UnitType.BATTER, dir)) uc.recruitUnit(UnitType.BATTER, dir);
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

    void initialMap() {
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

    Direction[] getDirections(){
        int[] possibilities = new int[] {1,1,1,1};
        Location loc = uc.getLocation();
        Location[] possibleLocations = new Location[4];
        possibleLocations[0] = new Location(loc.x-8, loc.y);
        possibleLocations[1] = new Location(loc.x, loc.y-8);
        possibleLocations[2] = new Location(loc.x+8, loc.y);
        possibleLocations[3] = new Location(loc.x, loc.y+8);
        int counter = 0;
        for (int i=0; i<4; i++){
            if (uc.isOutOfMap(possibleLocations[i])){
                possibilities[i]=0;
                counter += 1; // can't be >2
            }
        }
        Direction[] result = new Direction[2];
        if (counter == 2){
            counter = 0;
            for (int i=0; i<4; i++){
                if (possibilities[i]==1){
                    result[counter].getDirection(possibleLocations[counter].x-loc.x, possibleLocations[counter].y-loc.y);
                }
            }
            return result;
        }
        if (counter == 3){
            int argZero = 0;
            for (int i=0; i<4; i++) {
                if (possibilities[i]==0) argZero=i;
            }
            if (argZero >1){
                result[0].getDirection(possibleLocations[0].x-loc.x, possibleLocations[0].y-loc.y);
                result[1].getDirection(possibleLocations[1].x-loc.x, possibleLocations[1].y-loc.y);
                return result;
            }
            result[0].getDirection(possibleLocations[2].x-loc.x, possibleLocations[2].y-loc.y);
            result[1].getDirection(possibleLocations[3].x-loc.x, possibleLocations[3].y-loc.y);
            return result;
        }
        result[0].getDirection(possibleLocations[0].x-loc.x, possibleLocations[0].y-loc.y);
        result[1].getDirection(possibleLocations[1].x-loc.x, possibleLocations[1].y-loc.y);
        return result;
    }

}

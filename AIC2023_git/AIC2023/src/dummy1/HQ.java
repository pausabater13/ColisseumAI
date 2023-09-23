package dummy1;

import aic2023.user.*;

public class HQ extends MyUnit {
    int[] unitCounter;

    HQ(UnitController uc) {
        super(uc);
    }

    public void runRound() {
        if (uc.getRound() == 0){
            initialMap();
            Direction[] pitcherDirections = getDirections(); // suposem que es pot spawnejar allà
            for (int i =0; i < pitcherDirections.length; i++){
                if (uc.canRecruitUnit(UnitType.PITCHER, pitcherDirections[i])){
                    uc.recruitUnit(UnitType.PITCHER, pitcherDirections[i]);
                    this.unitCounter[0] += 1;
                }
            }

            uc.write(3, encodeLocation(uc.getLocation()));
            if (pitcherDirections[0] == Direction.NORTH) uc.write(2, 1);
            if (pitcherDirections[0] == Direction.EAST) uc.write(2, 2);
            if (pitcherDirections[0] == Direction.SOUTH) uc.write(2, 3);
            if (pitcherDirections[0] == Direction.WEST) uc.write(2, 4);
        }
        int dirInt = uc.read(2);
        Direction dir = Direction.ZERO;

        if (dirInt == 1) dir = Direction.NORTH;
        if (dirInt == 2) dir = Direction.EAST;
        if (dirInt == 3) dir = Direction.SOUTH;
        if (dirInt == 4) dir = Direction.WEST;

        /*try to recruit a batter following direction dir*/
        if (uc.canRecruitUnit(UnitType.BATTER, dir)) {
            uc.recruitUnit(UnitType.BATTER, dir);
            this.unitCounter[1] += 1;
        }
        updateGraveyard();
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

    void updateGraveyard(){
        for (int i=20000-100; i<20000; i++){
            if (uc.read(i) == 1) uc.write(i, 0); // segueix viu
            else {
                uc.write(i + 100, 0); // està mort
                this.unitCounter[0] -= 1;
            }
        }
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
        possibleLocations[0] = new Location(loc.x+8, loc.y);
        possibleLocations[1] = new Location(loc.x, loc.y-8);
        possibleLocations[2] = new Location(loc.x-8, loc.y);
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
                    if (i == 0) result[counter] = Direction.EAST;
                    if (i == 1) result[counter] = Direction.SOUTH;
                    if (i == 2) result[counter] = Direction.WEST;
                    if (i == 3) result[counter] = Direction.NORTH;
                    counter += 1;
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
                result[0] = Direction.EAST;
                result[1] = Direction.SOUTH;
                return result;
            }
            result[0] = Direction.WEST;
            result[1] = Direction.NORTH;
            return result;
        }
        result[0] = Direction.EAST;
        result[1] = Direction.SOUTH;
        return result;
    }

}

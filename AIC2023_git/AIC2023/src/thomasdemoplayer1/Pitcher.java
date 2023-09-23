package thomasdemoplayer1;

import aic2023.user.*;

public class Pitcher extends MyUnit {
    Direction unitDirection;

    Pitcher(UnitController unitController) {
        super(unitController);
    }

    /* Si està a prop del HQ i no té res important al voltant, escull una
    direcció adequada de expedició. Si està a distància mitjana, prosegueix
     aquesta direcció*/

    public void runRound() {
        /*enemy team*/
        Team opponent = uc.getOpponent();

        /*Get random direction*/
        int randomNumberDir = (int)(Math.random()*8);
        Direction dir = Direction.values()[randomNumberDir];

        Location target = getTarget(uc);
        if (target != null) {
            /*Try to get closer to the target */
            Direction targetDir = uc.getLocation().directionTo(target);
            if (uc.canMove(targetDir)) uc.move(targetDir);
                /*Otherwise move random*/
            else if (uc.canMove(dir)) uc.move(dir);
        }
        /*If there is no target, also move random*/
        else if (uc.canMove(dir)) uc.move(dir);
    }

    void runDistributed(Direction direction) {

    }

    /**
     * Returns a location with a base or stadium that does nt have a pitcher on top. If there is none
     * inside vision range, it returns null.
     */
    Location getTarget(UnitController uc){
        
        float myVision = uc.getType().getStat(UnitStat.VISION_RANGE);

        Location[] bases = uc.senseObjects(MapObject.BASE, myVision);
        Location base = getFirstAvailable(uc, bases);
        if (base != null) return base;

        Location[] stadiums = uc.senseObjects(MapObject.STADIUM, myVision);
        Location stadium = getFirstAvailable(uc, stadiums);
        if (stadium != null) return stadium;

        Location[] waters = uc.senseObjects(MapObject.WATER, myVision);
        Location water = getFirstAvailable(uc, stadiums);
        if (water != null) return water;



        if (water != null) return water;
        return water;
    }

    Location getDistributedTarget(UnitController uc, Direction unitDirection){
        float myVision = uc.getType().getStat(UnitStat.VISION_RANGE);

        Location iterator = uc.getLocation();
        bool found = false;
        for (int i = 1; i < myVision; i++) {
            if(us.canMove(uc.getLocation().add(unitDirection)))
                iterator = uc.getLocation().add(unitDirection);
            if(senseObjectAtLocation(iterator, false) != GRASS)
                

            
        }

        Location[] bases = uc.senseObjects(MapObject.BASE, myVision);
        Location base = getFirstAvailable(uc, bases);
        if (base != null) return base;

        Location[] stadiums = uc.senseObjects(MapObject.STADIUM, myVision);
        Location stadium = getFirstAvailable(uc, stadiums);
        if (stadium != null) return stadium;

        Location[] waters = uc.senseObjects(MapObject.WATER, myVision);
        Location water = getFirstAvailable(uc, stadiums);
        if (water != null) return water;



        if (water != null) return water;
        return water;
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

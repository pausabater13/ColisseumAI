package AlexDumbPlayer;

import aic2023.user.*;

public class UnitPlayer {

	public void run(UnitController uc) {
		/*Insert here the code that should be executed only at the beginning of the unit's lifespan*/
		UnitType myType = uc.getType();
		/*enemy team*/
		Team opponent = uc.getOpponent();
		if (myType == UnitType.HQ){
			HQ me = new HQ(uc);
			me.run();
		}
		else if (myType == UnitType.CATCHER){}
		else if (myType == UnitType.PITCHER){
			PITCHER me = new PITCHER(uc);
			me.run();
		}
		else if (myType == UnitType.BATTER){}
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
		return stadium;
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

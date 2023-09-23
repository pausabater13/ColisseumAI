package AlexDumbPlayer;

import aic2023.user.*;

public class PITCHER {
	UnitController uc;

	public PITCHER(UnitController uc){
		this.uc = uc;
	}

	public void run() {
		/*Insert here the code that should be executed only at the beginning of the unit's lifespan*/
		UnitType myType = UnitType.PITCHER;
		/*enemy team*/
		Team opponent = uc.getOpponent();

		while (true){
			/*Insert here the code that should be executed every round*/
			int randomNumberDir = (int)(Math.random()*8);
			Direction dir = Direction.values()[randomNumberDir];
			/*Otherwise, if there is a stadium or a base *without* a pitcher on top, go there*/
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

			uc.yield(); //End of turn
		}
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

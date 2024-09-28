package AlexDumbPlayer;

import aic2023.user.*;

public class HQ {
	UnitController uc;

	public HQ (UnitController uc){
		this.uc = uc;
	}

	public void run() {
		/*Insert here the code that should be executed only at the beginning of the unit's lifespan*/
		UnitType myType = UnitType.HQ;
		Location SpawnLoc = getBestSpawnLoc(uc);
		/*enemy team*/
		Team opponent = uc.getOpponent();

		while (true){
			/*Insert here the code that should be executed every round*/

			/*Get random direction*/
			int randomNumberDir = (int)(Math.random()*8);
			Direction dir = Direction.values()[randomNumberDir];
			/*If this unit is a HQ, try to recruit a pitcher following direction dir*/
			if (uc.canConstructBall(dir)) uc.constructBall(dir);
			if (uc.canRecruitUnit(UnitType.PITCHER, dir)) uc.recruitUnit(UnitType.PITCHER, dir);

		}
	}

	/**
	 * Returns a location with a base or stadium that does nt have a pitcher on top. If there is none
	 * inside vision range, it returns null.
	 */
	Location getBestSpawnLoc(UnitController uc){
		return null;
	}

}

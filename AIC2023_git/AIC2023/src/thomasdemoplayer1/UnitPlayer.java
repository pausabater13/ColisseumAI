package thomasdemoplayer1;

import aic2023.user.*;

public class UnitPlayer {

	public void run(UnitController uc) {
		// custom generic unit
		MyUnit me;

		// instantiate the generic class according to our unit type
		UnitType myType = uc.getType();
		if(myType == UnitType.HQ) me = new HQ(uc);
		else if(myType == UnitType.PITCHER) me = new Pitcher(uc);
		else if(myType == UnitType.BATTER) me = new Batter(uc);
		else me = new Catcher(uc);

		while(true){
			me.runRound();
			uc.yield(); //End of turn
		}
	}
}

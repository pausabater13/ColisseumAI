package thomasdemoplayer1;

import aic2023.user.*;

public class HQ extends MyUnit {
    HQ(UnitController unitController) {
        super(unitController);
    }

    public void runRound() {
        /*enemy team*/
        Team opponent = uc.getOpponent();
        /*Get random direction*/
        int randomNumberDir = (int)(Math.random()*8);
        Direction dir = Direction.values()[randomNumberDir];

        /*If this unit is a HQ, try to recruit a pitcher following direction dir*/
        if (uc.canRecruitUnit(UnitType.PITCHER, dir)) uc.recruitUnit(UnitType.PITCHER, dir);
    }
}

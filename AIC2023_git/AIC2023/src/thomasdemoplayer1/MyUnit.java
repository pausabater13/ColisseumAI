package thomasdemoplayer1;

import aic2023.user.UnitController;

//Custom generic unit class. It contains all methods and attributes used by (mostly) all units.
public abstract class MyUnit {
    UnitController uc;
    MyUnit(UnitController unitController) {
        uc = unitController;
    }

    abstract void runRound();
    abstract void runDistributed();
}

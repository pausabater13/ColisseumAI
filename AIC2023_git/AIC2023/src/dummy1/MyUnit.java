package dummy1;

import aic2023.user.Location;
import aic2023.user.UnitController;

//Custom generic unit class. It contains all methods and attributes used by (mostly) all units.
public abstract class MyUnit {
    UnitController uc;
    PathFinding pf;
    MyUnit(UnitController unitController) {
        uc = unitController;
        pf = new PathFinding(uc);
    }
    abstract void runRound();

